import sys
import io
import weaviate
import openai
import requests
import json
import re
import chardet
from sklearn.metrics.pairwise import cosine_similarity
import numpy as np
from tiktoken import encoding_for_model

sys.stdout = io.TextIOWrapper(sys.stdout.buffer, encoding="utf-8", errors="replace")
sys.stderr = io.TextIOWrapper(sys.stderr.buffer, encoding="utf-8", errors="replace")

original_stdout = sys.stdout  # 保存原始標準輸出
original_stderr = sys.stderr  # 保存原始標準錯誤
# 重定向輸出
log_file = open("C:\\Users\\Jerrylin\\IdeaProjects\\sodd-backend\\python\\llama3_ai\\output.log", "w", encoding="utf-8",
                errors="replace")
sys.stdout = log_file
sys.stderr = log_file


def generate_embedding(text):
    """
    使用 OpenAI API 生成文本的嵌入向量。
    """
    try:
        response = openai.Embedding.create(
            model="text-embedding-ada-002", input=text
        )
        return response["data"][0]["embedding"]
    except Exception as e:
        print(f"生成嵌入向量時發生錯誤: {e}")
        sys.exit(1)


def fetch_relevant_docs(class_name, query_vector, attributes):
    """
    從 Weaviate 中根據向量檢索數據。
    """
    graphql_query = {
        "query": f"""
        {{
            Get {{
                {class_name}(nearVector: {{vector: {query_vector}}}) {{
                    {', '.join(attributes)}
                }}
            }}
        }}
        """
    }

    try:
        # 發送請求
        response = requests.post("http://120.97.29.13:8090/v1/graphql", json=graphql_query)

        # 檢查回應是否成功
        if response.status_code != 200:
            print("查詢失敗，狀態碼:", response.status_code)
            print("錯誤訊息:", response.text)
            return []

        # 嘗試解析 JSON
        try:
            result = response.json()
        except ValueError:
            print("回應 JSON 解析失敗，內容為:", response.text)
            return []

        # 檢查返回結構是否正確
        if "data" in result and "Get" in result["data"] and class_name in result["data"]["Get"]:
            return result["data"]["Get"][class_name]
        else:
            print("GraphQL 回應結構不符合預期:", result)
            return []

    except requests.exceptions.RequestException as e:
        print(f"網絡錯誤: {e}")
        return []

    except Exception as e:
        print(f"發生未知錯誤: {e}")
        return []


def extract_key_mapping(schema):
    """ 從 schema 中提取 key 與 description 的映射關係 """
    key_mapping = {}
    for class_info in schema.get('classes', []):
        for prop in class_info.get('properties', []):
            key_mapping[prop['name']] = prop['description']
    return key_mapping


def prepare_context(retrieved_docs):
    """ 將 retrieved_docs 中的 key 替換成 schema 中對應的 description """
    converted_docs = []
    key_mapping = extract_key_mapping(schema)
    for doc in retrieved_docs:
        new_doc = {key_mapping.get(key, key): value for key, value in doc.items()}
        converted_docs.append(new_doc)
    return converted_docs


def determine_class(user_input, schema):
    """
    根據使用者輸入判斷最適合的 Weaviate 類別。
    """
    class_descriptions = {cls["class"]: cls["description"] for cls in schema["classes"]}
    print(f"user_input: {user_input}")
    user_vector = generate_embedding(user_input)
    class_vectors = {cls: generate_embedding(desc) for cls, desc in class_descriptions.items()}

    similarities = {
        cls: cosine_similarity([user_vector], [vec])[0][0] for cls, vec in class_vectors.items()
    }

    return max(similarities, key=similarities.get)


def query_lm_studio(lm_studio_url, company_context, prompt):
    """
    向 LM Studio 發送查詢請求並返回結果。
    """
    try:
        # 構造請求資料
        data = {
            "model": "llama3-taide-lx-8b-chat-alpha1:2",
            "messages": [
                {"role": "system", "content": "你是一位益張實業股份有限公司(後續簡稱我們公司)的專業執行長，請針對我們所提供的公司訊息"
                                              f"以下是我們公司的資訊: {company_context}，並使用1000字以內為我們提供決策建議。"},
                {"role": "user", "content": prompt},
            ],
            "temperature": 0.6,
            "max_tokens": 1500,
            "stream": False
        }
        print(f"data: {data}")

        # 發送 POST 請求
        response = requests.post(f"{lm_studio_url}/chat/completions", json=data)
        print(f"Response: {response.status_code}")
        response.raise_for_status()
        print(f"response: {response.raise_for_status}  {response.text}  {response.json}  {response.status_code}")

        # 返回回應內容
        return response.json()["choices"][0]["message"]["content"]

    except Exception as e:
        print(f"與 LM Studio 通訊時發生錯誤: {e}")
        return None


def truncate_text_to_limit(text, max_tokens=4000, model="text-embedding-ada-002"):
    """
    確保傳入的文本不超過最大 token 限制，進行裁剪。
    """
    enc = encoding_for_model(model)
    encoded = enc.encode(text)
    if len(encoded) > max_tokens:
        # 裁剪到最大 token 長度
        truncated = enc.decode(encoded[:max_tokens])
        return truncated
    return text


def process_user_query(user_input, description, schema, lm_studio_url):
    """
    處理整個使用者查詢流程。
    """
    try:
        user_input = truncate_text_to_limit(user_input, max_tokens=4000)

        # 動態判斷類別
        matched_class = determine_class(user_input, schema)

        print(f"選定的類別: {matched_class}")

        # 獲取屬性列表
        attributes = [prop["name"] for cls in schema["classes"] if cls["class"] == matched_class for prop in
                      cls["properties"]]
        # 固定查詢 CompanyProfile 類別
        company_profile_attributes = [
            prop["name"] for cls in schema["classes"] if cls["class"] == "CompanyProfile" for prop in cls["properties"]
        ]

        # 生成向量並檢索
        query_vector = generate_embedding(description)
        retrieved_docs = fetch_relevant_docs(matched_class, query_vector, attributes)
        retrieved_docs_company = fetch_relevant_docs("CompanyProfile", query_vector, company_profile_attributes)
        print(f"retrieved_docs: {retrieved_docs}")

        # 整理檢索結果
        context = prepare_context(retrieved_docs)
        company_context = prepare_context(retrieved_docs_company)

        # 整合 Prompt
        prompt = (
            f"這是我們圖表的所呈現的數據: {user_input}"
            f"以下是與我們公司與圖表相關的資料與背景知識：{context}"
            f"我們的圖表對應的現實狀況為: {description}"
            f"根據參考我們公司的簡介及此圖表的數據並針對圖表對應的現實狀況，請提供關於這方面對應的決策建議，並以繁體中文回答我，謝謝"
        )

        result = query_lm_studio(lm_studio_url, company_context, prompt)
        sys.stdout = original_stdout
        sys.stderr = original_stderr
        return result

    except Exception as e:
        print(f"執行過程中發生錯誤: {e}")
        sys.exit(1)


if __name__ == "__main__":
    if len(sys.argv) != 3:
        print(f"用法: python ai_suggestions.py <輸入文本> <描述>")
        sys.exit(1)

    # 參數初始化
    sys.stdin = io.TextIOWrapper(sys.stdin.buffer, encoding='utf-8')
    user_input = sys.stdin.read()
    # 將 JSON 格式字串轉換為字典
    try:
        user_input_dict = json.loads(user_input)
    except json.JSONDecodeError as e:
        print(f"JSON 解析錯誤: {e}")
        exit(1)

    # 處理去掉雙引號
    cleaned_data = {
        key.replace('"', ''): [item.replace('"', '') if isinstance(item, str) else item for item in value]
        for key, value in user_input_dict.items()
    }
    cleaned_data = json.dumps(cleaned_data, ensure_ascii=False, indent=2)
    cleaned_data = re.sub(r'\n', '', cleaned_data)
    description = sys.argv[2]
    lm_studio_url = "http://120.97.29.13:2345/v1"  # 替換為 LM Studio URL

    schema = {
        "classes": [
            {
                "class": "CompanyProfile",
                "description": "公司簡介",
                "properties": [
                    {"name": "companyName", "dataType": ["text"], "description": "公司名稱"},
                    {"name": "foundedDate", "dataType": ["text"], "description": "成立時間"},
                    {"name": "ipoDate", "dataType": ["date"], "description": "上櫃日期"},
                    {"name": "stockCode", "dataType": ["string"], "description": "股票代號"},
                    {"name": "mainProducts", "dataType": ["text"], "description": "主要產品"},
                    {"name": "companyAddress", "dataType": ["text"], "description": "公司地址"},
                    {"name": "phone", "dataType": ["string"], "description": "電話"},
                    {"name": "businessPhilosophy", "dataType": ["text"], "description": "經營理念"},
                    {"name": "businessStrategy", "dataType": ["text"], "description": "經營策略"},
                    {"name": "corporateMission", "dataType": ["text"], "description": "企業使命"}
                ]
            },
            {
                "class": "ProductInfo",
                "description": "產品資訊",
                "properties": [
                    {"name": "productSeries", "dataType": ["text"], "description": "產品系列"},
                    {"name": "productName", "dataType": ["text"], "description": "產品名稱"},
                    {"name": "productSpecifications", "dataType": ["text"], "description": "產品規格"}
                ]
            },
            {
                "class": "ConsolidatedRevenue",
                "description": "合併營業收入",
                "properties": [
                    {"name": "year", "dataType": ["int"], "description": "年度"},
                    {"name": "month", "dataType": ["int"], "description": "月份"},
                    {"name": "revenueLastYear", "dataType": ["number"], "description": "去年同期營收"},
                    {"name": "revenueCurrent", "dataType": ["number"], "description": "本期營收"},
                    {"name": "revenueChangePercent", "dataType": ["number"], "description": "增減百分比"}
                ]
            },
            {
                "class": "ShareholderInfo",
                "description": "股東資訊",
                "properties": [
                    {"name": "shareholderName", "dataType": ["text"], "description": "主要股東名稱"},
                    {"name": "sharesHeld", "dataType": ["int"], "description": "持有股數"},
                    {"name": "shareholdingPercentage", "dataType": ["number"], "description": "持股比例（百分比）"}
                ]
            },
            {
                "class": "DividendPolicy",
                "description": "股利政策",
                "properties": [
                    {"name": "year", "dataType": ["int"], "description": "年份"},
                    {"name": "cashDividendFromEarnings", "dataType": ["number"],
                     "description": "盈餘分配之現金股利 (元/股)"},
                    {"name": "cashFromStatutorySurplusAndCapitalReserve", "dataType": ["number"],
                     "description": "法定盈餘公積、資本公積發放之現金(元/股)"},
                    {"name": "stockDividendFromEarnings", "dataType": ["number"],
                     "description": "盈餘轉增資配股(元/股)"},
                    {"name": "stockFromStatutorySurplusAndCapitalReserve", "dataType": ["number"],
                     "description": "法定盈餘公積，資本公積轉增資配股(元/股)"}
                ]
            },
            {
                "class": "ComplaintChannel",
                "description": "申訴管道",
                "properties": [
                    {"name": "description", "dataType": ["text"], "description": "申訴管道說明"}
                ]
            },
            {
                "class": "SupplierManagementPolicy",
                "description": "供應商管理政策",
                "properties": [
                    {"name": "policyDescription", "dataType": ["text"], "description": "政策說明"}
                ]
            },
            {
                "class": "NewsCenter",
                "description": "新聞中心",
                "properties": [
                    {"name": "title", "dataType": ["text"], "description": "標題"},
                    {"name": "content", "dataType": ["text"], "description": "內文"},
                    {"name": "publishDate", "dataType": ["text"], "description": "發布日期"}
                ]
            },
            {
                "class": "Document",
                "description": "公司文件的段落、表格和圖片",
                "properties": [
                    {"name": "title", "dataType": ["text"], "description": "文件的標題"},
                    {"name": "text_content", "dataType": ["text"], "description": "段落的內容"},
                    {"name": "table_content", "dataType": ["text"], "description": "包含表格數據的 JSON 格式"},
                    {"name": "source", "dataType": ["text"], "description": "文檔的來源"},
                    {"name": "createDate", "dataType": ["date"], "description": "檔案的建立時間"}
                ]
            }
        ]
    }

    openai.api_key = ""

    # 初始化 Weaviate 客戶端
    client = weaviate.connect_to_custom(
        http_host="120.97.29.13",
        http_port=8090,
        http_secure=False,
        grpc_host="120.97.29.13",
        grpc_port=50051,
        grpc_secure=False,
        headers={
            "X-OpenAI-Api-Key": ""
        }
    )

    try:
        print(process_user_query(cleaned_data, description, schema, lm_studio_url))
    finally:
        client.close()

import sys
import io
from weaviate import Client as WeaviateClient
import requests  # 用於替代 OpenAI 客戶端呼叫 LM Studio

sys.stdout = io.TextIOWrapper(sys.stdout.buffer, encoding='utf-8')
sys.stderr = io.TextIOWrapper(sys.stderr.buffer, encoding='utf-8')

# 替代 OpenAI 客戶端的嵌入生成功能
def generate_embedding(description, lm_studio_url):
    # LM Studio 的嵌入向量生成請求
    response = requests.post(
        f"{lm_studio_url}/v1/embeddings",
        json={"model": "text-embedding-ada-002", "input": description},
    )
    response.raise_for_status()  # 確保請求成功
    return response.json()["data"][0]["embedding"]

# 主邏輯
def generate_ai_suggestion(description, lm_studio_url):
    import chardet  # 用於檢測編碼
    sys.stdin = io.TextIOWrapper(sys.stdin.buffer, encoding='utf-8', errors='ignore')

    # 嘗試讀取輸入數據
    raw_data = sys.stdin.buffer.read()
    detected_encoding = chardet.detect(raw_data)["encoding"]  # 檢測輸入數據的編碼
    if detected_encoding != "utf-8":
        data = raw_data.decode(detected_encoding, errors="ignore")  # 將輸入解碼為 UTF-8
    else:
        data = raw_data.decode("utf-8")

    # 初始化 Weaviate 客戶端
    vector_client = WeaviateClient(url="http://localhost:8090")  # 根據 Weaviate 端點修改

    # 將數據嵌入成向量
    query_vector = generate_embedding(description, lm_studio_url)

    # 在向量資料庫中查詢製造業相關資料
    retrieved_docs = vector_client.query.get("Product", ["name", "description", "category"]) \
        .with_near_vector({"vector": query_vector}).do()

    # 整理檢索到的文本內容作為上下文
    context = "\n".join([doc["description"] for doc in retrieved_docs["data"]["Get"]["Product"]])

    # 準備輸入的 Prompt
    prompt = (
        f"以下是生產數據表和檢索到的製造業背景資料。"
        f"請分析數據並找出規律或重要資訊，並給出相關的決策建議。請用繁體中文且用1000字回答：\n\n"
        f"{context}\n\n生產數據表：\n{data}"
    )

    # 傳送 Prompt 給 LM Studio 模型
    response = requests.post(
        f"{lm_studio_url}/v1/completions",
        json={
            "model": "nctu6/Llama3-TAIDE-LX-8B-Chat-Alpha1-GGUF",
            "prompt": prompt,
            "temperature": 0.5,
            "max_tokens": 1000,
        },
    )
    response.raise_for_status()  # 確保請求成功
    result = response.json()["choices"][0]["text"]

    print(result)


if __name__ == "__main__":
    if len(sys.argv) != 3:
        print(f"用法: python ai_assistant.py <type> <description>")
    elif sys.argv[1] == 'ai':
        lm_studio_url = "http://localhost:1234"  # LM Studio 的 API 端點
        generate_ai_suggestion(sys.argv[2], lm_studio_url)
    else:
        print("無效的參數，請使用 'ai'")




# import sys
# import io
# from openai import OpenAI
#
# sys.stdout = io.TextIOWrapper(sys.stdout.buffer, encoding='utf-8')
# sys.stderr = io.TextIOWrapper(sys.stderr.buffer, encoding='utf-8')
#
# def generate_ai_suggestion(description):
#     data = sys.stdin.read()
#
#     # 初始化 OpenAI 客戶端
#     client = OpenAI(base_url="http://localhost:1234/v1", api_key="lm-studio")
#
#     prompt = f"以下是生產數據表。請分析數據並找出規律或重要資訊。 請用繁體中文且用1000字回答我：\n{data}"
#
#     history = [
#         {"role": "system", "content": "你是一個智能助手。"
#                                       "你總是提供合理且正確且有幫助的答案。"
#                                       "你是一家公司中的高級主管。這是你的部門描述："
#                                       f"{description}"
#                                       "請告訴我根據我部門的情況，我的部門可以發展的方向或策略。"
#                                       "不要告訴我圖片中的趨勢是什麼，我希望得到根據數據提供的決策建議。"},
#         {"role": "user", "content": prompt},
#     ]
#
#     completion = client.chat.completions.create(
#         model="nctu6/Llama3-TAIDE-LX-8B-Chat-Alpha1-GGUF",
#         messages=history,
#         temperature=0.5,
#         max_tokens=1000
#     )
#
#     response = completion.choices[0].message.content
#
#     print(response)
#
#
# if __name__ == "__main__":
#     if len(sys.argv) != 3:
#         print(f"用法: python ai_assistant.py (type, description) {sys.argv}")
#     elif sys.argv[1] == 'ai':
#         generate_ai_suggestion(sys.argv[2])
#     else:
#         print("無效的參數，請使用 'ai'")

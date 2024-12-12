from weaviate import Client as WeaviateClient


# 初始化 Weaviate 客戶端
client = WeaviateClient(url="http://120.97.29.13:8090")

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
                {"name": "shareholderName","dataType": ["text"],"description": "主要股東名稱"},
                {"name": "sharesHeld","dataType": ["int"],"description": "持有股數"},
                {"name": "shareholdingPercentage","dataType": ["number"],"description": "持股比例（百分比）"}
            ]
        },
        {
            "class": "DividendPolicy",
            "description": "股利政策",
            "properties": [
                {"name": "year", "dataType": ["int"], "description": "年份"},
                {"name": "cashDividendFromEarnings", "dataType": ["number"], "description": "盈餘分配之現金股利 (元/股)"},
                {"name": "cashFromStatutorySurplusAndCapitalReserve", "dataType": ["number"], "description": "法定盈餘公積、資本公積發放之現金(元/股)"},
                {"name": "stockDividendFromEarnings", "dataType": ["number"], "description": "盈餘轉增資配股(元/股)"},
                {"name": "stockFromStatutorySurplusAndCapitalReserve", "dataType": ["number"], "description": "法定盈餘公積，資本公積轉增資配股(元/股)"}
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


# 檢查並創建 Schema
for class_definition in schema["classes"]:
    if not client.schema.contains(class_definition):
        client.schema.create_class(class_definition)
        print(f"成功創建 Schema: {class_definition['class']}")
    else:
        print(f"Schema 已存在: {class_definition['class']}")

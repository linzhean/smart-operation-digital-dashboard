# import weaviate
from weaviate import Client as WeaviateClient


# 初始化 Weaviate 客戶端
# client = weaviate.connect_to_custom(
#         http_host="120.97.29.13",
#         http_port=8090,
#         http_secure=False,
#         grpc_host="120.97.29.13",
#         grpc_port=50051,
#         grpc_secure=False,
#         headers={
#              "X-OpenAI-Api-Key": ""#         }
#     )
client = WeaviateClient(url="http://120.97.29.13:8090")


#公司簡介

data_object = {
    "companyName": "益張實業股份有限公司",
    "foundedDate": "1987",
    "ipoDate": "2017-11-23T00:00:00Z",
    "stockCode": "8342",
    "mainProducts": "商用置物設備、客製化產品、家用置物產品系列",
    "companyAddress": "彰化縣埤頭鄉興工路四號",
    "phone": "+886-4-8926130",
    "businessPhilosophy": "益張集團成立以來，以『誠信、團隊、速度、創新、共享』的經營理念，在不斷創造佳績。",
    "businessStrategy": "全面朝向高工藝的客定化產品服務,布局全球高階產品市場 創新產品設計 / 超越客戶期待 整合集團資源 / 優化效率管理 貫徹質量系統 / 落實全員管理",
    "corporateMission": "結合理念相同的伙伴，成為主動為顧客提升產品附加功能的最佳方案提供者!"
}

client.data_object.create(data_object, "CompanyProfile")

# 持股比例

shareholder_data_list = [
    {
        "shareholderName": "恆聚投資股份有限公司",
        "sharesHeld": 5761848,
        "shareholdingPercentage": 17.18
    },
    {
        "shareholderName": "鑫銳投資股份有限公司",
        "sharesHeld": 5503721,
        "shareholdingPercentage": 16.41
    },
    {
        "shareholderName": "益群投資股份有限公司",
        "sharesHeld": 5455833,
        "shareholdingPercentage": 16.27
    },
    {
        "shareholderName": "富隆達投資股份有限公司",
        "sharesHeld": 1533036,
        "shareholdingPercentage": 4.57
    },
    {
        "shareholderName": "茂騰投資股份有限公司",
        "sharesHeld": 1089000,
        "shareholdingPercentage": 3.25
    },
    {
        "shareholderName": "中盈投資開發股份有限公司",
        "sharesHeld": 1012000,
        "shareholdingPercentage": 3.02
    },
    {
        "shareholderName": "陳金定",
        "sharesHeld": 983034,
        "shareholdingPercentage": 2.93
    },
    {
        "shareholderName": "陳景松",
        "sharesHeld": 545000,
        "shareholdingPercentage": 1.63
    },
    {
        "shareholderName": "宥銓投資有限公司",
        "sharesHeld": 500000,
        "shareholdingPercentage": 1.49
    },
    {
        "shareholderName": "陳新賀",
        "sharesHeld": 483572,
        "shareholdingPercentage": 1.44
    },
]

# 批量插入資料
for data in shareholder_data_list:
    client.data_object.create(
        data_object=data,
        class_name="ShareholderInfo"
    )

# 股利政策

dividend_data_list = [
    {
        "year": 2023,
        "cashDividendFromEarnings": 3.8,
        "cashFromStatutorySurplusAndCapitalReserve": 0,
        "stockDividendFromEarnings": 0,
        "stockFromStatutorySurplusAndCapitalReserve": 0
    },
    {
        "year": 2022,
        "cashDividendFromEarnings": 4.5,
        "cashFromStatutorySurplusAndCapitalReserve": 0,
        "stockDividendFromEarnings": 0.0,
        "stockFromStatutorySurplusAndCapitalReserve": 0.0
    },
    {
        "year": 2021,
        "cashDividendFromEarnings": 3.4,
        "cashFromStatutorySurplusAndCapitalReserve": 0,
        "stockDividendFromEarnings": 0.0,
        "stockFromStatutorySurplusAndCapitalReserve": 0.0
    },
    {
        "year": 2020,
        "cashDividendFromEarnings": 3,
        "cashFromStatutorySurplusAndCapitalReserve": 0,
        "stockDividendFromEarnings": 0.0,
        "stockFromStatutorySurplusAndCapitalReserve": 0.0
    },
    {
        "year": 2019,
        "cashDividendFromEarnings": 2.0,
        "cashFromStatutorySurplusAndCapitalReserve": 1.5,
        "stockDividendFromEarnings": 0.0,
        "stockFromStatutorySurplusAndCapitalReserve": 0.0
    },
    {
        "year": 2018,
        "cashDividendFromEarnings": 2.0,
        "cashFromStatutorySurplusAndCapitalReserve": 1.5,
        "stockDividendFromEarnings": 0.0,
        "stockFromStatutorySurplusAndCapitalReserve": 0.0
    },
    {
        "year": 2017,
        "cashDividendFromEarnings": 3.0,
        "cashFromStatutorySurplusAndCapitalReserve": 0.0,
        "stockDividendFromEarnings": 0.0,
        "stockFromStatutorySurplusAndCapitalReserve": 0.0
    },
    {
        "year": 2016,
        "cashDividendFromEarnings": 5.8,
        "cashFromStatutorySurplusAndCapitalReserve": 0,
        "stockDividendFromEarnings": 0.0,
        "stockFromStatutorySurplusAndCapitalReserve": 0.0
    }
]

# 批量插入資料
for data in dividend_data_list:
    client.data_object.create(
        data_object=data,
        class_name="DividendPolicy"
    )

# 申訴管道
complaint_channel_data = {
    "description": """公司內、外部人員對於不合法與不道德行為的檢舉制度 
一、本公司設置檢舉信箱如下： 
1.內部檢舉信箱： 
書面投遞或郵寄稽核室信箱 
稽核室電子郵件信箱：ij0070@ijang.com.tw 
2.外部檢舉信箱： 
稽核室電子郵件信箱：ij0070@ijang.com.tw 
二、本公司針對不合法(包括貪汙)與不道德行為的檢舉制度之完整處理流程如 
下： 
1.本公司鼓勵內部及外部人員檢舉不誠信行為。內部人員如舉發屬實者， 
本公司得給予適當之獎勵，如有虛報或惡意指控之情事，應予以紀律處 
分，情節重大者應予以革職。 
2.本公司於公司網站及內部網站建立並公告檢舉信箱供本公司內部及外部 
人員使用。 
檢舉人得匿名檢舉，但應至少提供下列資訊： 
(1) 被檢舉人之姓名或其他足資識別被檢舉人身分特徵之資料。 
(2) 可供調查之具體事證。 
3.本公司處理檢舉情事之相關人員應對於檢舉人身分及檢舉內容予以保密 
，本公司並承諾保護檢舉人不因檢舉情事而遭不當處置。 
本公司專責單位應依下列程序處理檢舉情事： 
(1) 檢舉情事涉及一般員工者應呈報至部門主管，檢舉情事涉及董事或 
高階主管，應呈報至審計委員會。 
(2) 前款受呈報之主管或人員應即刻查明相關事實，必要時由本公司專 
責單位、人資單位、法規遵循或其他相關部門提供協助。 
(3) 如經證實被檢舉人確有違反相關法令或本公司誠信經營政策與規定 
者，應立即要求被檢舉人停止相關行為，並為適當之處置，且必要 
時向主管機關報告、移送司法機關偵辦，或透過法律程序請求損害 
賠償，以維護公司之名譽及權益。 
(4) 檢舉受理、調查過程、調查結果均應留存書面文件，並保存五年， 
其保存得以電子方式為之。保存期限未屆滿前，發生與檢舉內容相 
關之訴訟時，相關資料應續予保存至訴訟終結止。 
(5) 對於檢舉情事經查證屬實，應責成本公司相關單位檢討相關內部控 
制制度及作業程序，並提出改善措施，以杜絕相同行為再次發生。 
(6) 本公司專責單位得視各該檢舉案件之情節輕重，要求相關單位報告 
檢舉情事、其處理方式及後續檢討改善措施。 """
}

# 插入資料到 ComplaintChannel 類別
client.data_object.create(
    data_object=complaint_channel_data,
    class_name="ComplaintChannel"
)

# 供應商管理政策
supplier_policy_data_list = {
    "policyDescription": """本公司制定供應商管理政策，除了確保供應商供貨的品質、交期、價格及服務之能力外，並要求供應商必須符合安全衛生、環保、人權等相關規範，共同致力提升企業社會責任，共創永續發展的合作夥伴關係：
                                1. 本公司對新供應商進行評鑑，在品質/財務/價格/安全衛生/環保等項目評核，通過評鑑者才可成為合格供應商。 
                                2. 本公司要求供應商落實環保、安全衛生、人權等相關規範，善盡企業的社會責任。
                                3. 本公司要求供應商所提供之商品應符合所有政府相關法規，不會造成消費者健康安全及環保上之危害。
                                4. 本公司要求供應商履行合約時應秉持誠實信用原則，不得違反政府相關法規及公序良俗。 
                                5. 供應商如違反本公司相關約定或政府相關法規時，本公司得要求供應商立即進行改善、產品下架、終止契約 或賠償本公司因此所受損害。"""
}

client.data_object.create(
    data_object=supplier_policy_data_list,
    class_name="SupplierManagementPolicy"
)


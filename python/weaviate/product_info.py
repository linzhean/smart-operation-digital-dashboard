import requests
from bs4 import BeautifulSoup
import json
import weaviate
# from weaviate import Client as WeaviateClient


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
# client = WeaviateClient(url="http://120.97.29.13:8090")


# URL 陣列
urls = [
    "https://www.ijang.com.tw/product-kitchen-waste-recycling-cart-in4001.html",
    "https://www.ijang.com.tw/product-stainless-steel-work-table-in1097.html",
    "https://www.ijang.com.tw/product-stainless-steel-working-cart-in1096.html",
    "https://www.ijang.com.tw/product-wine-rack-hc3012.html",
    "https://www.ijang.com.tw/product-multifunction-wagon-hc0198.html",
    "https://www.ijang.com.tw/product-pizza-rack-ac0170.html",
    "https://www.ijang.com.tw/product-commercial-furniture-ac0168.html",
    "https://www.ijang.com.tw/product-stainless-steel-trolley-in1114.html",
    "https://www.ijang.com.tw/product-stainless-steel-trolley-in1113.html",
    "https://www.ijang.com.tw/product-stainless-steel-work-table-in1025.html",
    "https://www.ijang.com.tw/product-pan-racks-in1005.html",
    "https://www.ijang.com.tw/product-stainless-steel-storage-rack-in1004.html",
    "https://www.ijang.com.tw/product-pizza-rack-ac0171.html",
    "https://www.ijang.com.tw/product-Power-stroll-Power-stroll.html",
    "https://www.ijang.com.tw/product-storage-cart-in6007.html",
    "https://www.ijang.com.tw/product-tool-storage-cart-in3130.html",
    "https://www.ijang.com.tw/product-commercial-furniture-in1116.html",
    "https://www.ijang.com.tw/product-commercial-furniture-in1115.html",
    "https://www.ijang.com.tw/product-u-boat-in1110.html",
    "https://www.ijang.com.tw/product-heavy-duty-wire-carts-in1109.html",
    "https://www.ijang.com.tw/product-stainless-steel-commercial-cart-in1105.html",
    "https://www.ijang.com.tw/product-pallet-storage-shelf-in0082.html",
    "https://www.ijang.com.tw/product-three-layers-pallet-shelf-in1080.html",
    "https://www.ijang.com.tw/product-msr.html",
    "https://www.ijang.com.tw/product-cylinder-trolley-in1090.html",
    "https://www.ijang.com.tw/product-stainless-steel-security-cart-in1084.html",
    "https://www.ijang.com.tw/product-metal-pallet-in1043.html",
    "https://www.ijang.com.tw/product-cargo-trolley-in1126.html",
    "https://www.ijang.com.tw/product-tool-cart-in3005.html",
    "https://www.ijang.com.tw/product-tool-car-in3004.html",
    "https://www.ijang.com.tw/product-cargo-trolley-in1120.html",
    "https://www.ijang.com.tw/product-cargo-trolley-in1117.html",
    "https://www.ijang.com.tw/product-five-layers-display-stand-in1081.html",
    "https://www.ijang.com.tw/product-iv-stand-in0035.html",
    "https://www.ijang.com.tw/product-mobile-cart-in0019.html",
    "https://www.ijang.com.tw/product-storage-rack-in0018.html",
    "https://www.ijang.com.tw/product-hospital-trolley-ac1162.html",
    "https://www.ijang.com.tw/product-hospital-trolley-ac1161.html",
    "https://www.ijang.com.tw/product-hospital-trolley-ac1160.html",
    "https://www.ijang.com.tw/product-hospital-trolley-ac1159.html",
    "https://www.ijang.com.tw/product-hospital-trolley-ac1156.html",
    "https://www.ijang.com.tw/product-document-cart-ac1009.html",
    "https://www.ijang.com.tw/product-binder-cart-in1099.html",
    "https://www.ijang.com.tw/product-stainless-steel-wire-cart-in1098.html",
    "https://www.ijang.com.tw/product-binder-cart-ac1246.html",
    "https://www.ijang.com.tw/product-drive-medical-hamper-stand-in0043.html",
    "https://www.ijang.com.tw/product-wre-shelving-with-cover-ac1158.html",
    "https://www.ijang.com.tw/product-drive-medical-hamper-stand-in2320.html",
    "https://www.ijang.com.tw/product-stainless-steel-cart-in1129.html",
    "https://www.ijang.com.tw/product-stainless-steel-cart-in1100.html",
    "https://www.ijang.com.tw/product-display-storage-mobile-rack-in5044.html",
    "https://www.ijang.com.tw/product-back-basket-rack-in5042.html",
    "https://www.ijang.com.tw/product-five-baskets-mobile-rack-in5041.html",
    "https://www.ijang.com.tw/product-Mobile-Basket-Rack-IN5040.html",
    "https://www.ijang.com.tw/product-basket-rack-in5039.html",
    "https://www.ijang.com.tw/product-flower-rack-in5037.html",
    "https://www.ijang.com.tw/product-display-mobile-rack-in5036.html",
    "https://www.ijang.com.tw/product-flower-rack-in5033.html",
    "https://www.ijang.com.tw/product-brown-display-rack-in5031.html",
    "https://www.ijang.com.tw/product-three-baskets-display-rack-in5024.html",
    "https://www.ijang.com.tw/product-mobile-storage-rack-in5020.html",
    "https://www.ijang.com.tw/product-moving-wire-display-rack-in5019.html",
    "https://www.ijang.com.tw/product-display-wire-rack-with-baskets-in5017.html",
    "https://www.ijang.com.tw/product-four-layers-display-wire-rack-ac0166.html",
    "https://www.ijang.com.tw/product-fruit-rack-in5032.html",
    "https://www.ijang.com.tw/product-display-rack-in5030.html",
    "https://www.ijang.com.tw/product-flower-rack-in5038.html",
    "https://www.ijang.com.tw/product-display-rack-in1092.html",
    "https://www.ijang.com.tw/product-display-table-in5043.html",
    "https://www.ijang.com.tw/product-shopping-basket-stand-in1076.html",
    "https://www.ijang.com.tw/product-wire-rotary-displays-in1038.html",
    "https://www.ijang.com.tw/product-fruit-rack-in5035.html",
    "https://www.ijang.com.tw/product-mesh-shop-display-stand-ac0071.html",
    "https://www.ijang.com.tw/product-solid-stainless-steel-shelving-in0049.html",
    "https://www.ijang.com.tw/product-solid-stainless-steel-shelves-trolley-in0048.html",
    "https://www.ijang.com.tw/product-stainless-steel-mobile-shelving-in1147.html",
    "https://www.ijang.com.tw/product-solid-stainless-steel-shelves-in1150.html",
    "https://www.ijang.com.tw/product-mobile-shelving-in1149.html",
    "https://www.ijang.com.tw/product-the-security-carts-in1148.html",
    "https://www.ijang.com.tw/product-solid-stainless-steel-wire-carts-in1146.html",
    "https://www.ijang.com.tw/product-display-stand-in5050.html",
    "https://www.ijang.com.tw/product-agv-customized-pallet-in1134.html",
    "https://www.ijang.com.tw/product-foldable-display-rack-in5018.html",
    "https://www.ijang.com.tw/product-display-stand-in5016.html",
    "https://www.ijang.com.tw/product-4-tier-wire-shelving-ws1234.html",
    "https://www.ijang.com.tw/product-workbench-in3133.html",
    "https://www.ijang.com.tw/product-multifunctionwagon-hc0197.html",
    "https://www.ijang.com.tw/product-foldable-trolley-hc0189.html",
    "https://www.ijang.com.tw/product-foldabletrolley-hc0169.html",
    "https://www.ijang.com.tw/product-stool-top-rack-bc3303.html",
    "https://www.ijang.com.tw/product-metal-wardrobe-ac6915.html",
    "https://www.ijang.com.tw/product-clothes-rack-ac6117.html",
    "https://www.ijang.com.tw/product-4-layer-rack-ac1231.html",
    "https://www.ijang.com.tw/product-6-tiershelving-ac0152.html",
    "https://www.ijang.com.tw/product-5-tier-heavy-duty-shelving-ac0164.html",
    "https://www.ijang.com.tw/product-5-tier-heavy-duty-shelving-ac0165.html",
    "https://www.ijang.com.tw/product-5-tier-wagon-ac0162.html",
    "https://www.ijang.com.tw/product-5-tier-shelving-ac0155.html",
    "https://www.ijang.com.tw/product-5-tier-shelving-ac0036.html",
    "https://www.ijang.com.tw/product-5-tier-shelving-ac0001.html",
    "https://www.ijang.com.tw/product-5-tier-shelving%E6%9E%B6-ac1636.html",
    "https://www.ijang.com.tw/product-4-tier-wagon-ws1236.html",
    "https://www.ijang.com.tw/product-4-tier-heavy-duty-shelving-ac0163.html",
    "https://www.ijang.com.tw/product-4-tier-magazine-rack-ac0150.html",
    "https://www.ijang.com.tw/product-4-tier-shelving-ac1122.html",
    "https://www.ijang.com.tw/product-4-tier-shelving-ac1436.html",
    "https://www.ijang.com.tw/product-4-tier-shelving-ac0159.html",
    "https://www.ijang.com.tw/product-4-tier-shelving-ac0158.html",
    "https://www.ijang.com.tw/product-4-tier-shelving-ac0058.html",
    "https://www.ijang.com.tw/product-4-tier-shelving-ac0157.html",
    "https://www.ijang.com.tw/product-4-tier-slimline-shelving-ac0161.html",
    "https://www.ijang.com.tw/product-4-tier-shelving-ac0153.html",
    "https://www.ijang.com.tw/product-4-tier-corner-shelving-ac0610.html",
    "https://www.ijang.com.tw/product-4-tier-corner-shelving-ac0151.html",
    "https://www.ijang.com.tw/product-corner-shelving-ac0002.html",
    "https://www.ijang.com.tw/product-3-tier-slimline-shelving-ac0160.html",
    "https://www.ijang.com.tw/product-3-tier-wagon-hc0195.html",
    "https://www.ijang.com.tw/product-3-tier-wagon-ac1232.html",
    "https://www.ijang.com.tw/product-3-tier-wagon-hc0194.html",
    "https://www.ijang.com.tw/product-3-tier-wagon-hc0196.html",
    "https://www.ijang.com.tw/product-3-tier-shelving-ac0116.html",
    "https://www.ijang.com.tw/product-3-tier-shelving-ac0119.html",
    "https://www.ijang.com.tw/product-3-tier-shelving-ac0055.html",
    "https://www.ijang.com.tw/product-3-tier-shelving-ac0032.html",
    "https://www.ijang.com.tw/product-2-tier-shelving-ac0038.html",
]

# 爬取產品資訊
def scrape_product_info(url):
    try:
        response = requests.get(url)
        response.encoding = "utf-8"
        soup = BeautifulSoup(response.text, "html.parser")

        # 提取產品資訊
        product_series = soup.find("ul", class_="breadcrumb").find_all("a")[3].text.strip()  # 產品系列
        product_name = soup.select_one("#name")["value"].replace('"', '')  # 產品名稱
        product_specifications = soup.find_all('p', class_='MsoNormal')[1].get_text(strip=True)
        product_features = None  # 預設為 null，若有相應資訊可更新
        feature_node = soup.find("div", class_="product-features")
        if feature_node:
            product_features = feature_node.text.strip()

        # 返回格式化資料
        return {"productSeries": product_series,
                "productName": product_name,
                "productSpecifications": product_specifications}
    except Exception as e:
        print(f"訪問 {url} 時出錯: {e}")
        return None

# 遍歷 URL 陣列
product_info_list = []
for url in urls:
    product_info = scrape_product_info(url)
    if product_info:
        product_info_list.append(product_info)

print(len(product_info_list))

# 將結果寫入 weaviate
for data in product_info_list:
    productInfo = client.collections.get("ProductInfo")
    productInfo.data.insert(properties=data)

client.close()

print(f"已將結果都寫入weaviate")

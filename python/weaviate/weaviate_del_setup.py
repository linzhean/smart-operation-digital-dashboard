# import weaviate
from weaviate import Client as WeaviateClient

# 連接 Weaviate 伺服器
# client = weaviate.connect_to_custom(
#         http_host="120.97.29.13",
#         http_port=8090,
#         http_secure=False,
#         grpc_host="120.97.29.13",
#         grpc_port=50051,
#         grpc_secure=False,
#         headers={
#              "X-OpenAI-Api-Key": ""
#         }
#     )
client = WeaviateClient(url="http://120.97.29.13:8090")


# 刪除指定 Class（清空資料）
class_name = "NewsCenter"  # 替換成您要刪除的 Class 名稱
client.schema.delete_class(class_name)

print(f"Class '{class_name}' 已刪除。")
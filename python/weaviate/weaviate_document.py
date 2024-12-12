import os
import pdfplumber
import weaviate
import datetime

# 設定 Weaviate 連接
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

# Step 1: 掃描資料夾中的所有 PDF 檔案
def get_all_pdf_files(directory_path):
    """獲取指定目錄下的所有 PDF 文件"""
    return [os.path.join(directory_path, file) for file in os.listdir(directory_path) if file.endswith('.pdf')]

# Step 2: 提取 PDF 中的段落和表格
def extract_text_from_pdf(pdf_path):
    """ 從 PDF 中提取段落文字 """
    paragraphs = []
    with pdfplumber.open(pdf_path) as pdf:
        for page_number, page in enumerate(pdf.pages, start=1):
            text = page.extract_text()
            if text:
                paragraphs.append(text.strip())
    return paragraphs

def extract_tables_from_pdf(pdf_path):
    """ 從 PDF 中提取表格 """
    tables = []
    with pdfplumber.open(pdf_path) as pdf:
        for page_number, page in enumerate(pdf.pages, start=1):
            table = page.extract_table()
            if table:
                tables.append({
                    "page": page_number,
                    "table": table
                })
    return tables

# Step 3: 將數據結構化並存入 Weaviate
def prepare_document_object(file_name, paragraphs, tables):
    """ 構造 Weaviate 中的 Document 數據結構 """
    document_data = {
        "title": f"文件: {file_name}",
        "text_content": "\n".join(paragraphs),
        "table_content": str(tables),  # 存為字符串，或可以轉成 JSON 格式
        "source": "內部 PDF 文件",
        "createDate": datetime.datetime.now(datetime.timezone.utc)
        .isoformat(timespec='milliseconds').replace('+00:00', 'Z')
    }
    return document_data

def upload_to_weaviate(document_data):
    """ 將文件的數據存入 Weaviate """
    document = client.collections.get("Document")
    document.data.insert(properties=document_data)


# Step 4: 遍歷每個 PDF 檔案，擷取數據，並存入 Weaviate
def process_all_pdfs_in_folder(folder_path):
    """ 讀取文件夾中的所有 PDF，並將它們的數據存入 Weaviate """
    pdf_files = get_all_pdf_files(folder_path)
    for i, pdf_path in enumerate(pdf_files):
        print(f"開始處理文件: {pdf_path} ({i+1}/{len(pdf_files)})")

        # 提取 PDF 內容
        paragraphs = extract_text_from_pdf(pdf_path)
        tables = extract_tables_from_pdf(pdf_path)

        # 構造文件數據
        file_name = os.path.basename(pdf_path)
        document_data = prepare_document_object(file_name, paragraphs, tables)

        # 將數據上傳至 Weaviate
        upload_to_weaviate(document_data)
        print(f"文件 {file_name} 已成功上傳到 Weaviate！")

# Step 5: 執行主程序
if __name__ == "__main__":
    folder_path = r'C:\\Users\\Jerrylin\\python-project\\weaviate\\company_file'  # PDF 文件夾路徑
    process_all_pdfs_in_folder(folder_path)

import requests
from bs4 import BeautifulSoup
import json
import weaviate

# from weaviate import Client as WeaviateClient


client = weaviate.connect_to_custom(
    http_host="120.97.29.13",
    http_port=8090,
    http_secure=False,
    grpc_host="120.97.29.13",
    grpc_port=50051,
    grpc_secure=False,
    headers={
        "X-OpenAI-Api-Key": ""}
)


# client = WeaviateClient(url="http://120.97.29.13:8090")


def scrape_links_from_tbody(url):
    try:
        # 發送 HTTP 請求取得網頁內容
        response = requests.get(url)
        response.raise_for_status()  # 確保請求成功
        soup = BeautifulSoup(response.text, 'html.parser')

        # 找到 <tbody>
        tbody = soup.find('tbody')
        if tbody:
            data = []
            rows = tbody.find_all('tr')  # 找到所有 <tr>
            for row in rows:
                # 提取 <a> 的 href 和文字內容
                a_tag = row.find('a', href=True)
                link = a_tag['href'] if a_tag else None
                text = a_tag.text.strip() if a_tag else None

                # 提取日期（假設日期是最後一個 <td>）
                tds = row.find_all('td')
                date = tds[-1].text.strip() if tds else None

                # 添加到資料列表
                data.append({'href': link, 'title': text, 'date': date})
            return data
        else:
            print("找不到 <tbody> 元素")
            return []
    except requests.exceptions.RequestException as e:
        print(f"HTTP請求發生錯誤：{e}")
    except Exception as e:
        print(f"處理過程中出現問題：{e}")
        return []


# 爬取產品資訊
def scrape_news_center(url, title, date):
    try:
        response = requests.get(url)
        response.encoding = "utf-8"
        soup = BeautifulSoup(response.text, "html.parser")

        # 找到 <tbody> 標籤並提取其中的所有文字
        tbody = soup.find("tbody")
        text = tbody.get_text(separator=" ", strip=True)  # 用空格分隔段落，移除多餘空白

        # 返回格式化資料
        return {"title": title,
                "content": text,
                "publishDate": date, }
    except Exception as e:
        print(f"訪問 {url} 時出錯: {e}")
        return None


# 主程式
if __name__ == "__main__":
    # 替換成你的目標網址
    urls = [
        "https://www.ijang.com.tw/msg/message-%E5%85%AC%E5%8F%B8%E5%8B%95%E6%85%8B-46_off1_mid46_cid3_pid46.html",
        "https://www.ijang.com.tw/msg/message-%E5%85%AC%E5%8F%B8%E5%8B%95%E6%85%8B-46_off0_mid46_cid3_pid46.html",
        "https://www.ijang.com.tw/msg/message-%E5%85%AC%E5%8F%B8%E5%8B%95%E6%85%8B-46_off2_mid46_cid3_pid46.html"
    ]
    for url in urls:
        news_list = scrape_links_from_tbody(url)
        for news in news_list:
            # 將結果寫入 weaviate'
            href = news.get("href")
            title = news.get("title")
            date = news.get("date")
            data = scrape_news_center(href, title, date)
            newsCenter = client.collections.get("NewsCenter")
            newsCenter.data.insert(properties=data)

    client.close()

    print(f"已將結果都寫入weaviate")

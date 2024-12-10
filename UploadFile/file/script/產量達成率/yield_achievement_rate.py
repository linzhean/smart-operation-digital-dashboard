import json
import pandas as pd
import plotly.graph_objects as go
import plotly.io as pio

def generate_html_chart(file_name):
    import sys

    # 從標準輸入讀取 JSON 字串，確保使用 UTF-8 編碼
    data = sys.stdin.read()

    # 將 JSON 轉換為 DataFrame
    df = pd.DataFrame(json.loads(data))

    # 圖表讀資料生成圖表
    # 資料表 EISLE
    # 欄位
    # LE005 productNumber
    # LE010 date
    # LE011 expectedOutput
    # LE012 productionVolume
    # LE012 advanceQuantity

    # 確保日期欄位格式正確
    df['date'] = pd.to_datetime(df['date'])

    # 修正品名亂碼（如果需要，保證編碼一致性）
    df['productNumber'] = df['productNumber'].astype(str)

    # 篩選最近七天的數據
    recent_data = df[df['date'] >= pd.to_datetime('today') - pd.Timedelta(days=7)]

    # 確保每個品號都有一列，沒有數據的補 0
    all_product_numbers = df['productNumber'].unique()
    grouped_data = recent_data.groupby('productNumber').agg({
        'yieldAchievementRate': 'mean'  # 計算每個品名的平均達成率
    }).reindex(all_product_numbers, fill_value=0).reset_index()

    # 創建圖表
    fig = go.Figure()

    # 添加長條圖：品名為 x 軸，七天綜合的產量達成率為 y 軸
    fig.add_trace(go.Bar(
        x=grouped_data['productNumber'],
        y=grouped_data['yieldAchievementRate'],
        text=grouped_data['yieldAchievementRate'].round(2),  # 顯示達成率，保留兩位小數
        textposition='auto',
        marker=dict(
            color=grouped_data['yieldAchievementRate'],  # 根據達成率設置顏色
            colorscale='Teal',  # 藍綠漸層
            cmin=grouped_data['yieldAchievementRate'].min(),
            cmax=grouped_data['yieldAchievementRate'].max(),
            colorbar=dict(
                title="達成率",  # 顏色條標題
            )
        )
    ))

    # 設定中文字體與 X 軸標籤顯示
    fig.update_layout(
        title='各產品的近七天綜合產量達成率長條圖',
        xaxis_title='品名',
        yaxis_title='平均產量達成率 (%)',
        font=dict(
            family="Microsoft JhengHei, PingFang TC, Arial, sans-serif",  # 中文字體
            size=14
        ),
        xaxis=dict(
            tickangle=45,  # 標籤旋轉 45 度
            tickmode='linear'  # 確保所有標籤均顯示
        ),
        yaxis=dict(
            autorange=True
        ),
        autosize=True
    )

    # 儲存圖表為互動式 HTML
    pio.write_html(fig, file_name)

# 這裡直接複製
if __name__ == "__main__":
    import sys

    if len(sys.argv) != 3:
        print(f"用法: python yield_achievement_rate.py html|photo file_name")
    elif sys.argv[1] == 'html':
        generate_html_chart(sys.argv[2])
    elif sys.argv[1] == 'photo':
        generate_html_chart(sys.argv[2])
    else:
        print(f"無效的參數，請使用 'html' 或 'photo'")

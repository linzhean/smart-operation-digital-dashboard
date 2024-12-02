import json
import pandas as pd
import plotly.graph_objects as go
import plotly.io as pio

def generate_html_chart(file_name):
    # 從標準輸入讀取 JSON 字串
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

    # 將日期轉換為日期格式
    df['date'] = pd.to_datetime(df['date'])

    # 篩選最近七天的數據
    df = df[df['date'] >= pd.to_datetime('today') - pd.Timedelta(days=7)]

    # 將數據轉換為數字型
    product_numbers = df['productNumber']

    # 依照品號進行分組，並計算每個品號在七天內的平均產量達成率
    grouped_data = df.groupby('productNumber').agg({
        'yieldAchievementRate': 'mean'  # 計算每個品號的平均達成率
    }).reset_index()

    # 創建圖表
    fig = go.Figure()

    # 添加長條圖：品號為 x 軸，七天綜合的產量達成率為 y 軸
    fig.add_trace(go.Bar(
        x=grouped_data['productNumber'],
        y=grouped_data['yieldAchievementRate'],
        text=grouped_data['yieldAchievementRate'],  # 在圖表中顯示達成率數字
        textposition='auto',
        marker=dict(
            color=grouped_data['yieldAchievementRate'],  # 使用達成率數據來設置顏色
            colorscale='Teal',  # 設置為藍綠到白色的漸層
            cmin=grouped_data['yieldAchievementRate'].min(),  # 設置最小值
            cmax=grouped_data['yieldAchievementRate'].max(),  # 設置最大值
            colorbar=dict(
                title="達成率",  # 顏色條標題
            )
        )
    ))

    # 設定圖表標題與軸標籤
    fig.update_layout(
        title='各品號的近七天綜合產量達成率長條圖',
        xaxis_title='品號',
        yaxis_title='平均產量達成率 (%)',
        xaxis=dict(autorange=True),
        yaxis=dict(autorange=True),
        autosize=True
    )

    # 儲存圖表為互動式 HTML
    pio.write_html(fig, file_name)

# 這裡直接複製
if __name__ == "__main__":
    import sys

    if len(sys.argv) != 3:
        print(f"用法: python yield_achievement_rate.py (filePath, type, fileName) {sys.argv}")
    elif sys.argv[1] == 'html':
        generate_html_chart(sys.argv[2])
    elif sys.argv[1] == 'photo':
        generate_html_chart(sys.argv[2])
    else:
        print(f"無效的參數，請使用 'html' 或 'photo'")

import json
import pandas as pd
import plotly.graph_objects as go
import plotly.io as pio

def generate_html_chart(file_name):
    # 從標準輸入讀取 JSON 字串
    data = sys.stdin.read()

    # 將 JSON 轉換為 DataFrame
    df = pd.DataFrame(json.loads(data))

    # 將日期轉換為日期格式
    df['date'] = pd.to_datetime(df['date'])

    # 將數據轉換為數字型
    product_numbers = df['productNumber']

    # 創建圖表
    fig = go.Figure()

    # 依照品號進行分組，為每個品號生成一條長條圖
    for product_number in product_numbers.unique():
        product_data = df[product_numbers == product_number]

        # 添加長條圖：品號為名稱，日期為 x 軸，產量達成率為 y 軸
        fig.add_trace(go.Bar(
            x=product_data['date'],
            y=product_data['yieldAchievementRate'],
            name=product_number,  # 品號作為長條圖的名稱
            text=product_data['yieldAchievementRate'],  # 在圖表中顯示達成率數字
            textposition='auto',
            marker=dict(
                color=product_data['yieldAchievementRate'],  # 使用達成率數據來設置顏色
                colorscale='Teal',  # 設置為藍綠到白色的漸層
                cmin=product_data['yieldAchievementRate'].min(),  # 設置最小值
                cmax=product_data['yieldAchievementRate'].max(),  # 設置最大值
                colorbar=dict(
                    title="達成率",  # 顏色條標題
                )
            )
        ))

    # 設定圖表標題與軸標籤
    fig.update_layout(
        title='各品號的產量達成率長條圖',
        xaxis_title='日期',
        yaxis_title='產量達成率 (%)',
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

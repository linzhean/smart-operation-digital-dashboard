import json
import pandas as pd
import plotly.graph_objects as go
import plotly.io as pio
import io

def generate_html_chart(file_name):
    import sys
    sys.stdin = io.TextIOWrapper(sys.stdin.buffer, encoding='utf-8')
    # 從標準輸入讀取 JSON 字串
    data = sys.stdin.read()

    # 將 JSON 轉換為 DataFrame
    df = pd.DataFrame(json.loads(data))

    # 圖表讀資料生成圖表
    # 資料表 SASLA
    # 欄位 LF005 productNumber LF015 date LF016 salesVolume LF019 refundVolume

    # 將日期轉換為日期格式
    df['date'] = pd.to_datetime(df['date'])

    # 計算最近一個月的日期範圍
    recent_date = df['date'].max()
    start_date = recent_date - pd.Timedelta(days=30)

    product_numbers = df['productNumber'].unique()

    # 創建圖表
    fig = go.Figure()

    # 依照品名進行分組，為每個品名生成一條線
    for product_number in product_numbers:
        product_data = df[df['productNumber'] == product_number]

        # 添加折線圖：品名為名稱，日期為 x 軸，銷售退貨率為 y 軸
        fig.add_trace(go.Scatter(
            x=product_data['date'],
            y=product_data['refundRate'],
            mode='lines+markers',
            name=product_number,  # 品名作為線的名稱
            line=dict(width=2),
            marker=dict(size=6)
        ))

    # 設定圖表標題與軸標籤
    fig.update_layout(
        title='各產品的銷售退貨率折線圖',
        xaxis_title='日期',
        yaxis_title='退貨率 (%)',
        xaxis=dict(
            autorange=False,  # 設定自動範圍為 False
            range=[start_date, recent_date],  # 聚焦近一個月
            rangeslider=dict(visible=True),  # 啟用滑動條功能
            type="date"
        ),
        yaxis=dict(autorange=True),  # 讓 Y 軸根據數據自動調整
        autosize=True,
        legend=dict(
            title="品名",
            orientation="v",  # 垂直排列
            yanchor="top",
            y=1,
            xanchor="left",
            x=1.05,  # 將圖例移動到圖表右側
            font=dict(size=10)  # 調整文字大小
        ),
        showlegend=True  # 確保顯示圖例
    )

    # 圖表讀資料生成圖表
    # 儲存圖表為互動式 HTML
    pio.write_html(fig, file_name)

# 這裡直接複製
if __name__ == "__main__":
    import sys

    if len(sys.argv) != 3:
        print(f"用法: python refund_rate.py"
              f"ate.py (filePath, type, fileName) {sys.argv}")
    elif sys.argv[1] == 'html':
        generate_html_chart(sys.argv[2])
    elif sys.argv[1] == 'photo':
        generate_html_chart(sys.argv[2])
    else:
        print(f"無效的參數，請使用 'html' 或 'photo'")
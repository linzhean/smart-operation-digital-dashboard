import json
import pandas as pd
import plotly.graph_objects as go
import plotly.io as pio
import numpy as np
import io


def generate_html_chart(file_name):
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

    product_numbers = df['productNumber'].unique()

    # 創建圖表
    fig = go.Figure()

    # 依照品號進行分組，為每個品號生成一條線
    for product_number in product_numbers:
        product_data = df[df['productNumber'] == product_number]

        # 計算峰值與谷值
        data = product_data['refundRate'].values
        doublediff = np.diff(np.sign(np.diff(data)))
        peak_locations = np.where(doublediff == -2)[0] + 1
        trough_locations = np.where(doublediff == 2)[0] + 1

        # 添加折線圖：品號為名稱，日期為 x 軸，銷售退貨率為 y 軸
        fig.add_trace(go.Scatter(
            x=product_data['date'],
            y=product_data['refundRate'],
            mode='lines+markers',
            name=product_number,  # 品號作為線的名稱
            line=dict(width=2),
            marker=dict(size=6)
        ))

        # 添加峰值標記
        fig.add_trace(go.Scatter(
            x=product_data['date'].iloc[peak_locations],
            y=product_data['refundRate'].iloc[peak_locations],
            mode='markers',
            marker=dict(size=10, color='green', symbol='triangle-up'),
            name=f"{product_number} 峰值"
        ))

        # 添加谷值標記
        fig.add_trace(go.Scatter(
            x=product_data['date'].iloc[trough_locations],
            y=product_data['refundRate'].iloc[trough_locations],
            mode='markers',
            marker=dict(size=10, color='red', symbol='triangle-down'),
            name=f"{product_number} 谷值"
        ))

    # 設定圖表標題與軸標籤
    fig.update_layout(
        title='各品號的銷售退貨率折線圖',
        xaxis_title='日期',
        yaxis_title='退貨率 (%)',
        xaxis=dict(autorange=True),
        yaxis=dict(autorange=True),  # 可根據數據調整範圍
        autosize=True,
        legend_title="品號",  # 顯示圖表旁邊的品號標籤
        showlegend=True,  # 確保顯示圖例
    )

    # 儲存圖表為互動式 HTML
    pio.write_html(fig, file_name)


# 這裡直接複製
if __name__ == "__main__":
    import sys

    if len(sys.argv) != 3:
        print(f"用法: python refund_rate.py (filePath, type, fileName) {sys.argv}")
    elif sys.argv[1] == 'html':
        generate_html_chart(sys.argv[2])
    elif sys.argv[1] == 'photo':
        generate_html_chart(sys.argv[2])
    else:
        print(f"無效的參數，請使用 'html' 或 'photo'")

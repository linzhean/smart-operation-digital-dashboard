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
    # 資料表 EISLH
    # 欄位 LF005 productNumber LF012 date LF014 processedVolume LF017 refundVolume

    # 將每個品號的退貨數量與比率結合，生成新的數據
    labels = df['productNumber']
    values = df['subcontractingRefundRate']

    # 定義顏色
    colors = ['#f1c40f', '#e67e22', '#3498db', '#1abc9c', '#2ecc71']

    # 創建空白圖表
    fig = go.Figure()

    # 繪製多層環狀條形圖
    for i, (label, value) in enumerate(zip(labels, values)):
        # 添加每個產品的環狀弧線
        fig.add_trace(go.Scatterpolar(
            r=[0.9 - i * 0.15, 0.9 - i * 0.15],  # 每層的半徑
            theta=[0, value * 360],  # 根據數值設定角度範圍
            mode='lines',
            line=dict(color=colors[i], width=18),  # 設定弧線的顏色與粗細
            hoverinfo='text',
            text=f'{label}: {value:.2f}%',  # 顯示標籤與數值
            showlegend=False
        ))

    # 設定圖表的佈局
    fig.update_layout(
        title='各品號的委外加工退貨率環狀條形圖',
        polar=dict(
            radialaxis=dict(visible=False),
            angularaxis=dict(visible=False)
        ),
        showlegend=False,
        autosize=True
    )

    # 儲存圖表為互動式 HTML
    pio.write_html(fig, file_name)

# 這裡直接複製
if __name__ == "__main__":
    import sys

    if len(sys.argv) != 3:
        print(f"用法: python subcontracting_refund_rate.py html/photo (fileName) {sys.argv}")
    elif sys.argv[1] == 'html':
        generate_html_chart(sys.argv[2])
    elif sys.argv[1] == 'photo':
        generate_html_chart(sys.argv[2])
    else:
        print(f"無效的參數，請使用 'html' 或 'photo'")

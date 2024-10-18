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

    # 設定環狀條形圖的顏色列表
    colors = ['#3498db', '#e74c3c', '#2ecc71', '#9b59b6', '#f39c12']

    # 創建圖表
    fig = go.Figure()

    # 根據產品號碼進行分組
    for idx, product_number in enumerate(df['productNumber'].unique()):
        product_data = df[df['productNumber'] == product_number]

        # 添加環狀條形圖的每一個部分
        fig.add_trace(go.Barpolar(
            r=product_data['subcontractingRefundRate'],
            theta=[idx * 360 / len(df['productNumber'].unique())] * len(product_data),
            width=[30] * len(product_data),  # 條形圖寬度
            name=product_number,
            marker_color=colors[idx % len(colors)]  # 根據索引來選擇顏色
        ))

    # 設定圖表標題與外觀
    fig.update_layout(
        title='各品號的委外加工退貨率環狀條形圖',
        polar=dict(
            radialaxis=dict(visible=True, range=[0, 100])  # 根據退貨率調整範圍
        ),
        showlegend=True,
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

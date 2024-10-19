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

    # 創建環狀條形圖
    fig = go.Figure()

    # 添加圓環分段圖
    fig.add_trace(go.Pie(
        labels=labels,
        values=values,
        hole=0.4,  # 中間的圓孔大小，0 是實心圓，1 是空心圓
        marker=dict(colors=['#3498db', '#e74c3c', '#2ecc71', '#9b59b6', '#f39c12']),
        textinfo='label+percent',  # 顯示品號和百分比
        insidetextorientation='radial'  # 文字方向
    ))

    # 設定圖表標題與外觀
    fig.update_layout(
        title='各品號的委外加工退貨率環狀條形圖',
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

import json
import pandas as pd
import plotly.graph_objects as go
import plotly.io as pio
import matplotlib.pyplot as plt
import numpy as np
import io

def generate_html_chart(file_name):
    sys.stdin = io.TextIOWrapper(sys.stdin.buffer, encoding='utf-8')
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

    # 生成顏色
    colors = [f'rgba({int(r*255)}, {int(g*255)}, {int(b*255)}, 1)' for r, g, b in
              np.random.rand(len(labels), 3)]  # 隨機生成顏色

    # 繪製環狀圖
    fig = go.Figure(data=[go.Pie(labels=labels,
                                 values=values,
                                 hole=0.4,  # 設定為環狀圖
                                 textinfo='label+percent',  # 顯示標籤和百分比
                                 marker=dict(colors=colors))])  # 使用隨機顏色

    # 設定圖表的佈局
    fig.update_layout(title='各品號的委外加工退貨率環狀圖')

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

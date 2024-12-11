import json
import io
import pandas as pd
import plotly.graph_objects as go
import plotly.io as pio
import sys


def generate_unstacked_area_chart(file_name):
    # 設定標準輸入的編碼
    sys.stdin = io.TextIOWrapper(sys.stdin.buffer, encoding='utf-8')

    # 從標準輸入讀取 JSON 數據
    data = sys.stdin.read()

    # 將字典轉換為 DataFrame
    df = pd.DataFrame(json.loads(data))

    # 圖表讀資料生成圖表
    # 資料表 EISLF
    # 欄位 LF001 productionLineName LF002 date LF005 totalValidHours LF006 totalHoursInvested

    # 將日期轉換為日期格式
    df['date'] = pd.to_datetime(df['date'])

    # 確保 timeEfficiency 為數值型
    df['timeEfficiency'] = pd.to_numeric(df['timeEfficiency'], errors='coerce')

    # 過濾無效數據
    df = df.dropna(subset=['timeEfficiency'])

    # 獲取唯一產線名稱
    production_line_names = df['productionLineName'].unique()

    # 創建未堆疊面積圖
    fig = go.Figure()

    colors = ['blue', 'red', 'green', 'orange', 'purple', 'brown', 'pink', 'gray', 'olive', 'cyan']

    for i, line_name in enumerate(production_line_names):
        line_data = df[df['productionLineName'] == line_name]
        fig.add_trace(go.Scatter(
            x=line_data['date'],
            y=line_data['timeEfficiency'],
            mode='lines',
            name=line_name,
            fill='tozeroy',  # 填充到 X 軸
            line=dict(color=colors[i % len(colors)], width=2),  # 循環使用顏色
            opacity=0.5  # 增加透明度，避免過多線條重疊
        ))

    # 圖表配置
    fig.update_layout(
        title='各產線的工時效率未堆疊面積圖',
        xaxis_title='日期',
        yaxis_title='工時效率 (%)',
        xaxis=dict(autorange=True),
        yaxis=dict(range=[0, 100]),  # 假設工時效率範圍在 0-100%
        autosize=True,
        legend_title="產線名稱",
        showlegend=True,
    )

    # 儲存圖表為互動式 HTML
    pio.write_html(fig, file_name)

# 這裡直接複製
if __name__ == "__main__":
    import sys

    if len(sys.argv) != 3:
        print(f"用法: python time_efficiency.py (filePath, type, fileName) {sys.argv}")
    elif sys.argv[1] == 'html':
        generate_html_chart(sys.argv[2])
    elif sys.argv[1] == 'photo':
        generate_html_chart(sys.argv[2])
    else:
        print(f"無效的參數，請使用 'html' 或 'photo'")

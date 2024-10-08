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
# 資料表 EISLF
# 欄位 LF001 productionLineName LF002 date LF005 totalValidHours LF006 totalHoursInvested

    # 將日期轉換為日期格式
    df['date'] = pd.to_datetime(df['date'])

    production_line_names = df['productionLineName']

    # 創建圖表
    fig = go.Figure()

    # 依照產線進行分組，為每個產線生成一條線
    for production_line_name in production_line_names.unique():
        production_line_data = df[production_line_names == production_line_name]

        # 添加折線圖：產線為名稱，日期為 x 軸，工時效率為 y 軸
        fig.add_trace(go.Scatter(
            x=production_line_data['date'],
            y=production_line_data['timeEfficiency'],
            mode='lines+markers',
            name=production_line_name,  # 產線作為線的名稱
            line=dict(width=2),
            marker=dict(size=6)
        ))

    # 設定圖表標題與軸標籤
    fig.update_layout(
        title='各產線的工時效率折線圖',
        xaxis_title='日期',
        yaxis_title='工時效率 (%)',
        xaxis=dict(autorange=True),
        yaxis=dict(autorange=True),# 可根據數據調整範圍
        autosize=True,
        # responsive=True  # 啟用自適應設計
    )
    # 圖表讀資料生成圖表


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
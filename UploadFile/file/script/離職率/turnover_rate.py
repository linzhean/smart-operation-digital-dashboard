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
    # 資料表 EISLJ
    # 欄位
    # LF001 date
    # LF002 departmentName
    # LF004 initialHeadcount
    # LF006 resignedHeadcount
    # LF007 endingHeadcount

    # 將日期轉換為日期格式
    df['date'] = pd.to_datetime(df['date'])

    department_names = df['departmentName']

    # 創建圖表
    fig = go.Figure()

    # 依照部門進行分組，為每個部門生成一條線
    for department_name in department_names.unique():
        department_data = df[department_names == department_name]

        # 添加折線圖：部門為名稱，日期為 x 軸，離職率為 y 軸
        fig.add_trace(go.Scatter(
            x=department_data['date'],
            y=department_data['turnoverRate'],
            mode='lines+markers',
            name=department_name,  # 部門作為線的名稱
            line=dict(width=2),
            marker=dict(size=6)
        ))

    # 設定圖表標題與軸標籤
    fig.update_layout(
        title='各部門的離職率折線圖',
        xaxis_title='日期',
        yaxis_title='離職率 (%)',
        xaxis=dict(autorange=True),
        yaxis=dict(autorange=True),  # 可根據數據調整範圍
        autosize=True
    )
    # 圖表讀資料生成圖表

    # 儲存圖表為互動式 HTML
    pio.write_html(fig, file_name)


# 這裡直接複製
if __name__ == "__main__":
    import sys

    if len(sys.argv) != 3:
        print(f"用法: python turnover_rate.py"
              f"ate.py (filePath, type, fileName) {sys.argv}")
    elif sys.argv[1] == 'html':
        generate_html_chart(sys.argv[2])
    elif sys.argv[1] == 'photo':
        generate_html_chart(sys.argv[2])
    else:
        print(f"無效的參數，請使用 'html' 或 'photo'")

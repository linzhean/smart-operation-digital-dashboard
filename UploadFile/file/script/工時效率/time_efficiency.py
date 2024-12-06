import json
import io
import pandas as pd
import plotly.graph_objects as go
import plotly.io as pio


def generate_unstacked_area_chart(file_name):
    # 設定標準輸入的編碼
    sys.stdin = io.TextIOWrapper(sys.stdin.buffer, encoding='utf-8')

    # 從標準輸入讀取 JSON 字串
    data = sys.stdin.read()

    # 將 JSON 字串轉換為 DataFrame
    df = pd.DataFrame(json.loads(data))

    # 圖表讀資料生成圖表
    # 資料表 EISLF
    # 欄位 LF001 productionLineName LF002 date LF005 totalValidHours LF006 totalHoursInvested

    # 確保日期欄位轉換為日期格式
    df['date'] = pd.to_datetime(df['date'])

    # 找出所有數值欄位（假設非日期欄位均為數據）
    value_columns = [col for col in df.columns if col != 'date']

    # 創建未堆疊面積圖
    fig = go.Figure()

    # 動態添加每一條數據線及其面積
    colors = ['blue', 'red', 'green', 'orange', 'purple', 'brown', 'pink', 'gray', 'olive', 'cyan']
    for i, col in enumerate(value_columns):
        fig.add_trace(go.Scatter(
            x=df['date'],
            y=df[col],
            mode='lines',
            name=col,  # 使用欄位名作為圖例名稱
            fill='tozeroy',  # 填充至 X 軸
            line=dict(color=colors[i % len(colors)], width=2)  # 循環使用顏色
        ))

    # 設定圖表的標題和軸標籤
    fig.update_layout(
        title='各產線的工時效率未堆疊面積圖',
        xaxis_title='日期',
        yaxis_title='工時效率 (%)',
        xaxis=dict(autorange=True),
        yaxis=dict(autorange=True),
        autosize=True,
        legend_title="產線名稱",    # 顯示圖表旁邊的產線名稱標籤
        showlegend=True  # 確保顯示圖例
    )

    # 儲存圖表為互動式 HTML
    pio.write_html(fig, file_name)


if __name__ == "__main__":
    import sys

    if len(sys.argv) != 3:
        print(f"用法: python script.py html [output_file_name.html]")
    elif sys.argv[1].lower() == 'html':
        generate_unstacked_area_chart(sys.argv[2])
    else:
        print("無效的參數，請使用 'html'")

import plotly.graph_objects as go
import plotly.io as pio


def generate_html_chart():
    # 定義數據
    dates = ["2024-07-01", "2024-07-02", "2024-07-03"]
    planned_production = [1000, 2000, 1800]
    actual_production = [950, 2100, 1750]

    # 創建折線圖
    fig = go.Figure()

    fig.add_trace(go.Scatter(
        x=dates,
        y=planned_production,
        mode='lines+markers',
        name='計劃產量'
    ))

    fig.add_trace(go.Scatter(
        x=dates,
        y=actual_production,
        mode='lines+markers',
        name='實際產量'
    ))

    # 添加標題和標籤
    fig.update_layout(
        title='生產數據分析',
        xaxis_title='日期',
        yaxis_title='產量',
        legend_title='數據類型'
    )

    # 將圖表轉換為 HTML 字符串
    graph_html = pio.to_html(fig, full_html=True)

    html_file = sys.argv[2]
    # 將 HTML 字符串寫入文件
    with open(html_file, 'w', encoding='utf-8') as f:
        f.write(graph_html)

    # print(html_file)


def generate_png_chart():
    # 定義數據
    dates = ["2024-07-01", "2024-07-02", "2024-07-03"]
    planned_production = [1000, 2000, 1800]
    actual_production = [950, 2100, 1750]

    # 創建折線圖
    fig = go.Figure()

    fig.add_trace(go.Scatter(
        x=dates,
        y=planned_production,
        mode='lines+markers',
        name='計劃產量'
    ))

    fig.add_trace(go.Scatter(
        x=dates,
        y=actual_production,
        mode='lines+markers',
        name='實際產量'
    ))

    # 添加標題和標籤
    fig.update_layout(
        title='生產數據分析',
        xaxis_title='日期',
        yaxis_title='產量',
        legend_title='數據類型'
    )

    # 將圖表保存為 PNG 圖片
    png_file = sys.argv[2]
    fig.write_image(png_file)

    # print(png_file)


if __name__ == "__main__":
    import sys

    if len(sys.argv) != 2:
        print("用法: python gen_png_html.py (type, fileName)")
    elif sys.argv[1] == 'html':
        generate_html_chart()
    elif sys.argv[1] == 'png':
        generate_png_chart()
    else:
        print("無效的參數，請使用 'html' 或 'png'")

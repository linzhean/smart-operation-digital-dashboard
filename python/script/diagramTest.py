import plotly.graph_objects as go
import numpy as np

# 圖表生成函數
def generate_chart(x_values, y_values=None, chart_type='line', title='Chart', xlabel='x', ylabel='y', color='white', save_to_file=False, file_name='chart.html', title_font_size=20):
    # 驗證圖表類型是否有效
    if chart_type not in ['line', 'bar', 'scatter', 'gauge']:
        raise ValueError("Unsupported chart type: choose from 'line', 'bar', 'scatter', or 'gauge'")

    # 驗證 x_values 是否為列表或 numpy 陣列（儀表圖除外）
    if not isinstance(x_values, (list, np.ndarray)) and chart_type != 'gauge':
        raise TypeError("x_values must be a list or numpy array")

    # 驗證 y_values 是否為列表或 numpy 陣列
    if y_values is not None and not isinstance(y_values, (list, np.ndarray)):
        raise TypeError("y_values must be a list or numpy array")

    # 驗證 x_values 和 y_values 的長度是否一致
    if y_values is not None and len(x_values) != len(y_values):
        raise ValueError("x_values and y_values must have the same length")

    # 驗證 save_to_file 是否為布林值
    # if not isinstance(save_to_file, bool):
    #     raise TypeError("save_to_file must be a boolean")

    # 創建一個空的 Figure 對象
    fig = go.Figure()

    # 根據圖表類型添加對應的圖表
    if chart_type == 'line':
        fig.add_trace(go.Scatter(x=x_values, y=y_values, mode='lines', line=dict(color=color)))
    elif chart_type == 'bar':
        # 根據規則設置顏色
        bar_colors = []
        for val in y_values:
            bar_color = ('#02F78E'
                         ''
                         ''
                         ''
                         ''
                         '')
            if title == '每日廢品率 - 長條圖' and val > 5:
                bar_color = '#e71e24'
            elif (title == '每日產能利用率 - 長條圖' or title == '每日生產進度達成率 - 長條圖') and val < 85:
                bar_color = '#e71e24'
            bar_colors.append(bar_color)

        fig.add_trace(go.Bar(x=x_values, y=y_values, marker=dict(color=bar_colors)))
    elif chart_type == 'scatter':
        fig.add_trace(go.Scatter(x=x_values, y=y_values, mode='markers', marker=dict(color=color)))
    elif chart_type == 'gauge':
        # 如果是儀表盤類型，則調用 create_gauge_chart 函數生成圖表
        return create_gauge_chart(x_values, title, save_to_file, file_name, title_font_size)

    # 更新圖表佈局
    fig.update_layout(
        title=title,
        xaxis_title=xlabel,
        yaxis_title=ylabel,
        plot_bgcolor='black',
        paper_bgcolor='black',
        font=dict(color='white'),
        xaxis=dict(showgrid=True, gridcolor='gray', gridwidth=0.5),  # 更淺的格線
        yaxis=dict(showgrid=True, gridcolor='gray', gridwidth=0.5)   # 更淺的格線
    )

    # 如果需要保存為文件，則將圖表寫入 HTML 文件
    # if save_to_file:
    #     fig.write_html(file_name)

    # 在 Jupyter Notebook 中顯示圖表
    return fig.show()

# 儀表圖生成函數
def create_gauge_chart(value, title='Gauge Chart', save_to_file=False, file_name='gauge_chart.html', title_font_size=20):
    # 驗證 value 是否在 0 到 100 之間
    if not isinstance(value, (int, float)) or not (0 <= value <= 100):
        raise ValueError("value must be a number between 0 and 100")

    # 根據標題和數值設置顏色
    color = '#02F78E'
    if title == '廢品率' and value > 5:
        color = '#e71e24'
    elif (title == '產能利用率'or title == '生產進度達成率') and value < 85:
        color = '#e71e24'

    # 創建一個儀表盤圖表
    fig = go.Figure(go.Indicator(
        mode="gauge+number",
        value=value,
        title={'text': title, 'font': {'color': 'white', 'size': title_font_size}},
        number={'font': {'size': 100, 'color': color}},
        gauge={
            'axis': {'range': [0, 100], 'tickvals': [0, 10, 20, 30, 40, 50, 60, 70, 80, 90, 100], 'ticktext': ['0', '10', '20', '30', '40', '50', '60', '70', '80', '90', '100'], 'tickcolor': 'white'},
            'bar': {'color': color},
            'bgcolor': 'black',
            'borderwidth': 2,
            'bordercolor': 'white',
            'steps': [
                {'range': [0, value], 'color': color}],
        }
    ))

    # 更新儀表盤圖表的佈局
    fig.update_layout(
        paper_bgcolor='black',
        font={'color': 'white'}
    )

    # 如果需要保存為文件，則將圖表寫入 HTML 文件
    # if save_to_file:
    #     fig.write_html(file_name)

    # 在 Jupyter Notebook 中顯示儀表圖
    return fig.show()

# 計算廢品率
def calculate_defective_rate(defective_quantity, total_quantity):
    return (defective_quantity / total_quantity) * 100

# 計算產能利用率
def calculate_capacity_utilization_rate(actual_output, theoretical_output):
    return (actual_output / theoretical_output) * 100

# 計算生產進度達成率
def calculate_production_progress_achievement_rate(actual_progress, planned_progress):
    return (actual_progress / planned_progress) * 100

# 假設的數據
defective_quantity = 150
total_quantity = 1000

actual_output = 800
theoretical_output = 1000

actual_progress = 95
planned_progress = 100

# 計算指標
defective_rate = calculate_defective_rate(defective_quantity, total_quantity)
capacity_utilization_rate = calculate_capacity_utilization_rate(actual_output, theoretical_output)
production_progress_achievement_rate = calculate_production_progress_achievement_rate(actual_progress, planned_progress)

# 生成三個指標的儀表圖並顯示
# 廢品率
gauge_chart_defective_rate = generate_chart(
    x_values=defective_rate,
    chart_type='gauge',
    title='廢品率',
    save_to_file=True,
    file_name='defective_rate.html',
    title_font_size=30
)

# 產能利用率
gauge_chart_capacity_utilization_rate = generate_chart(
    x_values=capacity_utilization_rate,
    chart_type='gauge',
    title='產能利用率',
    save_to_file=True,
    file_name='capacity_utilization_rate.html',
    title_font_size=30
)

# 生產進度達成率
gauge_chart_production_progress_achievement_rate = generate_chart(
    x_values=production_progress_achievement_rate,
    chart_type='gauge',
    title='生產進度達成率',
    save_to_file=True,
    file_name='production_progress_achievement_rate.html',
    title_font_size=30
)

# 每日數據
days = np.arange(1, 11)
daily_defective_rates = np.random.uniform(0, 10, len(days))  # 假設的每日廢品率
daily_capacity_utilization_rates = np.random.uniform(70, 100, len(days))  # 假設的每日產能利用率
daily_production_progress_achievement_rates = np.random.uniform(80, 100, len(days))  # 假設的每日生產進度達成率

# 生成每日廢品率的長條圖和折線圖
generate_chart(
    x_values=days,
    y_values=daily_defective_rates,
    chart_type='bar',
    title='每日廢品率 - 長條圖',
    xlabel='天數',
    ylabel='廢品率 (%)',
    save_to_file=True,
    file_name='daily_defective_rates_bar.html'
)

generate_chart(
    x_values=days,
    y_values=daily_defective_rates,
    chart_type='line',
    title='每日廢品率 - 折線圖',
    xlabel='天數',
    ylabel='廢品率 (%)',
    save_to_file=True,
    file_name='daily_defective_rates_line.html'
)

# 生成每日產能利用率的長條圖和折線圖
generate_chart(
    x_values=days,
    y_values=daily_capacity_utilization_rates,
    chart_type='bar',
    title='每日產能利用率 - 長條圖',
    xlabel='天數',
    ylabel='產能利用率 (%)',
    save_to_file=True,
    file_name='daily_capacity_utilization_rates_bar.html'
)

generate_chart(
    x_values=days,
    y_values=daily_capacity_utilization_rates,
    chart_type='line',
    title='每日產能利用率 - 折線圖',
    xlabel='天數',
    ylabel='產能利用率 (%)',
    save_to_file=True,
    file_name='daily_capacity_utilization_rates_line.html'
)

# 生成每日生產進度達成率的長條圖和折線圖
generate_chart(
    x_values=days,
    y_values=daily_production_progress_achievement_rates,
    chart_type='bar',
    title='每日生產進度達成率 - 長條圖',
    xlabel='天數',
    ylabel='生產進度達成率 (%)',
    save_to_file=True,
    file_name='daily_production_progress_achievement_rates_bar.html'
)

generate_chart(
    x_values=days,
    y_values=daily_production_progress_achievement_rates,
    chart_type='line',
    title='每日生產進度達成率 - 折線圖',
    xlabel='天數',
    ylabel='生產進度達成率 (%)',
    save_to_file=True,
    file_name='daily_production_progress_achievement_rates_line.html'
)

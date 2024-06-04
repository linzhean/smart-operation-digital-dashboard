import matplotlib.pyplot as plt
import numpy as np
import io

def generate_chart(x_values, y_values=None, chart_type='line', title='Chart', xlabel='x', ylabel='y', color='white', save_to_file=False, file_name='chart.png'):
    # 驗證圖表類型
    if chart_type not in ['line', 'bar', 'scatter', 'gauge']:
        raise ValueError("Unsupported chart type: choose from 'line', 'bar', 'scatter', or 'gauge'")

    # 驗證 x_values
    if not isinstance(x_values, (list, np.ndarray)) and chart_type != 'gauge':
        raise TypeError("x_values must be a list or numpy array")

    # 驗證 y_values
    if y_values is not None and not isinstance(y_values, (list, np.ndarray)):
        raise TypeError("y_values must be a list or numpy array")

    # 驗證 x_values 和 y_values 的長度
    if y_values is not None and len(x_values) != len(y_values):
        raise ValueError("x_values and y_values must have the same length")

    # 驗證 save_to_file
    if not isinstance(save_to_file, bool):
        raise TypeError("save_to_file must be a boolean")

    plt.figure(facecolor='black')

    if chart_type == 'line':
        plt.plot(x_values, y_values, color=color)
    elif chart_type == 'bar':
        plt.bar(x_values, y_values, color=color)
    elif chart_type == 'scatter':
        plt.scatter(x_values, y_values, color=color)
    elif chart_type == 'gauge':
        return create_gauge_chart(x_values, title, save_to_file, file_name)

    plt.xlabel(xlabel, color='white')
    plt.ylabel(ylabel, color='white')
    plt.title(title, color='white')
    plt.gca().set_facecolor('black')
    plt.gca().tick_params(colors='white')
    plt.gca().spines['bottom'].set_color('white')
    plt.gca().spines['left'].set_color('white')

    buffer = io.BytesIO()
    plt.savefig(buffer, format='png', facecolor='black')
    buffer.seek(0)
    image_binary = buffer.getvalue()
    buffer.close()

    if save_to_file:
        with open(file_name, 'wb') as f:
            f.write(image_binary)

    return image_binary

def create_gauge_chart(value, title='Gauge Chart', save_to_file=False, file_name='gauge_chart.png'):
    # 驗證 value
    if not isinstance(value, (int, float)) or not (0 <= value <= 100):
        raise ValueError("value must be a number between 0 and 100")

    fig, ax = plt.subplots(figsize=(6, 3), subplot_kw={'projection': 'polar'})
    fig.patch.set_facecolor('black')
    ax.set_facecolor('black')

    # 設置角度和數據
    theta = np.linspace(0, np.pi, 100)
    radii = np.ones(100)
    ax.fill_between(theta, 0, radii, color='black')

    # 繪製儀表值
    value_theta = np.pi * (1 - value / 100)
    ax.fill_between(theta, 0, radii, where=theta >= value_theta, color='white')

    # 添加中心文本
    ax.text(0, 0, f'{value}%', ha='center', va='center', fontsize=20, color='grey', weight='bold')

    # 設置標題
    plt.title(title, fontsize=15, color='white')

    # 移除極座標網格和標記
    ax.set_yticklabels([])
    ax.set_xticklabels([])
    ax.spines['polar'].set_visible(False)
    ax.grid(False)

    # 設置角度範圍
    ax.set_ylim(0, 1)

    if save_to_file:
        plt.savefig(file_name, bbox_inches='tight', facecolor=fig.get_facecolor())

    buffer = io.BytesIO()
    plt.savefig(buffer, format='png', bbox_inches='tight', facecolor=fig.get_facecolor())
    buffer.seek(0)
    image_binary = buffer.getvalue()
    buffer.close()

    if save_to_file:
        with open(file_name, 'wb') as f:
            f.write(image_binary)

    return image_binary

# 使用NumPy生成數據
x_values = np.linspace(0, 10, 100)  # 生成0到10之間的100個點
y_values = np.sin(x_values)  # 計算這些點的正弦值

# 呼叫函數並生成折線圖
line_chart_data = generate_chart(
    x_values=x_values,
    y_values=y_values,
    chart_type='line',
    title='Sine Wave',
    xlabel='x',
    ylabel='sin(x)',
    color='white',
    save_to_file=True,
    file_name='sine_wave.png'
)

# 打印折線圖二進制數據以16進制格式輸出
print("Line Chart Data:", line_chart_data.hex())

# 使用NumPy生成長條圖數據
bar_x_values = np.arange(1, 6)
bar_y_values = np.random.randint(1, 10, size=5)  # 生成5個隨機整數作為Y值

# 呼叫函數並生成長條圖
bar_chart_data = generate_chart(
    x_values=bar_x_values,
    y_values=bar_y_values,
    chart_type='bar',
    title='Bar Chart',
    xlabel='Category',
    ylabel='Value',
    color='white',
    save_to_file=True,
    file_name='bar_chart.png'
)

# 打印長條圖二進制數據以16進制格式輸出
print("Bar Chart Data:", bar_chart_data.hex())

# 呼叫函數並生成儀表圖
gauge_chart_data = generate_chart(
    x_values=60,  # 設置儀表值
    chart_type='gauge',
    title='Gauge Chart',
    save_to_file=True,
    file_name='gauge_chart.png'
)

# 打印儀表圖二進制數據以16進制格式輸出
print("Gauge Chart Data:", gauge_chart_data.hex())

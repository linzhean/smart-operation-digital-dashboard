import matplotlib.pyplot as plt
import numpy as np
import io

def generate_chart(x_values, y_values=None, chart_type='line', title='Example Chart', xlabel='x', ylabel='y', color='blue', save_to_file=False, file_name='chart.png'):
    # Validate chart_type
    if chart_type not in ['line', 'bar', 'scatter', 'gauge']:
        raise ValueError("Unsupported chart type: choose from 'line', 'bar', 'scatter', or 'gauge'")

    # Validate x_values
    if not isinstance(x_values, (list, np.ndarray)) and chart_type != 'gauge':
        raise TypeError("x_values must be a list or numpy array")

    # Validate y_values
    if y_values is not None and not isinstance(y_values, (list, np.ndarray)):
        raise TypeError("y_values must be a list or numpy array")

    # Validate length of x_values and y_values
    if y_values is not None and len(x_values) != len(y_values):
        raise ValueError("x_values and y_values must have the same length")

    # Validate save_to_file
    if not isinstance(save_to_file, bool):
        raise TypeError("save_to_file must be a boolean")

    plt.figure()

    if chart_type == 'line':
        plt.plot(x_values, y_values, color=color)
    elif chart_type == 'bar':
        plt.bar(x_values, y_values, color=color)
    elif chart_type == 'scatter':
        plt.scatter(x_values, y_values, color=color)
    elif chart_type == 'gauge':
        return create_gauge_chart(x_values, title, save_to_file, file_name)

    plt.xlabel(xlabel)
    plt.ylabel(ylabel)
    plt.title(title)

    buffer = io.BytesIO()
    plt.savefig(buffer, format='png')
    buffer.seek(0)
    image_binary = buffer.getvalue()
    buffer.close()

    if save_to_file:
        with open(file_name, 'wb') as f:
            f.write(image_binary)

    return image_binary

def create_gauge_chart(value, title='Gauge Chart', save_to_file=False, file_name='gauge_chart.png'):
    # Validate value
    if not isinstance(value, (int, float)) or not (0 <= value <= 100):
        raise ValueError("value must be a number between 0 and 100")

    fig, ax = plt.subplots(figsize=(6, 3))

    # 建立儀表的角度陣列
    angles = np.linspace(np.pi, 0, 100)
    radius = 1.0

    # 繪製儀表背景
    ax.fill_between(angles, 0, radius, color='lightgrey')

    # 繪製儀表值
    value_angle = np.pi - value / 100 * np.pi
    ax.fill_betweenx([0, radius], 0, value_angle, color='black')

    # 設置文字和標題
    ax.text(0, -0.2, f'{value}%', horizontalalignment='center', verticalalignment='center', fontsize=20, weight='bold')
    ax.set_title(title, fontsize=15)

    # 移除軸線
    ax.axis('off')

    if save_to_file:
        plt.savefig(file_name, bbox_inches='tight')

    buffer = io.BytesIO()
    plt.savefig(buffer, format='png')
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
    color='blue',
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
    title='Random Bar Chart',
    xlabel='Category',
    ylabel='Value',
    color='green',
    save_to_file=True,
    file_name='bar_chart.png'
)

# 打印長條圖二進制數據以16進制格式輸出
print("Bar Chart Data:", bar_chart_data.hex())

# 呼叫函數並生成儀表圖
gauge_chart_data = generate_chart(
    x_values=60,  # 設置儀表值
    chart_type='gauge',
    title='Example Gauge Chart',
    save_to_file=True,
    file_name='gauge_chart.png'
)

# 打印儀表圖二進制數據以16進制格式輸出
print("Gauge Chart Data:", gauge_chart_data.hex())

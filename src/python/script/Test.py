import matplotlib.pyplot as plt
import io

def generate_chart(x_values, y_values, chart_type='line', title='Example Chart', xlabel='x', ylabel='y', color='blue', save_to_file=False, file_name='chart.png'):

    # 資料驗證
    if not isinstance(x_values, list) or not isinstance(y_values, list):
        raise TypeError("x_values and y_values must be lists of numeric values.")
    if len(x_values) != len(y_values):
        raise ValueError("x_values and y_values must have the same length.")
    if not all(isinstance(val, (int, float)) for val in x_values) or not all(isinstance(val, (int, float)) for val in y_values):
        raise TypeError("x_values and y_values must contain only numeric values.")

    plt.figure()

    if chart_type == 'line':
        plt.plot(x_values, y_values, color=color)
    elif chart_type == 'bar':
        plt.bar(x_values, y_values, color=color)
    elif chart_type == 'scatter':
        plt.scatter(x_values, y_values, color=color)
    else:
        raise ValueError("Unsupported chart type: choose from 'line', 'bar', or 'scatter'")

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

# 呼叫函數並將圖表數據輸出到標準輸出流
chart_data = generate_chart(
    x_values=[1, 2, 3, 4],
    y_values=[1, 4, 9, 16],
    chart_type='line',
    title='Example Chart',
    xlabel='x',
    ylabel='y',
    color='blue',
    save_to_file=True,
    file_name='chart.png'
)

# 將二進制數據以16進制格式輸出
print(chart_data.hex())
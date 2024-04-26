import matplotlib.pyplot as plt
import io

def generate_chart():
    # 生成圖表
    plt.plot([1, 2, 3, 4], [1, 4, 9, 16])
    plt.xlabel('x')
    plt.ylabel('y')
    plt.title('Example Chart')

    # 將圖表轉換為二進制數據
    buffer = io.BytesIO()
    plt.savefig(buffer, format='png')
    buffer.seek(0)
    image_binary = buffer.getvalue()
    buffer.close()

    return image_binary

# 呼叫函數並將圖表數據輸出到標準輸出流
chart_data = generate_chart()
print(chart_data)
import sys
from openai import OpenAI

# 初始化 OpenAI 客戶端
client = OpenAI(base_url="http://localhost:1234/v1", api_key="lm-studio")

# 接收來自 Java 的輸入

chart_data = """
日期       | 生產線 | 產品編號 | 計劃產量 | 實際產量
2024-07-01 | A      | P001     | 1000     | 950
2024-07-01 | B      | P002     | 1500     | 1600
2024-07-02 | A      | P003     | 2000     | 2100
2024-07-02 | B      | P001     | 1200     | 1100
2024-07-03 | A      | P002     | 1800     | 1750
"""

prompt = f"以下是生產數據表格，請分析這些數據並找出其中的規律或重要訊息：\n{chart_data}"

history = [
    {"role": "system", "content": "You are an intelligent assistant. You always provide well-reasoned answers that are both correct and helpful."},
    {"role": "user", "content": prompt},
]

completion = client.chat.completions.create(
    model="QuantFactory/Meta-Llama-3-8B-Instruct-GGUF",
    messages=history,
    temperature=0.7
)

response = completion.choices[0].message.content

print(response)
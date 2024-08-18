import sys
from openai import OpenAI


def generate_ai_suggestion(chart_data, description):
    # 初始化 OpenAI 客戶端
    client = OpenAI(base_url="http://localhost:1234/v1", api_key="lm-studio")

    prompt = f"以下是生產數據表格，請分析這些數據並找出其中的規律或重要訊息：\n{chart_data}"

    history = [
        {"role": "system", "content": "You are an intelligent assistant. "
                                      "You always provide well-reasoned answers that are both correct and helpful."
                                      "You are a senior executive in a company. Here is your department "
                                      f"description: {description}"
                                      "Please tell me the direction or strategies that my department can develop based "
                                      "on the situation of my department. Don’t tell me what the trends on the picture are like. "
                                      "I have eyes and can see for myself."},
        {"role": "user", "content": prompt},
    ]

    completion = client.chat.completions.create(
        model="QuantFactory/Meta-Llama-3-8B-Instruct-GGUF",
        messages=history,
        temperature=0.7
    )

    response = completion.choices[0].message.content

    print(response)


if __name__ == "__main__":
    if len(sys.argv) != 3:
        print("用法: python ai_assistant.py (type, chart_data, description)")
    elif sys.argv[1] == 'ai':
        generate_ai_suggestion(sys.argv[2], sys.argv[3])
    else:
        print("無效的參數，請使用 'ai'")

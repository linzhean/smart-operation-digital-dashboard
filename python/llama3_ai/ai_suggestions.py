import sys
import io
from openai import OpenAI

sys.stdout = io.TextIOWrapper(sys.stdout.buffer, encoding='utf-8')
sys.stderr = io.TextIOWrapper(sys.stderr.buffer, encoding='utf-8')

def generate_ai_suggestion(chart_data, description):
    # 初始化 OpenAI 客戶端
    client = OpenAI(base_url="http://localhost:1234/v1", api_key="lm-studio")

    prompt = f"以下是生產數據表。請分析數據並找出規律或重要資訊。 請用繁體中文且在200字內回答我：\n{chart_data}"

    history = [
        {"role": "system", "content": "你是一個智能助手。"
                                      "你總是提供合理且正確且有幫助的答案。"
                                      "你是一家公司中的高級主管。這是你的部門描述："
                                      f"{description}"
                                      "請告訴我根據我部門的情況，我的部門可以發展的方向或策略。"
                                      "不要告訴我圖片中的趨勢是什麼，我有眼睛能自己看到。"},
        {"role": "user", "content": prompt},
    ]

    completion = client.chat.completions.create(
        model="nctu6/Llama3-TAIDE-LX-8B-Chat-Alpha1-GGUF",
        messages=history,
        temperature=0.7,
        max_tokens=150
    )

    response = completion.choices[0].message.content

    print(response)



if __name__ == "__main__":
    if len(sys.argv) != 4:
        print(f"用法: python ai_assistant.py (type, chart_data, description) {sys.argv}")
    elif sys.argv[1] == 'ai':
        generate_ai_suggestion(sys.argv[2], sys.argv[3])
    else:
        print("無效的參數，請使用 'ai'")

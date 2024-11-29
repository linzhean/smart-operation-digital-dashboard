import sys
import io
from openai import OpenAI

sys.stdout = io.TextIOWrapper(sys.stdout.buffer, encoding='utf-8')
sys.stderr = io.TextIOWrapper(sys.stderr.buffer, encoding='utf-8')


def generate_ai_suggestion(messages):
    sys.stdin = io.TextIOWrapper(sys.stdin.buffer, encoding='utf-8')
    # 初始化 OpenAI 客戶端
    client = OpenAI(base_url="http://localhost:1234/v1", api_key="lm-studio")

    completion = client.chat.completions.create(
        model="nctu6/Llama3-TAIDE-LX-8B-Chat-Alpha1-GGUF",
        messages=messages,
        temperature=0.5,
        max_tokens=1000
    )

    response = completion.choices[0].message.content

    print(response)


if __name__ == "__main__":
    if len(sys.argv) != 3:
        print(f"用法: python ai_chat.py (type, messages) {sys.argv[2]}")
    elif sys.argv[1] == 'ai':
        generate_ai_suggestion(sys.argv[2])
    else:
        print("無效的參數，請使用 'ai'")

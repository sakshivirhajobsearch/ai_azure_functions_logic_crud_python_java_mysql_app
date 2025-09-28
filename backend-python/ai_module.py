import openai
from config import OPENAI_API_KEY

openai.api_key = OPENAI_API_KEY

def analyze_function_response(response_text):
    prompt = f"""
    Analyze this Azure Function response and return a summary:
    
    {response_text}
    """
    try:
        response = openai.ChatCompletion.create(
            model="gpt-4",
            messages=[
                {"role": "user", "content": prompt}
            ],
            temperature=0.3,
            max_tokens=300
        )
        return response.choices[0].message.content.strip()
    except Exception as e:
        return f"AI Analysis failed: {str(e)}"

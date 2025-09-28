from flask import Flask, jsonify, request
from flask_cors import CORS
import mysql.connector
import requests
from config import *
from ai_module import analyze_function_response

app = Flask(__name__)
CORS(app)

# ✅ Root route to confirm app is running
@app.route('/')
def index():
    return jsonify({
        'message': 'AI + Azure Functions & Logic Apps Flask Backend is Running',
        'endpoints': [
            '/functions',
            '/logicapps',
            '/ai/function_response?url=<function_url>'
        ]
    })

# ✅ Prevent favicon error in browser
@app.route('/favicon.ico')
def favicon():
    return '', 204

# ✅ Fetch all Azure Functions
@app.route('/functions', methods=['GET'])
def get_functions():
    try:
        conn = get_connection()
        cursor = conn.cursor(dictionary=True)
        cursor.execute("SELECT * FROM azure_functions")
        rows = cursor.fetchall()
        return jsonify(rows)
    except Exception as e:
        return jsonify({'error': str(e)}), 500
    finally:
        if conn: conn.close()

# ✅ Fetch all Logic Apps
@app.route('/logicapps', methods=['GET'])
def get_logicapps():
    try:
        conn = get_connection()
        cursor = conn.cursor(dictionary=True)
        cursor.execute("SELECT * FROM logic_apps")
        rows = cursor.fetchall()
        return jsonify(rows)
    except Exception as e:
        return jsonify({'error': str(e)}), 500
    finally:
        if conn: conn.close()

# ✅ AI: Analyze Azure Function Response using OpenAI
@app.route('/ai/function_response', methods=['GET'])
def ai_function_response():
    function_url = request.args.get('url')
    if not function_url:
        return jsonify({'error': 'Missing required parameter: url'}), 400

    headers = {'x-functions-key': AZURE_KEY}
    try:
        response = requests.get(function_url, headers=headers, timeout=10)
        response.raise_for_status()
        response_text = response.text
        ai_result = analyze_function_response(response_text)
        return jsonify({
            'function_response': response_text,
            'ai_analysis': ai_result
        })
    except requests.exceptions.RequestException as re:
        return jsonify({'error': f'Request to Azure Function failed: {str(re)}'}), 500
    except Exception as e:
        return jsonify({'error': f'AI Analysis failed: {str(e)}'}), 500

# ✅ MySQL DB Connection Helper
def get_connection():
    return mysql.connector.connect(
        host=MYSQL_HOST,
        user=MYSQL_USER,
        password=MYSQL_PASSWORD,
        database=MYSQL_DB
    )

# ✅ Start the Flask app
if __name__ == '__main__':
    app.run(debug=True)

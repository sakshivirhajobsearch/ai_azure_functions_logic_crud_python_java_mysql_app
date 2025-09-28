from flask import Flask, jsonify, request
import mysql.connector
import pandas as pd
from azure_functions import AzureFunctionsClient
from logic_apps import LogicAppsClient
import configparser
from ai_processor import AIProcessor

# Load config
config = configparser.ConfigParser()
config.read('config.properties')

app = Flask(__name__)
ai = AIProcessor()

# MySQL connection
def get_mysql_connection():
    return mysql.connector.connect(
        host=config['DEFAULT']['mysql_host'],
        user=config['DEFAULT']['mysql_user'],
        password=config['DEFAULT']['mysql_password'],
        database=config['DEFAULT']['mysql_db']
    )

# Clients
azure_client = AzureFunctionsClient(config)
logic_client = LogicAppsClient(config)

# Home route
@app.route('/')
def home():
    return """
    <h2>AI Azure CRUD API</h2>
    <p>Available endpoints:</p>
    <ul>
        <li>/functions/list</li>
        <li>/functions/create</li>
        <li>/functions/delete</li>
        <li>/logic/list</li>
        <li>/logic/create</li>
        <li>/logic/delete</li>
    </ul>
    """

# Azure Functions CRUD
@app.route('/functions/list', methods=['GET'])
def list_functions():
    data = azure_client.list_functions()
    summary = ai.process_functions(data)
    return jsonify({"data": data, "summary": summary})

@app.route('/functions/create', methods=['POST'])
def create_function():
    payload = request.json
    result = azure_client.create_function(payload)
    return jsonify(result)

@app.route('/functions/delete', methods=['POST'])
def delete_function():
    name = request.json.get('name')
    result = azure_client.delete_function(name)
    return jsonify(result)

# Logic Apps CRUD
@app.route('/logic/list', methods=['GET'])
def list_logic_apps():
    data = logic_client.list_logic_apps()
    summary = ai.process_logic_apps(data)
    return jsonify({"data": data, "summary": summary})

@app.route('/logic/create', methods=['POST'])
def create_logic_app():
    payload = request.json
    result = logic_client.create_logic_app(payload)
    return jsonify(result)

@app.route('/logic/delete', methods=['POST'])
def delete_logic_app():
    name = request.json.get('name')
    result = logic_client.delete_logic_app(name)
    return jsonify(result)

if __name__ == '__main__':
    app.run(debug=True, host='127.0.0.1', port=5001)

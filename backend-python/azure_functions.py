import requests

class AzureFunctionsClient:
    def __init__(self, config):
        self.mode = config['DEFAULT'].get('mode', 'mock')
        if self.mode == 'real':
            self.base_url = config['DEFAULT']['azure_function_url']
            self.api_key = config['DEFAULT']['azure_function_key']
        else:
            self.functions = [
                {"name": "TestFunction1", "status": "Running"},
                {"name": "TestFunction2", "status": "Stopped"}
            ]

    def list_functions(self):
        if self.mode == 'real':
            try:
                url = f"{self.base_url}list?code={self.api_key}"
                response = requests.get(url, timeout=10)
                response.raise_for_status()
                return response.json()
            except requests.exceptions.RequestException as e:
                return {"error": str(e)}
        else:
            return self.functions

    def create_function(self, payload):
        if self.mode == 'real':
            try:
                url = f"{self.base_url}create?code={self.api_key}"
                response = requests.post(url, json=payload, timeout=10)
                response.raise_for_status()
                return response.json()
            except requests.exceptions.RequestException as e:
                return {"error": str(e)}
        else:
            name = payload.get("name", f"Function{len(self.functions)+1}")
            function = {"name": name, "status": "Running"}
            self.functions.append(function)
            return {"status": "created", "function": function}

    def delete_function(self, name):
        if self.mode == 'real':
            try:
                url = f"{self.base_url}delete/{name}?code={self.api_key}"
                response = requests.delete(url, timeout=10)
                response.raise_for_status()
                return {"status": "deleted", "function": name}
            except requests.exceptions.RequestException as e:
                return {"error": str(e)}
        else:
            for f in self.functions:
                if f["name"] == name:
                    self.functions.remove(f)
                    return {"status": "deleted", "function": name}
            return {"error": "Function not found"}

import requests

class LogicAppsClient:
    def __init__(self, config):
        self.mode = config['DEFAULT'].get('mode', 'mock')
        if self.mode == 'real':
            self.base_url = config['DEFAULT']['logic_app_url']
            self.api_key = config['DEFAULT']['logic_app_key']
        else:
            self.logic_apps = [
                {"name": "TestLogicApp1", "status": "Enabled"},
                {"name": "TestLogicApp2", "status": "Disabled"}
            ]

    def list_logic_apps(self):
        if self.mode == 'real':
            try:
                url = f"{self.base_url}list?sp={self.api_key}"
                response = requests.get(url, timeout=10)
                response.raise_for_status()
                return response.json()
            except requests.exceptions.RequestException as e:
                return {"error": str(e)}
        else:
            return self.logic_apps

    def create_logic_app(self, payload):
        if self.mode == 'real':
            try:
                url = f"{self.base_url}create?sp={self.api_key}"
                response = requests.post(url, json=payload, timeout=10)
                response.raise_for_status()
                return response.json()
            except requests.exceptions.RequestException as e:
                return {"error": str(e)}
        else:
            name = payload.get("name", f"LogicApp{len(self.logic_apps)+1}")
            logic_app = {"name": name, "status": "Enabled"}
            self.logic_apps.append(logic_app)
            return {"status": "created", "logic_app": logic_app}

    def delete_logic_app(self, name):
        if self.mode == 'real':
            try:
                url = f"{self.base_url}delete/{name}?sp={self.api_key}"
                response = requests.delete(url, timeout=10)
                response.raise_for_status()
                return {"status": "deleted", "logic_app": name}
            except requests.exceptions.RequestException as e:
                return {"error": str(e)}
        else:
            for la in self.logic_apps:
                if la["name"] == name:
                    self.logic_apps.remove(la)
                    return {"status": "deleted", "logic_app": name}
            return {"error": "Logic App not found"}

import configparser

config = configparser.ConfigParser()
config.read('config.properties')

print("Sections:", config.sections())
print("MySQL host:", config['DEFAULT'].get('mysql_host'))
print("Azure Function URL:", config['DEFAULT'].get('azure_function_url'))
print("Logic App URL:", config['DEFAULT'].get('logic_app_url'))
print("Mode:", config['DEFAULT'].get('mode'))

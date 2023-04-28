from jproperties import Properties

import os
from pathlib import Path


class AppConfig:
    def __init__(self):
        configs = Properties()
        self.prop = dict()
        config_path = Path(__file__).with_name('config.properties')
        with open(config_path, 'rb') as config_file:
            configs.load(config_file)
        for item in configs.items():    
            key = item[0]
            self.prop[key] = os.environ.get(key, item[1].data)            
        print(self.prop)

    def get_db_table(self):
        return self.prop['APP_DYNAMO_DB_TABLE']

    def get_aws_profile(self):
        return self.prop['APP_AWS_PROFILE']
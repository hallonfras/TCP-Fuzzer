import tomllib
from jsonschema import validate


class TOMLParser:
    """This class is responsible for parsing the mappers config file, written in TOML"""

    config = {}
    

    schema = {
        "type" : "object",
        "properties" : {
            "Learner": {
                "type" : "object",
                "properties": {
                    "ip": {"type": "string"},
                    "port": {"type": "integer"}
                }
            },
            "SUT": {
                "type" : "object",
                "properties": {
                    "ip": {"type": "string"},
                    "port": {"type": "integer"},
                    "waittime": {"type": "number"}
                }
            }
        },
    }


    def __init__(self, file):
        with open(file, 'rb') as f:
            self.config = tomllib.load(f)
            validate(self.config, self.schema)
            print(self.config)

    def getConfig(self):
        return self.config    

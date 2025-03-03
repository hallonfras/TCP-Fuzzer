from sender import Sender
from SUTSocket import SUTSocket
from learnerSocket import LearnerSocket
from tomlParser import TOMLParser

parser = TOMLParser("config.toml")

print(parser.getConfig())

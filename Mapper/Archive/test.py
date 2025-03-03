from sender import Sender
from SUTSocket import SUTSocket
from learnerSocket import LearnerSocket
from tomlParser import TOMLParser

# parser = TOMLParser("config.toml")

sender = Sender(serverIP="tcp", serverPort=9000)
# actionSender = ActionSender("127.0.0.1", 5000)


sock = LearnerSocket()

sock.start(sender)

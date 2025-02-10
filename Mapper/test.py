from sender import Sender
from actionSender import ActionSender
from learnerSocket import learnerSocket

sender = Sender(serverIP="google.com", serverPort=80)
actionSender = ActionSender("127.0.0.1", 5000)


# sock = learnerSocket()

# sock.start(sender)

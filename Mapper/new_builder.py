'''
    Used to instantiate all other components via the TOMLParser module.
'''

# Modules which will be built
from sender import Sender
from learnerSocket import LearnerSocket
from SUTSocket import SUTSocket

# Parsing the arguments for those modules
from tomlParser import TOMLParser


class Builder(object):
    config = {}

    def __init__(self, configFile):
        self.config = TOMLParser(configFile).getConfig()
    
    def buildSender(self):
        """Builds TCP packet sender"""
        return Sender(serverIP=self.config['SUT']['ip'], serverPort=self.config['SUT']['port'])
    
    # builds the actionSender as a wrapper over the original sender component
    def buildSUTSocket(self, sender):
        values = self.argParser.parseArguments(args.actionSenderArguments, "actionSender")
        values.update({"sender" : sender})
        actionSender = SUTSocket(**values)
        return actionSender
    
    def buildLearnerSocket(self):
        """Builds the socket which connects the sender to the learner"""
        
        return LearnerSocket(self.config["Learner"]["ip"], self.config["Learner"]["port"])
    

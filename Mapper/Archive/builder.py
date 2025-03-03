'''
    Used to instantiate all other components via the TOMLParser module.
'''

# Modules which will be built
from sender import Sender
from learnerSocket import LearnerSocket
from SUTSocket import SUTSocket

# Parsing the arguments for those modules
from argparser import ArgumentParser

# the available arguments
import args

class Builder(object):
    def __init__(self):
        self.argParser = ArgumentParser()
    
    # builds the sender component of the learning setup
    # this can either be a simple packet sender or a more advanced sender, that also has action
    # send capability
    def buildSender(self):
        values = self.argParser.parseArguments(args.senderArguments, "sender")
        # remove the control value which dictates if the sender is simple or is extended with actions
        senderType = values.pop("sendActions")
        simpleSender = Sender(**values) 
        if senderType == False:
            sender = simpleSender
        else:
            actionSender = self.buildSUTSocket(simpleSender)
            sender = actionSender
        return sender
    
    # builds the actionSender as a wrapper over the original sender component
    def buildSUTSocket(self, sender):
        values = self.argParser.parseArguments(args.actionSenderArguments, "actionSender")
        values.update({"sender" : sender})
        actionSender = SUTSocket(**values)
        return actionSender
    
    # builds the adapter component of the learning setup
    def buildLearnerSocket(self):
        values = self.argParser.parseArguments(args.LearnerSocketArguments, "LearnerSocket")
        # values = self.getValueMapForArguments(self.LearnerSocketArguments, values)
        LearnerSocket = LearnerSocket(**values)
        return LearnerSocket
    

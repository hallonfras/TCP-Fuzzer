__author__ = 'paul,ramon,isaac'
import signal
import argparse
from new_builder import Builder

"""
   listens on a port for actions, packet strings or reset, forwards them to the sender component,
   retrieves responses, processes them back to strings and writes them to the socket

   usage: sudo python main.py [networkAdapter param] [sender param] [actionSender param] 
     
   For parameters of each component, see args.py
   
   for learning:
       run the C TCP server adapter (which by itself runs a TCP server)
       run this program which opens a connection to the adapter and starts the listener
       run the learner tool and commence learning of the TCP server implementation
    
"""


global learnerSocket
learnerSocket = None
global origSigInt
origSigInt = None
global parser
parser = argparse.ArgumentParser(
                    prog='TCP-Mapper',
                    description='TCP mapper implementation for TCP fuzzer',
                    epilog='Further settings contained within the TOML config file')
parser.add_argument('config_file') 


# routine used to close the server socket, so you don't have to
def signal_handler(sign, frame):
    signal.signal(sign, origSigInt)
    print("\n==Processing Interrupt==")
    print('You pressed Ctrl+C, meaning you want to stop the system!')
    learnerSocket.closeSockets()
    signal.signal(sign, signal_handler)
    
# sets up the close server socket routine
def setupSignalHandler():
    signal.signal(signal.SIGINT, signal_handler)

# main method. An initial local port can be given as parameter for the program
if __name__ == "__main__":
    print("==Preparation==")
    args = parser.parse_args()
    origSigInt = signal.getsignal(signal.SIGINT)
    setupSignalHandler()
    builder = Builder(args.config_file)
    
    print("\n==Sender Setup==")
    sender = builder.buildSender()
    print(str(sender))

    print("\n==LearnerSocket setup=="    )
    learnerSocket = builder.buildLearnerSocket()
    print(str(learnerSocket))
    
    print("\n==Starting LearnerSocket==")
    learnerSocket.start(sender)

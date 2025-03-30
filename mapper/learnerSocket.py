from time import sleep
__author__ = 'paul,ramon,isaac'
import socket
from select import select
import time
import sys
import signal
from response import Timeout


class LearnerSocket:
    """This class allows access to the functions from sender.py via sockets. It is used to communicate with the learner"""

    learnerSocket = None
    serverSocket = None
    sender = None
    data = None

    # Determines whether the server shutsdown when the learner closes or waits for reconnect
    continous = None

    def __init__(self, socketIP = 'localhost', socketPort = 18200, continuous=True):
        self.socketIP = socketIP
        self.socketPort = socketPort
        self.continuous = continuous

    def __str__(self):
        return "LearnerSocket: " + str(self.__dict__)

    def listen(self, commPort):
        """Starts listening for connections on the serversocket"""
        self.serverSocket = socket.socket(
            socket.AF_INET, socket.SOCK_STREAM)
        self.serverSocket.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
        # bind the socket to a public host and a well-known port
        self.serverSocket.bind((self.socketIP, commPort))
        # start listening
        self.serverSocket.listen(1)

    def accept(self):
        """Accepts a connection to the serverSocket"""

        (clientSocket, address) = self.serverSocket.accept()
        print("Learner connected at: " + str(address))
        self.learnerSocket = clientSocket

    def closeSockets(self):
        """Closes all sockets, displaying eventual errors"""

        print(self.serverSocket)
        print(self.learnerSocket)
        try:
            try:
                if self.learnerSocket is not None:
                    print("Closing local server socket")
                    self.learnerSocket.close()
            except IOError as e:
                print("Error closing learner socket ", e)
            try:
                print(self.serverSocket)
                if self.serverSocket is not None:
                    print("Closing gateway server socket")
                    self.serverSocket.close()
            except IOError as e:
                print("Error closing network adapter socket ", e)
            if self.sender is not None:
                try:
                    self.sender.shutdown()
                except Exception as e:
                    print("Error closing sender ", e)
            print("Have a nice day")
            time.sleep(1)
            sys.exit(1)
        except KeyboardInterrupt:
            sys.exit(1)

    def closeLearnerSocket(self):
        """Closes just the learner socket"""
        try:
            if self.learnerSocket is not None:
                print("Closing local server socket")
                self.learnerSocket.close()
        except IOError as e:
                print("Error closing learner socket " + e)


    def receiveInput(self):
        """Receives input from the socket, delimited by space or newline"""

        inputstring = '';
        finished = False
        while not finished:
            if not self.data:
                try:
                    ready = select([self.learnerSocket], [], [], 1000)
                    if ready[0]:
                        self.data = self.learnerSocket.recv(1024)
                        if len(self.data) == 0:
                            print("learner closed")
                            return None
                    else:
                        self.fault("Learner socket has been unreadable for too long")
                except IOError:
                    self.fault("No output received from client, closing")
            else:
                c = self.data[0]
                self.data = self.data[1:]
                if chr(c) == '\n' or chr(c) == ' ':
                    finished = True
                else:
                    inputstring = inputstring + chr(c)
        return inputstring

    def receiveNumber(self):
        """Receives input from the socket with validation to check if it's a number"""

        inputString = self.receiveInput()
        if inputString.isdigit() == False:
            self.fault("Received "+ inputString + " but expected a number")
        else:
            return int(inputString)


    # accepts input from the learner, and process it. Sends network packets, looks at the
    # response, extracts the relevant parameters and sends them back to the learner
    def handleInput(self, sender):
        """
        Accepts input from the learner and sends the appopriate packet sending the received
        response back to the learner
        """
        self.sender = sender

        while (True):
            # TODO Why is this called input1 and not just input?
            input = self.receiveInput()
            if input is None:
                print(" Learner closed socket. Closing learner socket.")
                self.closeLearnerSocket()
                self.accept()
                continue
            print("received input " + input)
            seqNr = 0
            ackNr = 0


            match(input):
                case "reset":
                    print("Received reset signal.")
                    self.sender.sendReset()
                    self.sendOutput("resetok")
                case "exit":
                    msg = "Received exit signal " +  "(continuous" +  "=" + str(self.continuous) + ") :"
                    if self.continuous == False:
                        msg = msg + " Closing all sockets"
                        print(msg)
                        self.closeSockets()
                        self.sender.sendReset()
                        return
                    else:
                        msg = msg + " Closing only learner socket (so we are ready for a new session)"
                        print(msg)
                        self.closeLearnerSocket()
                        self.accept()
                case _:
                    self.parseInput(input)



    def parseInput(self, input):
        """Parses the input string and takes the corresponding action"""

        if self.sender.isFlags(input):
            seqNr = self.receiveNumber()
            ackNr = self.receiveNumber()
            payload = self.receiveInput()[1:-1]
            print(("send packet: " +input + " " + str(seqNr) + " " + str(ackNr)))
            response = self.sender.sendInput(input, seqNr, ackNr, payload);
        elif "sendAction" in dir(self.sender) and self.sender.isAction(input):
            # TODO this functionality seems to pertain to sending actions to the SUTAdapter, but that's been split off to a different file.
            print(("send action: " +input))
            input = input.lower().replace("\n","")
            try:
                response = sender.sendAction(input) # response might arrive before sender is ready
            except Exception as e:
                print(str(e))
                response = "BROKENPIPE"
        elif input == "nil":
            # TODO in what case is this used?
            print("send nothing (nil)")
            response = sender.captureResponse()
        else:
            self.fault("invalid input " + input)

        if type(response) is not Timeout:
            print('received ' + response.__str__() + "\n")
            self.sendOutput(str(response.seq) + "," + str(response.ack) + "," + str(response['TCP'].flags))
        else:
            print("received timeout")
            self.sendOutput("timeout")

    def sendOutput(self, outputString):
        """Sends a string to the learner, adds a newline as a delimiter"""

        self.learnerSocket.send((outputString + "\n").encode())

    def fault(self, msg):
        """Handles faults and closes sockets"""

        print('===FAULT EXIT WITH MESSAGE===')
        print(msg)
        self.closeSockets()
        sys.exit()

    def start(self, sender):
        """Starts the socket, waits for a connection and tries to read input"""

        print("listening on "+str(self.socketIP) + ":" +str(self.socketPort))
        signal.getsignal(signal.SIGINT)
        self.listen(self.socketPort)
        self.accept()
        self.handleInput(sender)

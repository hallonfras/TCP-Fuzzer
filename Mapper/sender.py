from scapy.all import sr1, IP, TCP 
from response import Timeout, ConcreteResponse


# variables used to retain last sequence/acknowledgment sent
seqVar = 0
ackVar = 0

class Sender:
"""This class contains functions for creating and sending TCP packets. It communicates with the learner via the learnerSocket class"""
        def __init__(self, serverMAC=None, serverIP="191.168.10.1", serverPort = 7991,
                 networkInterface="lo", networkInterfaceType=0, senderPort=15000, senderPortMinimum=20000,
                 senderPortMaximum=40000, portNumberFile = "sn.txt", 
                 isVerbose=0, waitTime=0.02, resetMechanism=0):
        # data on sender and server needed to send packets 
        self.serverIP = serverIP
        self.serverPort = serverPort
        self.serverMAC = serverMAC
        self.networkInterface = networkInterface
        self.senderPort = senderPort
        self.senderPortMinimum = senderPortMinimum
        self.senderPortMaximum = senderPortMaximum
        self.portNumberFile = portNumberFile;
        
        # time to wait for a response from the server before concluding a timeout
        self.waitTime = waitTime
        
        
        # TODO: need to remove this, the client should decide on the reset mechanism.
        self.resetMechanism = resetMechanism
        
        #set verbosity (0/1)
        self.isVerbose = isVerbose
        
    
    def __str__(self):
        return "Sender with parameters: " + str(self.__dict__)

    # TODO the functions pertaining to refreshing ports seem a bit odd, they read from a file. Couldn't they just increment the number instead?
    def refreshNetworkPort(self):
        """Chooses a new port to send packets to"""
        print("previous local port: " + str(self.senderPort))
        self.setSenderPort(self.getNextPort())
        print("next local port: " + str(self.senderPort)+"\n")
        return self.senderPort

    # TODO the functions pertaining to refreshing ports seem a bit odd, they read from a file. Couldn't they just increment the number instead?
    def getNextPort(self):
        f = open(self.portNumberFile,"a+")
        f.seek(0)
        line = f.readline()
        if line == '' or int(line) < self.senderPortMinimum:
            networkPort = self.senderPortMinimum
        else:
            senderPortRange = self.senderPortMaximum - self.senderPortMinimum
            if senderPortRange == 0:
                networkPort = self.senderPortMinimum
            else:
                networkPort = self.senderPortMinimum + (int(line) + 1) % senderPortRange 
        f.closed
        f = open(self.portNumberFile, "w")
        f.write(str(networkPort))
        f.closed
        return networkPort

    def sendPacket(self,flagsSet, seqNr, ackNr):
        """Creates a packet and sends it over the network using scapy, the response is also gathered using scapy"""
        packet = self.createPacket(flagsSet, seqNr, ackNr, '')
        response = self.sendAndRecv(packet)
        return response
    
    def setServerPort(self, newPort):
        self.serverPort = newPort;
            
    def setSenderPort(self, newPort):
        self.senderPort = newPort
    
    # function that creates packet from data strings/integers
    def createPacket(self, tcpFlagsSet, seqNr, ackNr, payload, destIP = None, destPort = None, srcPort = None, ipFlagsSet="DF"):
        """Creates a packet from the given arguments"""

        # TODO couldn't this be done using default values in the function header?
        if destIP is None:
            destIP = self.serverIP
        if destPort is None:
            destPort = self.serverPort
        if srcPort is None:
            srcPort = self.senderPort

        pIP = IP(dst=destIP, flags=ipFlagsSet, version=4)
        pTCP = TCP(sport=srcPort,
        dport=destPort,
        seq=seqNr,
        ack=ackNr,
        flags=tcpFlagsSet)

        # Either we have a payload or we don't
        p = pIP / pTCP / Raw(load=payload) if payload else pIP /pTCP 
        return p
    
    def sendAndRecv(self, packet, waitTime = None):
        """Sends a packet and retrieves a response"""

        if waitTime is None:
            waitTime = self.waitTime
        
        if packet is not None:
            #todo find a more elegant way of finding the client IP?
            self.clientIP = packet[IP].src
            # consider adding the parameter: iface="ethx" if you don't receive a response. Also consider increasing the wait time
            response = sr1(packet, timeout=waitTime, iface=self.networkInterface, verbose=self.isVerbose)
            
            return response

    # FIXME possibly refactor response.py a bit, the names are confusing    
    def scapyResponseParse(self, scapyResponse):
        """Extracts the relevant TCP data from the scapy response"""

        flags = scapyResponse[TCP].flags
        seq = scapyResponse[TCP].seq
        ack = scapyResponse[TCP].ack
        concreteResponse = ConcreteResponse(self.intToFlags(flags), seq, ack)
        return concreteResponse
    

    def checkForFlag(self, x, flagPosition):
    """Checks if the TCP flag at the given position is set"""

        # Check if the bit is set
        if x & 2 ** flagPosition == 0:
            return False
        else:
            return True

    def intToFlags(self, x):
        """Convert TCP flags from int to string

        The flags-parameter of a network packets is returned as an int, this function converts
        it to a string (such as "FA" if the Fin-flag and Ack-flag have been set)
        """

        result = ""
        if self.checkForFlag(x, 0):
            result = result + "F"
        if self.checkForFlag(x, 1):
            result = result + "S"
        if self.checkForFlag(x, 2):
            result = result + "R"
        if self.checkForFlag(x, 3):
            result = result + "P"
        if self.checkForFlag(x, 4):
            result = result + "A"
        if self.checkForFlag(x, 5):
            result = result + "U"
        return result
    
    def isFlags(self, inputString):
        """Checks if a given string is a valid TCP flag"""
        
        isFlags = False
        matchResult = re.match("[FSRPAU]*", inputString)
        if matchResult is not None:
            isFlags = matchResult.group(0) == inputString
        return isFlags

    # TODO Why is both this and the tracker included
    def sniffPackets(self):
        sniffedPackets = sniff(lfilter=lambda x: IP in x and x[IP].src == self.serverIP and
                                                 TCP in x and x[TCP].dport == self.senderPort,
                               timeout=self.waitTime)
        print('sniffed')
        return sniffedPackets

    # TODO When is this needed?
    def captureResponse(self, waitTime=None):
        if waitTime is None:
            waitTime = self.waitTime
        return self.sendInput("nil", None, None, None, waitTime);

    # sends input over the network to the server
    def sendInput(self, input1, seqNr, ackNr, payload, waitTime=None):
        if waitTime is None:
            waitTime = self.waitTime
        # add the MAC-address of the server to scapy's ARP-table to use LAN
        # used every iteration, otherwise the entry somehow
        # w disappears after a while
        # conf.netcache.arp_cache[self.serverIP] = self.serverMAC
        conf.sniff_promisc=False

        timeBefore = time.time()
        
        if input1 != "nil":
            #response = self.sendPacket(input1, seqNr, ackNr)
            ###
            packet = self.createPacket(input1, seqNr, ackNr, payload)
            ###
        else:
            packet = None
        response = self.sendAndRecv(packet, waitTime)
        
        # wait a certain amount of time after sending the packet
        timeAfter = time.time()
        timeSpent = timeAfter - timeBefore
        if timeSpent < waitTime:
            time.sleep(waitTime - timeSpent)
        if type(response) is not Timeout:
            global seqVar, ackVar
            seqVar = response.seq;
            ackVar = response.ack;
        return response

    # resets by way of a valid reset. Requires a valid sequence number. Avoids problems encountered with the maximum
    # number of connections allowed on a port.
    def sendValidReset(self,seq):
        if self.resetMechanism == 0 or self.resetMechanism == 2:
            self.sendInput("R", seq, 0, '')
            if self.useTracking == True:
                self.tracker.clearLastResponse()
        if self.resetMechanism == 1 or self.resetMechanism == 2:
            self.sendReset()

    # resets the connection by changing the port number. Be careful, on some OSes (Win 8) upon hitting a certain number of
    # connections opened on a port, packets are sent to close down connections, which affects learning. TCP configurations
    # can be altered, but I'd say in case learning involves many queries, use the other method.
    def sendReset(self):
        self.refreshNetworkPort()
        if self.useTracking == True:
            self.tracker.reset()
            
            
    def shutdown(self):
        if self.useTracking == True:
            self.tracker.stop();

# example on how to run the sender
if __name__ == "__main__":
    print("main test")
    sender = Sender(serverMAC="08:00:27:23:AA:AF", serverIP="131.174.142.227", serverPort=8000, useTracking=False, isVerbose=0, networkPortMinimum=20000, waitTime=1)
    seq = 50
    sender.refreshNetworkPort()
    sender.sendInput("S", seq, 1, '') #SA svar seq+1 | SYN_REC
    sender.sendInput("A", seq + 1, seqVar + 1, '') #A svar+1 seq+2 | CLOSE_WAIT
    sender.sendInput("FA", seq + 1, seqVar + 1, '')

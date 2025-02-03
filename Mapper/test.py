from sender import Sender

sender = Sender(serverIP="google.com", serverPort=80)

syn = sender.createPacket("S",0,0,"")

syn_ack = sender.sendPacketAndRetrieveResponse(syn)

ack = sender.createPacket("A", syn_ack.ack + 1, syn_ack.seq + 1, "")

final = sender.sendPacketAndRetrieveResponse(ack)

print(syn_ack)
print(final)

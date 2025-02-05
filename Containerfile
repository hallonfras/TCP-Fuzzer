FROM ubuntu:latest
ENV TZ="Europe/Stockholm" 
RUN apt-get update && apt-get install -y curl build-essential
RUN apt-get install -y python3 python3-scapy python3-pcapy python3-impacket

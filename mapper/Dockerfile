FROM python:3.13-bookworm
RUN ["apt", "update"]
RUN ["apt", "install", "-y", "libpcap-dev"]
RUN ["apt", "install", "-y", "iptables"]
RUN ["apt", "install", "-y", "dos2unix"]
WORKDIR /app

ENV PYTHONUNBUFFERED=1

COPY *.py ./
COPY requirements.txt ./
COPY start.sh ./

RUN chmod +x ./start.sh
RUN dos2unix ./start.sh

RUN pip3 install -r requirements.txt


ENTRYPOINT bash start.sh

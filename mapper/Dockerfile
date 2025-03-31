FROM python:3.13-bookworm
RUN ["apt", "update"]
RUN ["apt", "install", "-y", "libpcap-dev"]
RUN ["apt", "install", "-y", "iptables"]
WORKDIR /app

ENV PYTHONUNBUFFERED=1

COPY *.py ./
COPY requirements.txt ./
COPY start.sh ./

RUN chmod +x ./start.sh

RUN pip3 install -r requirements.txt


ENTRYPOINT sh start.sh

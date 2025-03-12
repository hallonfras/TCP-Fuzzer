FROM python:3.13-bookworm
RUN ["apt", "update"]
RUN ["apt", "install", "-y", "libpcap-dev"]

WORKDIR /app

COPY *.py ./
COPY requirements.txt ./

RUN pip3 install -r requirements.txt


ENTRYPOINT ["python3","-u", "main.py"]

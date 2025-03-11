FROM python:3.9-bookworm
RUN ["apt", "update"]
RUN ["apt", "install", "-y", "libpcap-dev"]

WORKDIR /app

COPY *.py ./
COPY requirements.txt ./

RUN pip3 install -r requirements.txt


ENTRYPOINT ["python3", "main.py", "/config/mapperconfig.toml"]

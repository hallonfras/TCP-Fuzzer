#!/bin/bash

iptables -A OUTPUT -p tcp --tcp-flags RST RST -j DROP
python3 main.py /config/mapperconfig.toml

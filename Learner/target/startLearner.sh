#!/bin/bash
java -jar TCP-Learner.jar state-fuzzer-server -tcpMapperAddress 127.0.0.1:18200 -connect 127.0.0.1:18200 -alphabet default_alphabet.xml

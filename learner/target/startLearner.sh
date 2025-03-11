#!/bin/bash
java -jar TCP-Learner.jar state-fuzzer-server -tcpMapperAddress 127.0.0.1:18200 -connect 127.0.0.1:18200 -minLength 1 -maxLength 1 -cacheTests -alphabet default_alphabet.xml

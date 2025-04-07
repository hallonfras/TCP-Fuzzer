!#/bin/bash
java -jar TCP-Learner.jar state-fuzzer-server -connect mapper:18200 -tcpMapperAddress mapper:18200 -alphabet /config/alphabet.xml -output /app/output_folder/ -memQueryRuns 3

Cannot invoke "String.split(String)" because "output" is null
java.lang.NullPointerException: Cannot invoke "String.split(String)" because "output" is null
	at Learner.TCPMapperSul.step(TCPMapperSul.java:128)
	at Learner.TCPMapperSul.step(TCPMapperSul.java:17)
	at com.github.protocolfuzzing.protocolstatefuzzer.components.sul.core.sulwrappers.SulLivenessWrapper.step(SulLivenessWrapper.java:71)
	at de.learnlib.filter.statistic.sul.SymbolCounterSUL.step(SymbolCounterSUL.java:49)
	at de.learnlib.filter.statistic.sul.ResetCounterSUL.step(ResetCounterSUL.java:49)
	at com.github.protocolfuzzing.protocolstatefuzzer.components.sul.core.sulwrappers.LoggingWrapper.step(LoggingWrapper.java:61)
	at de.learnlib.oracle.membership.SULOracle.answerQuery(SULOracle.java:59)
	at de.learnlib.oracle.membership.SULOracle.answerQuery(SULOracle.java:31)
	at de.learnlib.oracle.MembershipOracle.answerQuery(MembershipOracle.java:41)
	at com.github.protocolfuzzing.protocolstatefuzzer.components.learner.oracles.NonDeterminismRetryingSULOracle.processQuery(NonDeterminismRetryingSULOracle.java:49)
	at de.learnlib.oracle.MembershipOracle.answerQuery(MembershipOracle.java:47)
	at de.learnlib.oracle.MembershipOracle.answerQuery(MembershipOracle.java:41)
	at com.github.protocolfuzzing.protocolstatefuzzer.components.learner.oracles.CachingSULOracle.processQueries(CachingSULOracle.java:75)
	at de.learnlib.oracle.MembershipOracle.processQuery(MembershipOracle.java:63)
	at com.github.protocolfuzzing.protocolstatefuzzer.components.learner.oracles.LoggingSULOracle.processQuery(LoggingSULOracle.java:58)
	at de.learnlib.oracle.MembershipOracle.answerQuery(MembershipOracle.java:47)
	at de.learnlib.algorithm.ttt.base.AbstractTTTLearner.query(AbstractTTTLearner.java:907)
	at de.learnlib.algorithm.ttt.base.AbstractTTTLearner.query(AbstractTTTLearner.java:921)
	at de.learnlib.algorithm.ttt.mealy.TTTLearnerMealy.createTransition(TTTLearnerMealy.java:58)
	at de.learnlib.algorithm.ttt.base.AbstractTTTLearner.initializeState(AbstractTTTLearner.java:181)
	at de.learnlib.algorithm.ttt.base.AbstractTTTLearner.startLearning(AbstractTTTLearner.java:156)
	at com.github.protocolfuzzing.protocolstatefuzzer.statefuzzer.core.StateFuzzerStandard.inferStateMachine(StateFuzzerStandard.java:136)
	at com.github.protocolfuzzing.protocolstatefuzzer.statefuzzer.core.StateFuzzerStandard.startFuzzing(StateFuzzerStandard.java:84)
	at com.github.protocolfuzzing.protocolstatefuzzer.entrypoints.CommandLineParser.executeCommand(CommandLineParser.java:355)
	at com.github.protocolfuzzing.protocolstatefuzzer.entrypoints.CommandLineParser.parseAndExecuteCommand(CommandLineParser.java:226)
	at com.github.protocolfuzzing.protocolstatefuzzer.entrypoints.CommandLineParser.parse(CommandLineParser.java:168)
	at com.github.protocolfuzzing.protocolstatefuzzer.entrypoints.CommandLineParser.parse(CommandLineParser.java:199)
	at Learner.Main.main(Main.java:24)

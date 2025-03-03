import com.github.protocolfuzzing.protocolstatefuzzer.components.learner.*;
import com.github.protocolfuzzing.protocolstatefuzzer.components.learner.statistics.MealyMachineWrapper;
import com.github.protocolfuzzing.protocolstatefuzzer.entrypoints.*;

import java.util.List;


public class Main {
    public static void main(String[] args) {
        // Multibuilder implements all necessary builders
        MultiBuilder mb = new MultiBuilder();

        // single parentLogger, if Main resides in the outermost package
        String[] parentLoggers = {Main.class.getPackageName()};

        CommandLineParser<MealyMachineWrapper<TCPInput, TCPOutput>> commandLineParser = new CommandLineParser<>(mb, mb, mb, mb);

        commandLineParser.setExternalParentLoggers(parentLoggers);

        List<LearnerResult<MealyMachineWrapper<TCPInput, TCPOutput>>> results = commandLineParser.parse(args, true);

        // further process the results if needed
    }
}

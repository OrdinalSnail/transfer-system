package com.nordea.transfer_system.processing;

import com.nordea.transfer_system.consts.ConsoleArg;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

import static com.nordea.transfer_system.consts.ConsoleArg.*;

/**
 * Console arguments parser
 */
@NoArgsConstructor
@Getter
public class ConsoleParser {

    private static final Logger LOG = LoggerFactory.getLogger(ConsoleParser.class);

    private HashMap<ConsoleArg, String> argMap = new HashMap<ConsoleArg, String>();

    /**
     * Parsing console parameters
     *
     * @param args console arguments array
     */
    public void parseParameters(String[] args) {
        LOG.info("Start parsing");

        Options options = new Options();
        options.addOption(buildOption(CA_INC_QUEUE, false));
        options.addOption(buildOption(CA_OUT_QUEUE, false));
        options.addOption(buildOption(CA_CONFIG_PATH, false));

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = null;
        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            LOG.error("Arguments parsing error", e);
        } finally {
            argMap.put(CA_INC_QUEUE, parseValue(CA_INC_QUEUE, cmd));
            argMap.put(CA_OUT_QUEUE, parseValue(CA_OUT_QUEUE, cmd));
            argMap.put(CA_CONFIG_PATH, parseValue(CA_CONFIG_PATH, cmd));
        }
    }

    /**
     * Parsing command line parameter
     *
     * @param arg console argument
     * @param cmd CommandLineParser
     * @return value from console or by default
     */
    private String parseValue(ConsoleArg arg, CommandLine cmd) {
        String value = cmd != null && cmd.hasOption(arg.getName()) ? cmd.getOptionValue(arg.getName()) : arg.getDefValue();
        LOG.info(arg.getDescription() + ": " + value);
        return value;
    }

    /**
     * Get value from console param
     *
     * @param arg console param
     * @return value of param
     */
    public String getValue(ConsoleArg arg) {
        return argMap.get(arg);
    }


    private Option buildOption(ConsoleArg arg, boolean required){
        Option propertyOption = Option.builder()
                .longOpt(arg.getName())
                .argName("property=value")
                .hasArgs()
                .required(required)
                .valueSeparator()
                .numberOfArgs(2)
                .desc(arg.getDescription())
                .build();
        return propertyOption;
    }

}

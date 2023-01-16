package com.nordea.transfer_system.consts;

import java.util.Arrays;
import java.util.Optional;

/**
 * Console arguments characteristics
 * example: --cfg=example-transfer-system.json --inc=cusInQueue --out=custOutQueue
 */
public enum ConsoleArg {

    CA_INC_QUEUE("inc", "Incoming queue name", "inpQueue"),
    CA_OUT_QUEUE("out", "Outgoing queue name", "outQueue"),
    CA_CONFIG_PATH("cfg", "Path to config json", null);

    private final String name;
    private final String description;
    private final String defValue;

    ConsoleArg(String name, String description, String defValue) {
        this.name = name;
        this.description = description;
        this.defValue = defValue;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getDefValue() {
        return defValue;
    }

    // Reverse lookup methods
    public static Optional<ConsoleArg> getAccountStatusByName(String value) {
        return Arrays.stream(ConsoleArg.values())
                .filter(accStatus -> accStatus.name.equals(value))
                .findFirst();
    }


}

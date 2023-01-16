package com.nordea.transfer_system;

import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

import java.io.InputStream;

class AppUtilTest {

    @Test
    void getResourceAsStream() {
        InputStream str = AppUtil.getResourceAsStream("schema/transfersystem.schema.json");
        Assert.notNull(str, "Bad resource");
    }

    @Test
    void getFileAsStream() {
        InputStream str = AppUtil.getFileAsStream("src/main/resources/schema/example/example-transfer-system.json");
        Assert.notNull(str, "Bad resource");
    }


}
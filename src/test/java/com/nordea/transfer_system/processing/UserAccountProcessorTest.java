package com.nordea.transfer_system.processing;

import com.networknt.schema.JsonSchema;
import com.nordea.transfer_system.AppUtil;
import com.nordea.transfer_system.dto.AccountsData;
import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

import java.io.InputStream;
import java.util.stream.Stream;

class UserAccountProcessorTest {

    UserAccountProcessor impl = new UserAccountProcessor();

    @Test
    void readSchemaFromResource() {
        JsonSchema schema = impl.readSchemaFromResources(impl.getSCHEMA_PATH());
        Assert.notNull(schema, "Something wrong with schema");
    }

    @Test
    void validate() {
        JsonSchema schema = impl.readSchemaFromResources(impl.getSCHEMA_PATH());
        InputStream example = AppUtil.getResourceAsStream("schema/example/example-transfer-system.json");
        Assert.isTrue(impl.validate(schema, example), "test json is incorrect");
    }

    @Test
    void readAccountData() {
        AccountsData acc = impl.readAccountsData("src/main/resources/schema/example/example-transfer-system.json");
        Assert.notNull(acc, "Error converting json to Account");
    }




}
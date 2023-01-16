package com.nordea.transfer_system.processing;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;
import com.nordea.transfer_system.AppUtil;
import com.nordea.transfer_system.dto.AccountsData;
import com.nordea.transfer_system.generated.schema.Account;
import com.nordea.transfer_system.generated.schema.CurrencyAmount;
import com.nordea.transfer_system.model.AccountEntity;
import com.nordea.transfer_system.model.CurrencyAmountEntity;
import com.nordea.transfer_system.repo.AccountRepo;
import com.nordea.transfer_system.repo.CurrencyAmountRepo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
@Getter
@Component
public class UserAccountProcessor {

    private static final Logger LOG = LoggerFactory.getLogger(UserAccountProcessor.class);

    private final String SCHEMA_PATH = "schema/transfersystem.schema.json";

    @Autowired
    private AccountRepo accountRepo;
    @Autowired
    private CurrencyAmountRepo currencyAmountRepo;

    private ObjectMapper mapper = new ObjectMapper();
    private AccountsData accounts = null;

    private String filePath;


    public void updateInfo(String filePath) {
        this.filePath = filePath;
        accounts = readAccountsData(filePath);
        if (accounts == null || CollectionUtils.isEmpty(accounts.getAccounts())) {
            return;
        }
        for (Account account : accounts.getAccounts()) {

            List<CurrencyAmount> incCurrencies = account.getCurrencyAmounts();

            if (!CollectionUtils.isEmpty(incCurrencies)) {

                AccountEntity accountEntity = accountRepo.findByAccountNumber(account.getAccountNumber());
                if (accountEntity == null) {
                    continue;
                }
                if (accountEntity.getCurrencyAmounts() != null && !accountEntity.getCurrencyAmounts().isEmpty()) {
                    LOG.info("Clean list: " + accountEntity.getId());
                    accountEntity.getCurrencyAmounts().clear();
                    accountRepo.save(accountEntity);
                }
                AccountEntity accountEntityForAddRecs = accountRepo.findByAccountNumber(account.getAccountNumber());
                accountEntityForAddRecs.setCurrencyAmounts(new ArrayList<>());

                for (CurrencyAmount rec : incCurrencies) {
                    CurrencyAmountEntity entity = new CurrencyAmountEntity();
                    entity.setAccount(accountEntityForAddRecs);
                    entity.setCurrency(rec.getCurrency());
                    entity.setAmount(new BigDecimal(rec.getAmount()));
                    accountEntityForAddRecs.getCurrencyAmounts().add(entity);
                }
                LOG.info("Save user account: " + accountEntityForAddRecs.getId());
                accountRepo.save(accountEntityForAddRecs);
            }
        }
    }


    /**
     * read, check and convert AccauntsData from file
     *
     * @param filePath path to file
     * @return AccauntsData
     */
    public AccountsData readAccountsData(String filePath) {
        JsonSchema jsonSchema = readSchemaFromResources(SCHEMA_PATH);
        InputStream jsonStream = AppUtil.getFileAsStream(filePath);
        if (jsonStream == null || !validate(jsonSchema, jsonStream)) {
            return null;
        }
        jsonStream = AppUtil.getFileAsStream(filePath);//TODO not very good solution, need find how duplicate stream
        try {
            AccountsData accounts = mapper.readValue(jsonStream, AccountsData.class);
            return accounts;
        } catch (IOException e) {
            LOG.error(null, e);
        }
        return null;
    }

    /**
     * Validate json file
     *
     * @param jsonSchema for validation
     * @param jsonStream InputStream from checked json
     * @return true if json matches the pattern
     */
    public boolean validate(JsonSchema jsonSchema, InputStream jsonStream) {
        JsonNode jsonNode;
        try {
            jsonNode = mapper.readTree(jsonStream);
        } catch (IOException e) {
            LOG.error("ReadTree error", e);
            return false;
        }
        Set<ValidationMessage> errors = jsonSchema.validate(jsonNode);
        if (errors == null || errors.isEmpty()) {
            return true;
        } else {
            errors.forEach(rec -> LOG.error(rec.getMessage()));
        }
        return false;
    }

    /**
     * Read schema from resource
     *
     * @param path path to resource
     * @return JsonSchema for validate
     */
    public JsonSchema readSchemaFromResources(String path) {
        JsonSchemaFactory factory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V202012);
        InputStream str = AppUtil.getResourceAsStream(path);
        if (str == null) {
            return null;
        }
        return factory.getSchema(str);
    }


    public void changeUserAccountData(CurrencyAmountEntity currency) {
        if (accounts == null || accounts.getAccounts() == null || accounts.getAccounts().isEmpty()) {
            return;
        }
        long currencyId = currency.getId();
        CurrencyAmountEntity upCurrency = currencyAmountRepo.findById(currencyId);
        for (Account account : accounts.getAccounts()) {
            if (account.getCurrencyAmounts() == null || account.getCurrencyAmounts().isEmpty()) {
                continue;
            }
            for (CurrencyAmount curAmountDto : account.getCurrencyAmounts()) {
                if (upCurrency.getCurrency().equals(curAmountDto.getCurrency())
                        && upCurrency.getAccount().getAccountNumber().equals(account.getAccountNumber())) {
                    curAmountDto.setAmount(upCurrency.getAmount().doubleValue());
                }
            }

        }
        try {
            mapper.writeValue(new File("filePath"), accounts);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}

package com.nordea.transfer_system.processing;

import com.nordea.transfer_system.generated.xsd.ActionType;
import com.nordea.transfer_system.generated.xsd.OutcomeType;
import com.nordea.transfer_system.generated.xsd.TransferRequestType;
import com.nordea.transfer_system.model.AccountEntity;
import com.nordea.transfer_system.model.CurrencyAmountEntity;
import com.nordea.transfer_system.repo.AccountRepo;
import com.nordea.transfer_system.repo.CurrencyAmountRepo;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.math.BigDecimal;

@NoArgsConstructor
@Component
public class AccountProcessor {

    @Autowired
    private AccountRepo repo;
    @Autowired
    private CurrencyAmountRepo currencyRepo;
    @Autowired
    UserAccountProcessor userAccountProcessor;

    @Transactional
    public OutcomeType processingRequest(TransferRequestType request) {
        if (request == null || request.getTargetAccountNumber() == null) {
            return OutcomeType.REJECT;
        }
        AccountEntity account = repo.findByAccountNumber(request.getTargetAccountNumber());

        if (account == null) {
            return OutcomeType.REJECT;
        }
        CurrencyAmountEntity currency = currencyRepo.findByAccountIdAndCurrency(account.getId(), request.getCurrency());

        //If Account don't have currency
        if (currency == null) {
            return OutcomeType.REJECT;
        }
        if (ActionType.CREDIT.equals(request.getAction())) {
            BigDecimal sum = currency.getAmount().add(request.getQuantity());//??? check limit
            currency.setAmount(sum);
            currencyRepo.save(currency);
            userAccountProcessor.changeUserAccountData(currency);
            return OutcomeType.ACCEPT;
        }
        // DEBIT checkout - ActionType is not null
        if (currency.getAmount().compareTo(request.getQuantity()) > -1) {//balance can't to be < 0
            BigDecimal balance = currency.getAmount().subtract(currency.getAmount());
            currency.setAmount(balance);
            currencyRepo.save(currency);
            userAccountProcessor.changeUserAccountData(currency);
            return OutcomeType.ACCEPT;
        }
        return OutcomeType.REJECT;//If DEBIT more thrn Balance
    }


}

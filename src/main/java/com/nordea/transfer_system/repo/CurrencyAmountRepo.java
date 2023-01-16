package com.nordea.transfer_system.repo;


import com.nordea.transfer_system.model.CurrencyAmountEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrencyAmountRepo extends CrudRepository<CurrencyAmountEntity, Long> {

    CurrencyAmountEntity findById(long id);

    CurrencyAmountEntity findByAccountIdAndCurrency(Long accountId, String currency);

}

package com.nordea.transfer_system.repo;


import com.nordea.transfer_system.model.AccountEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepo extends CrudRepository<AccountEntity, Long> {

    /**
     * Work variant
     *
     * @param id
     * @return
     */
    Optional<AccountEntity> findById(long id);

    /**
     * Work variant
     *
     * @param accountNumber
     * @return
     */
    AccountEntity findByAccountNumber(String accountNumber);

}

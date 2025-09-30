package code.with.vanilson.springtx.repositories;


import code.with.vanilson.springtx.models.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Long> {
    List<Account> findByAccountTypeOrderByTransactionDateDesc(String accountType);
}

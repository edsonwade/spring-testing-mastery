package code.with.vanilson.springtx.services;


import code.with.vanilson.springtx.models.Account;
import code.with.vanilson.springtx.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    public void transfer(String fromAccountType, String toAccountType, BigDecimal amount, String description) {
        // Get current balances
        BigDecimal fromBalance = getCurrentBalance(fromAccountType);
        BigDecimal toBalance = getCurrentBalance(toAccountType);

        // Create debit transaction
        Account debit = new Account();
        debit.setAccountType(fromAccountType);
        debit.setTransactionDate(LocalDate.now());
        debit.setAmount(amount.negate());
        debit.setBalance(fromBalance.subtract(amount));
        accountRepository.save(debit);

        // Create credit transaction
        Account credit = new Account();
        credit.setAccountType(toAccountType);
        credit.setTransactionDate(LocalDate.now());
        credit.setAmount(amount);
        credit.setBalance(toBalance.add(amount));
        accountRepository.save(credit);
    }

    private BigDecimal getCurrentBalance(String accountType) {
        return accountRepository.findByAccountTypeOrderByTransactionDateDesc(accountType)
                .stream()
                .findFirst()
                .map(Account::getBalance)
                .orElse(BigDecimal.ZERO);
    }
} 
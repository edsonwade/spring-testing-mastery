package code.with.vanilson.springtx.services;

import code.with.vanilson.springtx.models.Account;
import code.with.vanilson.springtx.repositories.AccountRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@SuppressWarnings("unused")
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {this.accountRepository = accountRepository;}

    @Transactional
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
        // just to simulate an error during the transfer, to explain why the transaction is important.
        // to guarantee that the transaction is a success, we need to annotate with @Transaction on class ou method
        //  which now worked because transactional finds the exception and rolls everything back,
        //  so when we query the
        // account balance, everything is proper state, before we try to debit and credit the accounts.
        if (true) {throw new RuntimeException("Transfer failed");}

        // Create a credit transaction
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
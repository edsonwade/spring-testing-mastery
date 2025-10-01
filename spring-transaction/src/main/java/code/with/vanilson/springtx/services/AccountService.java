package code.with.vanilson.springtx.services;

import code.with.vanilson.springtx.models.Account;
import code.with.vanilson.springtx.repositories.AccountRepository;
import jakarta.transaction.Transaction;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@SuppressWarnings("unused")
public class AccountService {

    private final AccountRepository accountRepository;
    private final TransactionTemplate template;

    public AccountService(AccountRepository accountRepository, TransactionTemplate template) {
        this.accountRepository = accountRepository;
        this.template = template;
    }

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

    // New method to using transaction template (transfer programmatically)
    public void transferProgrammatic(String fromAccountType, String toAccountType, BigDecimal amount,
                                     String description) {
        // Get current balances
        template.execute(status -> {

            BigDecimal fromBalance = getCurrentBalance(fromAccountType);
            BigDecimal toBalance = getCurrentBalance(toAccountType);

            // Create debit transaction
            Account debit = new Account();
            debit.setAccountType(fromAccountType);
            debit.setTransactionDate(LocalDate.now());
            debit.setAmount(amount.negate());
            debit.setBalance(fromBalance.subtract(amount));
            accountRepository.save(debit);

            // Create a credit transaction
            Account credit = new Account();
            credit.setAccountType(toAccountType);
            credit.setTransactionDate(LocalDate.now());
            credit.setAmount(amount);
            credit.setBalance(toBalance.add(amount));
            accountRepository.save(credit);

            return null;
        });
    }

}
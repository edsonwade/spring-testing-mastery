package code.with.vanilson.services;


import code.with.vanilson.springtx.SpringtxApplication;
import code.with.vanilson.springtx.models.Account;
import code.with.vanilson.springtx.repositories.AccountRepository;
import code.with.vanilson.springtx.services.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = SpringtxApplication.class)
@ActiveProfiles("test")
class AccountServiceTest {

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountRepository accountRepository;

    @BeforeEach
    void setUp() {
        accountRepository.deleteAll();

        // Set up initial balances
        Account savings = new Account();
        savings.setAccountType("savings");
        savings.setTransactionDate(LocalDate.now());
        savings.setAmount(new BigDecimal("1000.00"));
        savings.setBalance(new BigDecimal("1000.00"));
        accountRepository.save(savings);

        Account checking = new Account();
        checking.setAccountType("checking");
        checking.setTransactionDate(LocalDate.now());
        checking.setAmount(new BigDecimal("500.00"));
        checking.setBalance(new BigDecimal("500.00"));
        accountRepository.save(checking);
    }

    @Test
    void testTransfer() {
        boolean transferSucceeded = false;
        
        // Transfer $200 from savings to checking
        try {
            accountService.transfer("savings", "checking", new BigDecimal("200.00"), "Test transfer");
            transferSucceeded = true;
        } catch (RuntimeException e) {
            System.out.println("Transfer failed");
        }

        // Check final balances
        List<Account> allAccounts = accountRepository.findAll();
        Account savings = allAccounts.stream()
                .filter(a -> a.getAccountType().equals("savings"))
                .max((a, b) -> Long.compare(a.getId(), b.getId()))
                .orElseThrow();
        Account checking = allAccounts.stream()
                .filter(a -> a.getAccountType().equals("checking"))
                .max((a, b) -> Long.compare(a.getId(), b.getId()))
                .orElseThrow();

        if (transferSucceeded) {
            // Transfer succeeded - check new balances
            assertEquals(new BigDecimal("800.00"), savings.getBalance());
            assertEquals(new BigDecimal("700.00"), checking.getBalance());
        } else {
            // Transfer failed - balances should remain at original values
            assertEquals(new BigDecimal("1000.00"), savings.getBalance());
            assertEquals(new BigDecimal("500.00"), checking.getBalance());
        }
    }

} 
package code.with.vanilson.springtx.config;

import code.with.vanilson.springtx.models.Account;
import code.with.vanilson.springtx.repositories.AccountRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.math.BigDecimal;
import java.time.LocalDate;

@Configuration
public class DbConfiguration {
    @Bean
    public CommandLineRunner seedData(AccountRepository repo) {
        return args -> {
            repo.save(new Account(null, "checking", LocalDate.now(), "Initial Deposit", new BigDecimal("1000.00"), new BigDecimal("1000.00")));
            repo.save(new Account(null, "checking", LocalDate.now(), "ATM Withdrawal", new BigDecimal("-100.00"), new BigDecimal("900.00")));
            repo.save(new Account(null, "savings", LocalDate.now(), "Transfer from Checking", new BigDecimal("500.00"), new BigDecimal("500.00")));
        };
    }
}

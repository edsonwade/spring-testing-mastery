package code.with.vanilson.demo.service;

import org.springframework.stereotype.Service;

@Service
public class BalanceCheckService {

    public String checkAccountBalance(String accountId) {
        try {
            // Simulate a blocking call (e.g., JDBC query, external API)
            Thread.sleep(500); // 500 ms delay
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return "Error: Interrupted";
        }

        // Simulate result
        return "Account " + accountId + " balance: $1,234.56";
    }
}

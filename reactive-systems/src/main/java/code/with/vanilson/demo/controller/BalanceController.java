package code.with.vanilson.demo.controller;

import code.with.vanilson.demo.service.BalanceCheckService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@RestController
public class BalanceController {

    private final BalanceCheckService balanceCheckService;
    private final Executor virtualThreadExecutor = Executors.newVirtualThreadPerTaskExecutor();

    public BalanceController(BalanceCheckService balanceCheckService) {
        this.balanceCheckService = balanceCheckService;
    }

    @GetMapping("/balance/{accountId}")
    public Mono<String> getBalance(@PathVariable String accountId) {
        return Mono.fromCallable(() -> {
            // Runs in a Virtual Thread — no need for complex Reactive chains
            return balanceCheckService.checkAccountBalance(accountId);
        }).subscribeOn(Schedulers.fromExecutor(virtualThreadExecutor));
    }
}

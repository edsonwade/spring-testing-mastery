package code.with.vanilson.demo.controller;

import code.with.vanilson.demo.operators.StockQuote;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@RestController
@RequestMapping("/api/v1")
public class ReactionControllerOne {
    @GetMapping("/message")
    public Mono<String> getMessage() {
        // We return a Mono that emits a single message
        return Mono.just("Sorry, couldn't help, but react!").log();
        // return Flux.just(1972, 1964, 2002, 2025).log();
    }

    @GetMapping("/demo-pipeline")
    public Flux<StockQuote> demoPipeline() {
        return getQuotesForIndustry("software")
                .map(quote -> {
                    quote.setLanguageCode("ENRICHED");
                    return quote;
                })
                .filter(quote -> quote.getLanguageCode().startsWith("E"))
                .take(1);
    }

    private @NotNull Flux<StockQuote> getQuotesForIndustry(String industryCode) {
        return Flux.range(1, 5)
                .map(i -> {
                    StockQuote q = new StockQuote();
                    q.setIndustryCode(industryCode);
                    q.setLanguageCode("en");
                    return q;
                })
                .delayElements(Duration.ofMillis(100));
    }

    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> streamData() {
        return Flux.interval(Duration.ofSeconds(1))
                .map(seq -> "Data item " + seq)
                .take(5);
    }
}

package code.with.vanilson.demo.operators;

import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.Comparator;
import java.util.List;

public class ReactiveOperatorsExample {

    public static void main(String[] args) {
        new ReactiveOperatorsExample().demoReactivePipeline();
    }

    public void demoReactivePipeline() {

        // Simulate a few industries
        List<String> industries = List.of("software", "finance", "healthcare");

        Flux<StockQuote> industryFlux = Flux.fromIterable(industries)
                .delayElements(Duration.ofMillis(100)) // Simulate async delay per element
                .map(String::toUpperCase) // Transform to uppercase
                .filter(code -> code.startsWith("S") || code.startsWith("F")) // Filter only certain codes
                .flatMap(this::getQuotesForIndustry) // Flatten results
                .take(5) // Take only the first 5 quotes
                .doOnNext(quote -> System.out.println("Processing quote: " + quote)) // Side effect
                .sort(Comparator.comparing(StockQuote::getIndustryCode)); // Sort the quotes

        // Run (subscribe)
        industryFlux.blockLast(); // Only for demo - block to see output
    }

    private @NotNull Flux<StockQuote> getQuotesForIndustry(String industryCode) {
        StockQuote quote1 = new StockQuote();
        quote1.setIndustryCode(industryCode);
        quote1.setLanguageCode("en");

        StockQuote quote2 = new StockQuote();
        quote2.setIndustryCode(industryCode);
        quote2.setLanguageCode("fr");

        return Flux.just(quote1, quote2);
    }
}

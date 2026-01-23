package code.with.vanilson.demo.operators;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class IndustryService {

    public Flux<StockQuote> getAllStockQuotes(String industryCode, String language) {
        StockQuote quote = new StockQuote();
        quote.setIndustryCode(industryCode);
        quote.setLanguageCode(language);
        return Flux.just(quote); // Simulated dummy data
    }
}

package code.with.vanilson.demo;

import code.with.vanilson.demo.operators.FlatMapOne;
import code.with.vanilson.demo.operators.Industry;
import code.with.vanilson.demo.operators.IndustryService;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class FlatMapOneTest {

    @Test
    public void testGetIndustryQuotes() {
        // Arrange: Use the default stubbed service
        IndustryService stubService = new IndustryService();
        FlatMapOne flatMapOne = new FlatMapOne(stubService);

        // Act
        Flux<Industry> result = flatMapOne.getIndustryQuotes("software");

        // Assert
        StepVerifier.create(result)
                .expectNextMatches(industry ->
                        "software".equals(industry.getIndustryCode()) &&
                                industry.getStockQuoteList() != null &&
                                !industry.getStockQuoteList().isEmpty() &&
                                "software".equals(industry.getStockQuoteList().getFirst().getIndustryCode())
                )
                .expectComplete()
                .verify();
    }
}

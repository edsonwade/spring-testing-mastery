package code.with.vanilson.demo.operators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
public class FlatMapOne {

    private final IndustryService service;

    @Autowired
    public FlatMapOne(IndustryService service) {
        this.service = service;
    }

    @EventListener(ContextRefreshedEvent.class)
    public void onApplicationEvent() {
        System.out.println(">> App is ready. Triggering getIndustryQuotes...");
        getIndustryQuotes("software")
                .doOnNext(System.out::println)
                .subscribe();
    }

    public Flux<Industry> getIndustryQuotes(String industryCode) {
        return service.getAllStockQuotes(industryCode, "en")
                .flatMap(e -> service.getAllStockQuotes(e.getIndustryCode(), "en")
                        .collectList()
                        .map(list -> new Industry(e.getIndustryCode(), list))
                );
    }
}



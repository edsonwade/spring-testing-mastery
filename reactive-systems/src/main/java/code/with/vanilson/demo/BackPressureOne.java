package code.with.vanilson.demo;

import reactor.core.publisher.Flux;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.concurrent.atomic.AtomicInteger;

public class BackPressureOne {

    public static void main(String[] args) {

        Flux<Integer> flux = Flux.range(1, 100)
                .doOnRequest(n -> System.out.println("[Flux] Request received: " + n))
                .limitRate(5); // Limit the rate to 5 items per request downstream

        flux.subscribe(new Subscriber<>() {

            private Subscription subscription;
            private final AtomicInteger count = new AtomicInteger();
            private final int BATCH_SIZE = 5;

            @Override
            public void onSubscribe(Subscription s) {
                this.subscription = s;
                System.out.println("[Subscriber] Subscribed");
                subscription.request(BATCH_SIZE);
            }

            @Override
            public void onNext(Integer item) {
                System.out.println("[Subscriber] Received: " + item);
                count.getAndIncrement();
                if (count.get() % BATCH_SIZE == 0) {
                    System.out.println("[Subscriber] Requesting next batch of " + BATCH_SIZE);
                    subscription.request(BATCH_SIZE);
                }
            }

            @Override
            public void onError(Throwable t) {
                System.err.println("[Subscriber] Error: " + t.getMessage());
            }

            @Override
            public void onComplete() {
                System.out.println("[Subscriber] Completed");
            }
        });
    }
}

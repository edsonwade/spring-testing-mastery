package code.with.vanilson.demo;

import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.concurrent.TimeoutException;

class GnarlyReactiveOrderProcessor {

    public static void main(String[] args) {
        Order order = new Order(123L, true, true);
        new GnarlyReactiveOrderProcessor()
                .processOrder(order)
                .block();
    }

    public Mono<Order> processOrder(Order order) {
        return Mono.just(order)
                .flatMap(this::validateOrder)
                .retryWhen(Retry.fixedDelay(3, Duration.ofSeconds(1)))
                .flatMap(validatedOrder -> {
                    if (validatedOrder.requiresInventoryReservation()) {
                        return reserveInventory(validatedOrder)
                                .retryWhen(Retry.backoff(5, Duration.ofMillis(500)).maxBackoff(Duration.ofSeconds(5)))
                                .onErrorResume(e -> fallbackToManualReservation(validatedOrder));
                    } else {
                        return Mono.just(validatedOrder);
                    }
                })
                .flatMap(this::chargeCustomer)
                .retryWhen(Retry.backoff(3, Duration.ofSeconds(1)).jitter(0.5))
                .flatMap(chargedOrder -> {
                    if (chargedOrder.shouldExpediteShipping()) {
                        return shipOrderExpedited(chargedOrder);
                    } else {
                        return shipOrderStandard(chargedOrder);
                    }
                })
                .timeout(Duration.ofSeconds(10))
                .onErrorResume(TimeoutException.class, e -> notifyOpsTeam(order, e).then(Mono.error(e)))
                .flatMap(this::completeOrder)
                .doOnError(e -> System.err.println("Order processing failed for " + order.getId() + ": " + e))
                .doOnSuccess(o -> System.out.println("Order " + o.getId() + " processed successfully"));
    }

    private Mono<Order> validateOrder(Order order) {
        System.out.println("Validating order " + order.getId());
        return Mono.just(order).delayElement(Duration.ofMillis(200));
    }

    private Mono<Order> reserveInventory(Order order) {
        System.out.println("Reserving inventory for order " + order.getId());
        return Mono.just(order).delayElement(Duration.ofMillis(300));
    }

    private Mono<Order> fallbackToManualReservation(Order order) {
        System.out.println("Falling back to manual reservation for order " + order.getId());
        return Mono.just(order).delayElement(Duration.ofSeconds(1));
    }

    private Mono<Order> chargeCustomer(Order order) {
        System.out.println("Charging customer for order " + order.getId());
        return Mono.just(order).delayElement(Duration.ofMillis(250));
    }

    private Mono<Order> shipOrderExpedited(Order order) {
        System.out.println("Shipping order " + order.getId() + " with expedited shipping");
        return Mono.just(order).delayElement(Duration.ofMillis(400));
    }

    private Mono<Order> shipOrderStandard(Order order) {
        System.out.println("Shipping order " + order.getId() + " with standard shipping");
        return Mono.just(order).delayElement(Duration.ofMillis(400));
    }

    private Mono<Void> notifyOpsTeam(Order order, Throwable e) {
        System.out.println("Notifying ops team about timeout for order " + order.getId());
        return Mono.empty();
    }

    private Mono<Order> completeOrder(Order order) {
        System.out.println("Completing order " + order.getId());
        return Mono.just(order).delayElement(Duration.ofMillis(150));
    }

    // Simple Order class for demonstration
    static class Order {
        private final long id;
        private final boolean requiresInventoryReservation;
        private final boolean expediteShipping;

        public Order(long id, boolean requiresInventoryReservation, boolean expediteShipping) {
            this.id = id;
            this.requiresInventoryReservation = requiresInventoryReservation;
            this.expediteShipping = expediteShipping;
        }

        public long getId() {
            return id;
        }

        public boolean requiresInventoryReservation() {
            return requiresInventoryReservation;
        }

        public boolean shouldExpediteShipping() {
            return expediteShipping;
        }
    }
}
package code.with.vanilson.demo;

import code.with.vanilson.demo.controller.ReactionControllerOne;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.web.reactive.server.WebTestClient;

@WebFluxTest(ReactionControllerOne.class)
public class ReactionControllerOneTest {

    @Autowired
    WebTestClient webTestClient;

    @Test
    public void testMessage() {
        webTestClient.get().uri("/api/v1/message")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(String.class).isEqualTo("Sorry, couldn't help, but react!");
    }
}

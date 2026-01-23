package code.with.vanilson.e2e;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * End-to-End tests for the Coffee Service using Selenium.
 * Note: Since this service is a REST API without a frontend,
 * these tests demonstrate how Selenium could be used if a UI existed,
 * or can be used to test the API's discoverability/responses in a browser-like environment.
 *
 * @author vanilson
 * @version 1.0
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CoffeeE2ETest {

    @LocalServerPort
    private int port;

    private WebDriver driver;

    @BeforeAll
    static void setupClass() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setupTest() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless"); // Run in headless mode for CI/CD
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");
        driver = new ChromeDriver(options);
    }

    @AfterEach
    void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    @DisplayName("Check if the coffee service API is accessible via browser")
    void testApiIsAccessible() {
        String url = "http://localhost:" + port + "/coffees";
        driver.get(url);

        // Since it's a REST API, the browser will display JSON.
        // We can check if the page source contains expected JSON elements.
        String pageSource = driver.getPageSource();

        // Verify that we get a JSON response (even if empty list [])
        assertTrue(pageSource.contains("[]") || pageSource.contains("name"),
                "The API response should be visible in the browser");
    }
}

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests for App class.
 */
public class AppTest {

    @Test
    public void isGreetingHelloWorld() {
        Assertions.assertEquals("Hello World!", new App().sayHello());
    }
}

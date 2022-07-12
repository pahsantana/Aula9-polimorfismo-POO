/**
 * App class.
 */
public class App {

    public String sayHello() {
        return "Hello World!";
    }

    public static void main(String[] args) {
        System.out.println(new App().sayHello());
    }
}

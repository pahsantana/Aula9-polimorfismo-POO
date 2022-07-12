import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Tests for circuit hierarchy.
 */
public class CircuitTest {
    private static class TestCase {
        public final String caseName;
        public final double expectedResistance;
        public final double actualResistance;

        public TestCase(String caseName, double expectedResistance, double actualResistance) {
            this.caseName = caseName;
            this.expectedResistance = expectedResistance;
            this.actualResistance = actualResistance;
        }
    }

    private static final List<TestCase> testCases = new ArrayList<>();
    private static final String circuitsFile = "src/test/resources/circuits.txt";
    private static final double tolerance = 0.0001;

    @BeforeAll
    public static void init() {
        Locale.setDefault(Locale.US);
    }

    /**
     * Parse a single circuit from the test case file.
     *
     * @param scanner the input scanner
     * @return the circuit
     */
    private static Circuit parseCircuit(Scanner scanner) {
        var circuitType = scanner.next();

        if ("Resistor".equals(circuitType)) {
            return new Resistor(scanner.nextDouble());
        } else if ("SerialCircuit".equals(circuitType)) {
            var circuits = new Circuit[scanner.nextInt()];

            for (var i = 0; i < circuits.length; i++) {
                circuits[i] = parseCircuit(scanner);
            }
            return new SerialCircuit(circuits);
        } else if ("ParallelCircuit".equals(circuitType)) {
            var circuits = new Circuit[scanner.nextInt()];

            for (var i = 0; i < circuits.length; i++) {
                circuits[i] = parseCircuit(scanner);
            }

            return new ParallelCircuit(circuits);
        }

        throw new IllegalStateException("Unknown circuit type: " + circuitType);
    }

    /**
     * Load all cases from case file.
     */
    @BeforeAll
    public static void loadCases() {
        try (var scanner = new Scanner(new File(circuitsFile))) {
            while (scanner.hasNext()) {
                var caseName = scanner.next();
                var circuit = parseCircuit(scanner);
                var expectedResistance = scanner.nextDouble();

                testCases.add(new TestCase(caseName, expectedResistance, circuit.getResistance()));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to parse circuits file", e);
        }
    }

    @Test
    public void checkInheritance() {
        Assertions.assertEquals(Resistor.class.getSuperclass(), Circuit.class,
            "Resistor should inherit from Circuit");
        Assertions.assertEquals(CompositeCircuit.class.getSuperclass(), Circuit.class,
            "CompositeCircuit should inherit from Circuit");
        Assertions.assertEquals(ParallelCircuit.class.getSuperclass(), CompositeCircuit.class,
            "ParallelCircuit should inherit from Circuit");
        Assertions.assertEquals(SerialCircuit.class.getSuperclass(), CompositeCircuit.class,
            "SerialCircuit should inherit from Circuit");
    }

    @Test
    public void checkCircuitApi() {
        Assertions.assertTrue(Modifier.isAbstract(Circuit.class.getModifiers()),
            "Circuit should be abstract");
        var found = false;

        for (var method : Circuit.class.getMethods()) {
            if ("getResistance".equals(method.getName())) {
                Assertions.assertTrue(Modifier.isAbstract(method.getModifiers()));
                found = true;
            }
        }
        if (!found) {
            Assertions.fail("Circuit should have an abstract 'getResistance' method");
        }
    }

    @Test
    public void checkCompositeCircuitApi() {
        Assertions.assertTrue(Modifier.isAbstract(CompositeCircuit.class.getModifiers()),
            "CompositeCircuit should be abstract");
        Assertions.assertEquals(CompositeCircuit.class.getConstructors().length, 1,
            "CompositeCircuit should have a single constructor");
        Assertions.assertDoesNotThrow(
            () -> CompositeCircuit.class.getConstructor(Circuit[].class),
            "CompositeCircuit constructor should receive array of Circuit as parameter");

        var found = false;

        for (var method : CompositeCircuit.class.getMethods()) {
            if ("getCircuits".equals(method.getName())) {
                found = true;
                break;
            }
        }
        if (!found) {
            Assertions.fail("CompositeCircuit should a have getter method 'getCircuits'");
        }
    }

    @Test
    public void testResistance() {
        for (var testCase : testCases) {
            Assertions.assertEquals(testCase.expectedResistance,
                testCase.actualResistance, tolerance,
                testCase.caseName + ": Wrong value for circuit resistance.");
        }
    }

}

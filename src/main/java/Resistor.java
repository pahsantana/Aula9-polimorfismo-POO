public class Resistor extends Circuit {

    private final double resistance;

    public Resistor(double resistance) {
        if (resistance < 0){
            throw new IllegalArgumentException(" o valor tem que ser positivo");
        }
        this.resistance = resistance;
    }

    @Override
    public double getResistance() {
        return resistance;
    }
}

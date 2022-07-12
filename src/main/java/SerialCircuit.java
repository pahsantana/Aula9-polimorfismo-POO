public class SerialCircuit extends CompositeCircuit {

    public SerialCircuit(Circuit[] circuits) {
        super(circuits);
    }

    @Override
    public double getResistance() {
        double soma = 0;
        for (int i = 0; i < this.getCircuits().length; i++) {
            soma += this.getCircuits()[i].getResistance();
        }
        return soma;
    }
}

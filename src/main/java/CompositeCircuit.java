public abstract class  CompositeCircuit extends Circuit {

    private final Circuit[] circuits;

    public CompositeCircuit(Circuit[] circuits) {
        this.circuits = circuits;
    }

    public Circuit[] getCircuits() {
        return circuits;
    }
}

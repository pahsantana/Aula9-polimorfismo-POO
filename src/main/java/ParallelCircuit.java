public class ParallelCircuit extends CompositeCircuit {

    public ParallelCircuit(Circuit[] circuits){
        super(circuits);
    }

    @Override
    public double getResistance() {
        double resultado = 0;
        for (int i = 0; i < this.getCircuits().length; i++) {
            resultado += (1.0 / this.getCircuits()[i].getResistance());
        }
        resultado = 1/resultado;

        return resultado;
    }

}




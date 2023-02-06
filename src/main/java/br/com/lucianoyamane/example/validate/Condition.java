package br.com.lucianoyamane.example.validate;

public abstract class Condition<T extends Validate> {

    private T valida;

    protected Condition(T valida) {
        this.setValida(valida);
    }

    protected T getValida() {
        return valida;
    }

    private void setValida(T valida) {
        this.valida = valida;
    }

    protected abstract void rule(BlockChainValidateApp.PreviousBlockData previousBlockData);

    public void execute(BlockChainValidateApp.PreviousBlockData previousBlockData){
        this.rule(previousBlockData);
    }

}

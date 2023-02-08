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

    protected abstract String getMessage();

    protected abstract Boolean rule(BlockChainValidateApp.PreviousBlockData previousBlockData);

    public Boolean check(BlockChainValidateApp.PreviousBlockData previousBlockData){
        return this.rule(previousBlockData);
    }

}

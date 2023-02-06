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

    protected abstract void definicao(BlockChainValidateApp.PreviousBlockData previousBlockData);

    public void executa(BlockChainValidateApp.PreviousBlockData previousBlockData){
        this.definicao(previousBlockData);
    }

}

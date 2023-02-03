package br.com.lucianoyamane.example.valida;

public abstract class Condicao<T extends Valida> {

    private T valida;

    protected Condicao(T valida) {
        this.setValida(valida);
    }

    protected T getValida() {
        return valida;
    }

    private void setValida(T valida) {
        this.valida = valida;
    }

    protected abstract void definicao(BlockChainValidaApp.PreviousBlockData previousBlockData);

    public void executa(BlockChainValidaApp.PreviousBlockData previousBlockData){
        this.definicao(previousBlockData);
    }

}

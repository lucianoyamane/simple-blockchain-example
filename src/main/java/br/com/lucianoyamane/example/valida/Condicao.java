package br.com.lucianoyamane.example.valida;

import br.com.lucianoyamane.example.valida.BlockChainValidaApp;
import br.com.lucianoyamane.example.valida.Valida;

public abstract class Condicao<T extends Valida> {

    private T valida;

    protected Condicao(T valida) {
        this.valida = valida;
    }

    protected T getValida() {
        return valida;
    }

    private void setPreviousBlockData(T valida) {
        this.valida = valida;
    }

    abstract public void executa(BlockChainValidaApp.PreviousBlockData previousBlockData);

}

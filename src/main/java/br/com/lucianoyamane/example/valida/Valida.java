package br.com.lucianoyamane.example.valida;

public abstract class Valida {

    abstract void valida(BlockChainValida.PreviousBlockData previousBlockData);

    abstract void processaDadosProximoBloco(BlockChainValida.PreviousBlockData previousBlockData);

    void executa(BlockChainValida.PreviousBlockData previousBlockData){
        this.valida(previousBlockData);
        this.processaDadosProximoBloco(previousBlockData);
    }
}

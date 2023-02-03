package br.com.lucianoyamane.example.valida;

import java.util.ArrayList;
import java.util.List;

public abstract class Valida {

    protected Valida() {
        this.setCondicoes(new ArrayList<>());
        this.defineCondicoes();
    }

    abstract protected void defineCondicoes();

    private List<Condicao> condicoes;

    public void valida(BlockChainValidaApp.PreviousBlockData previousBlockData) {
        List<Condicao> condicoes = this.getCondicoes();
        for(Condicao condicao : condicoes) {
            condicao.executa(previousBlockData);
        }
    };

    abstract public void processaDadosProximoBloco(BlockChainValidaApp.PreviousBlockData previousBlockData);

    public void executa(BlockChainValidaApp.PreviousBlockData previousBlockData){
        this.valida(previousBlockData);
        this.processaDadosProximoBloco(previousBlockData);
    }

    public List<Condicao> getCondicoes() {
        return condicoes;
    }

    private void setCondicoes(List<Condicao> condicoes) {
        this.condicoes = condicoes;
    }

    protected void addCondicao(Condicao condicao) {
        this.getCondicoes().add(condicao);
    }
}

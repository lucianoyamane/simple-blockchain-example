package br.com.lucianoyamane.example.valida.transaction.condicao;

import br.com.lucianoyamane.example.exception.BlockChainException;
import br.com.lucianoyamane.example.valida.BlockChainValidaApp;
import br.com.lucianoyamane.example.valida.Condicao;
import br.com.lucianoyamane.example.valida.transaction.TransactionValida;

public class AssinaturaNaoVerificadaCondicao extends Condicao<TransactionValida> {

    private AssinaturaNaoVerificadaCondicao(TransactionValida valida) {
        super(valida);
    }

    public static AssinaturaNaoVerificadaCondicao inicia(TransactionValida valida) {
        return new AssinaturaNaoVerificadaCondicao(valida);
    }

    @Override
    public void executa(BlockChainValidaApp.PreviousBlockData previousBlockData) {
        if (!this.getValida().getTransactionBlockChain().verifiySignature()) {
            throw new BlockChainException("Transaction Signature failed to verify");
        }
    }
}

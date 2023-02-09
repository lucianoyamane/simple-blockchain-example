package br.com.lucianoyamane.example.validate.transaction.condition;

import br.com.lucianoyamane.example.validate.BlockChainValidateApp;
import br.com.lucianoyamane.example.validate.Condicao;
import br.com.lucianoyamane.example.validate.transaction.TransactionValidate;

public class AssinaturaNaoVerificadaCondicao extends Condicao<TransactionValidate> {

    private AssinaturaNaoVerificadaCondicao(TransactionValidate valida) {
        super(valida);
    }

    @Override
    protected String getMessage() {
        return "Assinatura não verificada " + this.getValida().getTransactionBlockChain().getHash();
    }

    public static AssinaturaNaoVerificadaCondicao inicia(TransactionValidate valida) {
        return new AssinaturaNaoVerificadaCondicao(valida);
    }

    @Override
    protected Boolean rule(BlockChainValidateApp.PreviousBlockData previousBlockData) {
        return !this.getValida().getTransactionBlockChain().verifiySignature();
    }
}

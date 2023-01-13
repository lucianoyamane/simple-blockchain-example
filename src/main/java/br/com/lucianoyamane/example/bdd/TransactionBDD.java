package br.com.lucianoyamane.example.bdd;

import br.com.lucianoyamane.example.configurations.CarteirasRegistradas;
import br.com.lucianoyamane.example.configurations.SystemOutPrintlnDecorator;
import br.com.lucianoyamane.example.transaction.Transaction;
import br.com.lucianoyamane.example.wallet.Wallet;

public class TransactionBDD implements BDD {

    private BlockBDD blockBDD;

    private Wallet sender;

    private Wallet receiver;

    private Integer value;

    private TransactionBDD(BlockBDD blockBDD) {
        this.blockBDD = blockBDD;
    }

    public static TransactionBDD init(BlockBDD mainBDD) {
        return new TransactionBDD(mainBDD);
    }

    public TransactionBDD remetente(String sender) {
        this.sender = CarteirasRegistradas.abre().carteira(sender);
        return this;
    }

    public TransactionBDD destinatario(String receiver) {
        this.receiver = CarteirasRegistradas.abre().carteira(receiver);
        return this;
    }

    public TransactionBDD valor(Integer value) {
        this.value = value;
        return this;
    }

    @Override
    public BlockBDD fim() {
        return this.blockBDD;
    }

    public Transaction execute() {
        SystemOutPrintlnDecorator.verde("\nWallet's " + this.sender.toPublicData().getName() + " balance is: " + this.sender.getBalance());
        SystemOutPrintlnDecorator.azul("\nWallet " + this.sender.toPublicData().getName() + " is Attempting to send funds (" + value + ") to Wallet " + this.receiver.toPublicData().getName());
        return this.sender.sendFunds(this.receiver.toPublicData(), this.value);
    }
}

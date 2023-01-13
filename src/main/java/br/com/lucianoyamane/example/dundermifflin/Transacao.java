package br.com.lucianoyamane.example.dundermifflin;

import br.com.lucianoyamane.example.configurations.CarteirasRegistradas;
import br.com.lucianoyamane.example.entity.TransactionBlockChain;
import br.com.lucianoyamane.example.transaction.Transaction;

public class Transacao {

    private String remetente;
    private Integer valor;
    private String destinatario;

    private DunderMifflinCompanhiaDePapel dunderMifflinCompanhiaDePapel;

    private Transacao(DunderMifflinCompanhiaDePapel dunderMifflinCompanhiaDePapel) {
        this.dunderMifflinCompanhiaDePapel = dunderMifflinCompanhiaDePapel;
    }

    public static Transacao michaelScott(DunderMifflinCompanhiaDePapel dunderMifflinCompanhiaDePapel) {
        return new Transacao(dunderMifflinCompanhiaDePapel).remetente("Genesis");
    }

    public static Transacao cria(DunderMifflinCompanhiaDePapel dunderMifflinCompanhiaDePapel) {
        return new Transacao(dunderMifflinCompanhiaDePapel);
    }

    public Transacao remetente(String remetente) {
        this.remetente = remetente;
        return this;
    }

    public Transacao estaTransferindo(Integer valor) {
        this.valor = valor;
        return this;
    }

    public Transacao para(String destinatario) {
        this.destinatario = destinatario;
        return this;
    }

    public DunderMifflinCompanhiaDePapel confirmaAOperacao() {
        return this.dunderMifflinCompanhiaDePapel;
    }

    public TransactionBlockChain toTransaction() {
        return CarteirasRegistradas.abre().carteira(this.remetente).sendFunds(CarteirasRegistradas.abre().carteira(destinatario).toPublicData(), valor);
    }
}

package br.com.lucianoyamane.example.dundermifflin;

import br.com.lucianoyamane.example.blockchain.TransactionBlockChain;
import br.com.lucianoyamane.example.configurations.SystemOutPrintlnDecorator;

public class Transacao {

    private Vendedores remetente;
    private Integer valor;
    private Vendedores destinatario;

    private DunderMifflinCompanhiaDePapel dunderMifflinCompanhiaDePapel;

    private Transacao(DunderMifflinCompanhiaDePapel dunderMifflinCompanhiaDePapel) {
        this.dunderMifflinCompanhiaDePapel = dunderMifflinCompanhiaDePapel;
    }

    public static Transacao michaelScott(DunderMifflinCompanhiaDePapel dunderMifflinCompanhiaDePapel) {
        return new Transacao(dunderMifflinCompanhiaDePapel).remetente(Vendedores.MICHAEL_SCOTT);
    }

    public static Transacao cria(DunderMifflinCompanhiaDePapel dunderMifflinCompanhiaDePapel) {
        return new Transacao(dunderMifflinCompanhiaDePapel);
    }

    public Transacao remetente(Vendedores remetente) {
        this.remetente = remetente;
        return this;
    }

    public Transacao estaTransferindo(Integer valor) {
        this.valor = valor;
        return this;
    }

    public Transacao para(Vendedores destinatario) {
        this.destinatario = destinatario;
        return this;
    }

    public DunderMifflinCompanhiaDePapel confirmaAOperacao() {
        return this.dunderMifflinCompanhiaDePapel;
    }

    public TransactionBlockChain toTransaction() {
        SystemOutPrintlnDecorator.verde("\nVendedor " + this.remetente.nome() + " está transferindo (" + valor + ") para " + this.destinatario.nome());
        return VendedoresRegistrados.abre().wallet(this.remetente).sendFunds(VendedoresRegistrados.abre().wallet(this.destinatario).getPublicKeyDecorator(), valor);
    }
}

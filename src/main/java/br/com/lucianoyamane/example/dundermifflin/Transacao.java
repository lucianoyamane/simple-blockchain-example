package br.com.lucianoyamane.example.dundermifflin;

import br.com.lucianoyamane.example.blockchain.TransactionExecutor;
import br.com.lucianoyamane.example.configurations.SystemOutPrintlnDecorator;

public class Transacao {

    private Vendedores remetente;
    private Integer valor;
    private Vendedores destinatario;

    private DunderMifflinCompanhiaDePapelAdapter dunderMifflinCompanhiaDePapelAdapter;

    private Transacao(DunderMifflinCompanhiaDePapelAdapter dunderMifflinCompanhiaDePapelAdapter) {
        this.dunderMifflinCompanhiaDePapelAdapter = dunderMifflinCompanhiaDePapelAdapter;
    }

    public static Transacao michaelScott(DunderMifflinCompanhiaDePapelAdapter dunderMifflinCompanhiaDePapelAdapter) {
        return new Transacao(dunderMifflinCompanhiaDePapelAdapter).remetente(Vendedores.MICHAEL_SCOTT);
    }

    public static Transacao cria(DunderMifflinCompanhiaDePapelAdapter dunderMifflinCompanhiaDePapelAdapter) {
        return new Transacao(dunderMifflinCompanhiaDePapelAdapter);
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

    public DunderMifflinCompanhiaDePapelAdapter confirmaAOperacao() {
        return this.dunderMifflinCompanhiaDePapelAdapter;
    }

    public TransactionExecutor toTransaction() {
        SystemOutPrintlnDecorator.verde("\nVendedor " + this.remetente.nome() + " está transferindo (" + valor + ") para " + this.destinatario.nome());
        return VendedoresRegistrados.abre().wallet(this.remetente).sendFunds(VendedoresRegistrados.abre().wallet(this.destinatario).getPublicKeyDecorator(), valor);
    }
}

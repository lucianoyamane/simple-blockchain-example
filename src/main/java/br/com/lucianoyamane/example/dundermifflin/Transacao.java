package br.com.lucianoyamane.example.dundermifflin;

import br.com.lucianoyamane.example.blockchain.TransactionExecutor;
import br.com.lucianoyamane.example.configurations.SystemOutPrintlnDecorator;

public class Transacao {

    private Vendedor remetente;
    private Integer valor;
    private Vendedor destinatario;

    private DunderMifflinCompanhiaDePapelAdapter dunderMifflinCompanhiaDePapelAdapter;

    private Transacao(DunderMifflinCompanhiaDePapelAdapter dunderMifflinCompanhiaDePapelAdapter) {
        this.dunderMifflinCompanhiaDePapelAdapter = dunderMifflinCompanhiaDePapelAdapter;
    }

    public static Transacao michaelScott(DunderMifflinCompanhiaDePapelAdapter dunderMifflinCompanhiaDePapelAdapter) {
        return new Transacao(dunderMifflinCompanhiaDePapelAdapter).remetente(Vendedor.MICHAEL_SCOTT);
    }

    public static Transacao cria(DunderMifflinCompanhiaDePapelAdapter dunderMifflinCompanhiaDePapelAdapter) {
        return new Transacao(dunderMifflinCompanhiaDePapelAdapter);
    }

    public Transacao remetente(Vendedor remetente) {
        this.remetente = remetente;
        return this;
    }

    public Transacao estaTransferindo(Integer valor) {
        this.valor = valor;
        return this;
    }

    public Transacao para(Vendedor destinatario) {
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

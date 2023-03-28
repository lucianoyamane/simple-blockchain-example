package br.com.lucianoyamane.example.dundermifflin;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import br.com.lucianoyamane.example.configurations.SystemOutPrintlnDecorator;
import br.com.lucianoyamane.example.wallet.GenesisWallet;
import br.com.lucianoyamane.example.wallet.Wallet;

public class VendedoresRegistrados {

    private static VendedoresRegistrados instance;

    public static VendedoresRegistrados abre() {
        if (instance == null) {
            instance = new VendedoresRegistrados();
        }
        return instance;
    }

    List<Carteira> vendedores;

    private VendedoresRegistrados() {
        this.vendedores = new ArrayList<>();
        this.vendedores.add(Carteira.cria(Vendedores.MICHAEL_SCOTT, GenesisWallet.create()));
        this.vendedores.add(Carteira.cria(Vendedores.DWIGHT_SCHRUTE, Wallet.anonymous()));
        this.vendedores.add(Carteira.cria(Vendedores.JIM_HALPERT, Wallet.anonymous()));
        this.vendedores.add(Carteira.cria(Vendedores.PAM_BEESLY, Wallet.anonymous()));
    }


    public Wallet wallet(Vendedores vendedor) {
        Optional<Carteira> carteiraOptional =  this.vendedores.stream().filter(carteira -> carteira.getVendedor().nome().equals(vendedor.nome())).findFirst();
        if (carteiraOptional.isEmpty()) {
            return null;
        }
        return carteiraOptional.get().getWallet();
    }

    public void saldos() {
        for(Carteira vendedor : this.vendedores) {
            SystemOutPrintlnDecorator.verde("\nVendedor " + vendedor.nome() + " possui um saldo de " + vendedor.saldo());
        }
    }
    
    
}

package br.com.lucianoyamane.example.configurations;

import br.com.lucianoyamane.example.wallet.GenesisWallet;
import br.com.lucianoyamane.example.wallet.Wallet;

import java.util.HashMap;
import java.util.Map;

public class CarteirasRegistradas {

    public static final String LUCIANO = "Luciano";
    public static final String AUGUSTO = "Augusto";

    private static CarteirasRegistradas instance;

    public static CarteirasRegistradas abre() {
        if (instance == null) {
            instance = new CarteirasRegistradas();
        }
        return instance;
    }
    private Map<String, Wallet> registeredMap;

    public CarteirasRegistradas() {
        this.registeredMap = new HashMap<>();
        this.registra(GenesisWallet.novo());
        this.registra(Wallet.novo(LUCIANO));
        this.registra(Wallet.novo(AUGUSTO));
    }



    public CarteirasRegistradas registra(Wallet wallet) {
        this.registeredMap.put(wallet.getName(), wallet);
        return this;
    }

    public Wallet carteira(String name) {
        return this.registeredMap.get(name);
    }

    public void getFinalBalances() {
        for(Map.Entry<String, Wallet> entry : this.registeredMap.entrySet()) {
            SystemOutPrintlnDecorator.verde("\nWallet's " + entry.getValue().getName() + " balance is: " + entry.getValue().getBalance());
        }
    }
}

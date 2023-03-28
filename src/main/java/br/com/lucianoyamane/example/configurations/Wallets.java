package br.com.lucianoyamane.example.configurations;

import br.com.lucianoyamane.example.wallet.GenesisWallet;
import br.com.lucianoyamane.example.wallet.Wallet;

import java.util.LinkedHashMap;
import java.util.Map;

public class Wallets {

    public static final String SICRANO = "SICRANO";
    public static final String BELTRANO = "BELTRANO";

    private static Wallets instance;

    public static Wallets open() {
        if (instance == null) {
            instance = new Wallets();
        }
        return instance;
    }
    private Map<String, Wallet> registeredMap;

    public Wallets() {
        this.registeredMap = new LinkedHashMap<>();
        this.register(GenesisWallet.create());
        this.register(Wallet.create(SICRANO));
        this.register(Wallet.create(BELTRANO));
    }



    public Wallets register(Wallet wallet) {
        this.registeredMap.put(wallet.getName(), wallet);
        return this;
    }

    public Wallet wallet(String name) {
        return this.registeredMap.get(name);
    }

    public void getFinalBalances() {
        for(Map.Entry<String, Wallet> entry : this.registeredMap.entrySet()) {
            SystemOutPrintlnDecorator.verde("\nWallet's " + entry.getValue().getName() + " balance is: " + entry.getValue().getBalance());
        }
    }
}

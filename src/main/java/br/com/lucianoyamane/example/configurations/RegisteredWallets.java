package br.com.lucianoyamane.example.configurations;

import br.com.lucianoyamane.example.wallet.Wallet;

import java.util.HashMap;
import java.util.Map;

public class RegisteredWallets {

    private static RegisteredWallets instance;

    public static RegisteredWallets getInstance() {
        if (instance == null) {
            instance = new RegisteredWallets();
        }
        return instance;
    }
    private Map<String, Wallet> registeredMap;

    public RegisteredWallets() {
        this.registeredMap = new HashMap<>();
    }

    public RegisteredWallets register(Wallet wallet) {
        this.registeredMap.put(wallet.getName(), wallet);
        return this;
    }

    public Wallet getWallet(String name) {
        return this.registeredMap.get(name);
    }

    public void getFinalBalances() {
        for(Map.Entry<String, Wallet> entry : this.registeredMap.entrySet()) {
            System.out.println("\nWallet's " + entry.getValue().getName() + " balance is: " + entry.getValue().getBalance());
        }
    }
}

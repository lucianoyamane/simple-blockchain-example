package br.com.lucianoyamane.example.dundermifflin;

import br.com.lucianoyamane.example.wallet.Wallet;

public class Carteira{

    private Carteira(Vendedor nome, Wallet wallet) {
        this.vendedor = nome;
        this.wallet = wallet;
    }

    public static Carteira cria(Vendedor nome, Wallet wallet) {
        return new Carteira(nome, wallet);
    }

    private Vendedor vendedor;
    private Wallet wallet;

    public Vendedor getVendedor() {
        return this.vendedor;
    }

    public String nome() {
        return this.getVendedor().nome();
    }

    public void setVendedor(Vendedor nome) {
        this.vendedor = nome;
    }

    public Wallet getWallet() {
        return this.wallet;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }

    public Integer saldo() {
        return this.getWallet().getBalance();
    }
    
}
package br.com.lucianoyamane.example.dundermifflin;

import br.com.lucianoyamane.example.wallet.Wallet;

public class Carteira{

    private Carteira(Vendedores nome, Wallet wallet) {
        this.vendedor = nome;
        this.wallet = wallet;
    }

    public static Carteira cria(Vendedores nome, Wallet wallet) {
        return new Carteira(nome, wallet);
    }

    private Vendedores vendedor;
    private Wallet wallet;

    public Vendedores getVendedor() {
        return this.vendedor;
    }

    public String nome() {
        return this.getVendedor().nome();
    }

    public void setVendedor(Vendedores nome) {
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
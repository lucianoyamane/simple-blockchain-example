package br.com.lucianoyamane.example.dundermifflin;

import br.com.lucianoyamane.example.configurations.Wallets;

public class ExecuteBlockChain {

    public static void main(String[] args) {


        DunderMifflinCompanhiaDePapel dunderMifflinCompanhiaDePapel = DunderMifflinCompanhiaDePapel
                .iniciaOsTrabalhos()
                    .michaelScott()
                        .estaTransferindo(100)
                        .para(Wallets.SICRANO)
                    .confirmaAOperacao();

        dunderMifflinCompanhiaDePapel
                    .cliente(Wallets.SICRANO)
                        .estaTransferindo(40)
                        .para(Wallets.BELTRANO)
                    .confirmaAOperacao();

        dunderMifflinCompanhiaDePapel
                    .cliente(Wallets.SICRANO)
                        .estaTransferindo(1000)
                        .para(Wallets.BELTRANO)
                    .confirmaAOperacao();

        dunderMifflinCompanhiaDePapel
                    .cliente(Wallets.BELTRANO)
                        .estaTransferindo(20)
                        .para(Wallets.SICRANO)
                    .confirmaAOperacao();

        dunderMifflinCompanhiaDePapel
                    .cliente(Wallets.SICRANO)
                        .estaTransferindo(10)
                        .para(Wallets.BELTRANO)
                    .confirmaAOperacao();

        dunderMifflinCompanhiaDePapel
                    .cliente(Wallets.SICRANO)
                        .estaTransferindo(70)
                        .para(Wallets.BELTRANO)
                    .confirmaAOperacao();
        dunderMifflinCompanhiaDePapel.processa();


    }
}

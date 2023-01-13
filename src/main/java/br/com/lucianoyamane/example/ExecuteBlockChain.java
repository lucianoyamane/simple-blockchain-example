package br.com.lucianoyamane.example;

import br.com.lucianoyamane.example.bdd.DunderMifflinCompanhiaDePapel;
import br.com.lucianoyamane.example.configurations.CarteirasRegistradas;

public class ExecuteBlockChain {

    public static void main(String[] args) {


        DunderMifflinCompanhiaDePapel
                .iniciaOsTrabalhos()
                    .michaelScott()
                        .estaTransferindo(100)
                        .para(CarteirasRegistradas.LUCIANO)
                    .confirmaAOperacao()
                    .cliente(CarteirasRegistradas.LUCIANO)
                        .estaTransferindo(40)
                        .para(CarteirasRegistradas.AUGUSTO)
                    .confirmaAOperacao()
                    .cliente(CarteirasRegistradas.LUCIANO)
                        .estaTransferindo(1000)
                        .para(CarteirasRegistradas.AUGUSTO)
                    .confirmaAOperacao()
                    .cliente(CarteirasRegistradas.AUGUSTO)
                        .estaTransferindo(20)
                        .para(CarteirasRegistradas.LUCIANO)
                    .confirmaAOperacao()
                    .cliente(CarteirasRegistradas.LUCIANO)
                        .estaTransferindo(10)
                        .para(CarteirasRegistradas.AUGUSTO)
                    .confirmaAOperacao()
                    .cliente(CarteirasRegistradas.LUCIANO)
                        .estaTransferindo(70)
                        .para(CarteirasRegistradas.AUGUSTO)
                    .confirmaAOperacao()
                .processa();


    }
}

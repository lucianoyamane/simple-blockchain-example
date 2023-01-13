package br.com.lucianoyamane.example.dundermifflin;

import br.com.lucianoyamane.example.dundermifflin.DunderMifflinCompanhiaDePapel;
import br.com.lucianoyamane.example.configurations.CarteirasRegistradas;

public class ExecuteBlockChain {

    public static void main(String[] args) {


        DunderMifflinCompanhiaDePapel dunderMifflinCompanhiaDePapel = DunderMifflinCompanhiaDePapel
                .iniciaOsTrabalhos()
                    .michaelScott()
                        .estaTransferindo(100)
                        .para(CarteirasRegistradas.LUCIANO)
                    .confirmaAOperacao();
//        dunderMifflinCompanhiaDePapel.processa();

        dunderMifflinCompanhiaDePapel
                    .cliente(CarteirasRegistradas.LUCIANO)
                        .estaTransferindo(40)
                        .para(CarteirasRegistradas.AUGUSTO)
                    .confirmaAOperacao();
        dunderMifflinCompanhiaDePapel.processa();

//        dunderMifflinCompanhiaDePapel
//                    .cliente(CarteirasRegistradas.LUCIANO)
//                        .estaTransferindo(1000)
//                        .para(CarteirasRegistradas.AUGUSTO)
//                    .confirmaAOperacao();
//        dunderMifflinCompanhiaDePapel.processa();

//        dunderMifflinCompanhiaDePapel
//                    .cliente(CarteirasRegistradas.AUGUSTO)
//                        .estaTransferindo(20)
//                        .para(CarteirasRegistradas.LUCIANO)
//                    .confirmaAOperacao();
//        dunderMifflinCompanhiaDePapel.processa();

//        dunderMifflinCompanhiaDePapel
//                    .cliente(CarteirasRegistradas.LUCIANO)
//                        .estaTransferindo(10)
//                        .para(CarteirasRegistradas.AUGUSTO)
//                    .confirmaAOperacao();
//        dunderMifflinCompanhiaDePapel.processa();

//        dunderMifflinCompanhiaDePapel
//                    .cliente(CarteirasRegistradas.LUCIANO)
//                        .estaTransferindo(70)
//                        .para(CarteirasRegistradas.AUGUSTO)
//                    .confirmaAOperacao();
//        dunderMifflinCompanhiaDePapel.processa();


    }
}

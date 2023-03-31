package br.com.lucianoyamane.example.dundermifflin;

public class ExecuteBlockChain {

    public static void main(String[] args) {


        DunderMifflinCompanhiaDePapel dunderMifflinCompanhiaDePapel = DunderMifflinCompanhiaDePapel
                .iniciaOsTrabalhos()
                    .michaelScott()
                        .estaTransferindo(100)
                        .para(Vendedores.DWIGHT_SCHRUTE)
                    .confirmaAOperacao();

        dunderMifflinCompanhiaDePapel
                    .cliente(Vendedores.DWIGHT_SCHRUTE)
                        .estaTransferindo(40)
                        .para(Vendedores.JIM_HALPERT)
                    .confirmaAOperacao();

        dunderMifflinCompanhiaDePapel
                    .cliente(Vendedores.DWIGHT_SCHRUTE)
                        .estaTransferindo(1000)
                        .para(Vendedores.JIM_HALPERT)
                    .confirmaAOperacao();

        dunderMifflinCompanhiaDePapel
                    .cliente(Vendedores.JIM_HALPERT)
                        .estaTransferindo(20)
                        .para(Vendedores.DWIGHT_SCHRUTE)
                    .confirmaAOperacao();

        dunderMifflinCompanhiaDePapel
                    .cliente(Vendedores.DWIGHT_SCHRUTE)
                        .estaTransferindo(10)
                        .para(Vendedores.JIM_HALPERT)
                    .confirmaAOperacao();

        dunderMifflinCompanhiaDePapel
                    .cliente(Vendedores.DWIGHT_SCHRUTE)
                        .estaTransferindo(70)
                        .para(Vendedores.JIM_HALPERT)
                    .confirmaAOperacao();
        dunderMifflinCompanhiaDePapel.processa();


    }
}

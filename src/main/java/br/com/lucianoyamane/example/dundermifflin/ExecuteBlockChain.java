package br.com.lucianoyamane.example.dundermifflin;

public class ExecuteBlockChain {

    public static void main(String[] args) {

        DunderMifflinCompanhiaDePapelAdapter dunderMifflinCompanhiaDePapel = DunderMifflinCompanhiaDePapelAdapter.iniciaOsTrabalhos();


        dunderMifflinCompanhiaDePapel
                    .michaelScott()
                        .estaTransferindo(100)
                        .para(Vendedor.DWIGHT_SCHRUTE)
                    .confirmaAOperacao();

        dunderMifflinCompanhiaDePapel
                    .emissor(Vendedor.DWIGHT_SCHRUTE)
                        .estaTransferindo(40)
                        .para(Vendedor.JIM_HALPERT)
                    .confirmaAOperacao();

        dunderMifflinCompanhiaDePapel
                    .emissor(Vendedor.DWIGHT_SCHRUTE)
                        .estaTransferindo(1000)
                        .para(Vendedor.JIM_HALPERT)
                    .confirmaAOperacao();

        dunderMifflinCompanhiaDePapel
                    .emissor(Vendedor.JIM_HALPERT)
                        .estaTransferindo(20)
                        .para(Vendedor.DWIGHT_SCHRUTE)
                    .confirmaAOperacao();

        dunderMifflinCompanhiaDePapel
                    .emissor(Vendedor.DWIGHT_SCHRUTE)
                        .estaTransferindo(10)
                        .para(Vendedor.JIM_HALPERT)
                    .confirmaAOperacao();

        dunderMifflinCompanhiaDePapel
                    .emissor(Vendedor.DWIGHT_SCHRUTE)
                        .estaTransferindo(70)
                        .para(Vendedor.JIM_HALPERT)
                    .confirmaAOperacao();
        dunderMifflinCompanhiaDePapel.processa();


    }
}

package br.com.lucianoyamane.example.transaction;

public class GenesisTransaction extends Transaction{

    private GenesisTransaction(Operator senderOperator, Operator receiverOperator, Integer value) {
        super(senderOperator, receiverOperator, value);
        this.setHash("0");
    }

    public static Transaction create(Operator senderOperator, Operator receiverOperator, Integer value) {
        return new GenesisTransaction(senderOperator, receiverOperator, value);
    }
}

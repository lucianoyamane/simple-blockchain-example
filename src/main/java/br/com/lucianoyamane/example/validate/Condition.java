package br.com.lucianoyamane.example.validate;

public abstract class Condition<T extends Validate> {

    private T valida;

    protected Condition(T validate) {
        this.setValidate(validate);
    }

    protected T getValidate() {
        return valida;
    }

    private void setValidate(T validate) {
        this.valida = validate;
    }

    public abstract String getMessage();

    protected abstract Boolean rule(BlockChainValidateApp.PreviousBlockData previousBlockData);

    public Boolean check(BlockChainValidateApp.PreviousBlockData previousBlockData){
        return this.rule(previousBlockData);
    }

}

package br.com.lucianoyamane.example.validate;

import java.util.ArrayList;
import java.util.List;

public abstract class Validate {

    protected Validate() {
        this.setConditions(new ArrayList<>());
        this.setConditions();
    }

    abstract protected void setConditions();

    private List<Condition> conditions;

    public void validate(BlockChainValidateApp.PreviousBlockData previousBlockData) {
        List<Condition> conditions = this.getConditions();
        for(Condition condition : conditions) {
            condition.execute(previousBlockData);
        }
    };

    abstract public void processNextBlockData(BlockChainValidateApp.PreviousBlockData previousBlockData);

    public void execute(BlockChainValidateApp.PreviousBlockData previousBlockData){
        this.validate(previousBlockData);
        this.processNextBlockData(previousBlockData);
    }

    public List<Condition> getConditions() {
        return conditions;
    }

    private void setConditions(List<Condition> conditions) {
        this.conditions = conditions;
    }

    protected void addCondition(Condition condition) {
        this.getConditions().add(condition);
    }
}

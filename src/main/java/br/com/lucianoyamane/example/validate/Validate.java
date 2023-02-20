package br.com.lucianoyamane.example.validate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Validate {

    protected Validate() {
        this.initConditions();
        this.initConditionsErrors();
        this.initValidates();
        this.configConditions();
    }

    abstract protected void configConditions();

    private List<Condition> conditions;

    private List<Map<String, String>> conditionsErrors;

    private List<Validate> validates;

    private void validate(BlockChainValidateApp.PreviousBlockData previousBlockData) {
        List<Condition> conditions = this.getConditions();
        for(Condition condition : conditions) {
            if (condition.check(previousBlockData)) {
                Map<String, String> errorMap = new HashMap<>();
                errorMap.put("LEVEL", this.getLevel());
                errorMap.put("MESSAGE", condition.getMessage());
                this.addConditionError(errorMap);
            }
        }
    }

    private void executeSubValidates(BlockChainValidateApp.PreviousBlockData previousBlockData) {
        for(Validate validate : this.getValidates()) {
            validate.execute(previousBlockData);
        }
    }

    protected abstract void processNextBlockData(BlockChainValidateApp.PreviousBlockData previousBlockData);

    abstract protected String getLevel();

    public Validate execute(BlockChainValidateApp.PreviousBlockData previousBlockData){
        this.validate(previousBlockData);
        this.executeSubValidates(previousBlockData);
        this.processNextBlockData(previousBlockData);
        return this;
    }

    public List<Condition> getConditions() {
        return conditions;
    }

    private void initConditions() {
        this.conditions = new ArrayList<>();
    }

    protected void addCondition(Condition condition) {
        this.getConditions().add(condition);
    }

    public List<Map<String, String>> getConditionsErrors() {
        return conditionsErrors;
    }

    public Boolean hasErrors() {
        return !this.getConditionsErrors().isEmpty();
    }

    private void addConditionError(Map<String, String> error) {
        this.getConditionsErrors().add(error);
    }

    private void initConditionsErrors() {
        this.conditionsErrors = new ArrayList<>();
    }

    protected List<Validate> getValidates() {
        return validates;
    }

    protected void addValidate(Validate validate) {
        this.getValidates().add(validate);
    }

    private void initValidates() {
        this.validates = new ArrayList<>();
    }

    public List<Map<String, String>> getErrorsMessages() {
        List<Map<String, String>> errorsMessages = new ArrayList<>();
        for (Validate validate : this.getValidates()) {
            errorsMessages.addAll(validate.getErrorsMessages());
        }
        if (this.hasErrors()) {
            errorsMessages.addAll(this.getConditionsErrors());
        }
        return errorsMessages;
    }
}

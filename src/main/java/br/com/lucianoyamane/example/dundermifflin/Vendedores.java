package br.com.lucianoyamane.example.dundermifflin;

public enum Vendedores {

    MICHAEL_SCOTT("Genesis"),
    DWIGHT_SCHRUTE("Dwight Schrute"),
    JIM_HALPERT("Jim Halpert"),
    PAM_BEESLY("Pam Beesly");


    private String _nome;

    private Vendedores(String nome){
        this._nome = nome;
    }

    public String nome() {
        return this._nome;
    }
    
}

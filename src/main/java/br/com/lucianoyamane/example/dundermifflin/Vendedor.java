package br.com.lucianoyamane.example.dundermifflin;

public enum Vendedor {

    MICHAEL_SCOTT("Michael Scott"),
    DWIGHT_SCHRUTE("Dwight Schrute"),
    JIM_HALPERT("Jim Halpert");


    private String _nome;

    private Vendedor(String nome){
        this._nome = nome;
    }

    public String nome() {
        return this._nome;
    }
    
}

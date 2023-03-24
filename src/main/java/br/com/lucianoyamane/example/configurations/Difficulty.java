package br.com.lucianoyamane.example.configurations;

public class Difficulty {

    private static Difficulty instance;

    private Difficulty(Integer difficulty) {
        this.difficulty = difficulty;
    }

    public static Difficulty getInstance() {
        if (instance == null) {
            instance = new Difficulty(5);
        }
        return instance;
    }

    private Integer difficulty;

    public Integer getDifficulty() {
        return difficulty;
    }
}

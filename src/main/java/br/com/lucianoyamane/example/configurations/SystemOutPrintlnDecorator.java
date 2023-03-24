package br.com.lucianoyamane.example.configurations;

public class SystemOutPrintlnDecorator {

    public static final String ANSI_BLACK_BACKGROUND = "\u001B[40m";
    public static final String ANSI_RED_BACKGROUND = "\u001B[41m";
    public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
    public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
    public static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
    public static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
    public static final String ANSI_CYAN_BACKGROUND = "\u001B[46m";
    public static final String ANSI_WHITE_BACKGROUND = "\u001B[47m";

    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_BLACK = "\u001B[30m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_PURPLE = "\u001B[35m";
    private static final String ANSI_CYAN = "\u001B[36m";
    private static final String ANSI_WHITE = "\u001B[37m";

    public static void vermelho(String message) {
        printFormated(ANSI_RED, message);
    }

    public static void verde(String message) {
        printFormated(ANSI_GREEN, message);
    }

    public static void amarelo(String message) {
        printFormated(ANSI_YELLOW, message);
    }

    public static void azul(String message) {
        printFormated(ANSI_BLUE, message);
    }

    public static void ciano(String message) {
        printFormated(ANSI_CYAN, message);
    }

    public static void roxo(String message) {
        printFormated(ANSI_PURPLE, message);
    }



    private static void printFormated(String color, String message) {
        StringBuilder messageFormated = new StringBuilder().append(color).append(message).append(ANSI_RESET);
        System.out.println(messageFormated);
    }

}


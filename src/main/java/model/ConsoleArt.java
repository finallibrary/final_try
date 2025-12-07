package model;

import java.util.Scanner;

public class ConsoleArt {
    // ANSI Colors
    public static final String RESET = "\u001B[0m";
    public static final String BOLD = "\u001B[1m";
    public static final String CYAN = "\u001B[36m";
    public static final String PURPLE = "\u001B[35m";
    public static final String GREEN = "\u001B[32m";
    public static final String RED = "\u001B[31m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String WHITE_BG = "\u001B[47m";
    public static final String BLACK_TXT = "\u001B[30m";

    public static void clear() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static void title(String text) {
        String line = "═".repeat(60);
        System.out.println(CYAN + BOLD + line + RESET);
        System.out.println(BLUE + WHITE_BG + BLACK_TXT + BOLD + 
            "       " + text.toUpperCase() + "       " + RESET);
        System.out.println(CYAN + BOLD + line + RESET);
        System.out.println();
    }

    public static void box(String content) {
        String[] lines = content.split("\n");
        int width = 62;
        String top = PURPLE + "╔" + "═".repeat(width) + "╗" + RESET;
        String bottom = PURPLE + "╚" + "═".repeat(width) + "╝" + RESET;
        String side = PURPLE + "║" + RESET;

        System.out.println(top);
        for (String line : lines) {
            System.out.printf("%s %s%-60s%s %s%n", side, YELLOW, line, RESET, side);
        }
        System.out.println(bottom);
        System.out.println();
    }

    public static void success(String msg) {
        System.out.println(GREEN + BOLD + "SUCCESS: " + msg + RESET);
    }

    public static void error(String msg) {
        System.out.println(RED + BOLD + "ERROR: " + msg + RESET);
    }

    public static void info(String msg) {
        System.out.println(CYAN + "INFO: " + msg + RESET);
    }

    public static void waitEnter() {
        System.out.println(YELLOW + "\nPress Enter to continue..." + RESET);
        new Scanner(System.in).nextLine();
    }
}
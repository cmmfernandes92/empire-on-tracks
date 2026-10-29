package pt.ipp.isep.dei.ui.console.utils;

import java.util.Scanner;

public class Console {
    private static final Scanner sc = new Scanner(System.in);
    public static String readLine(String prompt) {
        System.out.print(prompt);
        return sc.nextLine();
    }
    public static void println(String msg) {
        System.out.println(msg);
    }
}

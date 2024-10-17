package presentation.util;

import lib.logy.Logy;
import lib.logy.LogyLevel;

import java.util.Scanner;

public final class InputHelper {
    private static final Scanner sc = new Scanner(System.in);

    private InputHelper() {}

    public static String getStringInput(String prompt) {
        Logy.log(LogyLevel.INFO, prompt);
        return sc.nextLine();
    }

    public static int getIntInput(String prompt) {
        Logy.log(LogyLevel.INFO, prompt);
        return Integer.parseInt(sc.nextLine());
    }

}

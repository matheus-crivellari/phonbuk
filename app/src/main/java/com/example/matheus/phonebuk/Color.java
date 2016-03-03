package com.example.matheus.phonebuk;

/**
 * Translates hexadecimal color INTEGERS to color's names.
 */
public class Color {
    public static int APP     = R.color.app_bg;
    public static int BLUE    = R.color.blue;
    public static int YELLOW  = R.color.yellow;
    public static int RED     = R.color.red;
    public static int GREEN   = R.color.green;
    public static int PURPLE  = R.color.purple;
    public static int ORANGE  = R.color.orange;
    public static int CYAN    = R.color.cyan;
    public static int MAGENTA = R.color.magenta;
    public static int GREEN2  = R.color.green2;
    public static int GREY    = R.color.grey;
    public static int WHITE   = R.color.white;
    public static int BLACK   = R.color.black;

    public static int getColor(int i){
        switch(i){
            case 0:
            return Color.BLUE;
            case 1:
            return Color.YELLOW;
            case 2:
            return Color.RED;
            case 3:
            return Color.GREEN;
            case 4:
            return Color.PURPLE;
            case 5:
            return Color.ORANGE;
            case 6:
            return Color.CYAN;
            case 7:
            return Color.MAGENTA;
            case 8:
            return Color.GREEN2;
            case 9:
            return Color.GREY;
            case 10:
            return Color.WHITE;
            case 11:
            return Color.BLACK;
            default:
            return Color.APP;
        }
    }
}


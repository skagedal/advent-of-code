package tech.skagedal.javaaoc.tools.format;

public class Term {
    public static final String FG_BLACK = "\u001b[30m";
    public static final String FG_RED = "\u001b[31m";
    public static final String FG_GREEN = "\u001b[32m";
    public static final String FG_YELLOW = "\u001b[33m";
    public static final String FG_BLUE = "\u001b[34m";
    public static final String FG_MAGENTA = "\u001b[35m";
    public static final String FG_CYAN = "\u001b[36m";
    public static final String FG_WHITE = "\u001b[37m";

    public static final String FG_BRIGHT_BLACK = "\u001b[30;1m";
    public static final String FG_BRIGHT_RED = "\u001b[31;1m";
    public static final String FG_BRIGHT_GREEN = "\u001b[32;1m";
    public static final String FG_BRIGHT_YELLOW = "\u001b[33;1m";
    public static final String FG_BRIGHT_BLUE = "\u001b[34;1m";
    public static final String FG_BRIGHT_MAGENTA = "\u001b[35;1m";
    public static final String FG_BRIGHT_CYAN = "\u001b[36;1m";
    public static final String FG_BRIGHT_WHITE = "\u001b[37;1m";

    public static final String BG_BLACK = "\u001b[40m";
    public static final String BG_RED = "\u001b[41m";
    public static final String BG_GREEN = "\u001b[42m";
    public static final String BG_YELLOW = "\u001b[43m";
    public static final String BG_BLUE = "\u001b[44m";
    public static final String BG_MAGENTA = "\u001b[45m";
    public static final String BG_CYAN = "\u001b[46m";
    public static final String BG_WHITE = "\u001b[47m";

    public static final String BG_BRIGHT_BLACK = "\u001b[40;1m";
    public static final String BG_BRIGHT_RED = "\u001b[41;1m";
    public static final String BG_BRIGHT_GREEN = "\u001b[42;1m";
    public static final String BG_BRIGHT_YELLOW = "\u001b[43;1m";
    public static final String BG_BRIGHT_BLUE = "\u001b[44;1m";
    public static final String BG_BRIGHT_MAGENTA = "\u001b[45;1m";
    public static final String BG_BRIGHT_CYAN = "\u001b[46;1m";
    public static final String BG_BRIGHT_WHITE = "\u001b[47;1m";

    public static final String RESET = "\u001b[0m";


    public enum Color {
        BLACK,
        RED,
        GREEN,
        YELLOW,
        BLUE,
        MAGENTA,
        CYAN,
        WHITE,
        BRIGHT_BLACK,
        BRIGHT_RED,
        BRIGHT_GREEN,
        BRIGHT_YELLOW,
        BRIGHT_BLUE,
        BRIGHT_MAGENTA,
        BRIGHT_CYAN,
        BRIGHT_WHITE
    }
}

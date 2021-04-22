package langdefsubalgo.unpack;

import java.util.regex.Pattern;

public abstract class Regex {
    public static final RegexFunFactory[] regexes = {
        RegexFunFactory.builder().
                p(Pattern.compile("^(.+)\\(\\)$")).
                g(1).
                s(null).
                build(),// empty
//        new Unpack(Pattern.compile("^(.+)\\(([0-9]+)\\)$"), null, 1, 2),
//        new Unpack(Pattern.compile("^(.+)\\((([0-9]+[,])+[0-9]+)\\)$"), null, 1, 2),//2 has the comma-separated list
//        new Unpack(Pattern.compile("^(.+)\\([:]([0-9]+)\\)$"), null, 1, 2),
//        new Unpack(Pattern.compile("^(.+)\\(([0-9]+)[:]\\)$"), null, 1, 2),
//        new Unpack(Pattern.compile("^(.+)\\(([0-9]+)[:]([0-9]+)\\)$"), null, 1, 2, 3),//returns the numbers
//        new Unpack(Pattern.compile("^(.+)\\([$]([A-Za-z][A-Za-z0-9_]*)\\)$"), null, 1, 2),// returns constant with $ removed
//        new Unpack(Pattern.compile("^TRUE|true$"), null, 0),
//        new Unpack(Pattern.compile("^FALSE|false$"), null, 0),
//        new Unpack(Pattern.compile("^[0-9]+$"), null, 0),
//        new Unpack(Pattern.compile("^([A-Z][A-Z0-9_]+)\\[([A-Z][A-Z0-9_]*)\\]$"), null, 1, 2),// returns category, item
//        new Unpack(Pattern.compile("^[A-Z][A-Z0-9_]+$"), null, 0),
//        new Unpack(Pattern.compile("^(.+)\\(((TRUE)|(FALSE))\\)$"), null, 1, 2),
//        new Unpack(Pattern.compile("^(.+)\\(([A-Za-z0-9_\\.]+)\\)$"), null, 1, 2),
//        new Unpack(Pattern.compile("^(.+)\\([']([A-Za-z0-9_]+)[']\\)$"), null, 1, 2),
//        new Unpack(Pattern.compile("^(.+)\\((([A-Za-z0-9]+[,])+[A-Za-z0-9]+)\\)$"), null, 1, 2),//2 has the comma-separated list
//        new Unpack(Pattern.compile("^(.+)\\(['](([A-Za-z0-9]+[,])+[A-Za-z0-9]+)[']\\)$"), null, 1, 2),//2 has the comma-separated list
//        new Unpack(Pattern.compile("."), null, 0),
//        // FX
//        new Unpack(Pattern.compile("^(.+)\\(([A-Za-z][A-Za-z0-9_]*)\\[([A-Za-z0-9_]+)\\]\\)$"), null, 1, 2, 3),//fun name, category, item
//        new Unpack(Pattern.compile("^(.+)\\(([A-Z0-9_]+)\\)$"), null, 1, 2),//fun name, category
//        new Unpack(Pattern.compile("^.+\\(([<][>])\\)$"), null),
//        new Unpack(Pattern.compile("^.+\\(([<][<])\\)$"), null),
//        new Unpack(Pattern.compile("^.+\\(([>][>])\\)$"), null),
//        new Unpack(Pattern.compile("^.+\\((([A-Za-z0-9_]+\\[[A-Za-z0-9_]+\\][,]\\s*)+[A-Za-z0-9_]+\\[[A-Za-z0-9_]+\\])\\)$"), null),
//
//        // FX ACCESS
//        new Unpack(Pattern.compile("^[<][>]$"), null),
//        new Unpack(Pattern.compile("^[<][<]$"), null),
//        new Unpack(Pattern.compile("^[>][>]$"), null),
//
//        new Unpack(Pattern.compile("^[0-9]+$"), null, 1),
//        new Unpack(Pattern.compile("^[-][0-9]+$"), null, 1),
//        new Unpack(Pattern.compile("^[<][<]([0-9]+)$"), null, 1),
//        new Unpack(Pattern.compile("^[>][>]([0-9]+)$"), null, 1),
//        new Unpack(Pattern.compile("^(([0-9]+[,])+[0-9]+)$"), null, 1),
//
//        new Unpack(Pattern.compile("^[:]([0-9]+)$"), null, 1),
//        new Unpack(Pattern.compile("^([0-9]+)[:]$"), null, 1),
//        new Unpack(Pattern.compile("^([0-9]+)[:]([0-9]+)$"), null, 1, 2),
//
//        // PRIM
//        new Unpack(Pattern.compile("^(TRUE)|(FALSE)$"), null),
//        new Unpack(Pattern.compile("^([0-9]+)$"), null),
//        new Unpack(Pattern.compile("^[0-9]+$"), null),
//        new Unpack(Pattern.compile("."), null),
//        new Unpack(Pattern.compile("null"), null)
    };
}

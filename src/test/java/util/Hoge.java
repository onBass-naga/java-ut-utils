package util;

/**
 * Created by naga on 13/10/19.
 */
public class Hoge {
    private String greeting() {
        return "Hi !!";
    }

    private String greeting(String name) {
        return String.format("Hi, %s !!", name) ;
    }

    private String greeting(String name, int i) {
        String s = name + i;
        return String.format("Hi, %s !!", s) ;
    }

    private static String greeting2() {
        return "Hello.";
    }

    private void doNothing() {

    }
}

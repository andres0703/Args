package com.company;

/**
 *  This program is a command line reader example from the book Clean Code by Robert C. Martin.
 *
 *  Example schema: "l, p#, s*"
 *  Example command line input: -l true -p 8080 -s /usr/andres
 *
 */
public class Main {

    public static void main(String[] args) {
        try {
            String schema = "l, p#, s*";
            String[] mockArgs = new String[]{"-l", "true", "-p", "1234", "-s", "abcd"};
            Args arg = new Args(schema, mockArgs);

            boolean booleanValue = arg.getBoolean('l');
            int intValue = arg.getInt('p');
            String stringValue = arg.getString('s');

            printArguments(booleanValue, intValue, stringValue);
        } catch (ArgsException e) {
            System.out.println("Error occurred.");
        }
    }

    private static void printArguments(boolean booleanValue, int integerValue, String stringValue) {
        System.out.println("Boolean value: " + booleanValue);
        System.out.println("Integer value: " + integerValue);
        System.out.println("String value: " + stringValue);
    }
}

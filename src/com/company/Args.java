package com.company;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 *  Command line argument parser.
 *
 *  Schema e.g.: "l, a#", meaning in command line argument inputs, "-l" is followed by boolean value,
 *               and "-a" is followed by integer value.
 *
 *  Supported type: boolean(), integer(#), string(*)
 *
 */
public class Args {
    private String schema;
    private String[] args;
    private Map<Character, Boolean> booleanArg = new HashMap<>();
    private Map<Character, Integer> integerArg = new HashMap<>();
    private Map<Character, String> stringArg = new HashMap<>();
    private Set<Character> argsFound = new HashSet<>();
    private int currentArgumentIndex = 0;
    private boolean valid = true;

    public Args(String schema, String[] args) throws ArgsException {
        this.schema = schema;
        this.args = args;
        parseSchema();
        parseArgs();

        System.out.println("Argument input is " + (valid ? "valid." : "invalid."));
    }

    private void parseSchema() throws ArgsException {
        for (String element : schema.split(",")) {
            parseSchemaElement(element.trim());
        }
    }

    private void parseSchemaElement(String element) throws ArgsException {
        char token = element.charAt(0);
        if (!Character.isLetter(token)) {
            throw new ArgsException("Illegal schema.");
        }
        if (element.length() == 1) {
            booleanArg.put(token, false);
        } else if (element.substring(1).equals("#")) {
            integerArg.put(token, 0);
        } else if (element.substring(1).equals("*")) {
            stringArg.put(token, "");
        }
    }

    private void parseArgs() {
        for (currentArgumentIndex = 0; currentArgumentIndex < args.length; currentArgumentIndex++) {
            String arg = args[currentArgumentIndex];
            if (arg.startsWith("-")) {
                parseArg(arg.charAt(1));
            }
        }
    }

    private void parseArg(char argChar) {
        if (addToArgMap(argChar)) {
            argsFound.add(argChar);
        } else {
            valid = false;
        }
    }

    private boolean addToArgMap(char argChar) {
        boolean rtn = true;
        if (isBooleanArg(argChar)) {
            addArgToBooleanMap(argChar);
        } else if (isIntegerArg(argChar)) {
            addArgToIntegerMap(argChar);
        } else if (isStringArg(argChar)) {
            addArgToStringMap(argChar);
        } else {
            rtn = false;
        }

        return rtn;
    }

    private boolean isBooleanArg(char token) {
        return booleanArg.containsKey(token);
    }

    private boolean isIntegerArg(char token) {
        return integerArg.containsKey(token);
    }

    private boolean isStringArg(char token) {
        return stringArg.containsKey(token);
    }

    private boolean addArgToBooleanMap(char argChar) {
        try {
            String argValue = args[++currentArgumentIndex];
            if (argValue.toLowerCase().equals("true")) {
                booleanArg.put(argChar, true);
            } else if (argValue.toLowerCase().equals("false")) {
                booleanArg.put(argChar, false);
            }
        } catch (IndexOutOfBoundsException e) {
            booleanArg.put(argChar, true);
        }
        return true;
    }

    private boolean addArgToIntegerMap(char argChar) {
        try {
            int argValue = Integer.parseInt(args[++currentArgumentIndex]);
            integerArg.put(argChar, argValue);
            return true;
        } catch (IndexOutOfBoundsException | NumberFormatException e) {
            valid = false;
            return false;
        }
    }

    private boolean addArgToStringMap(char argChar) {
        try {
            String argValue = args[++currentArgumentIndex];
            stringArg.put(argChar, argValue);
            return true;
        } catch (IndexOutOfBoundsException e) {
            valid = false;
            return false;
        }
    }

    public boolean has(char token) {
        return argsFound.contains(token);
    }

    public boolean getBoolean(char token) throws ArgsException {
        validateToken(token);
        return booleanArg.get(token);
    }

    public int getInt(char token) throws ArgsException {
        validateToken(token);
        return integerArg.get(token);
    }

    public String getString(char token) throws ArgsException {
        validateToken(token);
        return stringArg.get(token);
    }

    private void validateToken(char token) throws ArgsException {
        if (!has(token)) {
            throw new ArgsException("Invalid token: " + token);
        }
    }
}

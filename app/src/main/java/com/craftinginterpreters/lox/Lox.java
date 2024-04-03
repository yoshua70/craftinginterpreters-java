package com.craftinginterpreters.lox;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Lox {
    static boolean hadError = false;

    public static void main(String[] args) throws IOException {
        // Our interpreter supports two ways of running code:
        // - read and execute a file
        // - start a REPL
        if (args.length > 1) {
            System.out.println("Usage: jlox script");
            // Exit code from UNIX "sysexits.h"
            // EX_USAGE (64): the command was used incorrectly.
            System.exit(64);
        } else if (args.length == 1) {
            runFile(args[0]);
        } else {
            runPrompt();
        }
    }

    // Reads the file given through the path and executes it.
    private static void runFile(String path) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(path));
        run(new String(bytes, Charset.defaultCharset()));

        // EX_NOINPUT (65): the input data was incorrect in some way.
        if (hadError)
            System.exit(65);
    }

    // Starts an interactive REPL.
    private static void runPrompt() throws IOException {
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);

        for (;;) {
            System.out.print("> ");
            String line = reader.readLine();
            if (line == null)
                break;
            run(line);
        }
    }

    private static void run(String source) {
        Scanner scanner = new Scanner(source);
        List<Token> tokens = scanner.scanTokens();

        // For now, just print the tokens.
        for (Token token : tokens) {
            System.out.println(token);
        }
    }

    // Tells the user some syntax error occured on a given line.
    static void error(int line, String message) {
        report(line, "", message);
    }

    // Helper method for the error method.
    private static void report(int line, String where, String message) {
        System.err.println("[line" + line + "] Error " + where + ": " + message);
        hadError = true;
    }
}

package com;

import com.wordle.WordGuesser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

public class Main {
    private final static Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        LOGGER.info("Welcome to the Wordle game!");
        LOGGER.info("Enter the length of word to start: ");

        Scanner scanner = new Scanner(System.in);

        int length = 0;
        boolean isLengthValid = false;
        while (!isLengthValid) {
            length = scanner.nextInt();
            if (length < 0) {
                LOGGER.info("Invalid length: The length of the word must be greater than 0");

            } else {
                isLengthValid = true;
            }
        }

        scanner.close();

        try {
            String guessWord = WordGuesser.guess(length);
            LOGGER.info("Guessed word: {}", guessWord);

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            LOGGER.error("Failed to guess word");
        }
    }
}
package com;

import com.wordle.WordGuesser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;
import java.util.Scanner;

public class Main {
    private final static Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        LOGGER.info("Welcome to the Wordle game!");
        LOGGER.info("Choose options to start the game: ");
        LOGGER.info("1. Guess the word with a determined length");
        LOGGER.info("2. Guess the random word");
        Scanner scanner = new Scanner(System.in);
        int option;
        int length = 0;
        boolean isValidOption = false;
        while (!isValidOption) {
            option = scanner.nextInt();

            if (option == 1) {
                LOGGER.info("Enter the length of the word to start: ");
                boolean isLengthValid = false;
                while (!isLengthValid) {
                    length = scanner.nextInt();
                    if (length < 0 || length > 22) {
                        LOGGER.info("The length of the word must be between 1 and 22");

                    } else {
                        isLengthValid = true;
                    }
                }
                isValidOption = true;

            } else if (option == 2) {
                Random random = new Random();
                length = random.nextInt(22) + 1;

                LOGGER.info("Guess word with length: {}", length);
                isValidOption = true;

            } else {
                LOGGER.info("Please choose 1 or 2");
            }
        }

        scanner.close();

        try {
            LOGGER.info("Start guessing the word.");
            String guessWord = WordGuesser.guess(length);
            LOGGER.info("Guessed word: {}", guessWord);

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            LOGGER.error("Failed to guess word");
        }
    }
}
package com.wordle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashSet;

public class WordGuesser {
    private final static Logger LOGGER = LoggerFactory.getLogger(WordGuesser.class);

    /*
     * This function will content the logic for guess the word.
     * This will take 2 steps:
     *   1. Try to scan alphabet to collect all correct/present characters in the word.
     *   2. Scan all present characters from step 1 to get correct positions of them.
     */
    public static String guess(int length) {
        char[] correctChars = new char[length];
        // use '-' character for identify missing character
        Arrays.fill(correctChars, '-');
        HashSet<Character> presentChars = new HashSet<>();

        scanAlphabet(length, correctChars, presentChars);

        scanPresentChars(length, correctChars, presentChars);

        return String.valueOf(correctChars);
    }

    private static void scanAlphabet(int length, char[] correctChars, HashSet<Character> presentChars) {
        char character = 'a';
        // Stop scanning when character over 'z' Or present characters size is less than to the word length
        while (character <= 'z' && presentChars.size() < length) {
            // Try to collect the guess word with word length. And scan from a-z.
            char[] guessChars = new char[length];
            for (int i = 0; i < length; i++) {
                if (character > 'z') {
                    guessChars[i] = (char)(character - 'z' + 'a' - 1);

                } else {
                    guessChars[i] = character;
                }

                character++;
            }

            String guessWord = String.valueOf(guessChars);
            LOGGER.debug("Guess word: {}", guessWord);

            // Check and collect only for correct/present character
            WordleService.guessRandom(guessWord, length).forEach(guessResult -> {
                if (guessResult.result().equals(ResultKind.correct.toString())) {
                    correctChars[guessResult.slot()] = guessResult.guess();
                    presentChars.add(guessResult.guess());

                } else if (guessResult.result().equals(ResultKind.present.toString())) {
                    presentChars.add(guessResult.guess());
                }
            });
        }

        LOGGER.info("Present chars: {}", presentChars);
    }

    private static void scanPresentChars(int length, char[] correctChars, HashSet<Character> presentChars) {
        // Scan all present characters
        presentChars.forEach(character -> {
            // Replace the present character to all place not found correct character
            String guessWord = String.valueOf(correctChars).replaceAll("-", character.toString());

            //collect only for correct position
            WordleService.guessRandom(guessWord, length).forEach(guessResult -> {
                if (guessResult.result().equals(ResultKind.correct.toString())) {
                    correctChars[guessResult.slot()] = guessResult.guess();
                }
            });
        });
    }
}

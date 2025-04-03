package com.wordle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicReferenceArray;

public class WordGuesser {
    private final static Logger LOGGER = LoggerFactory.getLogger(WordGuesser.class);
    private final static int THREAD_POOL_SIZE = 5;
    private final static ExecutorService EXECUTOR = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

    /*
     * This function will content the logic for guess the word.
     * This will take 2 steps:
     *   1. Try to scan alphabet to collect all correct/present characters in the word.
     *   2. Scan all present characters from step 1 to get correct positions of them.
     */
    public static String guess(int length) {
        AtomicReferenceArray<Character> correctChars = new AtomicReferenceArray<>(new Character[length]);
        Set<Character> presentChars = Collections.synchronizedSet(new HashSet<>());

        try {
            scanAlphabet(length, correctChars, presentChars);

            scanPresentChars(length, correctChars, presentChars);

        } finally {
            EXECUTOR.shutdown();
        }

        return getWord(correctChars);
    }

    private static void scanAlphabet(int length,
                                     AtomicReferenceArray<Character> correctChars,
                                     Set<Character> presentChars) {
        List<Future<Void>> futures = new ArrayList<>();
        char character = 'a';
        while (character <= 'z') {
            // Try to collect the guess word with word length. And scan from a-z.
            char[] guessChars = new char[length];
            for (int i = 0; i < length; i++) {
                if (character > 'z') {
                    guessChars[i] = (char) (character - 'z' + 'a' - 1);

                } else {
                    guessChars[i] = character;
                }

                character++;
            }

            String guessWord = String.valueOf(guessChars);
            LOGGER.debug("Guess word: {}", guessWord);

            // Check and collect only for correct/present character
            Future<Void> future = EXECUTOR.submit(() -> {
                if (presentChars.size() != length) {
                    WordleService.guessRandom(guessWord, length).forEach(guessResult -> {
                        if (guessResult.result().equals(ResultKind.correct.toString())) {
                            correctChars.set(guessResult.slot(), guessResult.guess());
                            presentChars.add(guessResult.guess());

                        } else if (guessResult.result().equals(ResultKind.present.toString())) {
                            presentChars.add(guessResult.guess());
                        }
                    });
                }
                return null;
            });

            futures.add(future);
        }

        waitThreadComplete(futures);

        LOGGER.info("Present chars: {}", presentChars);
    }

    private static void scanPresentChars(int length,
                                         AtomicReferenceArray<Character> correctChars,
                                         Set<Character> presentChars) {
        List<Future<Void>> futures = new ArrayList<>();
        // Scan all present characters
        presentChars.forEach(character -> {
            // Replace the present character to all place not found correct character
            Future<Void> future = EXECUTOR.submit(() -> {
                String guessWord = getWord(correctChars).replaceAll("-", character.toString());

                LOGGER.debug("Guess word: {}", guessWord);

                //collect only for correct position
                WordleService.guessRandom(guessWord, length).forEach(guessResult -> {
                    if (guessResult.result().equals(ResultKind.correct.toString())) {
                        correctChars.set(guessResult.slot(), guessResult.guess());
                    }
                });

                return null;
            });

            futures.add(future);
        });

        waitThreadComplete(futures);
    }

    private static void waitThreadComplete(List<Future<Void>> futures) {
        for (Future<Void> future : futures) {
            try {
                future.get();

            } catch (ExecutionException | InterruptedException e) {
                throw new RuntimeException("Thread execution failed", e);
            }
        }
    }

    private static String getWord(AtomicReferenceArray<Character> chars) {
        StringBuilder word = new StringBuilder();
        for (int i = 0; i < chars.length(); i++) {
            word.append(chars.get(i) != null ? chars.get(i).toString() : "-");
        }

        return word.toString();
    }
}

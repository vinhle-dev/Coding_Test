package com.wordle;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class WordleService {
    private final static String API_URL = "https://wordle.votee.dev:8000/random";
    private final static HttpClient CLIENT = HttpClient.newHttpClient();
    private final static Logger LOGGER = LoggerFactory.getLogger(WordleService.class);

    public static List<GuessResult> guessRandom(String guessWord, int length) {
        List<GuessResult> guessResults;
        String query = "?guess=" + guessWord + "&size=" + length + "&seed=" + length;
        try {
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(API_URL + query)).build();
            HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                ObjectMapper mapper = new ObjectMapper();
                guessResults = mapper.readValue(response.body(), new TypeReference<>() {});

            } else {
                throw new IOException("Status code: " + response.statusCode() + " - Body: " + response.body());
            }

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new RuntimeException("Error in wordle service", e);
        }

        return guessResults;
    }
}

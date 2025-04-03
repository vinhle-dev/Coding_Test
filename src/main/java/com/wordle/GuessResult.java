package com.wordle;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GuessResult(@JsonProperty int slot,
                          @JsonProperty char guess,
                          @JsonProperty String result) {
    @Override
    public String toString() {
        return "GuessResult [slot=" + slot + ", guess=" + guess + ", result=" + result + "]";
    }
}

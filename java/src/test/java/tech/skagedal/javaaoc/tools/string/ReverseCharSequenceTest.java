package tech.skagedal.javaaoc.tools.string;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ReverseCharSequenceTest {
    @Test
    void simple() {
        var reverse = new ReverseCharSequence("abc");

        assertEquals("cba", reverse.toString());
    }

    @Test
    void subsequence() {
        var reverse = new ReverseCharSequence("simon");
        assertEquals(
            "mi",
            reverse.subSequence(2, 4).toString()
        );
    }
}

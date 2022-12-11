package tech.skagedal.javaaoc.tools;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class AocOcrTest {
    AocOcr ocr = new AocOcr();

    @Test
    void scansA() {
        assertEquals(
            "A",
            ocr.scan(".##.\n#..#\n#..#\n####\n#..#\n#..#")
        );
    }
}
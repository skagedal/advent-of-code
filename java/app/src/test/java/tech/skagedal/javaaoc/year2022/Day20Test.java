package tech.skagedal.javaaoc.year2022;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class Day20Test {
    Day20.Mixer mixer;

    @Test
    void mixerTest1() {
        long[] array = {5, 0, 0, 0, 0};
        long[] expectedResult = {5, 0, 0, 0, 0};
        assertMixed(expectedResult, array);
    }

    @Test
    void mixerTest2() {
        long[] array = {10, 0, 0, 0, 0};
        long[] expectedResult = {10, 0, 0, 0, 0};
        assertMixed(expectedResult, array);
    }

    @Test
    void mixerTest3() {
        long[] array = {42, 0, 0, 0, 0};
        long[] expectedResult = {0, 0, 42, 0, 0};
        assertMixed(expectedResult, array);
    }

    @Test
    void mixerTest4() {
        long[] array = {-1, 0, 0, 0, 0};
        long[] expectedResult = {0, 0, 0, 0, -1};
        assertMixed(expectedResult, array);
        Day20.printArr(mixer.positionArray);
        Assertions.assertArrayEquals(new int[] { 4, 1, 2, 3, 0}, mixer.positionArray);
    }

    @Test
    void mixerTest5() {
        long[] array = {-11, 0, 0, 0, 0};
        long[] expectedResult = {0, 0, 0, 0, -11};
        assertMixed(expectedResult, array);
        Day20.printArr(mixer.positionArray);
        Assertions.assertArrayEquals(new int[] { 4, 1, 2, 3, 0}, mixer.positionArray);
    }

    private void assertMixed(long[] mixedResult, long[] arr) {
        this.mixer = new Day20.Mixer(true, arr);
        mixer.run();
        Assertions.assertArrayEquals(
            mixedResult,
            mixer.mixArray
        );
    }
}
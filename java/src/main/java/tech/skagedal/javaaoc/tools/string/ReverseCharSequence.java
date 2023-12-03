package tech.skagedal.javaaoc.tools.string;

import tech.skagedal.javaaoc.tools.exceptions.NotImplementedException;

public class ReverseCharSequence implements CharSequence {
    private final CharSequence baseSequence;
    private final int length;

    public ReverseCharSequence(CharSequence sequence) {
        this.baseSequence = sequence;
        this.length = baseSequence.length();
    }

    @Override
    public int length() {
        return baseSequence.length();
    }

    @Override
    public char charAt(int index) {
        return baseSequence.charAt(length - 1 - index);
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        return new ReverseCharSequence(
            baseSequence.subSequence(length - end, length - start)
        );
    }

    @Override
    public String toString() {
        @SuppressWarnings("StringBufferReplaceableByString")
        final StringBuilder stringBuilder = new StringBuilder(length());
        stringBuilder.append(this);
        return stringBuilder.toString();
    }
}

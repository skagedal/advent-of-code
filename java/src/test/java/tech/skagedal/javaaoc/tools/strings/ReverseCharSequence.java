package tech.skagedal.javaaoc.tools.strings;

public class ReverseCharSequence implements CharSequence {
    private final CharSequence baseSequence;

    public ReverseCharSequence(CharSequence sequence) {
        this.baseSequence = sequence;
    }

    @Override
    public int length() {
        return baseSequence.length();
    }

    @Override
    public char charAt(int index) {
        return baseSequence.charAt(baseSequence.length() - 1 - index);
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        // TODO
        return null;
    }
}

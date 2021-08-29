package net.morimori.simpletranslation.exception;

public class TranslationException extends Exception {
    private static final long serialVersionUID = 1L;

    public TranslationException(int code) {
        super("Error Code: " + code);
    }
}

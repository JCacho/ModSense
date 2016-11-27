package ru.pvolan.strip1.view;

public class EW<T> {

    public final T data;
    public final Exception error;

    public EW (T data, Exception error) {
        this.data = data;
        this.error = error;
    }
}

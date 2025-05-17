package com.coordinadora.technicaltest.util;

public class ResponseState<T> {
    public enum Status { LOADING, SUCCESS, ERROR }

    public final Status status;
    public final T data;
    public final String message;

    private ResponseState(Status status, T data, String message) {
        this.status = status;
        this.data = data;
        this.message = message;
    }

    public static <T> ResponseState<T> loading() {
        return new ResponseState<>(Status.LOADING, null, null);
    }

    public static <T> ResponseState<T> success(T data) {
        return new ResponseState<>(Status.SUCCESS, data, null);
    }

    public static <T> ResponseState<T> error(String message) {
        return new ResponseState<>(Status.ERROR, null, message);
    }
}
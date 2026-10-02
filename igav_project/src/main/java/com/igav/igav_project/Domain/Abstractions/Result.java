package com.igav.igav_project.Domain.Abstractions;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * Contenedor genérico del Patrón Result para manejo funcional de respuestas de negocio.
 *
 * @param <T> Tipo del valor retornado en caso de éxito.
 */
public class Result<T> {

    private final T value;
    private final boolean isSuccess;
    private final Error error;

    protected Result(T value, boolean isSuccess, Error error) {
        if (isSuccess && !Objects.equals(error, Error.NONE)) {
            throw new IllegalArgumentException("Un resultado exitoso no puede contener un error.");
        }
        if (!isSuccess && Objects.equals(error, Error.NONE)) {
            throw new IllegalArgumentException("Un resultado fallido debe especificar un error.");
        }
        this.value = value;
        this.isSuccess = isSuccess;
        this.error = error;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public boolean isFailure() {
        return !isSuccess;
    }

    public T getValue() {
        if (!isSuccess) {
            throw new IllegalStateException("No se puede obtener el valor de un Result fallido. Error: " + error.description());
        }
        return value;
    }

    public Error getError() {
        return error;
    }

    public static <T> Result<T> success(T value) {
        return new Result<>(value, true, Error.NONE);
    }

    public static <T> Result<T> failure(Error error) {
        return new Result<>(null, false, error);
    }

    public static Result<Void> success() {
        return new Result<>(null, true, Error.NONE);
    }

    public void onSuccess(Consumer<T> action) {
        if (isSuccess) {
            action.accept(value);
        }
    }

    public void onFailure(Consumer<Error> action) {
        if (!isSuccess) {
            action.accept(error);
        }
    }
}

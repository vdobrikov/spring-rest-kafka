package com.vdobrikov.restconsumer.event;

import java.util.Objects;

public abstract class Event<T> {
    private T value;

    public Event(T value) {
        Objects.requireNonNull(value, "'value' is mandatory");
        this.value = value;
    }

    public T getValue() {
        return value;
    }
}

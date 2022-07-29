package com.kafka.unicorn.service;

public interface ButcheryService<T, R> {
    R execute(T t);
}

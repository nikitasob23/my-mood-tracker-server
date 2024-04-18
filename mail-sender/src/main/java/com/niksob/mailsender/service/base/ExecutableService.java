package com.niksob.mailsender.service.base;

public interface ExecutableService<T, R> {

    R execute(T t);
}

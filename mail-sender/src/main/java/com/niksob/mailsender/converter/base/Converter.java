package com.niksob.mailsender.converter.base;

public interface Converter<T, R> {

    R convert(T t);
}

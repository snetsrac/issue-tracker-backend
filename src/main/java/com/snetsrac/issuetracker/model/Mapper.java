package com.snetsrac.issuetracker.model;

public interface Mapper<T, D> {
    public D toDto(T entity);
}

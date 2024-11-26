package com.example.jpademojava.repository;

import java.util.List;

public interface IJdbcTemplateDao<T> {

    List<T> findAll();

    T create(T t);

    T update();

    void delete(int id);


}

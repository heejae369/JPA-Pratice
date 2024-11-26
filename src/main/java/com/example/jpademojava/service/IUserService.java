package com.example.jpademojava.service;

import com.example.jpademojava.domain.dto.UserRequestDto;
import com.example.jpademojava.domain.dto.UserResponsDto;

public interface IUserService {

    public abstract UserResponsDto findById(Integer id);

    public abstract void deleteById(Integer id);

    public abstract UserResponsDto createUser(String name, Integer age);

    public UserResponsDto updateUser(Integer id, UserRequestDto requestDto);

}

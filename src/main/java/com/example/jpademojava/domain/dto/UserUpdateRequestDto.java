package com.example.jpademojava.domain.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class UserUpdateRequestDto {

    @Getter
    @Setter
    @NoArgsConstructor
    public class UserRequestDto {

        //@NotBlank
        private String name;
        private Integer age;

    }


}

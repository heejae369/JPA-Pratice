package com.example.jpademojava.controller;


import com.example.jpademojava.domain.dto.UserRequestDto;
import com.example.jpademojava.domain.dto.UserResponsDto;
import com.example.jpademojava.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<UserResponsDto> getUser(@PathVariable Integer id) {
        UserResponsDto userResponsDto = userService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(userResponsDto);
    }

    // userRequestDto, 제작 필요
    @PostMapping()
    public ResponseEntity<UserResponsDto> createUser(@RequestBody UserRequestDto request) {
        UserResponsDto userResponsDto = userService.createUser(request.getName(), request.getAge());
        return ResponseEntity.status(HttpStatus.CREATED).body(userResponsDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Integer id) {
        userService.deleteById(id);
        return ResponseEntity
            .status(HttpStatus.OK)
            .body("해당 유저가 삭제되었습니다");
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponsDto> updateUser(@PathVariable Integer id,
        UserRequestDto requestDto) {
        UserResponsDto userResponsDto = userService.updateUser(id, requestDto);
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(userResponsDto);
    }

}

/*
user

postgresql, data jpa 추가

CRUD post get

entity  - table, DB  -> autoddl, double quote
* */
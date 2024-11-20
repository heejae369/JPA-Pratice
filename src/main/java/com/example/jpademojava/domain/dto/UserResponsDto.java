package com.example.jpademojava.domain.dto;

import com.example.jpademojava.domain.Message;
import com.example.jpademojava.domain.User;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class UserResponsDto {

    private Integer id;
    private String name;
    private Integer age;
    private List<MessageResponseDto> messages;

    public static UserResponsDto from(User user) {
        return new UserResponsDto(
            user.getId(),
            user.getName(),
            user.getAge(),
            null
        );
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages.stream()
            .map(MessageResponseDto::from)
            .toList();
    }
}

package com.example.jpademojava.domain.dto;


import com.example.jpademojava.domain.Message;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonInclude(Include.NON_NULL)
@JsonPropertyOrder({"createdAt", "message"})
public class MessageResponseDto {

    @DateFormat
    private LocalDateTime createdAt;
    private String message;

    public static MessageResponseDto from(Message entity) {
        return new MessageResponseDto(
            entity.getCreatedAt(),
            entity.getMessage()
        );
    }
}

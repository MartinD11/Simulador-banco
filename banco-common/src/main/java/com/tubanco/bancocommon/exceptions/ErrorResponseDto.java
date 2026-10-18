package com.tubanco.bancocommon.exceptions;

import lombok.*;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponseDto {
    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private String path;

}

package com.yemiadeoye.banks_ms.controlleradvice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.yemiadeoye.banks_ms.dtos.response.ErrorResponseDto;
import com.yemiadeoye.banks_ms.exceptions.AccountNotFoundException;
import com.yemiadeoye.banks_ms.exceptions.InvalidAccountTypeException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestControllerAdvice
public class AccountsControllerAdvice {
    @ExceptionHandler
    public ResponseEntity<ErrorResponseDto> handleException(AccountNotFoundException exception) {
        ErrorResponseDto errorDto = new ErrorResponseDto(HttpStatus.NOT_FOUND.value(), exception.getMessage());

        return new ResponseEntity<>(errorDto, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponseDto> handleException(InvalidAccountTypeException exception) {
        ErrorResponseDto errorDto = new ErrorResponseDto(HttpStatus.BAD_REQUEST.value(), exception.getMessage());

        return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponseDto> handleException(Exception exception, HttpServletRequest request,
            HttpServletResponse response) {

        ErrorResponseDto errorDto = new ErrorResponseDto(HttpStatus.BAD_REQUEST.value(), exception.getMessage());

        return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
    }
}

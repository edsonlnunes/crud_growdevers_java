package br.com.growdev.growdevers.config;

import br.com.growdev.growdevers.dtos.ErrorData;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class HandleException {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity handle400(MethodArgumentNotValidException exception){
        var errors = exception.getFieldErrors().stream().map(ErrorData::new).toList();
        return ResponseEntity.badRequest().body(errors);
    }

}

package dev.ghanshyam.productservicev2.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllerAdvisor {

    @ExceptionHandler(NullPointerException.class)
    ResponseEntity<ExceptionDto> NullPointerException(NullPointerException e){
        ResponseEntity<ExceptionDto>responseEntity = new ResponseEntity<>(new ExceptionDto("requested resource did not exist", HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
        return responseEntity;
    }
    @ExceptionHandler(NotFoundException.class)
    ResponseEntity<ExceptionDto> notFoundException(NotFoundException e){
        ResponseEntity<ExceptionDto>responseEntity = new ResponseEntity<>(new ExceptionDto(e.getMessage(), HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
        return responseEntity;
    }

    @ExceptionHandler(AddException.class)
    ResponseEntity<ExceptionDto> addException(AddException e){
        ResponseEntity<ExceptionDto>responseEntity = new ResponseEntity<>(new ExceptionDto(e.getMessage(),HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
        return responseEntity;
    }

    @ExceptionHandler(DeleteException.class)
    ResponseEntity<ExceptionDto> deleteException(DeleteException e){
        ResponseEntity<ExceptionDto>responseEntity = new ResponseEntity<>(new ExceptionDto(e.getMessage(),HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
        return responseEntity;
    }

    @ExceptionHandler(HttpMessageNotReadableException.class) // if the product is missing in addProduct()
    ResponseEntity<ExceptionDto> missingProductException(HttpMessageNotReadableException e){
        ResponseEntity<ExceptionDto>responseEntity = new ResponseEntity<>(new ExceptionDto("Required product details are missing",HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
        return responseEntity;
    }

    @ExceptionHandler( HttpRequestMethodNotSupportedException.class) // if the url is wrong
    ResponseEntity<ExceptionDto> GeneralException( HttpRequestMethodNotSupportedException e){
        ResponseEntity<ExceptionDto>responseEntity = new ResponseEntity<>(new ExceptionDto("This method is not supported, please check the method type or url endpoint",HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
        return responseEntity;
    }

    @ExceptionHandler(Exception.class)
    ResponseEntity<ExceptionDto> GeneralException(Exception e){
        ResponseEntity<ExceptionDto>responseEntity = new ResponseEntity<>(new ExceptionDto("Some unkown error occurred",HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
        return responseEntity;
    }

}

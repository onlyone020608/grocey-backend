package com.hyewon.grocey_api.global.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({
            UserNotFoundException.class,
            ProductNotFoundException.class,
            CartItemNotFoundException.class,
            CartNotFoundException.class,
            FridgeNotFoundException.class,
            FridgeIngredientNotFoundException.class,
            OrderNotFoundException.class,
            InvalidRequestException.class,
            InvalidEnumValueException.class,
            RecipeNotFoundException.class,
            DuplicateSavedRecipeException.class,
            SavedRecipeNotFoundException.class
    })
    public ResponseEntity<ErrorResponse> handleDomainExceptions(RuntimeException ex) {
        if (ex instanceof UserNotFoundException userEx) {
            return buildResponse(userEx.getErrorCode());
        } else if (ex instanceof ProductNotFoundException productEx) {
            return buildResponse(productEx.getErrorCode());
        } else if (ex instanceof CartItemNotFoundException cartItemEx) {
            return buildResponse(cartItemEx.getErrorCode());
        } else if (ex instanceof CartNotFoundException cartEx) {
            return buildResponse(cartEx.getErrorCode());
        } else if (ex instanceof FridgeNotFoundException fridgeEx) {
            return buildResponse(fridgeEx.getErrorCode());
        } else if (ex instanceof FridgeIngredientNotFoundException fridgeIngredientEx) {
            return buildResponse(fridgeIngredientEx.getErrorCode());
        }  else if (ex instanceof OrderNotFoundException orderEx) {
            return buildResponse(orderEx.getErrorCode());
        } else if (ex instanceof InvalidRequestException invalidEx) {
            return buildResponse(invalidEx.getErrorCode());
        } else if (ex instanceof InvalidEnumValueException invalidEnumEx) {
            return buildResponse(invalidEnumEx.getErrorCode());
        } else if (ex instanceof RecipeNotFoundException recipeEx) {
            return buildResponse(recipeEx.getErrorCode());
        }  else if (ex instanceof DuplicateSavedRecipeException dupEx) {
            return buildResponse(dupEx.getErrorCode());
        } else if (ex instanceof SavedRecipeNotFoundException savedEx) {
            return buildResponse(savedEx.getErrorCode());
        }

        return buildResponse(ErrorCode.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(AccessDeniedException ex) {
        return buildResponse(ErrorCode.ACCESS_DENIED);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex) {
        return buildResponse(ErrorCode.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException ex) {
        return buildResponse(ErrorCode.INVALID_REQUEST);
    }

    private ResponseEntity<ErrorResponse> buildResponse(ErrorCode errorCode) {
        return ResponseEntity.status(errorCode.getStatus())
                .body(new ErrorResponse(errorCode));
    }



}

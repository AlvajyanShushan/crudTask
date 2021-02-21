package egs.task.controllers;

import egs.task.models.ResponseModel;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;
import java.util.function.Function;

public class BaseController {

    protected <T, R> ResponseModel returnResponseModel(Function<T, R> action) {
        try {
            R data = action.apply(null);
            return createSuccessResult(data);
        } catch (Exception e) {
            return createErrorResult(e);
        }
    }

    private <R> ResponseModel<R> createSuccessResult(R data) {
        ResponseModel<R> responseModel = new ResponseModel<>();
        responseModel.setData(data);
        responseModel.setSuccess(true);
        responseModel.setMessage("OK");
        return responseModel;
    }

    protected <R> ResponseModel<R> createResult(R data, String message) {
        ResponseModel<R> responseModel = new ResponseModel<>();
        responseModel.setData(data);
        responseModel.setSuccess(true);
        responseModel.setMessage(message);
        return responseModel;
    }

    protected ResponseModel createErrorResult(Exception e) {
        ResponseModel responseModel = new ResponseModel<>();
        responseModel.setSuccess(false);
        responseModel.setMessage(e.getMessage());
        return responseModel;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseModel handleException(MethodArgumentNotValidException exception) {
        ResponseModel responseModel = new ResponseModel();
        responseModel.setMessage(getFieldError(exception.getBindingResult().getFieldErrors()));
        responseModel.setSuccess(false);
        return responseModel;
    }

    private String getFieldError(List<FieldError> fieldErrors) {
        if (!fieldErrors.isEmpty()) {
            FieldError fieldError = fieldErrors.get(0);
            return String.format("Field '%s' %s", fieldError.getField(), fieldError.getDefaultMessage());
        }
        return "Validation error";
    }
}
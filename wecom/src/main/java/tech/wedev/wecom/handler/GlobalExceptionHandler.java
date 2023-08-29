package tech.wedev.wecom.handler;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import tech.wedev.wecom.entity.vo.ResponseVO;
import tech.wedev.wecom.exception.ExceptionCode;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * 功能描述: @Validated 校验失败时的异常，将校验结果以json格式返回
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseVO hanleMethodArgumentNotValidException(MethodArgumentNotValidException ex){
        BindingResult bindingResult = ex.getBindingResult();
        StringBuilder sb = new StringBuilder();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        for (FieldError fieldError : fieldErrors){
            sb.append(fieldError.getDefaultMessage()).append(", ");
        }
        sb.deleteCharAt(sb.lastIndexOf(", "));
        return ResponseVO.error(ExceptionCode.INVALID_PARAMETER,sb.toString());
    }
}

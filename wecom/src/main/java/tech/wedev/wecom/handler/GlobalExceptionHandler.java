package tech.wedev.wecom.handler;

import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
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
    public ResponseVO handleMethodArgumentNotValidException(MethodArgumentNotValidException ex){
        BindingResult bindingResult = ex.getBindingResult();
        StringBuilder sb = new StringBuilder();
        List<ObjectError> globalErrors = bindingResult.getGlobalErrors();
        for (ObjectError error : globalErrors){
            sb.append(error.getDefaultMessage()).append(", ");
        }
        sb.deleteCharAt(sb.lastIndexOf(", "));
        return ResponseVO.error(ExceptionCode.INVALID_PARAMETER,sb.toString());
    }
}

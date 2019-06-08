package com.juntao.blogsystem.util;

import org.apache.commons.lang3.StringUtils;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

public class ConstraintViolationExceptionHandler { //异常处理类，主要处理hibernate的一些约束条件异常，获取此异常并返回信息给前端
    /**
     * 获取批量异常信息
     * @param e
     * @return
     * */
    public static String getMessage(ConstraintViolationException e){
        List<String> msgList =new ArrayList<>();
        for (ConstraintViolation<?> constraintViolationException:e.getConstraintViolations()) {
            msgList.add(constraintViolationException.getMessage());
        }
        String messages = StringUtils.join(msgList.toArray(),";");
        return messages;
    }
}

package common.ServerAdvice.controller;

import common.DataFormatter.ErrorCode;
import common.DataFormatter.Result;
import common.ServerAdvice.util.LogUtil;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Errol on 17/5/28.
 */
@ControllerAdvice
public class ExceptionAdvice {

//    private static final Logger logger = Logger.getLogger(ExceptionAdvice.class);

    @ResponseBody
    @ExceptionHandler(Exception.class)
    public Object handleException(Exception e) {
//        logger.error(e.getMessage(), e);
        LogUtil.capture(e.getMessage(), e);
        return Result.ERROR(ErrorCode.CODING_ERROR);
    }


}

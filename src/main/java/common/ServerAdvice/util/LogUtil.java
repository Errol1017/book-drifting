package common.ServerAdvice.util;

import org.apache.log4j.Logger;

/**
 * Created by Errol on 17/5/28.
 */
public class LogUtil {

//    private static final LogUtil instance = new LogUtil();
    private static final Logger logger = Logger.getLogger(LogUtil.class);
//
//    private LogUtil() {
//    }
//
//    public static LogUtil getInstance(){
//        return instance;
//    }
    public static Logger getLogger(){
        return logger;
    }

    public static void debug(String s){
        logger.warn(s);
    }

    public static void capture(Object m, Throwable t){
        logger.error(m, t);
    }

}

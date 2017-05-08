package common.DataFormatter;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

/**
 * Created by Errol on 2016/6/14.
 */
public class DataManager {

    public static <T> T string2Object(String jsonStr, Class<T> valueType){
        if (jsonStr.equals("")) {
            return null;
        }
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
        try{
            return objectMapper.readValue(jsonStr, valueType);
        }catch (Exception e){
            e.printStackTrace();
            return  null;
        }
    }

    public static Map string2Map(String jsonStr){
        if (jsonStr.equals("")) {
            return null;
        }
        ObjectMapper objectMapper = new ObjectMapper();
        try{
            return objectMapper.readValue(jsonStr, Map.class);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static String object2String (Object object){
        if (object == null) {
            return null;
        }
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(object);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

}

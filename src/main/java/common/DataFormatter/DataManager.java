package common.DataFormatter;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by Errol on 2016/6/14.
 */
public class DataManager {

    public static <T> List<T> getListPaging(List<T> list, int page, int batch) {
        int size = list.size();
        if (size >= batch){
            if (size > batch*page) {
                list = list.subList(batch * (page - 1), batch * page);
            }else if (size <= batch*(page-1)){
                list = new ArrayList<T>();
            }else {
                list = list.subList(batch*(page-1), size);
            }
        }else {
            if (page > 1){
                list = new ArrayList<T>();
            }
        }
        return list;
    }

    public static <T> List<T> getListPagingReverse(List<T> list, int page, int batch) {
        int size = list.size();
        if (size >= batch) {
            if (size > batch*page) {
                list = list.subList(size-batch*page, size-batch*(page-1));
            }else if (size <= batch*(page-1)) {
                list = new ArrayList<T>();
            }else {
                list = list.subList(0, size-batch*(page-1));
            }
        }else {
            if (page > 1){
                list = new ArrayList<T>();
            }
            list = list.subList(0, list.size());
        }
        Collections.reverse(list);
        return list;
    }

    public static <T> T string2Object(String jsonStr, Class<T> valueType){
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
        try{
            return objectMapper.readValue(jsonStr, valueType);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static Map string2Map(String jsonStr){
        ObjectMapper objectMapper = new ObjectMapper();
        try{
            return objectMapper.readValue(jsonStr, Map.class);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static String object2String (Object object){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(object);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

}

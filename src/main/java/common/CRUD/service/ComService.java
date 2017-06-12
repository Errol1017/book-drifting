package common.CRUD.service;



import java.util.List;

/**
 * Created by Errol on 2016/7/16.
 * Generic Interface for Common CRUD Operation
 */
public interface ComService {

//    String getFileBathPath();
//    String getRemoteClientId();

    <T> long getCount(Class<T> typeValue);
    <T> long getCount(Class<T> typeValue, String condition);
    <T> List<Object[]> getCountGroupBy(Class<T> typeValue, String countField, String groupField);
    <T> List<Object[]> getCountGroupBy(Class<T> typeValue, String countField, String groupField, String conditionOrSortField);
    <T> List<Object[]> getCountGroupBy(Class<T> typeValue, String countField, String groupField, String condition, String sortField);

    <T> long getSum(Class<T> typeValue, String sumField);
    <T> long getSum(Class<T> typeValue, String sumField, String condition);
    <T> List<Object[]> getSumGroupBy(Class<T> typeValue, String sumField, String groupField);
    <T> List<Object[]> getSumGroupBy(Class<T> typeValue, String sumField, String groupField, String conditionOrSortField);
    <T> List<Object[]> getSumGroupBy(Class<T> typeValue, String sumField, String groupField, String condition, String sortField);

    <T> List<T> getList(Class<T> typeValue);
    <T> List<T> getList(Class<T> typeValue, String conditionOrSortField);
    <T> List<T> getList(Class<T> typeValue, String condition, String sortField);
    <T> List<T> getList(Class<T> typeValue, int page, int size);
    <T> List<T> getList(Class<T> typeValue, int page, int size, String conditionOrSortField);
    <T> List<T> getList(Class<T> typeValue, int page, int size, String condition, String sortField);

    <T> List<Object[]> getFields(Class<T> typeValue, String fields);
    <T> List<Object[]> getFields(Class<T> typeValue, String fields, String conditionOrSortField);
    <T> List<Object[]> getFields(Class<T> typeValue, String fields, String condition, String sortField);
    <T> List<Object[]> getFields(Class<T> typeValue, String fields, int page, int size);
    <T> List<Object[]> getFields(Class<T> typeValue, String fields, int page, int size, String conditionOrSortField);
    <T> List<Object[]> getFields(Class<T> typeValue, String fields, int page, int size, String condition, String sortField);

    <T> T getDetail(Class<T> typeValue, long dataId);
    <T> T getDetail(Class<T> typeValue, int dataId);
    <T> T getDetail(Class<T> typeValue, String dataId);

    <T> T getFirst(Class<T> typeValue, String conditionOrSortField);
    <T> T getFirst(Class<T> typeValue, String condition, String sortField);

    void saveDetail(Object object);
    <T> void saveDetail(List<T> objects);

    <T> void updateDetail(Class<T> typeValue, int dataId, String setString);
    <T> void updateDetail(Class<T> typeValue, long dataId, String setString);
    <T> void updateDetail(Class<T> typeValue, String condition, String setString);

    void deleteDetail(Object object);
    <T> void deleteDetail(Class<T> typeValue, int id);
    <T> void deleteDetail(Class<T> typeValue, long id);
    <T> void deleteDetail(Class<T> typeValue, String condition);

    <T> boolean hasExist(Class<T> typeValue, String condition);

    long count(String hql);
    long sum(String hql);
    List<Object[]> query(String hql);
    List<Object[]> query(String hql, int page, int size);


    void test();
}

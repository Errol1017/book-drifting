package common.CRUD.service;
import common.CRUD.dao.ComDaoImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Errol on 2016/7/16.
 * Generic Implement of Common CRUD Interface
 */
@Service
public class ComServiceImpl implements ComService {

    @Autowired
    private ComDaoImpl comDao;

    @Value("${project.file.path}")
    private String fileBasePath;
    @Override
    public String getFileBathPath() {
        return fileBasePath;
    }

    @Override
    public <T> long getCount(Class<T> typeValue) {
        return comDao.getCount(typeValue);
    }
    @Override
    public <T> long getCount(Class<T> typeValue, String condition) {
        return comDao.getCount(typeValue, condition);
    }

    @Override
    public <T> long getSum(Class<T> typeValue, String sumField) {
        return comDao.getSum(typeValue, sumField);
    }
    @Override
    public <T> long getSum(Class<T> typeValue, String sumField, String condition) {
        return comDao.getSum(typeValue, sumField, condition);
    }

    @Override
    public <T> List<Object[]> getSumGroupBy(Class<T> typeValue, String sumField, String groupField) {
        return comDao.getSumGroupBy(typeValue, sumField, groupField);
    }
    @Override
    public <T> List<Object[]> getSumGroupBy(Class<T> typeValue, String sumField, String groupField, String conditionOrSortField) {
        return comDao.getSumGroupBy(typeValue, sumField, groupField, conditionOrSortField);
    }
    @Override
    public <T> List<Object[]> getSumGroupBy(Class<T> typeValue, String sumField, String groupField, String condition, String sortField) {
        return comDao.getSumGroupBy(typeValue, sumField, groupField, condition, sortField);
    }

    @Override
    public <T> List<Object[]> getCountGroupBy(Class<T> typeValue, String countField, String groupField) {
        return comDao.getCountGroupBy(typeValue, countField, groupField);
    }
    @Override
    public <T> List<Object[]> getCountGroupBy(Class<T> typeValue, String countField, String groupField, String conditionOrSortField) {
        return comDao.getCountGroupBy(typeValue, countField, groupField, conditionOrSortField);
    }
    @Override
    public <T> List<Object[]> getCountGroupBy(Class<T> typeValue, String countField, String groupField, String condition, String sortField) {
        return comDao.getCountGroupBy(typeValue, countField, groupField, condition, sortField);
    }

    @Override
    public <T> List<T> getList(Class<T> typeValue) {
        return comDao.getList(typeValue);
    }
    @Override
    public <T> List<T> getList(Class<T> typeValue, String conditionOrSortField) {
        return comDao.getList(typeValue, conditionOrSortField);
    }
    @Override
    public <T> List<T> getList(Class<T> typeValue, String condition, String sortField) {
        return comDao.getList(typeValue, condition, sortField);
    }
    @Override
    public <T> List<T> getList(Class<T> typeValue, int page, int size) {
        return comDao.getList(typeValue, page, size);
    }
    @Override
    public <T> List<T> getList(Class<T> typeValue, int page, int size, String conditionOrSortField) {
        return comDao.getList(typeValue, page, size, conditionOrSortField);
    }
    @Override
    public <T> List<T> getList(Class<T> typeValue, int page, int size, String condition, String sortField) {
        return comDao.getList(typeValue, page, size, condition, sortField);
    }

    @Override
    public <T> List<Object[]> getFields(Class<T> typeValue, String fields) {
        return comDao.getFields(typeValue, fields);
    }
    @Override
    public <T> List<Object[]> getFields(Class<T> typeValue, String fields, String conditionOrSortField) {
        return comDao.getFields(typeValue, fields, conditionOrSortField);
    }
    @Override
    public <T> List<Object[]> getFields(Class<T> typeValue, String fields, String condition, String sortField) {
        return comDao.getFields(typeValue, fields, condition, sortField);
    }
    @Override
    public <T> List<Object[]> getFields(Class<T> typeValue, String fields, int page, int size) {
        return comDao.getFields(typeValue, fields, page, size);
    }
    @Override
    public <T> List<Object[]> getFields(Class<T> typeValue, String fields, int page, int size, String conditionOrSortField) {
        return comDao.getFields(typeValue, fields, page, size, conditionOrSortField);
    }
    @Override
    public <T> List<Object[]> getFields(Class<T> typeValue, String fields, int page, int size, String condition, String sortField) {
        return comDao.getFields(typeValue, fields, page, size, condition, sortField);
    }

    @Override
    public <T> T getDetail(Class<T> typeValue, long dataId) {
        return comDao.getDetail(typeValue, dataId);
    }
    @Override
    public <T> T getDetail(Class<T> typeValue, int dataId) {
        return comDao.getDetail(typeValue, dataId);
    }
    @Override
    public <T> T getDetail(Class<T> typeValue, String dataId) {
        return comDao.getDetail(typeValue, dataId);
    }

    @Override
    public <T> T getFirst(Class<T> typeValue, String conditionOrSortField) {
        return comDao.getFirst(typeValue, conditionOrSortField);
    }
    @Override
    public <T> T getFirst(Class<T> typeValue, String condition, String sortField) {
        return comDao.getFirst(typeValue, condition, sortField);
    }

    @Override
    public void saveDetail(Object object) {
        comDao.saveDetail(object);
    }
    @Override
    public <T> void saveDetail(List<T> objects) {
        comDao.saveDetail(objects);
    }

    @Override
    public <T> void updateDetail(Class<T> typeValue, int dataId, String setString) {
        comDao.updateDetail(typeValue, dataId, setString);
    }
    @Override
    public <T> void updateDetail(Class<T> typeValue, long dataId, String setString) {
        comDao.updateDetail(typeValue, dataId, setString);
    }
    @Override
    public <T> void updateDetail(Class<T> typeValue, String condition, String setString) {
        comDao.updateDetail(typeValue, condition, setString);
    }

    @Override
    public void deleteDetail(Object object) {
        comDao.deleteDetail(object);
    }

    @Override
    public <T> void deleteDetail(Class<T> typeValue, String condition) {
        comDao.deleteDetail(typeValue, condition);
    }

    @Override
    public <T> boolean hasExist(Class<T> typeValue, String condition) {
        return comDao.hasExist(typeValue, condition);
    }

    @Override
    public List<Object[]> query(String hql, int page, int size) {
        return comDao.query(hql, page, size);
    }

}

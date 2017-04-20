package common.CRUD.dao;

import common.CRUD.orm.BaseDao;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Created by Errol on 2016/7/16.
 * Generic DAO Implement for Common CRUD Operation
 */
@Repository
public class ComDaoImpl extends BaseDao {

    /**
     * matches  he_he asc    count(heh) desc    he asc,sum(hah) desc
     */
    private String patten = "^(\\w+\\(?\\w+\\)?\\s(asc|desc|ASC|DESC),?)+$";

    public <T> long getCount(Class<T> typeValue) {
        Query query = getSessionFactory().getCurrentSession().createQuery("select count(*) from " + typeValue.getSimpleName());
        Object res = query.uniqueResult();
        return res == null ? 0 : ((Long) res).longValue();
    }
    public <T> long getCount(Class<T> typeValue, String condition) {
        Query query = getSessionFactory().getCurrentSession().createQuery("select count(*) from " + typeValue.getSimpleName() + " where " + condition);
        Object res = query.uniqueResult();
        return res == null ? 0 : ((Long) res).longValue();
    }

    public <T> List<Object[]> getCountGroupBy(Class<T> typeValue, String countField, String groupField) {
        Query query = getSessionFactory().getCurrentSession().createQuery("select " + groupField + ", count(" + countField + ") from " + typeValue.getSimpleName() + " group by " + groupField);
        return query.list();
    }
    public <T> List<Object[]> getCountGroupBy(Class<T> typeValue, String countField, String groupField, String conditionOrSortField) {
        String con = "";
        String ord = "";
        if (conditionOrSortField.matches(patten)) {
            ord = " order by " + conditionOrSortField;
        } else {
            con = " where " + conditionOrSortField;
        }
        Query query = getSessionFactory().getCurrentSession().createQuery("select " + groupField + ", count(" + countField + ") from " + typeValue.getSimpleName() + con + " group by " + groupField + ord);
        return query.list();
    }
    public <T> List<Object[]> getCountGroupBy(Class<T> typeValue, String countField, String groupField, String condition, String sortField) {
        Query query = getSessionFactory().getCurrentSession().createQuery("select " + groupField + ", count(" + countField + ") from " + typeValue.getSimpleName() + " where " + condition + " group by " + groupField + " order by " + sortField);
        return query.list();
    }

    public <T> long getSum(Class<T> typeValue, String sumField) {
        Query query = getSessionFactory().getCurrentSession().createQuery("select sum(" + sumField + ") from " + typeValue.getSimpleName());
        Object res = query.uniqueResult();
        return res == null ? 0 : ((Long) res).longValue();
    }
    public <T> long getSum(Class<T> typeValue, String sumField, String condition) {
        Query query = getSessionFactory().getCurrentSession().createQuery("select sum(" + sumField + ") from " + typeValue.getSimpleName() + " where " + condition);
        Object res = query.uniqueResult();
        return res == null ? 0 : ((Long) res).longValue();
    }

    public <T> List<Object[]> getSumGroupBy(Class<T> typeValue, String sumField, String groupField) {
        StringBuffer sum = new StringBuffer();
        String[] arr = sumField.split(",");
        for (int i=0; i<arr.length; i++) {
            sum.append(",sum(" + arr[i] + ")");
        }
        Query query = getSessionFactory().getCurrentSession().createQuery("select " + groupField + sum.toString() + " from " + typeValue.getSimpleName() + " group by " + groupField);
        return query.list();
    }
    public <T> List<Object[]> getSumGroupBy(Class<T> typeValue, String sumField, String groupField, String conditionOrSortField) {
        String con = "";
        String ord = "";
        if (conditionOrSortField.matches(patten)) {
            ord = " order by " + conditionOrSortField;
        } else {
            con = " where " + conditionOrSortField;
        }
        StringBuffer sum = new StringBuffer();
        String[] arr = sumField.split(",");
        for (int i=0; i<arr.length; i++) {
            sum.append(",sum(" + arr[i] + ")");
        }
        Query query = getSessionFactory().getCurrentSession().createQuery("select " + groupField + sum.toString() + " from " + typeValue.getSimpleName() + con + " group by " + groupField + ord);
        return query.list();
    }
    public <T> List<Object[]> getSumGroupBy(Class<T> typeValue, String sumField, String groupField, String condition, String sortField) {
        StringBuffer sum = new StringBuffer();
        String[] arr = sumField.split(",");
        for (int i=0; i<arr.length; i++) {
            sum.append(",sum(" + arr[i] + ")");
        }
        Query query = getSessionFactory().getCurrentSession().createQuery("select " + groupField + sum.toString() + " from " + typeValue.getSimpleName() + " where " + condition + " group by " + groupField + " order by " + sortField);
        return query.list();
    }

    public <T> List<T> getList(Class<T> typeValue) {
        return (List<T>) this.getHibernateTemplate().find("from " + typeValue.getSimpleName());
    }
    public <T> List<T> getList(Class<T> typeValue, String conditionOrSortField) {
        String con;
        if (conditionOrSortField.matches(patten)) {
            con = " order by " + conditionOrSortField;
        } else {
            con = " where " + conditionOrSortField;
        }
        List<T>  list = (List<T>) this.getHibernateTemplate().find("from " + typeValue.getSimpleName() + con);
        return list;
    }
    public <T> List<T> getList(Class<T> typeValue, String condition, String sortField) {
        return (List<T>) this.getHibernateTemplate().find("from " + typeValue.getSimpleName() + " where " + condition + " order by " + sortField);
    }
    public <T> List<T> getList(Class<T> typeValue, int page, int size) {
        int min = Math.max(page * size - size, 0);
        Query query = getSessionFactory().getCurrentSession().createQuery("from " + typeValue.getSimpleName());
        query.setFirstResult(min);
        query.setMaxResults(size);
        return query.list();
    }
    public <T> List<T> getList(Class<T> typeValue, int page, int size, String conditionOrSortField) {
        String con = "";
        String ord = "";
        if (conditionOrSortField.matches(patten)) {
            ord = " order by " + conditionOrSortField;
        } else {
            con = " where " + conditionOrSortField;
        }
        int min = Math.max(page * size - size, 0);
        Query query = getSessionFactory().getCurrentSession().createQuery("from " + typeValue.getSimpleName() + con + ord);
        query.setFirstResult(min);
        query.setMaxResults(size);
        return query.list();
    }
    public <T> List<T> getList(Class<T> typeValue, int page, int size, String condition, String sortField) {
        int min = Math.max(page * size - size, 0);
        Query query = getSessionFactory().getCurrentSession().createQuery("from " + typeValue.getSimpleName() + " where " + condition + " order by " + sortField);
        query.setFirstResult(min);
        query.setMaxResults(size);
        return query.list();
    }

    public <T> List<Object[]> getFields(Class<T> typeValue, String fields) {
        Query query = getSessionFactory().getCurrentSession().createQuery("select " + fields + " from " + typeValue.getSimpleName());
        return query.list();
    }
    public <T> List<Object[]> getFields(Class<T> typeValue, String fields, String conditionOrSortField) {
        String con = "";
        String ord = "";
        if (conditionOrSortField.matches(patten)) {
            ord = " order by " + conditionOrSortField;
        } else {
            con = " where " + conditionOrSortField;
        }
        Query query = getSessionFactory().getCurrentSession().createQuery("select " + fields + " from " + typeValue.getSimpleName() + con + ord);
        return query.list();
    }
    public <T> List<Object[]> getFields(Class<T> typeValue, String fields, String condition, String sortField) {
        Query query = getSessionFactory().getCurrentSession().createQuery("select " + fields + " from " + typeValue.getSimpleName() + " where " + condition + " order by " + sortField);
        return query.list();
    }
    public <T> List<Object[]> getFields(Class<T> typeValue, String fields, int page, int size) {
        int min = Math.max(page * size - size, 0);
        Query query = getSessionFactory().getCurrentSession().createQuery("select " + fields + " from " + typeValue.getSimpleName());
        query.setFirstResult(min);
        query.setMaxResults(size);
        return query.list();
    }
    public <T> List<Object[]> getFields(Class<T> typeValue, String fields, int page, int size, String conditionOrSortField) {
        int min = Math.max(page * size - size, 0);
        String con = "";
        String ord = "";
        if (conditionOrSortField.matches(patten)) {
            ord = " order by " + conditionOrSortField;
        } else {
            con = " where " + conditionOrSortField;
        }
        Query query = getSessionFactory().getCurrentSession().createQuery("select " + fields + " from " + typeValue.getSimpleName() + con + ord);
        query.setFirstResult(min);
        query.setMaxResults(size);
        return query.list();
    }
    public <T> List<Object[]> getFields(Class<T> typeValue, String fields, int page, int size, String condition, String sortField) {
        int min = Math.max(page * size - size, 0);
        Query query = getSessionFactory().getCurrentSession().createQuery("select " + fields + " from " + typeValue.getSimpleName() + " where " + condition + " order by " + sortField);
        query.setFirstResult(min);
        query.setMaxResults(size);
        return query.list();
    }

    public <T> T getDetail(Class<T> typeValue, long dataId) {
        return this.getHibernateTemplate().get(typeValue, dataId);
    }
    public <T> T getDetail(Class<T> typeValue, int dataId) {
        return this.getHibernateTemplate().get(typeValue, dataId);
    }
    public <T> T getDetail(Class<T> typeValue, String dataId) {
        return this.getHibernateTemplate().get(typeValue, dataId);
    }

    public <T> T getFirst(Class<T> typeValue, String conditionOrSortField) {
        String con;
        if (conditionOrSortField.matches(patten)) {
            con = " order by " + conditionOrSortField;
        } else {
            con = " where " + conditionOrSortField;
        }
        Query query = getSessionFactory().getCurrentSession().createQuery("from " + typeValue.getSimpleName() + con);
        query.setFirstResult(0);
        query.setMaxResults(1);
        List<T> list = query.list();
        if (list.size() == 1) {
            return list.get(0);
        } else {
            return null;
        }
    }
    public <T> T getFirst(Class<T> typeValue, String condition, String sortField) {
        Query query = getSessionFactory().getCurrentSession().createQuery("from " + typeValue.getSimpleName() + " where " + condition + " order by " + sortField);
        query.setFirstResult(0);
        query.setMaxResults(1);
        List<T> list = query.list();
        if (list.size() == 1) {
            return list.get(0);
        } else {
            return null;
        }
    }

    public void saveDetail(Object object) {
        this.getHibernateTemplate().saveOrUpdate(object);
    }

    /**
     * getCurrentSession().save() 自动提交数据 ？ 打印 sql 语句， 但是数据表查不到值
     * getCurrentSession() 获取当前线程 session ，会自动 commit
     * openSession() 创建独立的新的 session ，需要手动 commit 和 close ，但以下代码依然会自动 commit
     */
    public <T> void saveDetail(List<T> objects) {
        Session session = getSessionFactory().getCurrentSession();
//        Transaction transaction = session.beginTransaction();
//        int i = 0;
        for (T object: objects) {
            session.save(object);
//            i++;
//            if (i%10 == 0) {
//                session.flush();
//                session.clear();
//            }
        }
//        transaction.commit();
//        session.close();
    }
//    public void insertDetail(Object object) {
//        this.getHibernateTemplate().save(object);
//    }
//    public void updateDetail(Object object) {
//        this.getHibernateTemplate().update(object);
//    }

    public <T> void updateDetail(Class<T> typeValue, int dataId, String setString) {
        Query query = getSessionFactory().getCurrentSession().createQuery("update " + typeValue.getSimpleName() + " set " + setString + " where id=" + dataId);
        query.executeUpdate();
    }
    public <T> void updateDetail(Class<T> typeValue, long dataId, String setString) {
        Query query = getSessionFactory().getCurrentSession().createQuery("update " + typeValue.getSimpleName() + " set " + setString + " where id=" + dataId);
        query.executeUpdate();
    }
    public <T> void updateDetail(Class<T> typeValue, String condition, String setString) {
        Query query = getSessionFactory().getCurrentSession().createQuery("update " + typeValue.getSimpleName() + " set " + setString + " where " + condition);
        query.executeUpdate();
    }

    public void deleteDetail(Object object) {
        this.getHibernateTemplate().delete(object);
    }
    public <T> void deleteDetail(Class<T> typeValue, String condition) {
        Query query = getSessionFactory().getCurrentSession().createQuery("delete from " + typeValue.getSimpleName() + " where " + condition);
        query.executeUpdate();

    }

    public <T> boolean hasExist(Class<T> typeValue, String condition) {
        Query query = getSessionFactory().getCurrentSession().createQuery("from " + typeValue.getSimpleName() + " where " + condition);
        query.setFirstResult(0);
        query.setMaxResults(1);
        List<T> list = query.list();
        if (list.size() == 1) {
            return true;
        } else {
            return false;
        }
    }

}

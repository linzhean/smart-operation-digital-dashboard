package tw.edu.ntub.imd.birc.sodd.scheduler;

import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.dao.BaseDAO;

import javax.persistence.Column;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DataSyncUtils<E> {
    private ApplicationContext applicationContext;
    private JdbcTemplate jdbcTemplate;
    private final Class<E> entityClass;

    public DataSyncUtils(Class<E> entityClass) {
        this.entityClass = entityClass;
    }

    public void syncAllData() {
        String tableName = entityClass.getSimpleName();
        BaseDAO<E, Integer> dao = (BaseDAO<E, Integer>) getDAOForEntityClass(entityClass);
        dao.deleteAll();
        List<E> entityList = getEntityList(tableName);
        dao.saveAll(entityList);
    }

    public List<E> getEntityList(String tableName) {
        String sql = "SELECT * FROM " + tableName;
        try {
            return mapToEntityList(jdbcTemplate.queryForList(sql), entityClass);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public <E> List<E> mapToEntityList(List<Map<String, Object>> rows, Class<E> clazz) throws Exception {
        List<E> entities = new ArrayList<>();
        for (Map<String, Object> row : rows) {
            entities.add(mapToEntity(row, clazz));
        }
        return entities;
    }

    // 將 Map<String, Object> 轉換成實體類
    public static <E> E mapToEntity(Map<String, Object> row, Class<E> clazz) throws Exception {
        E instance = clazz.getDeclaredConstructor().newInstance(); // 創建實體對象
        for (Map.Entry<String, Object> entry : row.entrySet()) {
            String columnName = entry.getKey(); // 資料庫欄位名稱
            Object value = entry.getValue();    // 欄位對應的值

            for (Field field : clazz.getDeclaredFields()) {
                if (field.isAnnotationPresent(Column.class)) {
                    Column column = field.getAnnotation(Column.class);
                    String columnAnnotationName = column.name();

                    // 檢查是否存在匹配的 @Column(name) 註解
                    if (columnAnnotationName.equalsIgnoreCase(columnName)) {
                        field.setAccessible(true); // 設置為可訪問
                        field.set(instance, value); // 將值設置到實體屬性中
                        break; // 找到匹配的屬性後，跳出循環
                    }
                }
            }
        }
        return instance;
    }

    public BaseDAO getDAOForEntityClass(Class<E> entityClass) {
        String daoBeanName = entityClass.getSimpleName() + "DAO";
        if (applicationContext.containsBean(daoBeanName)) {
            return (BaseDAO) applicationContext.getBean(daoBeanName);
        } else {
            throw new IllegalArgumentException("No DAO found for entity class: " + entityClass.getSimpleName());
        }
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }
}


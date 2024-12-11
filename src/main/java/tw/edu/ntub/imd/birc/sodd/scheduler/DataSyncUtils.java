package tw.edu.ntub.imd.birc.sodd.scheduler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
    private final Logger logger = LogManager.getLogger(DataSyncUtils.class);


    public DataSyncUtils(Class<E> entityClass) {
        this.entityClass = entityClass;
    }

    public void syncAllData() {
        String tableName = entityClass.getSimpleName();
        BaseDAO<E, Integer> dao = (BaseDAO<E, Integer>) getDAOForEntityClass(entityClass);
        if (dao == null) {
            logger.error("DAO is null for entity class: {}", entityClass.getSimpleName());
            return;
        }

        List<E> results = dao.findAll();
        results.forEach(e -> logger.info(entityToString(e)));
        dao.deleteAll();

        List<E> entityList = getEntityList(tableName);
        dao.saveAll(entityList);
    }

    private String entityToString(E entity) {
        StringBuilder sb = new StringBuilder();
        sb.append(entity.getClass().getSimpleName()).append(" {");
        Field[] fields = entity.getClass().getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true); // 確保可以訪問 private 屬性
            try {
                Object value = field.get(entity);
                sb.append(field.getName()).append("=").append(value).append(", ");
            } catch (IllegalAccessException ex) {
                logger.error("Failed to access field: {}", field.getName(), ex);
            }
        }

        if (fields.length > 0) {
            sb.setLength(sb.length() - 2); // 移除最後的 ", "
        }
        sb.append("}");
        return sb.toString();
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
            throw new IllegalArgumentException("查無此DAO: " + entityClass.getSimpleName());
        }
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }
}


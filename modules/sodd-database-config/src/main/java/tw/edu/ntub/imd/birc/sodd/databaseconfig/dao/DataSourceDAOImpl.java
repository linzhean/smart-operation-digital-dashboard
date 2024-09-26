package tw.edu.ntub.imd.birc.sodd.databaseconfig.dao;

import org.springframework.stereotype.Repository;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.enumerate.ChartDataSource;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.views.YieldAchievementRate;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@Repository
public class DataSourceDAOImpl implements DataSourceDAO {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Object[]> searchAll(String viewTableName) {
        String sql = "SELECT * FROM " + viewTableName;
        Query query = entityManager.createNativeQuery(sql);
        return query.getResultList();
    }

    @Override
    public String getJsonData(ChartDataSource dataSource) throws Exception {
        List<Object[]> resultList = searchAll(dataSource.getValue());
        return convertToJson(resultList, ChartDataSource.getChartClass(dataSource));
    }

    public String convertToJson(List<Object[]> data, Class<?> entityClass) {
        Field[] fields = entityClass.getDeclaredFields();
        List<String> fieldNames = new ArrayList<>();

        for (Field field : fields) {
            fieldNames.add(field.getName());
        }

        fieldNames = List.of(fieldNames.toArray(new String[0]));

        StringBuilder jsonBuilder = new StringBuilder();
        jsonBuilder.append("{");

        // 對每個欄位進行遍歷
        for (int i = 0; i < fieldNames.size(); i++) {
            String fieldName = fieldNames.get(i);
            jsonBuilder.append("\"").append(fieldName).append("\": [");

            // 將每個 Object[] 的對應欄位數據加進 JSON
            for (int j = 0; j < data.size(); j++) {
                jsonBuilder.append("\"").append(data.get(j)[i]).append("\"");

                if (j < data.size() - 1) {
                    jsonBuilder.append(", ");  // 每個元素之間加逗號
                }
            }

            jsonBuilder.append("]");

            if (i < fieldNames.size() - 1) {
                jsonBuilder.append(", ");  // 每個欄位之間加逗號
            }
        }

        jsonBuilder.append("}");
        return jsonBuilder.toString();
    }
}

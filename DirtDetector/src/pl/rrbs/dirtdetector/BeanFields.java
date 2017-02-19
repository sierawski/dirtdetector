package pl.rrbs.dirtdetector;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import edu.emory.mathcs.backport.java.util.Collections;

/**
 * Helper class storing fields of given object.
 * Current implementation doesn't handle inheritance and only takes into consideration the highest class in hierarchy.
 */
class BeanFields {
    private final Map<String, Object> fields;

    public BeanFields(Object bean) throws IllegalAccessException {
        fields = new HashMap<>();
        if (bean == null) {
            return;
        }
        for (Field field : bean.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            fields.put(field.getName(), field.get(bean));
        }
    }

    /**
     *
     * @param beforeBean
     * @param afterBean
     * @return Field names that have changed values
     *
     * @throws IllegalArgumentException whenever given beans have different amount of fields
     */
    public static List<String> getDifference(BeanFields beforeBean, BeanFields afterBean) {
        List<String> lazyReturn = null;
        if (beforeBean.fields.size() != afterBean.fields.size()) {
            throw new IllegalArgumentException("Beans are of different types");
        }
        for (String key : beforeBean.fields.keySet()) {
            Object beforeField = beforeBean.fields.get(key);
            Object afterField = afterBean.fields.get(key);
            if (!Objects.equals(beforeField, afterField)) {
                lazyReturn = lazyAdd(lazyReturn, key);
            }
        }
        return lazyReturn == null ? Collections.emptyList() : lazyReturn;
    }

    private static List<String> lazyAdd(List<String> collection, String value) {
        if (collection == null) {
            collection = new ArrayList<>();
        }
        collection.add(value);
        return collection;
    }
}

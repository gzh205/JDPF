package Cache;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class TableData {
    Class<?> data;
    Field primarykey;
    List<Object> records;

    //传入对应表的class
    TableData(Class<?> data) {
        this.records = new ArrayList<Object>();
        for (Field f : data.getDeclaredFields()) {
            if (f.getDeclaredAnnotation(Table.PrimaryKey.class) != null) {
                primarykey = f;
            }
        }
        this.data = data;
    }

    public void add(Object t) {
        if (t.getClass().equals(data))
            this.records.add(t);
    }

    public <T> Object getFromPk(T input) {
        for (Object t : records) {
            try {
                if (primarykey.get(t).equals(input)) {
                    return t;
                }
            } catch (IllegalAccessException i) {
                i.printStackTrace();
            }
        }
        return null;
    }

    public <T> void delete(T pk){
        for(Object t:records){
            try {
                if (primarykey.get(t).equals(pk)) {
                    records.remove(t);
                }
            } catch (IllegalAccessException i) {
                i.printStackTrace();
            }
        }
    }
}

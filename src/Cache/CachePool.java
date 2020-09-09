package Cache;

import java.util.ArrayList;
import java.util.List;

public class CachePool {
    List<TableData> tables;

    public CachePool() {
        tables = new ArrayList<>();
    }

    public <T> Object getFromPk(Class<?> clazz, T primarykey) {
        for (TableData table : tables) {
            if (table.data.equals(clazz)) {
                return table.getFromPk(primarykey);
            }
        }
        return null;
    }

    public void addElement(Object obj) {
        for (TableData dat : tables) {
            if (dat.data.equals(obj)) {
                dat.add(obj);
            }
        }
        TableData d = new TableData(obj.getClass());
        tables.add(d);
    }

    public <T> void addSomeElements(List<T> obj) {
        for (TableData dat : tables) {
            if (dat.data.equals(obj.getClass())) {
                for (Object o : obj) {
                    dat.add(o);
                }
            }
        }
        TableData d = new TableData(obj.getClass());
        for (Object o : obj) {
            d.add(o);
        }
    }

    public <T> void deleteElement(Class<?> clazz,T primarykey){
        for (TableData table : tables) {
            if (table.data.equals(clazz)) {
                table.delete(primarykey);
            }
        }
    }
}

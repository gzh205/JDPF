package Cache;

import java.util.ArrayList;
import java.util.List;

public class CachePool {
    List<TableData> tables;

    private CachePool() {
        tables = new ArrayList<>();
    }

    public <T> Object getFromPk(Class<T> clazz, T primarykey){
        for (TableData table : tables) {
            if (table.data.equals(clazz)) {
                return table.getFromPk(primarykey);
            }
        }
        return null;
    }

    public void addElement(Object obj){
        for(TableData dat:tables){
            if(dat.data.equals(obj.getClass())){
                dat.add(obj);
            }

        }
    }
}

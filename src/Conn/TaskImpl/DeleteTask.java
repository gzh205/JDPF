package Conn.TaskImpl;

import Conn.Task;
import com.sun.media.jfxmedia.logging.Logger;

import java.lang.reflect.Field;
import java.sql.Date;
import java.sql.SQLException;

public class DeleteTask extends Task {
    @Override
    public void run() {
        String sql = "delete from ";
        String whereData = "";
        Field[] fields = table.getClass().getDeclaredFields();
        Field pk = null;
        for (Field f : fields) {
            if (f.getAnnotation(Table.PrimaryKey.class) != null) {
                pk = f;
                try {
                    if (f.getType() == String.class || f.getType() == Date.class) {
                        whereData += " and " + f.getName() + " = '" + f.get(table) + "'";
                    } else {
                        whereData += " and " + f.getName() + " = " + f.get(table);
                    }
                } catch (IllegalArgumentException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        sql = sql + table.getClass().getName() + " where " + whereData.substring(5);
        this.connect();
        boolean res = false;
        try {
            res = this.connection.createStatement().execute(sql);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        this.close();
        if(res){
            try {
                pool.deleteElement(table.getClass(),pk.get(table));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        else{
            Logger.logMsg(Logger.WARNING,"数据删除失败");
        }
    }
}

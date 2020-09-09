package Conn.TaskImpl;

import Conn.Task;
import com.sun.media.jfxmedia.logging.Logger;

import java.lang.reflect.Field;
import java.sql.Date;
import java.sql.SQLException;

public class UpdateTask extends Task {
    @Override
    public void run() {
        String sql = "update ";
        String setData = "";
        String whereData = "";
        Field[] fields = table.getClass().getDeclaredFields();
        for (Field f : fields) {
            try {
                if (f.getAnnotation(Table.PrimaryKey.class) != null) {
                    if (f.getType() == String.class || f.getType() == Date.class)
                        whereData += " and " + f.getName() + " = '" + f.get(table) + "'";
                    else
                        whereData += " and " + f.getName() + " = " + f.get(table);
                } else {

                    if (f.getType() == String.class || f.getType() == Date.class)
                        setData += "," + f.getName() + " = '" + f.get(table) + "'";
                    else
                        setData += "," + f.getName() + " = " + f.get(table);
                }
            } catch (IllegalArgumentException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        sql = sql + table.getClass().getName() + " set " + setData.substring(1) + " where " + whereData.substring(5);
        this.connect();
        boolean res = false;
        try {
            res = this.connection.createStatement().execute(sql);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        this.close();
        if (res) {
            this.pool.addElement(table);
        } else {
            Logger.logMsg(Logger.WARNING, "数据修改失败");
        }
    }
}

package Conn.TaskImpl;

import Conn.Task;
import com.sun.media.jfxmedia.logging.Logger;
import java.lang.reflect.Field;
import java.sql.Date;
import java.sql.SQLException;

public class InsertTask extends Task {
    @Override
    public void run() {
        String sql = "insert into ";
        String data = "";
        String valuesData = "";
        Field[] fields = table.getClass().getDeclaredFields();
        for (Field f : fields) {
            data += "," + f.getName();
            try {
                if (f.getType() == String.class || f.getType() == Date.class)
                    valuesData += ",'" + f.get(table) + "'";
                else
                    valuesData += "," + f.get(table);
            } catch (IllegalArgumentException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        sql = sql + table.getClass().getName() + "(" + data.substring(1) + ") values(" + valuesData.substring(1) + ")";
        this.connect();
        boolean res = false;
        try {
            res = this.connection.createStatement().execute(sql);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        this.close();
        if(res){
            this.pool.addElement(table);
        }
        else{
            Logger.logMsg(Logger.WARNING,"数据插入失败");
        }
    }
}

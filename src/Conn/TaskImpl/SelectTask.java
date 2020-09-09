package Conn.TaskImpl;

import Conn.Task;
import Core.Tools;

import java.lang.reflect.Field;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SelectTask extends Task {
    @Override
    public void run() {
        Class<?> c = table.getClass();
        String sql = "select ";
        String selectDat = "";
        String whereDat = "";
        Field[] fields = c.getDeclaredFields();
        for (Field f : fields) {
            selectDat += "," + f.getName();
            if (f.getAnnotation(Table.PrimaryKey.class) != null) {
                Object poolResult = null;
                try {
                    poolResult = pool.getFromPk(c,f.get(table));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                if(poolResult!=null) {
                    this.table = Tools.copy(poolResult);
                    return;
                }else{
                    try {
                        if (f.getType() == String.class || f.getType() == Date.class)
                            whereDat += " " + f.getName() + "='" + f.get(table) + "' ";
                        else
                            whereDat += " " + f.getName() + "=" + f.get(table) + " ";
                    } catch (IllegalArgumentException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }
        sql += selectDat.substring(1) + " from " + table.getClass().getName() + " where " + whereDat;
        this.connect();
        ResultSet result = null;
        try {
            result = this.connection.createStatement().executeQuery(sql);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        if (result == null) {
            table = null;
            return;
        }
        else {
            try {
                result.last();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            for (int i = 0; i < fields.length; i++) {
                try {
                    fields[i].set(table, Tools.getResult(result, i + 1, fields[i].getType()));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        this.close();
        pool.addElement(table);
    }
}

package Main;

import Cache.CachePool;
import Conn.SqlTasks;
import Conn.Task;
import Conn.TaskImpl.DeleteTask;
import Conn.TaskImpl.InsertTask;
import Conn.TaskImpl.SelectTask;
import Conn.TaskImpl.UpdateTask;
import Core.HostInfo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SqlConnector {

    static HashMap<String,HostInfo> hosts = new HashMap<>();
    static HashMap<String, CachePool> pools = new HashMap<>();

    /*
    * 注册数据库连接
    * 注：一个包对应一个数据库，包下面的类则对应数据库中的表，包名和类名必须与数据库中的数据库名和表名一一对应
    */
    public static void regConnection(String packagename,HostInfo hostname){
        hosts.put(packagename,hostname);
        pools.put(packagename,new CachePool());
    }

    public static String getPackageName(Object table){
        return table.getClass().getPackage().getName();
    }

    public static String getPackageName(Class<?> tablename){
        return tablename.getPackage().getName();
    }

    public static void Start(int threadnum){
        SqlTasks.startThreads(threadnum);
    }

    private static void call(Task task,Object table){
        String packagename = getPackageName(table);
        task.setHost(hosts.get(packagename));
        task.setPool(pools.get(packagename));
        task.setTable(table);
        try {
            SqlTasks.get().add(task);
            task.waiting.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void select(Object table){
        SelectTask task = new SelectTask();
        call(task,table);
    }

    public static void insert(Object table){
        InsertTask task = new InsertTask();
        call(task,table);
    }

    public static void delete(Object table){
        DeleteTask task = new DeleteTask();
        call(task,table);
    }

    public static void update(Object table){
        UpdateTask task = new UpdateTask();
        call(task,table);
    }

    /*
     * 数据库修改操作，如果只是对表进行增加、删除、修改的操做，请使用
     * insert、delete、update
     * 否则缓存不会随之更新
    */
    public static boolean execute(String sql,HostInfo info){
        Connection connection = null;
        boolean result = false;
        try {
            connection = DriverManager.getConnection(info.host, info.username, info.password);
            result = connection.createStatement().execute(sql);
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return result;
    }

    public static List<List<Object>> query(String sql,HostInfo info){
        Connection connection = null;
        ResultSet result = null;
        List<List<Object>> output = new ArrayList<>();
        try {
            connection = DriverManager.getConnection(info.host, info.username, info.password);
            result = connection.createStatement().executeQuery(sql);
            if(result==null)
                return null;
            while (result.next()) {
                List<Object> row = new ArrayList<>();
                for(int i=0;i<result.getFetchSize();i++) {
                    row.add(result.getObject(i));
                }
                output.add(row);
            }
            connection.close();
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return output;
    }
}

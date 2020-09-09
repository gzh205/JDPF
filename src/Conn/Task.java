package Conn;

import Cache.CachePool;
import Core.HostInfo;
import Exceptions.HostInfoErrException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.Semaphore;

public abstract class Task {
    public Object table;//待操作的表
    protected Connection connection;//连接对象
    public HostInfo info;//数据库服务器的名称和登录账号密码
    protected CachePool pool;//缓冲区
    public String sql;//sql语句
    public Object result;//查询结果
    public Semaphore waiting = new Semaphore(0);//等待任务完成

    public abstract void run();

    public void setHost(HostInfo info) {
        if (!info.valid()) throw new HostInfoErrException();
        this.info = info;
    }

    public void setTable(Object table) {
        this.table = table;
    }

    public void setPool(CachePool pool) {
        this.pool = pool;
    }

    protected void connect() {
        try {
            this.connection = DriverManager.getConnection(info.host, info.username, info.password);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    protected void close() {
        try {
            if (!this.connection.isClosed())
                this.connection.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}

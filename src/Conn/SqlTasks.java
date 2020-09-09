package Conn;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;

//线程池
public class SqlTasks {
    private static SqlTasks inst;
    private Queue<Task> queue;
    private Semaphore read;//同时只能一个线程访问队列
    private Semaphore num;//队列的长度
    public static boolean running = true;//若该值为false，则线程池停止运行
    private static TaskThread[] tasks;

    public static void startThreads(int ThreadNum){//启动线程
        tasks = new TaskThread[ThreadNum];
        for(int i=0;i<ThreadNum;i++){
            tasks[i] = new TaskThread();
            tasks[i].run();
        }
    }

    public static SqlTasks get(){//获取该类唯一实例
        if(SqlTasks.inst==null)
            SqlTasks.inst = new SqlTasks();
        return SqlTasks.inst;
    }

    private SqlTasks() {
        this.queue = new ConcurrentLinkedQueue<>();
        this.read = new Semaphore(1);
        this.num = new Semaphore(0);
    }

    public void add(Task t) throws InterruptedException {//添加新任务
        queue.add(t);
        num.release();
    }

    public void execute() throws InterruptedException {//处理任务
        read.acquire();
        num.acquire();
        Task task = queue.poll();
        read.release();
        task.run();
        task.waiting.release();
    }

}

class TaskThread extends Thread{
    @Override
    public void run(){
        SqlTasks tasks = SqlTasks.get();
        while(SqlTasks.running){
            try {
                tasks.execute();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

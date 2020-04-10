package coding_club_java_course.utility;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class TaskManager {
    public int threadCount;
    private ExecutorService executorService;

    public TaskManager(int threadCount) {
        this.threadCount=threadCount;
        this.executorService=Executors.newFixedThreadPool(threadCount);
    }

    public void waitTillIsFreeAndAddTask(Runnable runnable) {
        while (getQueueSize()>=threadCount) {
            try {
                System.out.println("Sleeping");
                Thread.currentThread().sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        addTask(runnable);
    }

    public void addTask(Runnable runnable) {this.executorService.submit(runnable);}

    private int getQueueSize() {
        ThreadPoolExecutor executor=(ThreadPoolExecutor) (executorService);
        return executor.getQueue().size();
    }
}

package com.wpm.zookeeper.basic;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.util.concurrent.CountDownLatch;

public class SetDataAsyncUsage implements Watcher {
    private static CountDownLatch countDownLatch = new CountDownLatch(1);
    private static ZooKeeper zk;

    public static void main(String[] args) throws Exception{
        String path = "/zk-book-setdataasync";
        zk = new ZooKeeper("127.0.0.1:2181", 5000, new SetDataAsyncUsage());
        countDownLatch.await();

        zk.create(path, "1213".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        zk.setData(path, "456".getBytes(), -1, new IStatCallback(), null);
        Thread.sleep(Integer.MAX_VALUE);
    }

    public void process(WatchedEvent watchedEvent){
        if(Event.KeeperState.SyncConnected == watchedEvent.getState()){
            if(Event.EventType.None == watchedEvent.getType() && null == watchedEvent.getPath()){
                countDownLatch.countDown();
            }
        }
    }
}

class IStatCallback implements AsyncCallback.StatCallback{
    public void processResult(int rc, String path, Object ctx, Stat stat){
        if(rc == 0){
            System.out.println("Success");
        }
    }
}

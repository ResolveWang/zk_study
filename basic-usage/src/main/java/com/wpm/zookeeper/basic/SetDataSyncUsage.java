package com.wpm.zookeeper.basic;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.util.concurrent.CountDownLatch;

public class SetDataSyncUsage implements Watcher {
    private static CountDownLatch countDownLatch = new CountDownLatch(1);
    private static ZooKeeper zk;

    public static void main(String[] args) throws Exception{
        String path = "/zk-bookma";
        zk = new ZooKeeper("127.0.0.1:2181", 5000, new SetDataSyncUsage());
        countDownLatch.await();

        zk.create(path, "1234".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        zk.getData(path, true, null);
        Stat stat = zk.setData(path, "456".getBytes(), -1);
        System.out.println(stat.getCzxid()+","+stat.getMzxid()+","+stat.getVersion());
        Stat stat2 = zk.setData(path, "456".getBytes(), stat.getVersion());
        System.out.println(stat2.getCzxid()+","+stat2.getMzxid()+","+stat2.getVersion());

        try{
            zk.setData(path, "456".getBytes(), stat2.getVersion());
        }catch (KeeperException e){
            System.out.println("Error:"+e.code()+","+e.getMessage());
        }

        Thread.sleep(Integer.MAX_VALUE);
    }

    public void process(WatchedEvent event){
        if(Event.KeeperState.SyncConnected == event.getState()){
            if(Event.EventType.None == event.getType() && null == event.getPath()){
                countDownLatch.countDown();
            }
        }
    }
}

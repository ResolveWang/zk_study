package com.wpm.zookeeper.basic;

import org.apache.zookeeper.*;

import java.util.concurrent.CountDownLatch;

public class ExistsSyncUsage implements Watcher {
    private static CountDownLatch countDownLatch = new CountDownLatch(1);
    private static ZooKeeper zk;

    public static void main(String[] args) throws Exception{
        String path = "/zk-book-existssync";
        zk = new ZooKeeper("127.0.0.1:2181", 5000, new ExistsSyncUsage());
        countDownLatch.await();
        zk.exists(path, true);
        zk.create(path, "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        zk.setData(path, "123".getBytes(), -1);
        zk.create(path+"/c1", "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        zk.delete(path+"/c1", -1);
        zk.delete(path, -1);
        Thread.sleep(Integer.MAX_VALUE);
    }

    public void process(WatchedEvent event){
        try{
            if(Event.KeeperState.SyncConnected == event.getState()){
                if(Event.EventType.None == event.getType() && null == event.getPath()){
                    countDownLatch.countDown();
                }else if(Event.EventType.NodeCreated == event.getType()){
                    System.out.println("Node("+event.getPath()+") created");
                    zk.exists(event.getPath(), true);
                }else if(Event.EventType.NodeDeleted == event.getType()){
                    System.out.println("Node("+event.getPath()+") deleted");
                    zk.exists(event.getPath(), true);
                }else if(Event.EventType.NodeDataChanged == event.getType()){
                    System.out.println("Node("+event.getPath()+") datachanged");
                    zk.exists(event.getPath(), true);
                }
            }
        }catch (Exception e){

        }
    }
}

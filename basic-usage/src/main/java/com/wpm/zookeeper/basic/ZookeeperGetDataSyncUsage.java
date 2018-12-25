package com.wpm.zookeeper.basic;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.util.concurrent.CountDownLatch;

public class ZookeeperGetDataSyncUsage implements Watcher {
    private static CountDownLatch countDownLatch = new CountDownLatch(1);
    private static  ZooKeeper zk = null;
    private static  Stat stat = new Stat();

    public static void main(String[] args) throws Exception{
        String path = "/zk-books";
        zk = new ZooKeeper("127.0.0.1:2181", 5000, new ZookeeperGetDataSyncUsage());
        countDownLatch.await();
        zk.create(path, "123".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);

        System.out.println(new String(zk.getData(path, true, stat)));

        System.out.println(stat.getCzxid()+","+stat.getMzxid()+","+stat.getVersion());

        zk.setData(path, "123".getBytes(), -1);
        Thread.sleep(Integer.MAX_VALUE);
    }

    public void process(WatchedEvent event){
        if(Event.KeeperState.SyncConnected == event.getState()){
            if(Event.EventType.None == event.getType() && null == event.getPath()){
                countDownLatch.countDown();
            }else if(event.getType() == Event.EventType.NodeDataChanged){
                try{
                    System.out.println(new String(zk.getData(event.getPath(), true, stat)));
                    System.out.println(stat.getCzxid()+","+stat.getMzxid()+","+stat.getVersion());
                }catch (Exception e){}
            }
        }
    }

}

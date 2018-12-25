package com.wpm.zookeeper.basic;

import org.apache.zookeeper.*;
import org.apache.zookeeper.Watcher.Event.EventType;

import java.util.concurrent.CountDownLatch;
import java.util.List;

public class ZookeeperGetChildrenSyncUsage implements Watcher {
    private static CountDownLatch countDownLatch = new CountDownLatch(1);
    private static ZooKeeper zk = null;

    public static void main(String[] args) throws Exception{
        String path = "/zk-book";
        zk = new ZooKeeper("127.0.0.1:2181", 5000, new ZookeeperGetChildrenSyncUsage());
        countDownLatch.await();
        zk.create(path, "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        zk.create(path + "/c1", "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        List<String> childrenList = zk.getChildren(path, true);
        System.out.println(childrenList);

        zk.create(path + "/c2", "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        Thread.sleep(Integer.MAX_VALUE);

    }

    public void process(WatchedEvent event){
        if(Event.KeeperState.SyncConnected == event.getState()){
            if(EventType.None == event.getType() && null == event.getPath()){
                countDownLatch.countDown();
            }else if(event.getType() == EventType.NodeChildrenChanged){
                try{
                    System.out.println("ReGet Child:" + zk.getChildren(event.getPath(), true));
                }catch (Exception e){

                }
            }
        }
    }
}

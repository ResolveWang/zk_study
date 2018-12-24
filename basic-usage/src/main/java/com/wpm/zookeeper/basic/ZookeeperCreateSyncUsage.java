package com.wpm.zookeeper.basic;

import org.apache.zookeeper.*;

import java.util.concurrent.CountDownLatch;

public class ZookeeperCreateSyncUsage implements Watcher{
    private  static CountDownLatch connectedSemphore = new CountDownLatch(1);

    public static void main(String[] args) throws Exception{
        ZooKeeper zooKeeper = new ZooKeeper("127.0.0.1:2181", 5000,
                new ZookeeperCreateSyncUsage());
        connectedSemphore.await();
        String path1 = zooKeeper.create("/zk-test-ephemeral-", "".getBytes(),
                ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        System.out.println("Success create znode: " + path1);

        String path2 = zooKeeper.create("/zk-test-ephemeral-", "".getBytes(),
                ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
        System.out.println("Success create znode: " + path2);
    }

    public void process(WatchedEvent event){
        if (Event.KeeperState.SyncConnected == event.getState()){
            connectedSemphore.countDown();
        }
    }
}

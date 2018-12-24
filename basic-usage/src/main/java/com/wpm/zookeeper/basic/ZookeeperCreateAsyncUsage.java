package com.wpm.zookeeper.basic;

import org.apache.zookeeper.*;

import java.util.concurrent.CountDownLatch;

public class ZookeeperCreateAsyncUsage implements Watcher{
    private static CountDownLatch countDownLatch = new CountDownLatch(1);

    public static void main(String[] args) throws Exception{
        ZooKeeper zooKeeper = new ZooKeeper("127.0.0.1:2181", 5000, new ZookeeperCreateAsyncUsage());

        countDownLatch.await();

        zooKeeper.create("/zk-test-ephemeral-", "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.EPHEMERAL, new IStringCallback(), "I am context");
        zooKeeper.create("/zk-test-ephemeral-", "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.EPHEMERAL, new IStringCallback(), "I am context");
        zooKeeper.create("/zk-test-ephemeral-", "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.EPHEMERAL_SEQUENTIAL, new IStringCallback(), "I am context");
        Thread.sleep(Integer.MAX_VALUE);
    }

    public void process(WatchedEvent event){
        if(Watcher.Event.KeeperState.SyncConnected == event.getState()){
            countDownLatch.countDown();
        }
    }
}

class IStringCallback implements AsyncCallback.StringCallback{
    public void processResult(int rc, String path, Object ctx, String name){
        System.out.println("Create path result:[" + rc + ", " +
                path + ", " + ctx + ", real path name: " + name);
    }
}

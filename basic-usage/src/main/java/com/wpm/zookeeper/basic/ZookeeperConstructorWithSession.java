package com.wpm.zookeeper.basic;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.util.concurrent.CountDownLatch;

public class ZookeeperConstructorWithSession implements Watcher{
    private static CountDownLatch connectionSemaphore = new CountDownLatch(1);
    public static void main(String[] args) throws Exception{
        ZooKeeper zooKeeper = new ZooKeeper("127.0.0.1:2181", 5000,
                new ZookeeperConstructorWithSession());
        connectionSemaphore.await();

        long sessionId = zooKeeper.getSessionId();
        byte[] passwd = zooKeeper.getSessionPasswd();

        zooKeeper = new ZooKeeper("127.0.0.1:2181", 5000,
                new ZookeeperConstructorWithSession(), 1, "test".getBytes());

        zooKeeper = new ZooKeeper("127.0.0.1:2181", 5000,
                new ZookeeperConstructorWithSession(), sessionId, passwd);

        Thread.sleep(Integer.MAX_VALUE);
    }

    public void process(WatchedEvent event){
        System.out.println("Receive watched event:" + event);
        if(Event.KeeperState.SyncConnected == event.getState()){
            connectionSemaphore.countDown();
        }
    }

}

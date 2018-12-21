package com.wpm.zookeeper.basic;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;
import org.apache.log4j.PropertyConfigurator;

import java.util.concurrent.CountDownLatch;


public class ZookeeperConstructorUsageSimple implements Watcher{
    private static CountDownLatch connectedSemaphore = new CountDownLatch(1);
    public static void main(String[] args) throws Exception{
        PropertyConfigurator.configure("./basic-usage/config/log4j.properties");
        ZooKeeper zooKeeper = new ZooKeeper("127.0.0.1:2181", 5000,
                new ZookeeperConstructorUsageSimple());

        System.out.println(zooKeeper.getState());
        try{
            connectedSemaphore.await();
        }catch (InterruptedException e){
            System.out.println("zookeeper session established");
        }
    }

    public void process(WatchedEvent event){
        System.out.println("Receive watched event:" + event);
        if(Event.KeeperState.SyncConnected == event.getState()){
            connectedSemaphore.countDown();
        }
    }

}

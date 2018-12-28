package com.wpm.zookeeper.basic;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;

public class AuthSampleNoRight {
    final static String PATH = "/zk-book-auth_test";
    public static void main(String[] args) throws Exception{
        ZooKeeper zooKeeper2 = new ZooKeeper("127.0.0.1:2181", 5000, null);
        zooKeeper2.getData(PATH, false, null);
    }
}

package com.wpm.zookeeper.basic;

import org.apache.zookeeper.ZooKeeper;

public class AuthSampleGetWithRight {
    final static String PATH = "/zk-book-auth_test";

    public static void main(String[] args) throws Exception{
        ZooKeeper zooKeeper = new ZooKeeper("127.0.0.1:2181", 5000, null);
        zooKeeper.addAuthInfo("digest", "foo:true".getBytes());
        System.out.println(zooKeeper.getData(PATH, false, null));
    }
}

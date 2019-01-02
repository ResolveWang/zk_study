package com.wpm.zookeeper.zkclient;

import org.I0Itec.zkclient.ZkClient;

public class SessionCreation {
    public static void main(String[] args){
        ZkClient zkClient = new ZkClient("127.0.0.1:2181", 5000);
        System.out.println("Zookpeer session establised");
    }
}

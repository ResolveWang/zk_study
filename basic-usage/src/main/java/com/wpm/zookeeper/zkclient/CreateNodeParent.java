package com.wpm.zookeeper.zkclient;

import org.I0Itec.zkclient.ZkClient;

public class CreateNodeParent {
    public static void main(String[] args) throws Exception{
        ZkClient zkClient = new ZkClient("127.0.0.1:2181", 5000);
        String path = "/zkbook/zclient";
        // 递归创建
        zkClient.createPersistent(path, true);
    }
}

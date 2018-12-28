package com.wpm.zookeeper.basic;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;

public class AuthSampleDelete {
    final static String PATH = "/zk-book-auth_test_delete";
    final static String PATH2 = "/zk-book-auth_test_delete/child";

    public static void main(String[] args) throws Exception{
        ZooKeeper zooKeeper = new ZooKeeper("127.0.0.1:2181", 5000, null);
        zooKeeper.addAuthInfo("digest", "foo:true".getBytes());
        zooKeeper.create(PATH, "init".getBytes(), ZooDefs.Ids.CREATOR_ALL_ACL, CreateMode.PERSISTENT);
        zooKeeper.create(PATH2, "init".getBytes(), ZooDefs.Ids.CREATOR_ALL_ACL, CreateMode.EPHEMERAL);

        try{
            ZooKeeper zooKeeper2 = new ZooKeeper("127.0.0.1:2181", 5000, null);
            zooKeeper2.delete(PATH2, -1);
        }catch (Exception e){
            System.out.println("删除失败"+e.getMessage());
        }
        /*

           对于删除节点，如果对某个节点添加了权限信息，那么该节点不需要权限仍可删除，但是它的子节点需要权限才能删除
         */

        ZooKeeper zooKeeper3 = new ZooKeeper("127.0.0.1:2181", 5000, null);
        zooKeeper3.addAuthInfo("digest", "foo:true".getBytes());
        zooKeeper3.delete(PATH2, -1);
        System.out.println("成功删除节点"+PATH2);

        ZooKeeper zooKeeper4 = new ZooKeeper("127.0.0.1:2181", 5000, null);
        zooKeeper4.delete(PATH, -1);
        System.out.println("成功删除节点"+PATH);
    }
}

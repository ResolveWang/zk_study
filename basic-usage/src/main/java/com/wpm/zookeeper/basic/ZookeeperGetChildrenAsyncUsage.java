package com.wpm.zookeeper.basic;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.util.concurrent.CountDownLatch;
import java.util.List;

public class ZookeeperGetChildrenAsyncUsage implements Watcher {
    private static CountDownLatch countDownLatch = new CountDownLatch(1);
    private static ZooKeeper zk = null;

    public static void main(String[] args) throws Exception{
        String path = "/zk-book8";
        zk = new ZooKeeper("127.0.0.1:2181", 5000, new ZookeeperGetChildrenAsyncUsage());
        countDownLatch.await();
        zk.create(path, "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        zk.create(path+"/c1", "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);

        zk.getChildren(path, true, new IChildren2Callback(), null);
        zk.create(path+"/c2","".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);

        Thread.sleep(Integer.MAX_VALUE);
    }

    public void process(WatchedEvent event){
        if(Event.KeeperState.SyncConnected == event.getState()){
            if(Event.EventType.None == event.getType() && null == event.getPath()){
                countDownLatch.countDown();
            }else if(event.getType() == Event.EventType.NodeChildrenChanged){
                try{
                    System.out.println("ReGet Child:" + zk.getChildren(event.getPath(), true));
                }catch (Exception e){

                }
            }
        }
    }

}

class IChildren2Callback implements AsyncCallback.Children2Callback{
    public void processResult(int rc, String path, Object ctx, List<String> children, Stat stat){
        System.out.println("Get children znode result: [response code: " + rc + ", param path:" + path
                + ",ctx:"+ctx+",children list:"+children+",stat:"+stat);

    }
}
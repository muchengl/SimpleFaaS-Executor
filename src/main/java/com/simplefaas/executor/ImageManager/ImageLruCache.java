package com.simplefaas.executor.ImageManager;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.File;
import java.util.*;


@Data
@AllArgsConstructor
public class ImageLruCache {


    public static volatile ImageLruCache imageCache;

    private ImageLruCache(){}

    private ImageLruCache(int capacity) {
        this.capacity=capacity;
        num=0;
        head=new Node();
        tail=new Node();
        head.next=tail;
        tail.pre=head;
    }

    public static ImageLruCache getInstance(){
        if(imageCache==null){
            synchronized (ImageLruCache.class){
                if(imageCache==null){
                    imageCache=new ImageLruCache(10);
                }
            }
        }

        return imageCache;
    }




    int capacity;

    class Node{
        String funcName;
        String funcPath;
        Node pre,next;

    }

    Map<String,Node> map=new HashMap<>();
    Node head;
    Node tail;
    int num=0;


    public String get(String key) {
        if(map.containsKey(key)){
            Node node=map.get(key);;

            removeNode(node);
            insertNode(node);

            return node.funcPath;
        }
        return "";
    }


    public List<String> getNodeList(){
        Set<String> set=map.keySet();

        List<String> list=new ArrayList<>();

        for (String key : set) {
            Node node=map.get(key);
            list.add(node.funcName);
        }

        return list;
    }

    public void put(String funcName, String funcPath) {
        if(map.containsKey(funcName)){
            Node node=map.get(funcName);
            node.funcPath=funcPath;
            removeNode(node);
            insertNode(node);
        }
        else{
            Node node=new Node();
            node.funcName=funcName;
            node.funcPath=funcPath;
            insertNode(node);
            map.put(funcName,node);
            num++;
            if(num>capacity){
                num--;

                // 物理删除文件

                File file=new File(tail.pre.funcPath);
                boolean val=file.delete();
                if(!val){
                    System.out.println("Lru Delete Image "+file.getPath()+" Fail.");
                }

                System.out.println("Delete Image:"+tail.pre.funcName);

                // LUR删除
                map.remove(tail.pre.funcName);
                removeNode(tail.pre);


            }
        }
    }

    public void removeNode(Node node){
        Node pre=node.pre;
        Node next=node.next;
        pre.next=next;
        next.pre=pre;;
    }

    public void insertNode(Node node){
        Node next=head.next;

        head.next=node;
        node.next=next;
        node.pre=head;
        next.pre=node;
    }
}

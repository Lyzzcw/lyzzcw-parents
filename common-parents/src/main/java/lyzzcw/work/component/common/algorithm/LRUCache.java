package lyzzcw.work.component.common.algorithm;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lzy
 * @version 1.0
 * Date: 2023/8/29 10:41
 * Description: 手写一个LRU链表缓存，最近最少使用，是一种常用的置换算法，选择最近最久未使用的页面予以淘汰
 * 先明确缓存的结构，我们就按照redis的结构来设计
 * 首先要有一个hash表，作为o(1)时间复杂度的获取查询，但是具体的存储不能按照hash存，hash没有lru的概念，你也可以重设计一个
 * 加入使用时间去处理，但是太麻烦。所以hash表只存一个映射关系，就是key和节点的关系。
 * 真正的数据按照链表的方式存在链表中，我们通过对链表的排队来处理这个LRU的实现，为啥不选数组呢？
 * 因为我们查询一次就要修改他的顺序，把他提到最前面来，我们淘汰就是淘汰的后面的。而缓存的查询是高频次的，数组的修改非常耗时，所以选择链表。
 */
public class LRUCache {
    //map负责查找，构建一个虚拟的双向链表，它里面安装的就是一个个Node节点，作为数据载体。
    //1.构造一个node节点作为数据载体
    class Node<K,V>{
        //key值
        K key;
        //value值
        V value;
        //前置指针
        Node<K,V> prev;
        //后置指针
        Node<K,V> next;
        public Node(){
            this.prev = this.next = null;
        }
        public Node(K key,V value){
            this.key = key;
            this.value = value;
            this.prev = this.next = null;
        }
    }
    //2 构建一个双向链表,里面安放的就是我们的Node
    class DoubleLinkedList<K,V>{
        // 链表头节点指针
        Node<K,V> head;
        // 链表尾结点指针
        Node<K, V> tail;
        // 重写一下toString方法，返回存储链表的顺序遍历，看看是不是最近使用的被放到了队列头部
        @Override
        public String toString() {
            String result = "";
            if(this.head.next != this.tail){
                Node<K, V> pr = this.head.next;
                while (pr != this.tail){
                    result += pr.key + "\t";
                    pr = pr.next;
                }
            }
            return result;
        }
        // 构造函数
        public DoubleLinkedList(){
            head = new Node<>();
            tail = new Node<>();
            // 刚开始的时候头尾是在一起连着的，其余节点还没进来
            head.next = tail;
            tail.prev = head;
        }
        /**
         * 操作链表节点的一个原则就是断开链之后，别丢掉连接关系，所以最好先在纸上画个图看看再写
         */
        //3. 添加的时候，是头部插入
        public void addHead(Node<K,V> node) {
            // ----------------先处理这个节点和头节点的关系--------------------------
            node.next = head.next;
            node.prev = head;
            // ----------------再处理这个节点和他后面节点的关系-----------------------
            // 能用头尾节点处理的，尽量用，这个不变，比较稳
            head.next.prev = node;
            head.next = node;
        }
        //4.删除节点，根据key删除，redis你理解吧，具体操作就是断开链表的操作，别丢节点，画个图好理解
        public void removeNode(Node<K, V> node) {
            node.next.prev = node.prev;
            node.prev.next = node.next;
            node.prev = null;
            node.next = null;
        }
        //5.获得最后一个节点
        public Node getLast() {
            return tail.prev;
        }

    }
    // 链表长度，缓存大小
    private int cacheSize;
    // 缓存k-v映射关系的，那个map哈希表
    Map<Integer,Node<Integer,Integer>> map;
    // 缓存数据存放的具体链表
    DoubleLinkedList<Integer,Integer> doubleLinkedList;
    public LRUCache(int cacheSize) {
        this.cacheSize = cacheSize;//坑位
        map = new HashMap<>();//查找
        doubleLinkedList = new DoubleLinkedList<>();
    }
    // 获取缓存
    public int get(int key){
        if (!map.containsKey(key)){
            return -1;
        }
        Node<Integer, Integer> node = map.get(key);
        // 因为是LRU，最近使用优先，我们这里使用了，就要从原来位置移除，然后放到头部
        doubleLinkedList.removeNode(node);
        doubleLinkedList.addHead(node);
        return node.value;
    }
    public void put(int key, int value) {
        if (map.containsKey(key)){ //update
            Node<Integer, Integer> node = map.get(key);
            node.value = value;
            map.put(key, node);
            doubleLinkedList.removeNode(node);
            doubleLinkedList.addHead(node);
        }else {
            if (map.size() == cacheSize){ //坑位满了，就要淘汰了
                // 取出最后一个，你是最久没用的，用过的都被移到前面了
                Node<Integer,Integer> lastNode = doubleLinkedList.getLast();
                // 先从哈希表移除
                map.remove(lastNode.key);
                // 再从存储结构里移除
                doubleLinkedList.removeNode(lastNode);
            }
            // 把要加的新增进来，写在头部
            Node<Integer, Integer> newNode = new Node<>(key, value);
            map.put(key,newNode);
            doubleLinkedList.addHead(newNode);
        }
    }
    public static void main(String[] args) {
        LRUCache lruCacheDemo = new LRUCache(3);
        lruCacheDemo.put(1,1);
        lruCacheDemo.put(2,2);
        lruCacheDemo.put(3,3);
        System.out.println(lruCacheDemo.map.keySet());
        lruCacheDemo.put(4,1);
        System.out.println(lruCacheDemo.map.keySet());
        lruCacheDemo.put(3,1);
        System.out.println(lruCacheDemo.map.keySet());
        lruCacheDemo.put(3,1);
        System.out.println(lruCacheDemo.map.keySet());
        lruCacheDemo.put(3,1);
        System.out.println(lruCacheDemo.map.keySet());
        lruCacheDemo.put(5,1);
        System.out.println(lruCacheDemo.map.keySet());
        // 上面都是插入的，下面我们访问一个key为4的节点，看看他是不是被移动到队列头部了
        lruCacheDemo.get(4);
        System.out.println(lruCacheDemo.map.keySet());
        System.out.println("当前链表的顺序遍历：" +
                lruCacheDemo.doubleLinkedList.toString());
    }
}

package com.test;


import com.util.HashMap;
import com.util.LinkedHashMap;

import java.lang.reflect.Field;


public class LinkedHashMapTest {
    static LinkedHashMap<Test, String> linkedHashMap = new LinkedHashMap<Test, String>(1, 0.75f, true);

    public static void main(String[] args) {
        test1();
    }

    //数据加单链表
    private static void test1() {
        for (int i = 0; i < 4; i++) {
            linkedHashMap.put(new Test(1 + i * 8), "" + (1 + i * 8));
        }
        show("init");
        linkedHashMap.get(new Test(9));
        show("get new Test(9)");
    }

    private static void show(String tag) {
        Class<LinkedHashMap> linkedHashMapClass = LinkedHashMap.class;

        try {
            Field table = linkedHashMapClass.getField("table");
            Field size = linkedHashMapClass.getField("size");
            size.setAccessible(true);
            table.setAccessible(true);
            System.out.println("\n--------------------------------------");
            HashMap.Node[] tables = (HashMap.Node[]) table.get(linkedHashMap);
            if (tables == null) {
                return;
            }
            System.out.println("size:" + size.get(linkedHashMap) + " size():" + linkedHashMap.size() + " tables.length:" + tables.length);
            //展示每个槽的节点数据
            for (int i = 0; i < tables.length; i++) {
                HashMap.Node node = tables[i];
                if (node instanceof HashMap.TreeNode) {
                    System.out.println(i + " TreeNode");
                } else if (node instanceof LinkedHashMap.Entry) {
                    LinkedHashMap.Entry entry = (LinkedHashMap.Entry) node;
                    showNode(i, entry);
                } else {
                    System.out.println(i + " null");
                }
            }

            System.out.println(tag + "\n" + linkedHashMap);
            //展示LinkedHashMap双向列表
            showDoubleLinked();
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private static void showDoubleLinked() {
        LinkedHashMap.Entry<Test, String> head = linkedHashMap.head;
        while (head != null) {
            System.out.print(head + " --> ");
            head = head.after;
        }

        System.out.print(head);
    }

    private static void showNode(int index, HashMap.Node node) {
        if (node != null) {
            System.out.print(index + " " + node.getClass().getName() + " -> ");
        }
        while (node != null) {
            System.out.print(node + " --> ");
            node = node.next;
        }
        System.out.println(node);
    }

    static class Test {
        int key;

        public Test(int key) {
            this.key = key;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Test testNode = (Test) o;
            return key == testNode.key;
        }

        @Override
        public int hashCode() {
            return key % 8;
        }

        @Override
        public String toString() {
            return "TestNode{" +
                    "key=" + key +
                    '}';
        }
    }
}

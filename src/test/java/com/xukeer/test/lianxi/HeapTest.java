package com.xukeer.test.lianxi;

import java.util.Iterator;
import java.util.PriorityQueue;

/**
 * @author xqw
 * @description
 * @date 10:21 2022/7/27
 **/
public class HeapTest {
    /**
     * 树形结构
     * */
    private static class TreeNode<T> {
        private TreeNode<T> leftNode;
        private TreeNode<T> rightNode;
        private T currentNode;

    }

    private static class HeapQueue<T> {
        private TreeNode<T> tTreeNode;

        public void add(T treeNode) {
            if (tTreeNode == null) {
                tTreeNode = new TreeNode<>();
                tTreeNode.currentNode = treeNode;
            } else {

            }
        }

        public Iterator<T> iterator() {
            return new Iterator(){
                @Override
                public boolean hasNext() {
                    return false;
                }

                @Override
                public Object next() {
                    return null;
                }

                @Override
                public void remove() {

                }
            };
        }

    }


    private static class Student{
        public Student(int age, String name) {
            this.age = age;
            this.name = name;
        }
        private int age;
        private String name;
    }

    public static void main(String[] args) {
        PriorityQueue<Student> priorityQueue = new PriorityQueue<>((o1, o2) -> o2.age-o1.age);
        priorityQueue.offer(new Student(12,"12"));
        priorityQueue.offer(new Student(9,"9"));
        priorityQueue.offer(new Student(19,"19"));
        priorityQueue.offer(new Student(22,"22"));
        priorityQueue.offer(new Student(23,"23"));
        priorityQueue.offer(new Student(2,"2"));

        for (Student student : priorityQueue) {
            System.out.println(student.name);
        }
    }
}

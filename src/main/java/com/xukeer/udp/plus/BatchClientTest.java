//package com.xukeer.udp.plus;//package com.xukeer.udp.plus;
////
////import cn.hutool.json.JSONUtil;
////import com.xukeer.udp.plus.server.UpdPlusServer;
////import lombok.Data;
////import lombok.extern.slf4j.Slf4j;
////
////import java.net.InetSocketAddress;
////import java.util.LinkedList;
////import java.util.List;
////
/////*
//// * @author xqw
//// * @description
//// * @date 16:57 2021/11/9
//// **/
////@Slf4j
////public class UdpServer {
////    private static UpdPlusServer ud;
////
////    public static void main(String[] args) {
////        ud = new UpdPlusServer(9110, (msg, inetSocketAddress) -> {
////            String msgStr = new String(msg);
////            Message message =  JSONUtil.toBean(msgStr, Message.class);
////            log.warn("length={}, msg={}, success", msg.length, message.studentList.size());
////         send(message.getTimes(), inetSocketAddress);
////        });
////    }
////
////    private static void send(int times, InetSocketAddress inetSocketAddress) {
////        Message message = new Message();
////        List<Student> students = new LinkedList<>();
////
////        for (int i = 0; i < times; i++) {
////            Student student = new Student();
////            student.setAge(i);
////            student.setName("zhangsan");
////            student.setSchool("beida");
////            students.add(student);
////        }
////        message.setStudentList(students);
////        String obj = JSONUtil.toJsonStr(message);
////        ud.sendMsg(obj.getBytes(), inetSocketAddress);
////    }
////
////    @Data
////    private static class Message {
////        private int times;
////        private List<Student> studentList;
////    }
////
////    @Data
////    private static class Student {
////        private String name;
////        private String school;
////        private int age;
////    }
////}
//
//
//
//import cn.hutool.json.JSONUtil;
//import com.xukeer.udp.plus.client.UdpPlusClient;
//import com.xukeer.udp.plus.utils.ScheduleUtil;
//import lombok.Data;
//import lombok.extern.slf4j.Slf4j;
//
//import java.util.LinkedList;
//import java.util.List;
//import java.util.concurrent.TimeUnit;
//
//@Slf4j
//public class BatchClientTest {
//    private static final String address = "xukeer.com";
//    //        private static final String address = "127.0.0.1";
////        private static final String address = "172.93.1.253";
//    static volatile Integer count =0;
//    public static void main(String[] args) {
//        UdpPlusClient udpPlusClient = new UdpPlusClient(address, 9110, 9002, (msg, inetSocketAddress) -> {
//            Message message =  JSONUtil.toBean(new String(msg), Message.class);
//            synchronized (count) {
//                count++;
//            }
//            log.warn("receive length={}, msgObjectLength={},totalCount={}, success",msg.length, message.studentList.size(),count);
//        });
//
//        for(int i =0;i<20;i++){
//            ScheduleUtil.timer().scheduleAtFixedRate(() -> {
//                byte[] arr = getByteArr();
//                udpPlusClient.sendMsg(arr);
//                log.warn("length={}, send success", arr.length);
//            }     ,0, 15, TimeUnit.SECONDS);
//        }
//    }
//    private static byte[] getByteArr() {
//        Message message = new Message();
//        message.setTimes(300);
//        List<Student> list = new LinkedList<>();
//        for(int i=0;i<120;i++){
//            Student student = new Student();
//            student.setAge(i);
//            student.setName("xxxxxxxxx");
//            student.setSchool("scscscscsc");
//            list.add(student);
//        }
//        message.setStudentList(list);
//        String obj = JSONUtil.toJsonStr(message);
//        return obj.getBytes();
//    }
//
//
//    @Data
//    public static class Message{
//        private int times;
//        private List<Student> studentList;
//    }
//
//    @Data
//    public static class Student{
//        private String name;
//        private String school;
//        private int age;
//    }
//}

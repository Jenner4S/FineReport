//package com.hyp.plugin.workday;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.util.Scanner;
//
//public class InternetTest {
//    static BufferedReader bufferedReader;
//    public static boolean test(String url) {
//        int i = 0;
//        try {
//            Process process = Runtime.getRuntime()
//                    .exec("ping " + url + " -t");
//            bufferedReader = new BufferedReader(new InputStreamReader(
//                    process.getInputStream()));
//            String connectionStr = null;
//            while ((connectionStr = bufferedReader.readLine()) != null && i < 3) {
//                System.out.println(connectionStr);
//                i++;
//            }
//        } catch (IOException e) {
//            System.out.println("链接失败");
//            e.printStackTrace();
//        } finally {
//            try {
//                bufferedReader.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            if (i > 1) {
//                return true;
//            } else {
//                return false;
//            }
//        }
//    }
//}

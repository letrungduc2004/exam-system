package com.example.exam_system.features.exam_attempt.service;

import java.security.SecureRandom;
import java.util.*;

public class test {
    public static void main(String[] args) {
//        Map<Integer, String> testt = new HashMap<>();
//        testt.put(1, "hoa");
//        testt.put(2, "anh");
//        testt.put(3, "Ly");
//        String name = testt.get(5);
//        System.out.println(name);

//        Long a = 5L;
//        Long b = 5L;
//        if(a==b){
//            // a, b dao động từ -127 - 127 thì "Bằng" với kiểu Wrapper
//            // do Java chỉ cache các số Long từ -128 đến 127
//            // == so sánh địa chỉ
//            System.out.println("Bằng");
//        }else{
//            System.out.println("Không bằng");
//        }
//

// Kiểm tra đã là part cuối cùng hay chưa
//        Long currentPart = 9L;
//        List<Long> partListId = Arrays.asList(3L, 7L, 9L);
//        System.out.println(partListId.get(partListId.size() - 1));
//        Long cuoi = Long.valueOf(partListId.get(partListId.size() - 1 ));
//        if (currentPart.equals(cuoi)) {
//            System.out.println("Phần cuối rồi");
//        } else {
//            System.out.println("Chưa phải");
//        }

        // Tạo SecureRandom
        SecureRandom secureRandom = new SecureRandom();
        // Tạo mảng byte 32 byte = 256 bit
        byte[] key = new byte[32];
        secureRandom.nextBytes(key);
        // Encode sang Base64 để dễ lưu trữ/hiển thị
        String base64Key = Base64.getEncoder().encodeToString(key);
        System.out.println("Secure 256-bit key (Base64): " + base64Key);
    }
}

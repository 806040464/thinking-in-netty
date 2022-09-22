package com.zhaoccf.demo.nio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;

/**
 * @author zhaoccf
 * @version 1.0
 * @description
 * @date 2022/9/22 15:16
 */
public class Demo {
    public static void main(String[] args) throws Exception {
        //writeFile();
        //readFile();
        //copyFileByBio();
        copyFileByNio();
    }

    public static void writeFile() throws IOException {
        FileOutputStream fos = new FileOutputStream("basic.txt");
        FileChannel fc = fos.getChannel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        buffer.put("HelloJava".getBytes(StandardCharsets.UTF_8));
        buffer.flip();
        fc.write(buffer);
        fos.close();
    }

    public static void readFile() throws IOException {
        File file = new File("basic.txt");
        FileInputStream fis = new FileInputStream(file);
        FileChannel fc = fis.getChannel();
        ByteBuffer buffer = ByteBuffer.allocate((int) file.length());
        fc.read(buffer);
        System.out.println(new String(buffer.array(), StandardCharsets.UTF_8));
        fis.close();
    }

    public static void copyFileByBio() throws IOException {
        FileInputStream fis = new FileInputStream("basic.txt");
        FileOutputStream fos = new FileOutputStream("basic2.txt");
        byte[] b = new byte[1024];
        while (true) {
            int len;
            if ((len = fis.read(b)) == -1) {
                break;
            }
            fos.write(b, 0, len);
        }
    }

    public static void copyFileByNio() throws IOException {
        FileInputStream fis = new FileInputStream("basic.txt");
        FileOutputStream fos = new FileOutputStream("basic3.txt");

        FileChannel fisChannel = fis.getChannel();
        FileChannel fosChannel = fos.getChannel();

        fosChannel.transferFrom(fisChannel, 0, fisChannel.size());

        fis.close();
        fos.close();
    }

}

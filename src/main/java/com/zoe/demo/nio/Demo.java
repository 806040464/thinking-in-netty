package com.zoe.demo.nio;

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
//        writeFile();
//        readFile();
//        copyFileByBio();
        copyFileByNio();
    }

    public static void writeFile() throws IOException {
        //创建输出流
        FileOutputStream fos = new FileOutputStream("basic.txt");
        //从流中获取通道
        FileChannel channel = fos.getChannel();
        //创建缓冲区并放入数据
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        byteBuffer.put("HelloJava".getBytes(StandardCharsets.UTF_8));
        //写数据前需要翻转数据区，写之前position为HelloJava的最后位置，limit为1024，翻转后limit为HelloJava的最后位置，position为0，才能写HelloJava
        byteBuffer.flip();
        //将缓冲区数据写入通道
        channel.write(byteBuffer);
        //关闭
        fos.close();
    }

    public static void readFile() throws IOException {
        FileInputStream fis = new FileInputStream("basic.txt");
        FileChannel channel = fis.getChannel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        channel.read(byteBuffer);
        System.out.println(new String(byteBuffer.array()).trim());
        fis.close();
    }

    public static void copyFileByBio() throws IOException {
        FileInputStream fis = new FileInputStream("basic.txt");
        FileOutputStream fos = new FileOutputStream("basic2.txt");
        byte[] bytes = new byte[1024];
        while (true) {
            int read = fis.read(bytes);
            if (-1 == read) {
                break;
            }
            fos.write(bytes, 0, read);
        }
        fis.close();
        fos.close();
    }

    public static void copyFileByNio() throws IOException {
        FileInputStream fis = new FileInputStream("basic.txt");
        FileOutputStream fos = new FileOutputStream("basic3.txt");
        FileOutputStream fos2 = new FileOutputStream("basic4.txt");

        FileChannel sourceChannel = fis.getChannel();
        FileChannel destChannel = fos.getChannel();
        FileChannel destChannel2 = fos2.getChannel();

        //sourceChannel读取数据写入destChannel
        destChannel.transferFrom(sourceChannel, 0, sourceChannel.size());
        //sourceChannel读取数据写入destChannel2
        sourceChannel.transferTo(0, sourceChannel.size(), destChannel2);
    }

}

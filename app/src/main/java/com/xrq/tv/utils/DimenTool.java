package com.xrq.tv.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class DimenTool {
    public static void gen() {
        //以此文件夹下的dimens.xml文件内容为初始值参照
        File file = new File("./app/src/main/res/values/dimen.xml");

        BufferedReader reader = null;

        StringBuilder w960 = new StringBuilder();
        StringBuilder w1280 = new StringBuilder();

        try {

            System.out.println("生成不同分辨率：");

            reader = new BufferedReader(new FileReader(file));

            String tempString;

            int line = 1;

            // 一次读入一行，直到读入null为文件结束

            while ((tempString = reader.readLine()) != null) {


                if (tempString.contains("</dimen>")) {


                    String start = tempString.substring(0, tempString.indexOf(">") + 1);

                    String end = tempString.substring(tempString.lastIndexOf("<") - 2);
                    //截取<dimen></dimen>标签内的内容，从>右括号开始，到左括号减2，取得配置的数字
                    Double num = Double.parseDouble
                            (tempString.substring(tempString.indexOf(">") + 1,
                                    tempString.indexOf("</dimen>") - 2));

                    //根据不同的尺寸，计算新的值，拼接新的字符串，并且结尾处换行。
                    w960.append(start).append(num / 1.33).append(end).append("\r\n");
                    w1280.append(start).append(num / 1).append(end).append("\r\n");

                } else {

                    w960.append(tempString).append("");
                    w1280.append(tempString).append("");

                }

                line++;

            }

            reader.close();

            System.out.println("<!--  w960 -->");
            System.out.println(w960);

            String w960file = "./app/src/main/res/values-sw960dp/dimen.xml";
            String w1280file = "./app/src/main/res/values-sw1280dp/dimen.xml";
            //将新的内容，写入到指定的文件中去
            writeFile(w960file, w960.toString());
            writeFile(w1280file, w1280.toString());

        } catch (IOException e) {

            e.printStackTrace();

        } finally {

            if (reader != null) {

                try {

                    reader.close();

                } catch (IOException e1) {

                    e1.printStackTrace();

                }

            }

        }

    }


    /**
     * 写入方法
     */

    public static void writeFile(String file, String text) {

        PrintWriter out = null;

        try {

            out = new PrintWriter(new BufferedWriter(new FileWriter(file)));

            out.println(text);

        } catch (IOException e) {

            e.printStackTrace();

        }


        out.close();

    }

    public static void main(String[] args) {
//        init();
        gen();

    }

    public static void init() {
        File file = new File("./app/src/main/res/values/dimen.xml");
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
            bufferedWriter.write("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
            bufferedWriter.write("<resources>\n");
            for (int i = 1; i < 100; i++) {
                bufferedWriter.write("<dimen name=\"sp_" + i + "\">" + i + "sp</dimen>\n");

            }
            for (int i = 1; i < 1000; i++) {
                bufferedWriter.write(" <dimen name=\"dp_" + i + "\">" + i + "dp</dimen>\n");
            }
            bufferedWriter.write("</resources>\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

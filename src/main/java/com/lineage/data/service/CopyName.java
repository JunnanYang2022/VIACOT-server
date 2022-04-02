package com.lineage.data.service;

import cn.hutool.core.io.FileUtil;

import java.io.File;
import java.util.Arrays;

/**
 * @author YangJunNan
 * @description
 * @date 2021/11/21
 */
public class CopyName {
    public static void main(String[] args) {
        File file = new File("C:\\Users\\XT.LAPTOP-16FLO4CF\\Desktop\\YJN\\zipped_CD_Files\\");
        Arrays.stream(file.listFiles()).forEach(e -> {
            int i = e.getName().indexOf("_");
            String name = e.getName().substring(i + 1);
            File file1 = new File("C:\\Users\\XT.LAPTOP-16FLO4CF\\Desktop\\YJN\\zipped_CD_Files_change\\" + name);
            if (file1.exists()) {
                name = name.substring(0, name.lastIndexOf(".")) + System.currentTimeMillis() + ".csv";
                file1 = new File("C:\\Users\\XT.LAPTOP-16FLO4CF\\Desktop\\YJN\\zipped_CD_Files_change\\" + name);
            }

            FileUtil.copyFile(e, file1);
        });
    }
}

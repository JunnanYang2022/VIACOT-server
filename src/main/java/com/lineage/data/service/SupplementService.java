package com.lineage.data.service;

import cn.hutool.core.io.IoUtil;
import com.lineage.data.entity.Supplement;
import com.lineage.data.util.JxlsUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author YangJunNan
 * @description
 * @date 2021/7/21
 */
public class SupplementService {

    private final static String ORIGIN_PATH = "C:\\Users\\XT.LAPTOP-16FLO4CF\\Desktop\\a\\b\\origin\\";
    private final static String SPLIT_PATH = "C:\\Users\\XT.LAPTOP-16FLO4CF\\Desktop\\a\\b\\split\\";
    private final static String RESULT_PATH = "C:\\Users\\XT.LAPTOP-16FLO4CF\\Desktop\\a\\b\\result\\";

    public static void main(String[] args) throws Exception {

        split(new File(ORIGIN_PATH + "NIHMS1604359-supplement-Table_S8"));

        File file = new File(SPLIT_PATH);
        File[] files = file.listFiles();

        assert files != null;
        Arrays.stream(files).forEach(e -> {
            try {
                exportExcel(e);
            } catch (FileNotFoundException fileNotFoundException) {
                fileNotFoundException.printStackTrace();
            }
        });

    }

    private static void exportExcel(File file) throws FileNotFoundException {
        FileReader reader = new FileReader(file);
        List<String> line = new ArrayList<>();
        IoUtil.readLines(reader, line);

        Map<String, List<Supplement>> group = line.parallelStream()
                .map(e -> {
                    String[] split = e.split("\\t");
                    return new Supplement(split[0], split[1], split[2],split[3]);
                })
                .collect(Collectors.groupingBy(Supplement::getGene));

        group.forEach((k, v) -> {
            Map<String, Object> model = new HashMap<>();
            model.put("list", v);
            model.put("title", k);
            try (FileOutputStream os = new FileOutputStream(RESULT_PATH + k + ".xlsx")) {
                JxlsUtils.exportExcel(ORIGIN_PATH + "supplement.xlsx", os, model);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private static void split(File file) throws FileNotFoundException {

        if (file.isDirectory()) {
            throw new RuntimeException("file is directory！");
        }

        FileReader reader = new FileReader(file);
        List<String> line = new ArrayList<>();
        IoUtil.readLines(reader, line);
        Map<String, List<String>> collect = line.parallelStream()
                .collect(Collectors.groupingBy(e -> e.split("\\t")[0]));

        collect.forEach((k, v) -> {
            try {
                IoUtil.write(new FileOutputStream(SPLIT_PATH + k + ".txt"), true,
                        StringUtils.join(v.toArray(), "\n").getBytes());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        });
    }
}

package com.lineage.data.service;

import com.lineage.data.entity.AllelesFrequency;
import com.lineage.data.util.ExcelReadUtils;
import com.opencsv.CSVReader;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.StopWatch;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * @author YangJunNan
 * @description
 * @date 2021/9/26
 */
public class DealCrossData2 {

    private final static String NO_DATA = "NO_DATA";

    private final static String ALLELES_FREQUENCY_FILE_PATH = "C:\\Users\\XT.LAPTOP-16FLO4CF\\Desktop\\YJN\\cross\\ZF3" +
            "\\ZF3-AllelesFrequency.xlsx";
    private final static String GEST_MASTER_FILE_PATH = "C:\\Users\\XT.LAPTOP-16FLO4CF\\Desktop\\YJN\\cross\\ZF3" +
            "\\GSM2813986_ZF3.GestMaster.xlsx";
    private final static String GENES_FILE_PATH = "C:\\Users\\XT.LAPTOP-16FLO4CF\\Desktop\\YJN\\cross\\ZF3\\GSTLT_f6.noQ.genes.csv";

    private final static String SQL_FILE_PATH = "C:\\Users\\XT.LAPTOP-16FLO4CF\\Desktop\\YJN\\cross\\ZF3\\ZF3.sql";

    public static void main(String[] args) throws Exception {

        Map<String, String> crossMap = new HashMap<>();

        List<List<String>> gestList = ExcelReadUtils.readExcel(GEST_MASTER_FILE_PATH);

        List<List<String>> allelesList = ExcelReadUtils.readExcel(ALLELES_FREQUENCY_FILE_PATH);
        allelesList.parallelStream()
                .map(e -> {
                    List<String> list =
                            gestList.parallelStream().filter(g -> g.get(1).equalsIgnoreCase(e.get(1))).findFirst().orElse(null);
                    AllelesFrequency allelesFrequency = new AllelesFrequency(e.get(0), e.get(1), list == null ?
                            NO_DATA :
                            "f6_" + list.get(0));
                    System.out.println(allelesFrequency);
                    return allelesFrequency;
                })
                .filter(f -> !NO_DATA.equals(f.getBarcodeKey()))
                .collect(Collectors.toList())
                .forEach(e -> crossMap.put(e.getBarcodeKey(), e.getNodeName()));


        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        System.out.println("开始处理》》》》》》》》》》》》》》》》》》》》》");
        AtomicReference<Integer> dataSize = new AtomicReference<>(0);

        CSVReader csvReader = new CSVReader(new FileReader(GENES_FILE_PATH));
        //读取到第一行 是小标题
        String[] titles = csvReader.readNext();
        System.out.println("》》》源数据：" + titles.length + "列");

        HashMap<Integer, String> titleOriginMap = new HashMap<>();
        for (int i = 0; i < titles.length; i++) {
            titleOriginMap.put(i, titles[i]);
        }
        HashMap<Integer, String> titleMap = new HashMap<>();
        titleOriginMap.forEach((k, v) -> {
            if (StringUtils.isNotBlank(crossMap.get(v))) {
                titleMap.put(k,v);
            }
        });
        System.out.println("》》》有效数据：" + titleMap.size() + "列");

        try (FileWriter writer = new FileWriter(SQL_FILE_PATH)) {
            while (true) {
                System.out.println("》》》当读取前行：" + csvReader.getLinesRead());
                String[] content = csvReader.readNext();
                if (content == null) {
                    break;
                }
                final String geneName = content[0];
                titleMap.forEach((k, v) -> {

                    if (crossMap.get(v) != null && Integer.parseInt(content[k]) != 0) {
                        try {
                            String line =
                                    "INSERT INTO `tb_genes_expression` VALUES (null, '" + crossMap.get(v) + "', " + content[k] + "," +
                                            "'" + geneName + "', '" + 4 + "');\n";
                            writer.write(line);
                            dataSize.getAndSet(dataSize.get() + 1);
                        } catch (IOException ignore) {
                        }
                    }
                });
            }
        } catch (Exception ignore) {
        }


        stopWatch.stop();
        System.out.println("处理结束》》》》》》》》》》》》》》》》》》》》》");
        System.out.println("共处理数据：" + dataSize.get() + "条， 耗时：" + stopWatch.getTotalTimeSeconds() + "秒");
    }
}

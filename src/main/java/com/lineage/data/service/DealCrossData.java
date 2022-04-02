package com.lineage.data.service;

import com.lineage.chart.entity.GenesExpression;
import com.lineage.data.entity.AllelesFrequency;
import com.lineage.data.util.ExcelReadUtils;
import com.opencsv.CSVReader;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.StopWatch;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author YangJunNan
 * @description
 * @date 2021/9/26
 */
public class DealCrossData {

    private final static String NO_DATA = "NO_DATA";

    private final static String ALLELES_FREQUENCY_FILE_PATH = "C:\\Users\\XT.LAPTOP-16FLO4CF\\Desktop\\cross\\ZF1" +
            "\\ZF1-AllelesFrequency.xlsx";
    private final static String GEST_MASTER_FILE_PATH = "C:\\Users\\XT.LAPTOP-16FLO4CF\\Desktop\\cross\\ZF1" +
            "\\GSM2813984_ZF1.GestMaster.xlsx";
    private final static String GENES_FILE_PATH = "C:\\Users\\XT.LAPTOP-16FLO4CF\\Desktop\\cross\\ZF1\\GSTLT_f3.noQ" +
            ".genes.csv";

    private final static String SQL_FILE_PATH = "C:\\Users\\XT.LAPTOP-16FLO4CF\\Desktop\\cross\\ZF1\\ZF1.sql";

    public static void main(String[] args) throws Exception {

        Map<String,String> crossMap = new HashMap<>();

        List<List<String>> gestList = ExcelReadUtils.readExcel(GEST_MASTER_FILE_PATH);

        List<List<String>> allelesList = ExcelReadUtils.readExcel(ALLELES_FREQUENCY_FILE_PATH);
        allelesList.parallelStream()
                .map(e -> {
                    List<String> list =
                            gestList.parallelStream().filter(g -> g.get(1).equalsIgnoreCase(e.get(1))).findFirst().orElse(null);
                    AllelesFrequency allelesFrequency = new AllelesFrequency(e.get(0), e.get(1), list == null ?
                            NO_DATA :
                            "f3_" + list.get(0));
                    System.out.println(allelesFrequency);
                    return allelesFrequency;
                })
                .filter(f -> !NO_DATA.equals(f.getBarcodeKey()))
                .collect(Collectors.toList())
                .forEach(e -> {
                    crossMap.put(e.getBarcodeKey(),e.getNodeName());
                });
                ;


        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        System.out.println("开始处理》》》》》》》》》》》》》》》》》》》》》");
        List<GenesExpression> genesExpressionList = new ArrayList<>();

        CSVReader csvReader = new CSVReader(new FileReader(GENES_FILE_PATH));
        //读取到第一行 是小标题
        String[] titles = csvReader.readNext();
        try (FileWriter writer = new FileWriter(SQL_FILE_PATH)) {
            while (true) {
                String[] content = csvReader.readNext();
                if (content == null) {
                    break;
                }
                final String geneName = content[0];
                Arrays.stream(content).parallel().forEach(e -> {
                    Arrays.stream(titles).parallel().filter(StringUtils::isNotBlank).forEach(title -> {


                        if (crossMap.get(title) != null) {
//                            GenesExpression genesExpression = new GenesExpression();
//                            genesExpression.setGeneName(geneName);
//                            genesExpression.setNodeName(allelesFrequency.getNodeName());
//                            genesExpression.setExpressionOrBlot(Integer.parseInt(e));
//                            genesExpression.setTreeId("2");
//                            genesExpressionList.add(genesExpression);
                            try {

                                String line =
                                        "INSERT INTO `tb_genes_expression` VALUES (null, '" + crossMap.get(title) + "', " + e + ", '" + geneName +
                                        "', '" + 2 + "');\n";
                                writer.write(line);
                            } catch (IOException ignore) {
                            }
                        }
                    });
                });
            }
        } catch (Exception ignore) {
        }


        stopWatch.stop();
        System.out.println("处理结束》》》》》》》》》》》》》》》》》》》》》");
        System.out.println("共处理数据：" + genesExpressionList.size() + "条， 耗时：" + stopWatch.getTotalTimeSeconds() + "秒");
    }
}

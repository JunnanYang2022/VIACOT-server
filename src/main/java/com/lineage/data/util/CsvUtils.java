package com.lineage.data.util;

import com.lineage.chart.entity.GenesExpression;
import com.opencsv.CSVReader;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author YangJunNan
 * @description
 * @date 2021/9/26
 */
public class CsvUtils {

    public static ConcurrentHashMap<String, String> getMaps(String filePath) throws Exception {
        CSVReader csvReader = new CSVReader(new FileReader(filePath));
        //读取到第一行 是小标题
        String[] titles = csvReader.readNext();
        ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();
        while (true) {
            String[] content = csvReader.readNext();
            if (content == null) {
                break;
            }
            final String nodeId = content[1];
            final String nodeName = content[2];
            map.put(nodeId, nodeName);
        }
        return map;
    }

    public static void main(String[] args) throws Exception {
        String rootPath = "C:\\Users\\XT.LAPTOP-16FLO4CF\\Desktop\\tree1\\tree1";
        String outputPath = "C:\\Users\\XT.LAPTOP-16FLO4CF\\Desktop\\tree1\\gene";
        File file = new File(rootPath);
        try (FileWriter writer = new FileWriter(outputPath + "\\1.sql")) {
            for (File f : file.listFiles()) {
                String fileName = f.getName();
                if (!fileName.substring(fileName.lastIndexOf(".") + 1).equals("csv")) {
                    continue;
                }
                // 基因名称
                String geneName = fileName.substring(0, fileName.lastIndexOf("."));


                CSVReader csvReader = new CSVReader(new FileReader(f));
                //读取到第一行 是小标题
                List<String> titlesList = Arrays.asList(csvReader.readNext());
                List<String> titles = titlesList.subList(1, titlesList.size());

                Map<String, BigDecimal> currentCellBolt = new HashMap<>();


                while (true) {
                    String[] next = csvReader.readNext();
                    if (next == null) {
                        break;
                    }

                    List<String> contentList = Arrays.asList(next);

                    // 节点名称
                    final String nodeName = contentList.get(1);

                    if (currentCellBolt.get(nodeName) != null) {
                        continue;
                    }

                    // 基因表达量
                    BigDecimal bolt = new BigDecimal(contentList.get(6));

                    if (StringUtils.isBlank(geneName)) {
                        continue;
                    }

                    if (BigDecimal.ZERO.compareTo(bolt) > 0) {
                        bolt = BigDecimal.ZERO;
                    }
                    GenesExpression genesExpression = new GenesExpression();
                    genesExpression.setGeneName(geneName);
                    genesExpression.setNodeName(nodeName);
                    genesExpression.setExpressionOrBlot(bolt.intValue());
                    genesExpression.setTreeId("tree1");

                    currentCellBolt.put(nodeName, bolt);
                    writer.write(genesExpression.toString());

                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

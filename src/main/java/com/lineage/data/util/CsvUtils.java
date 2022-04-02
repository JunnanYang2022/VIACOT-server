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
import java.util.List;
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
        String rootPath = "D:\\YJN\\3\\";
        String outputPath = "D:\\YJN\\output\\";
        File file = new File(rootPath);
        for (File f : file.listFiles()) {

            String name = f.getName();
            if (!name.substring(name.lastIndexOf(".") + 1).equals("csv")) {
                continue;
            }
            name = name.substring(0, name.lastIndexOf("."));

            CSVReader csvReader = new CSVReader(new FileReader(f));
            //读取到第一行 是小标题
            List<String> titlesList = Arrays.asList(csvReader.readNext());
            List<String> titles = titlesList.subList(1, titlesList.size());

            try (FileWriter writer = new FileWriter(outputPath + "\\" + name + ".sql")) {

                while (true) {
                    String[] next = csvReader.readNext();
                    if (next == null) {
                        break;
                    }

                    List<String> contentList = Arrays.asList(next);
                    List<String> content = contentList.subList(1, titlesList.size());

                    final String geneName = contentList.get(0);

                    if (StringUtils.isBlank(geneName)) {
                        continue;
                    }

                    for (int i = 0; i < content.size(); i++) {
                        try {
                            String s = content.get(i);

                            if (StringUtils.isBlank(s) || "".equals(s)) {
                                continue;
                            }

                            if (s.equals(geneName)) {
                                continue;
                            }

                            BigDecimal expression = new BigDecimal(s);
                            if (BigDecimal.ZERO.equals(expression)) {
                                continue;
                            }

                            String finalName = name;
                            GenesExpression genesExpression = new GenesExpression();
                            genesExpression.setGeneName(geneName);
                            genesExpression.setNodeName(titles.get(i));
                            genesExpression.setExpressionOrBlot(expression.intValue());
                            genesExpression.setTreeId(finalName);

                            writer.write(genesExpression.toString());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

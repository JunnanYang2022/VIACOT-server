package com.lineage.data.service;

import cn.hutool.core.io.IoUtil;
import com.lineage.chart.entity.LineageTree;
import com.lineage.data.util.CsvUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author YangJunNan
 * @description
 * @date 2022/3/6
 */
public class DealCsvAndDocx {

    private final static String ROOT_PATH = "D:\\YJN\\20220306\\";

    private final static String PMA_CSV = ROOT_PATH + "pma.csv";
    private final static String MATCH_S = ROOT_PATH + "matchs.txt";

    private final static String CEL_CSV = ROOT_PATH + "cel.csv";
    private final static String MATCH_T = ROOT_PATH + "matcht.txt";

    private final static String OUTPUT_SQL_FILE_PATH = ROOT_PATH + "pmaAndCelSql.sql";

    private final static String SPLIT = "###";

    public static void main2(String[] args) throws Exception {
        List<LineageTree> matchsLineageList = getLineageTreeList(MATCH_T);
        ConcurrentHashMap<String, String> pmaMap = CsvUtils.getMaps(PMA_CSV);

        List<LineageTree> matchtLineageList = getLineageTreeList(MATCH_T);
        ConcurrentHashMap<String, String> celMap = CsvUtils.getMaps(CEL_CSV);

        try (FileWriter writer = new FileWriter(OUTPUT_SQL_FILE_PATH)) {
            String root1 = "INSERT tb_lineage_tree(node_id ,node_name ,ancestor_id ,generation ,tree_id)" +
                    "VALUES(" +
                    "'root'," +
                    "'P.Marina'," +
                    "''," +
                    +1 + "," +
                    "'P.Marina'" +
                    ");\n";

            String root2 = "INSERT tb_lineage_tree(node_id ,node_name ,ancestor_id ,generation ,tree_id)" +
                    "VALUES(" +
                    "'root'," +
                    "'C.elegans'," +
                    "''," +
                    +1 + "," +
                    "'C.elegans'" +
                    ");\n";
            writer.write(root1);
            writer.write(root2);
            for (LineageTree e : matchsLineageList) {
                e.setTreeId("P.Marina");
                e.setNodeName(StringUtils.isBlank(pmaMap.get(e.getNodeId())) ? e.getNodeId() :
                        pmaMap.get(e.getNodeId()));
                writer.write(e.toSqlString());
            }

            for (LineageTree e : matchtLineageList) {
                e.setTreeId("C.elegans");
                e.setNodeName(StringUtils.isBlank(celMap.get(e.getNodeId())) ? e.getNodeId() :
                        celMap.get(e.getNodeId()));
                writer.write(e.toSqlString());
            }
        } catch (Exception e) {

        }

    }

    public static void main1(String[] args) throws Exception {
        List<LineageTree> matchsLineageList = getLineageTreeList(MATCH_S);

        List<LineageTree> matchtLineageList = getLineageTreeList(MATCH_T);


        try (FileWriter writer = new FileWriter(ROOT_PATH + "compare.txt")) {
            for (int i = 0; i < matchsLineageList.size(); i++) {
                String s = matchsLineageList.get(i).getNodeId();
                String t = matchtLineageList.get(i).getNodeId();
                writer.write("第" + (i + 1) + "代:" + "PMA:" + s + "###"
                        + "CEL:" + t + "，是否对应上：" + s.equals(t) +
                        "\n");
            }
        } catch (Exception e) {

        }


    }


    public static List<LineageTree> getLineageTreeList(String filePath) throws FileNotFoundException {
        List<LineageTree> result = new ArrayList<>();

        FileReader reader = new FileReader(filePath);
        List<String> line = new ArrayList<>();
        IoUtil.readLines(reader, line);

        line.forEach(currentLine -> {
            List<LineageTree> lineageTreeList = Arrays.asList(currentLine.split(SPLIT)).parallelStream().map(e -> {
                LineageTree lineageTree = new LineageTree();
                String parentId = e.substring(0, e.length() - 1);
                if (e.length() == 1) {
                    parentId = "root";
                }
                lineageTree.setNodeId(e);
                lineageTree.setAncestorId(parentId);
                lineageTree.setGeneration(e.length() + 1);
                return lineageTree;
            }).collect(Collectors.toList());
            result.addAll(lineageTreeList);
        });
        return result;
    }

    public static void main(String[] args) throws FileNotFoundException {
        String root = "D:\\YJN\\20220328\\";

        FileReader reader = new FileReader(root + "hro.alm");
        List<String> line = new ArrayList<>();
        IoUtil.readLines(reader, line);
        List<LineageTree> lineageTreeList = new ArrayList<>();

        HashMap<String, String> exist = new HashMap<>();

        line.forEach(e -> {
            String[] split = e.split("\t");
            String nodeId = split[0];
            for (int i = 1; i <= nodeId.length(); i++) {

                String currentid = nodeId.substring(0, i);
                if (exist.get(currentid) != null) {
                    continue;
                }
                LineageTree lineageTree1 = new LineageTree();
                lineageTree1.setTreeId("hro");
                lineageTree1.setNodeName(currentid);
                lineageTree1.setNodeId(currentid);
                lineageTree1.setAncestorId(i == 1 ? "root" : currentid.substring(0, currentid.length() - 1));
                lineageTree1.setGeneration(i + 1);

                exist.put(currentid, currentid);
                lineageTreeList.add(lineageTree1);
            }

            int generation = nodeId.length();
            LineageTree lineageTree = new LineageTree();
            lineageTree.setTreeId("hro");
            lineageTree.setNodeName(nodeId);
            lineageTree.setNodeId(nodeId);
            lineageTree.setAncestorId(nodeId.substring(0, nodeId.length() - 1));
            lineageTree.setGeneration(generation + 1);

            exist.put(nodeId, nodeId);
            lineageTreeList.add(lineageTree);
        });

        try (FileWriter writer = new FileWriter(root + "hro.sql")) {
            String root1 = "INSERT tb_lineage_tree(node_id ,node_name ,ancestor_id ,generation ,tree_id)" +
                    "VALUES(" +
                    "'root'," +
                    "'hro'," +
                    "''," +
                    +1 + "," +
                    "'hro'" +
                    ");\n";

            writer.write(root1);
            for (LineageTree e : lineageTreeList) {
                e.setTreeId("hro");
                e.setNodeName(e.getNodeId());
                writer.write(e.toSqlString());
            }

        } catch (Exception e) {
            System.out.println("error");
        }
    }
}

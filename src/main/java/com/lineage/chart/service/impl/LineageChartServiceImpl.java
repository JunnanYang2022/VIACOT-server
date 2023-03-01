package com.lineage.chart.service.impl;

import cn.hutool.core.lang.UUID;
import com.alibaba.fastjson.JSONArray;
import com.lineage.chart.constant.CellTypeConstant;
import com.lineage.chart.entity.CellType;
import com.lineage.chart.entity.GenesExpression;
import com.lineage.chart.entity.ItemStyle;
import com.lineage.chart.entity.LineageTree;
import com.lineage.chart.mapper.CellTypeMapper;
import com.lineage.chart.mapper.GenesExpressionMapper;
import com.lineage.chart.mapper.LineageChartMapper;
import com.lineage.chart.qo.ConstructQO;
import com.lineage.chart.qo.SearchQo;
import com.lineage.chart.service.LineageChartService;
import com.lineage.chart.vo.GeneCompareTreeChartVO;
import com.lineage.chart.vo.SearchVO;
import com.lineage.chart.vo.SimilarityVO;
import com.lineage.chart.vo.TreeChartVO;
import com.lineage.data.util.NewickTree;
import com.lineage.utils.Similarity;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

/**
 * @author YangJunNan
 * @description
 * @date 2020/12/6
 */
@Service
public class LineageChartServiceImpl implements LineageChartService {

    private final static Integer ONE = 1;
    private final static Integer TWO = 2;
    private final static Integer THREE = 3;

    @Resource
    private LineageChartMapper mapper;

    @Resource
    private CellTypeMapper cellTypeMapper;

    @Resource
    private GenesExpressionMapper genesExpressionMapper;

    @Override
    public List<TreeChartVO> constructLineageTreeData(ConstructQO qo) {

        if (StringUtils.isNotBlank(qo.getNodeName())) {
            // 节点名称不为空  则查出此节点作为根节点构造树
            TreeChartVO root = mapper.queryOneByNodeName(qo.getTreeId(), qo.getNodeName());
            List<TreeChartVO> all = mapper.queryLineageTreeData(qo.getTreeId(), root.getGeneration());
            return getTree(Collections.singletonList(root), all, null);
        }

        Integer generation = 1;
        List<TreeChartVO> all = mapper.queryLineageTreeData(qo.getTreeId(), generation);

        //过滤出某代的数据作为一级节点
        List<TreeChartVO> root =
                all.parallelStream().filter(e -> e.getGeneration().equals(generation)).collect(Collectors.toList());

        List<CellType> cellTypeList = cellTypeMapper.selectListByTreeId(qo.getTreeId());
        Map<String, String> cellTypeMap =
                cellTypeList.stream().collect(Collectors.toConcurrentMap(CellType::getCellType,
                        CellType::getColor));
        return getTree(root, all, cellTypeMap);
    }

    private List<TreeChartVO> getTree(List<TreeChartVO> root, List<TreeChartVO> all, Map<String, String> cellTypeMap) {
        // 注入样式和value
        all.parallelStream().forEach(e -> {
            String value = "Generation:" + e.getGeneration() + ";  Species:" + e.getSpecies();
            if (StringUtils.isNotBlank(e.getCellDiscription())) {
                value += ";  Cell  Description:" + e.getCellDiscription();
            }
            e.setValue(value);

            ItemStyle itemStyle = new ItemStyle();
            if (cellTypeMap != null && StringUtils.isNotBlank(e.getCellType())) {
                String color = cellTypeMap.get(e.getCellType());
                itemStyle.setBorderColor(color);
                itemStyle.setBorderWidth(8);
            }
            e.setItemStyle(itemStyle);

        });
        settChildrenNode(root, all);
        return root;
    }

    @Override
    public GeneCompareTreeChartVO constructGeneCompareTreeData(ConstructQO qo) {

        Integer generation = 1;
        List<TreeChartVO> lineageTree;

        if (StringUtils.isNotBlank(qo.getNodeName())) {
            String[] split = qo.getNodeName().split("\\(");
            qo.setNodeName(split[0]);
            // 节点名称不为空  则查出此节点作为根节点构造树
            TreeChartVO treeChartVO = mapper.queryOneByNodeName(qo.getTreeId(), qo.getNodeName());

            lineageTree = mapper.queryLineageTreeData(qo.getTreeId(), treeChartVO.getGeneration());
        } else {
            lineageTree = mapper.queryLineageTreeData(qo.getTreeId(), generation);
        }

        List<GenesExpression> genesExpressions1 = genesExpressionMapper.queryByTreeIdAndGeneName(qo.getTreeId(),
                qo.getGeneName1());

        List<GenesExpression> genesExpressions2 = genesExpressionMapper.queryByTreeIdAndGeneName(qo.getTreeId(),
                qo.getGeneName2());


        // 默认的style
        ItemStyle defaultItemStyle = new ItemStyle();
        // 基因未表达的style
        ItemStyle noDetectedItemStyle = new ItemStyle("rgba(186, 186, 186, 1)");
        // 基因1表达的style
        ItemStyle expressionItemStyle1 = new ItemStyle("rgba(255, 0, 0, 1)");
        // 基因2表达的style
        ItemStyle expressionItemStyle2 = new ItemStyle("rgba(18, 145, 45, 1)");
        List<TreeChartVO> chartList1 = new ArrayList<>();
        List<TreeChartVO> chartList2 = new ArrayList<>();

        Map<String, Integer> genesExpressions1Map = genesExpressions1.parallelStream()
                .collect(Collectors.toMap(GenesExpression::getNodeName, GenesExpression::getExpressionOrBlot));
        Map<String, Integer> genesExpressions2Map = genesExpressions2.parallelStream()
                .collect(Collectors.toMap(GenesExpression::getNodeName, GenesExpression::getExpressionOrBlot));
        lineageTree.forEach(e -> {
            TreeChartVO chart1 = new TreeChartVO();
            TreeChartVO chart2 = new TreeChartVO();

            setChartData(chart1, e);
            setChartData(chart2, e);

            Integer expressionOrBlot1 = genesExpressions1Map.get(e.getName());
            Integer expressionOrBlot2 = genesExpressions2Map.get(e.getName());


            if (Objects.isNull(expressionOrBlot1)) {
                chart1.setItemStyle(noDetectedItemStyle);
                chart1.setName(e.getName() + "(Expression Undetermined)");
            } else {
                chart1.setItemStyle(expressionOrBlot1 <= 0 ? defaultItemStyle : expressionItemStyle1);
                chart1.setExpressionOrBlot(expressionOrBlot1);
                chart1.setName(e.getName() + "(" + expressionOrBlot1 + ")");
            }
            if (Objects.isNull(expressionOrBlot2)) {
                chart2.setItemStyle(noDetectedItemStyle);
                chart2.setName(e.getName() + "(Expression Undetermined)");
            } else {
                chart2.setItemStyle(expressionOrBlot2 <= 0 ? defaultItemStyle : expressionItemStyle2);
                chart2.setExpressionOrBlot(expressionOrBlot2);
                chart2.setName(e.getName() + "(" + expressionOrBlot2 + ")");
            }

            chartList1.add(chart1);
            chartList2.add(chart2);
        });


        List<TreeChartVO> root1;
        List<TreeChartVO> root2;

        if (StringUtils.isNotBlank(qo.getNodeName())) {
            root1 =
                    chartList1.parallelStream().filter(e -> qo.getNodeName().equals(e.getName().split("\\(")[0])).collect(Collectors.toList());
            root2 =
                    chartList2.parallelStream().filter(e -> qo.getNodeName().equals(e.getName().split("\\(")[0])).collect(Collectors.toList());
        } else {
            root1 =
                    chartList1.parallelStream().filter(e -> e.getGeneration().equals(generation)).collect(Collectors.toList());
            root2 =
                    chartList2.parallelStream().filter(e -> e.getGeneration().equals(generation)).collect(Collectors.toList());
        }

        // 构造value
        root1.parallelStream().forEach(e -> {
            String value = "Generation:" + e.getGeneration() + ";  Species:" + e.getSpecies();
            if (StringUtils.isNotBlank(e.getCellDiscription())) {
                value += ";  Cell  Description:" + e.getCellDiscription();
            }
            e.setValue(value);
        });

        // 构造value
        root2.parallelStream().forEach(e -> {
            String value = "Generation:" + e.getGeneration() + ";  Species:" + e.getSpecies();
            if (StringUtils.isNotBlank(e.getCellDiscription())) {
                value += ";  Cell  Description:" + e.getCellDiscription();
            }
            e.setValue(value);
        });

        settChildrenNode(root1, chartList1);
        settChildrenNode(root2, chartList2);

        return new GeneCompareTreeChartVO(root1, root2);
    }

    @Override
    public GeneCompareTreeChartVO constructCompareTreeData(ConstructQO qo) {
        Integer generation = 1;

        List<TreeChartVO> treeChartVOS1 = mapper.queryLineageTreeData(qo.getTreeId1(), generation);
        //过滤出某代的数据作为一级节点
        List<TreeChartVO> root1 =
                treeChartVOS1.parallelStream().filter(e -> e.getGeneration().equals(generation)).collect(Collectors.toList());


        List<TreeChartVO> treeChartVOS2 = mapper.queryLineageTreeData(qo.getTreeId2(), generation);
        //过滤出某代的数据作为一级节点
        List<TreeChartVO> root2 =
                treeChartVOS2.parallelStream().filter(e -> e.getGeneration().equals(generation)).collect(Collectors.toList());

        // 构造value
        List<CellType> cellTypeList1 = cellTypeMapper.selectListByTreeId(qo.getTreeId1());
        Map<String, String> cellTypeMap1 =
                cellTypeList1.stream().collect(Collectors.toConcurrentMap(CellType::getCellType,
                        CellType::getColor));
        treeChartVOS1.parallelStream().forEach(e -> {
            String value = "Generation:" + e.getGeneration() + ";  Species:" + e.getSpecies();
            if (StringUtils.isNotBlank(e.getCellDiscription())) {
                value += ";  Cell  Description:" + e.getCellDiscription();
            }
            e.setValue(value);

            setTypeColor(qo.getTreeId1(), e, cellTypeMap1);
        });

        // 构造value
        List<CellType> cellTypeList2 = cellTypeMapper.selectListByTreeId(qo.getTreeId2());
        Map<String, String> cellTypeMap2 =
                cellTypeList2.stream().collect(Collectors.toConcurrentMap(CellType::getCellType,
                        CellType::getColor));
        treeChartVOS2.parallelStream().forEach(e -> {
            String value = "Generation:" + e.getGeneration() + ";  Species:" + e.getSpecies();
            if (StringUtils.isNotBlank(e.getCellDiscription())) {
                value += ";  Cell  Description:" + e.getCellDiscription();
            }
            e.setValue(value);

            setTypeColor(qo.getTreeId2(), e, cellTypeMap2);
        });

        settChildrenNode(root1, treeChartVOS1);
        settChildrenNode(root2, treeChartVOS2);

        GeneCompareTreeChartVO result = new GeneCompareTreeChartVO();
        result.setTreeData1(root1);
        result.setTreeData2(root2);
        return result;
    }

    private void setTypeColor(String treeId, TreeChartVO e, Map<String, String> cellTypeMap) {
        if ("P.Marina".equals(treeId)) {
            String type = CellTypeConstant.getPmaMap().get(e.getNodeId());
            if (StringUtils.isNotBlank(type)) {
                String color = cellTypeMap.get(type);
                if (StringUtils.isNotBlank(color)) {
                    ItemStyle itemStyle = new ItemStyle();
                    itemStyle.setBorderColor(color);
                    itemStyle.setBorderWidth(9);
                    e.setItemStyle(itemStyle);
                }
            }
        } else if ("C.elegans".equals(treeId)) {
            String type = CellTypeConstant.getCelMap().get(e.getNodeId());
            if (StringUtils.isNotBlank(type)) {
                String color = cellTypeMap.get(type);
                if (StringUtils.isNotBlank(color)) {
                    ItemStyle itemStyle = new ItemStyle();
                    itemStyle.setBorderColor(color);
                    itemStyle.setBorderWidth(9);
                    e.setItemStyle(itemStyle);
                }
            }
        }
    }


    @Override
    public void upload(MultipartFile file) {
        String content = readFileContent(file);
        //每个顶级节点都会作为此对象的一个元素解析进来
        List<LineageTree> source = JSONArray.parseArray(content, LineageTree.class);
        //所有父子节点  都作为单个节点存入target 待入库
        List<LineageTree> target = new ArrayList<>();
        source.forEach(e -> {
            //设置顶级节点的节点id 顶级节点传了节点id就取之，没有默认uuid
            e.setNodeId(StringUtils.isBlank(e.getNodeId()) ? UUID.fastUUID().toString() : e.getNodeId());
            //设置顶级节点的代数为1 顶级节点传了代数  就取数据的  没有默认第一代
            e.setGeneration(e.getGeneration() == null ? 1 : e.getGeneration());
            //设置顶级节点的数id 顶级节点传了树id就取之，没有默认uuid
            e.setTreeId(StringUtils.isBlank(e.getTreeId()) ? UUID.fastUUID().toString() : e.getTreeId());
            target.add(e);
            //从顶级节点开始递归
            recursiveAnalysis(e, target);
        });
        if (!CollectionUtils.isEmpty(target)) {
            mapper.insertBatch(target);
        }
    }

    @Override
    public List<TreeChartVO> uploadNewick(MultipartFile file) {
        String content = readFileContent(file);

        List<LineageTree> lineageTreeList = NewickTree.newickToLineageTreeList(content);

        List<TreeChartVO> all = lineageTreeList.stream().map(e -> {
            TreeChartVO treeChartVO = new TreeChartVO();
            treeChartVO.setNodeId(e.getNodeId());
            treeChartVO.setName(e.getNodeName());
            treeChartVO.setAncestorId(e.getAncestorId());
            treeChartVO.setGeneration(e.getGeneration());
            return treeChartVO;
        }).collect(Collectors.toList());

        //过滤出某代的数据作为一级节点
        List<TreeChartVO> root =
                all.parallelStream().filter(e -> e.getGeneration().equals(1)).collect(Collectors.toList());

        return getTree(root, all, null);
    }

    @Override
    public List<SearchVO> search(SearchQo qo) {
        List<SearchVO> search = mapper.search(qo);
        Integer cellNum = qo.getCellNum();
        if (cellNum != null) {
            if (ONE.equals(cellNum)) {
                search = search.parallelStream().filter(e -> e.getCellNum() < 500).collect(Collectors.toList());
            }

            if (TWO.equals(cellNum)) {
                search =
                        search.parallelStream().filter(e -> e.getCellNum() >= 500 && e.getCellNum() <= 1000).collect(Collectors.toList());
            }

            if (THREE.equals(cellNum)) {
                search = search.parallelStream().filter(e -> e.getCellNum() > 1000).collect(Collectors.toList());
            }
        }
        return search;
    }


    @Override
    public SimilarityVO getSimilarity(ConstructQO qo) {
        SimilarityVO vo = new SimilarityVO();
        List<TreeChartVO> chartVOList = mapper.queryLineageTreeData(qo.getTreeId(), 1);
        List<GenesExpression> genesExpressions1 = genesExpressionMapper.queryByTreeIdAndGeneName(qo.getTreeId(),
                qo.getGeneName1());
        List<GenesExpression> genesExpressions2 = genesExpressionMapper.queryByTreeIdAndGeneName(qo.getTreeId(),
                qo.getGeneName2());

        vo.setNodeNum(chartVOList.size());
        vo.setInaccurate(genesExpressions1.stream().filter(e -> e.getExpressionOrBlot() > 0).count() < 10
                || genesExpressions2.stream().filter(e -> e.getExpressionOrBlot() > 0).count() < 10);

        if (qo.getGeneName1().equals(qo.getGeneName2())) {
            vo.setSimilarity(1d);
            return vo;
        }

        ConcurrentMap<String, Integer> genesExpressions1Map =
                genesExpressions1.parallelStream().collect(Collectors.toConcurrentMap(GenesExpression::getNodeName,
                        GenesExpression::getExpressionOrBlot));

        ConcurrentMap<String, Integer> genesExpressions2Map =
                genesExpressions2.parallelStream().collect(Collectors.toConcurrentMap(GenesExpression::getNodeName,
                        GenesExpression::getExpressionOrBlot));

        Similarity gene1 = new Similarity();
        Similarity gene2 = new Similarity();
        chartVOList.parallelStream().forEach(e -> {
            Integer gene1Expression = genesExpressions1Map.get(e.getName());
            gene1.ratingMap.put(e.getName(), gene1Expression == null ? 0d : gene1Expression.doubleValue());

            Integer gene2Expression = genesExpressions2Map.get(e.getName());
            gene2.ratingMap.put(e.getName(), gene2Expression == null ? 0d : gene2Expression.doubleValue());
        });
        vo.setSimilarity(gene1.getSimilarity(gene2));
        return vo;
    }

    @Override
    public List<CellType> getCellTypeList(String treeId) {
        return cellTypeMapper.selectListByTreeId(treeId);
    }


    /**
     * 递归解析数据
     *
     * @param parent 父节点
     */
    public void recursiveAnalysis(LineageTree parent, List<LineageTree> target) {
        if (CollectionUtils.isEmpty(parent.getChildren())) {
            return;
        }
        parent.getChildren().forEach(children -> {
            //设置本节点的节点id 传了取数据  没传默认uuid
            children.setNodeId(StringUtils.isBlank(children.getNodeId()) ? UUID.fastUUID().toString() :
                    children.getNodeId());
            //设置祖先节点id
            children.setAncestorId(parent.getNodeId());
            //根据祖先节点的代数 加 1  设置本节点代数。
            children.setGeneration(parent.getGeneration() + 1);
            //从父节点获取此棵树的id
            children.setTreeId(parent.getTreeId());
            target.add(children);
            recursiveAnalysis(children, target);
        });

    }

    /**
     * 递归构造子节点数据
     *
     * @param parent      父节点
     * @param allDataList 所有数据集合
     */
    private void settChildrenNode(List<TreeChartVO> parent, List<TreeChartVO> allDataList) {
        parent.forEach(p -> {
            List<TreeChartVO> children =
                    allDataList.stream().filter(e -> p.getNodeId().equals(e.getAncestorId()) && !p.getGeneration().equals(e.getGeneration())).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(children)) {
                settChildrenNode(children, allDataList);
                p.setChildren(children);
            }
        });
    }


    private void setChartData(TreeChartVO target, TreeChartVO source) {
        target.setNodeId(source.getNodeId());
        target.setAncestorId(source.getAncestorId());
        target.setGeneration(source.getGeneration());
        target.setName(source.getName());
        target.setValue(source.getValue());
        target.setExpressionOrBlot(source.getExpressionOrBlot());
        target.setChildren(source.getChildren());
        target.setItemStyle(source.getItemStyle());
    }

    private String readFileContent(MultipartFile file) {
        try (InputStream inputStream = file.getInputStream()) {
            byte[] buffer = new byte[1024];
            int len;
            StringBuilder sb = new StringBuilder();
            while ((len = inputStream.read(buffer)) != -1) {
                sb.append(new String(buffer, 0, len));
            }
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("read file error!");
        }
    }
}

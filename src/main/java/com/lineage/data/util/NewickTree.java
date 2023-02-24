package com.lineage.data.util;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.FileUtil;
import com.lineage.chart.entity.LineageTree;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.Charset;
import java.util.*;

/**
 * Created on 12/18/16
 *
 * @author @author <a href="mailto:[email protected]">Adam Knapp</a>
 * @version 0.1
 */
public class NewickTree {

    private static int node_uuid = 0;
    ArrayList<Node> nodeList = new ArrayList<>();
    private Node root;

    public ArrayList<Node> getNodeList() {
        return nodeList;
    }

    public static int getNode_uuid() {
        return node_uuid;
    }

    public Node getRoot() {
        return root;
    }

    static NewickTree readNewickFormat(String newick) {
        return new NewickTree().innerReadNewickFormat(newick);
    }

    private static String[] split(String s) {

        ArrayList<Integer> splitIndices = new ArrayList<>();

        int rightParenCount = 0;
        int leftParenCount = 0;
        for (int i = 0; i < s.length(); i++) {
            switch (s.charAt(i)) {
                case '(':
                    leftParenCount++;
                    break;
                case ')':
                    rightParenCount++;
                    break;
                case ',':
                    if (leftParenCount == rightParenCount) splitIndices.add(i);
                    break;
            }
        }

        int numSplits = splitIndices.size() + 1;
        String[] splits = new String[numSplits];

        if (numSplits == 1) {
            splits[0] = s;
        } else {

            splits[0] = s.substring(0, splitIndices.get(0));

            for (int i = 1; i < splitIndices.size(); i++) {
                splits[i] = s.substring(splitIndices.get(i - 1) + 1, splitIndices.get(i));
            }

            splits[numSplits - 1] = s.substring(splitIndices.get(splitIndices.size() - 1) + 1);
        }

        return splits;
    }

    private NewickTree innerReadNewickFormat(String newick) {

        // single branch = subtree (?)
        this.root = readSubtree(newick.substring(0, newick.length() - 1));

        return this;
    }

    private Node readSubtree(String s) {

        int leftParen = s.indexOf('(');
        int rightParen = s.lastIndexOf(')');

        if (leftParen != -1 && rightParen != -1) {

            String name = s.substring(rightParen + 1);
            String[] childrenString = split(s.substring(leftParen + 1, rightParen));

            Node node = new Node(name);
            node.children = new ArrayList<>();
            for (String sub : childrenString) {
                Node child = readSubtree(sub);
                node.children.add(child);
                child.parent = node;
            }

            nodeList.add(node);
            return node;
        } else if (leftParen == rightParen) {

            Node node = new Node(s);
            nodeList.add(node);
            return node;

        } else {
            throw new RuntimeException("unbalanced()'s");
        }
    }

    static class Node {
        final String name;
        final int weight;
        boolean realName = false;
        ArrayList<Node> children;
        Node parent;


        public int getWeight() {
            return weight;
        }

        public boolean isRealName() {
            return realName;
        }

        public ArrayList<Node> getChildren() {
            return children;
        }

        public Node getParent() {
            return parent;
        }

        /**
         * @param name name in "actualName:weight" format, weight defaults to zero if colon absent
         */
        Node(String name) {

            int colonIndex = name.indexOf(':');
            String actualNameText;
            if (colonIndex == -1) {
                actualNameText = name;
                weight = 0;
            } else {
                actualNameText = name.substring(0, colonIndex);
//                weight = Integer.parseInt(name.substring(colonIndex + 1, name.length()));
                weight = 0;
            }

            if (actualNameText.equals("")) {
                this.realName = false;
                this.name = Integer.toString(node_uuid);
                node_uuid++;
            } else {
                this.realName = true;
                this.name = actualNameText;
            }
        }

        public boolean hasChild() {
            return children != null && children.size() > 0;
        }

        @Override
        public int hashCode() {
            return name.hashCode();
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof Node)) {
                return false;
            }
            Node other = (Node) o;
            return this.name.equals(other.name);
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            if (children != null && children.size() > 0) {
                sb.append("(");
                for (int i = 0; i < children.size() - 1; i++) {
                    sb.append(children.get(i).toString());
                    sb.append(",");
                }
                sb.append(children.get(children.size() - 1).toString());
                sb.append(")");
            }
            if (name != null) {
                sb.append(this.getName());
            }
            return sb.toString();
        }

        String getName() {
//            if (realName)
            return name;
//            else
//                return "";
        }
    }

    @Override
    public String toString() {
        return root.toString() + ";";
    }


    public static void main(String[] args) {
        String rootPath = "D:\\YJN\\20220620\\gene_exp\\";

        File file = new File(rootPath);
        for (File newickFile : file.listFiles()) {
            String fileName = newickFile.getName().substring(0, newickFile.getName().lastIndexOf("_"));
            String content = FileUtil.readString(newickFile, Charset.forName("UTF-8"));
            NewickTree newickTree = NewickTree.readNewickFormat(content);

            Node root = newickTree.getRoot();

            List<LineageTree> list = new ArrayList<>();
            Integer generation = 1;
            each(root, list, generation, fileName);
            Map<String, Object> model = new HashMap<>();
            model.put("list", list);
            try (FileOutputStream os = new FileOutputStream(rootPath + fileName + ".xlsx")) {
                JxlsUtils.exportExcel("E:\\IdeaSpace\\Cloud\\lineageChart\\src\\main\\resources\\poi\\newick.xlsx",
                        os, model);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void each(Node node, List<LineageTree> list, Integer generation, String treeId) {
        ArrayList<Node> children = node.getChildren();
        String name = node.getParent() == null ? "" : node.getParent().getName();
        System.out.println(node.getName() + "\t" + name);

        LineageTree lineageTree = new LineageTree();

        lineageTree.setNodeId(node.getName());
        lineageTree.setNodeName(node.getName());
        lineageTree.setAncestorId(name);
        lineageTree.setSpecies("");
        lineageTree.setTreeId(treeId);
        lineageTree.setGeneration(generation);

        list.add(lineageTree);

        generation++;

        if (CollectionUtil.isEmpty(children)) {
            return;
        }
        final Integer i = generation;
        children.forEach(e -> each(e, list, i, treeId));
    }

} 
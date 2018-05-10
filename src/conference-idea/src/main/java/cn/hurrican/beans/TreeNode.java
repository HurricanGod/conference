package cn.hurrican.beans;

import cn.hurrican.utils.StringUtil;

import java.util.*;
import java.util.stream.Collectors;

public class TreeNode{
    public Integer key = 0;
    public String value;
    public String html;
    public TreeNode father = null;
    public Boolean isDeleted = false;
    public Boolean hasStrongTag = false;
    public List<Entry<String, Entry<String,String>>> strongTagList = null;
    public Map<String, List<String>> elementMap = new HashMap<>(4);

    public List<TreeNode> subNode = new ArrayList<>();

    public TreeNode putHtmlElementToMap(String ele, String text){
        if(this.elementMap.containsKey(ele)){
            this.elementMap.get(ele).add(text);
        }else{
            ArrayList<String> list = new ArrayList<>(4);
            list.add(text);
            this.elementMap.put(ele,list);
        }
        return this;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public TreeNode(Integer key, String value) {
        this.key = key;
        this.value = value;
    }

    public TreeNode() {
    }

    public TreeNode(Integer key) {
        this.key = key;
    }

    public TreeNode(String value) {
        this.value = value;
    }

    public TreeNode addNode(TreeNode node){
        this.subNode.add(node);
        return this;
    }

    public TreeNode removeChild(TreeNode child, Boolean...debug){
        boolean removeSuccess = this.subNode.remove(child);
        if(debug.length > 0 && debug[0]){
            System.out.println("removeSuccess = " + removeSuccess);
        }
        return this;
    }

    public static void simplifyTree(){

    }

    /**
     * 简化 DOM 树结构
     * @param root
     * @param debug
     */
    public static void simplifyTree(TreeNode root, Boolean...debug){
        if(root.subNode.size() == 0 && StringUtil.isEmpty(root.value)){
            root.isDeleted = true;
        }else if(root.subNode.size() > 0){
            for (int i = 0; i < root.subNode.size(); i++) {
                simplifyTree(root.subNode.get(i));
            }
            List<TreeNode> needDelete = root.subNode.stream()
                    .filter(TreeNode::getDeleted).collect(Collectors.toList());
            if(debug.length > 0 && debug[0]){
                System.out.println(StringUtil.debug("before remove root.subNode.size() = {}\n" +
                        "needDelete.size() = {}", root.subNode.size(), needDelete.size()));
            }
            root.subNode.removeAll(needDelete);
            if(debug.length > 0 && debug[0]){
                System.out.println(StringUtil.debug("after remove root.subNode.size() = {}\n", root.subNode.size()));
            }

            needDelete.forEach(e->e.father=null);
            if(root.subNode.size() == 0 && StringUtil.isEmpty(root.value)){
                root.isDeleted = true;
            }else if(root.subNode.size() == 1 && StringUtil.isEmpty(root.value)){
                TreeNode grandfather = root.father;
                if(grandfather != null){
                    int index = grandfather.subNode.indexOf(root);
                    grandfather.subNode.remove(root);
                    TreeNode son = root.subNode.get(0);
                    son.key = root.key;
                    grandfather.subNode.add(index, son);
                    son.father = grandfather;
                    root.father = null;
                    root.subNode = null;
                }
            }
        }
    }

    /**
     *
     * @param root 简化后 DOM 树根节点
     * @param lineMinLength 节点字符串最小长度，低于这个长度的字符串会被过滤不会出现在 list 里
     * @return
     */
    public static List<String> convertHtmlPageToString(TreeNode root, int lineMinLength){
        ArrayList<String> list = new ArrayList<>();
        ArrayDeque<TreeNode> deque = new ArrayDeque<>();
        for (int i = 0; i < root.subNode.size(); i++) {
            deque.offer(root.subNode.get(i));
        }
        while (deque.size() > 0){
            TreeNode first = deque.pollFirst();
            if(StringUtil.isNotEmpty(first.value) && first.value.length() > lineMinLength){
                list.add(first.value);
            }
            if(first.subNode.size() > 0){
                first.subNode.forEach(deque::offer);
            }
        }
        return list;
    }



    public static void main(String[] args) {
        ArrayDeque<Integer> deque = new ArrayDeque<>();
        for (int i = 0; i < 5; i++) {
            deque.offer(i+1);
        }

        System.out.println("deque = " + deque);
        System.out.println();
        Integer first = deque.pollFirst();
        Integer second = deque.pollFirst();
        System.out.println("first = " + first);
        System.out.println("second = " + second);
    }

}

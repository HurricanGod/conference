package cn.hurrican.utils;

import cn.hurrican.beans.Entry;
import cn.hurrican.beans.TreeNode;
import com.google.common.base.Strings;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class CrawlerUtils {

    private static List<String> strong_tag = new ArrayList<>();

    static {
        strong_tag.add("h1");
        strong_tag.add("h2");
        strong_tag.add("h3");
        strong_tag.add("h4");
        strong_tag.add("h5");
        strong_tag.add("strong");
    }

    public static String downloadSourceCode(String url){
        CloseableHttpResponse closeableHttpResponse = HttpClientUtils.doGet(url, null);
        int statusCode = closeableHttpResponse.getStatusLine().getStatusCode();
        System.out.println("statusCode = " + statusCode);
        String sourceCode = HttpClientUtils.toString(closeableHttpResponse);
        System.out.println("sourceCode = \n" + sourceCode);
        if(statusCode == 200){
            return sourceCode;
        }
        return "";
    }

    private static void dfsDomTree(Object node){
        if(node instanceof Comment){
            ((Comment) node).remove();
            System.out.println("删除注释节点");
        }else if(node instanceof Element){
            List<Node> childNodes = ((Element) node).childNodes();
            for (int i = 0; i < childNodes.size(); i++) {
                dfsDomTree(childNodes.get(i));
            }
        }else if(node instanceof Elements){
            for (int i = 0; i < ((Elements) node).size(); i++) {
                dfsDomTree(((Elements) node).get(i));
            }
        }
    }

    private static void dfsDomTree(Object node, TreeNode root){
        if(node instanceof Comment){
            ((Comment) node).remove();
        }else if(node instanceof Element){
            List<Node> childNodes = ((Element) node).childNodes();
            for (int i = 0; i < childNodes.size(); i++) {
                Node node1 = childNodes.get(i);
                TreeNode treeNode = new TreeNode(root.key + 1);
                treeNode.father = root;
                treeNode.html = node1.outerHtml();
                findStrongContent(treeNode);
                if(node1 instanceof TextNode){
                    if(StringUtil.isNotEmpty(((TextNode) node1).text())){
                        root.addNode(treeNode);
                        dfsDomTree(node1, treeNode);
                    }
                }else{
                    root.addNode(treeNode);
                    dfsDomTree(node1, treeNode);
                }
            }
        }else if(node instanceof TextNode){
            root.value = ((TextNode) node).text();
        }
    }

    public static void parseHtml(String doc){
        Document document = Jsoup.parse(doc);
        Tag tag = document.tag();
        // 删除 script 标签
        Elements script = document.select("script");
        script.remove();
        Elements style = document.select("style");
        style.remove();

        Element children = document.body();
        TreeNode root = new TreeNode();
        dfsDomTree(children, root);
        TreeNode.dfsTree(root);
        List<String> content = TreeNode.convertHtmlPageToString(root, 2);
        content.forEach(System.out::println);
    }

    public static void findStrongContent(TreeNode node){
        String html = node.html;
        Document document = Jsoup.parse(html);
        strong_tag.forEach(ele -> {
            Elements select = document.select(ele);
            if(select.size() > 0){
                for (int i = 0; i < select.size(); i++) {
                    Element element = select.get(i);
                    String tagName = element.tag().getName();
                    node.hasStrongTag = true;
                    if(node.strongTagList == null){
                        node.strongTagList = new ArrayList<>(4);
                    }
                    node.strongTagList.add(new Entry<>(tagName,
                            new Entry<>(element.text(),element.parent().text())));
                }
            }

        });


    }

    public static String repairUrl(String url){
        if(!Strings.isNullOrEmpty(url)){
            if(!url.startsWith("http")){
                return "http://" + url;
            }
            return url;
        }
        return "";
    }


    public static void main(String[] args) {
        String url = "http://www.cvnis.net/bigdata2018/";
//        String url = "http://icatse.org/icisa2018/";

        System.currentTimeMillis();
        System.out.println("url = " + url);
        String sourceCode = downloadSourceCode(url);
        parseHtml(sourceCode);
    }
}

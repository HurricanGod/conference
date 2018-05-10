package cn.hurrican.utils;

import cn.hurrican.beans.Entry;
import cn.hurrican.beans.TreeNode;
import com.google.common.base.Strings;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
//        System.out.println("sourceCode = \n" + sourceCode);
        if(statusCode == 200){
            return sourceCode;
        }
        return "";
    }

    public static Map<String, String> getSourceCode(String url){
        HashMap<String, String> map = new HashMap<>();
        CloseableHttpResponse closeableHttpResponse = HttpClientUtils.doGet(url, null);
        if(closeableHttpResponse == null){
            return map;
        }
        Integer statusCode = closeableHttpResponse.getStatusLine().getStatusCode();
        System.out.println("statusCode = " + statusCode);
        String sourceCode = HttpClientUtils.toString(closeableHttpResponse);
        map.put("sourceCode", sourceCode);
        map.put("statusCode", statusCode.toString());
        return map;
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

    /**
     * 解析HTML页面获取主要内容
     * @param doc HTML页面源码
     * @return {"mainContent":"Set &lt String &gt","fullText":"List &lt String &gt","domRoot":"TreeNode"}
     */
    public static Map<String, Object> parseHtml(String doc){
        Map<String, Object>  result = new HashMap<>();
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
        TreeNode.simplifyTree(root);
        Set<Entry<String, Entry<String,String>>> strongTag = new HashSet<>();
        calculateElementCount(root, strongTag);
        List<String> mainContent = extractMainContent(strongTag);
        if(mainContent.size() > 0){
            System.out.println("mainContent[0] = " + mainContent.get(0));
            System.out.println("mainContent[1] = " + mainContent.get(1));
            System.out.println("mainContent[2] = " + mainContent.get(2));
            System.out.println();
        }
        List<String> content = TreeNode.convertHtmlPageToString(root, 2);
        content.forEach(System.out::println);
        Set<String> collect = mainContent.stream().limit(3).collect(Collectors.toSet());
        result.put("mainContent", collect);
        result.put("fullText", content);
        result.put("domRoot", root);
        return result;
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

    public static void calculateElementCount(TreeNode root,
                                             Set<Entry<String, Entry<String,String>>> strongTag){
        if(root.subNode.size() == 0){
            return;
        }else{
            for (int i = 0; i < root.subNode.size(); i++) {
                TreeNode node = root.subNode.get(i);
                Document doc = Jsoup.parse(node.html);
                doc.select("body").get(0).childNodes().forEach(ele -> {
                    if(ele instanceof Element){
                        root.putHtmlElementToMap(((Element) ele).tag().getName(),
                                ((Element) ele).text());
                    }
                });
                if(root.hasStrongTag){
                    strongTag.addAll(root.strongTagList);
                }
                calculateElementCount(node, strongTag);
            }
        }
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

    public static List<String> extractMainContent(Set<Entry<String, Entry<String,String>>> strongTag){
        return strongTag.stream().filter(ele -> !ele.getKey().equals("strong"))
                .sorted(Comparator.comparing(Entry::getKey)).map(ele -> ele.getValue().getValue())
                .collect(Collectors.toList());
    }


    public static void main(String[] args) {
//        String url = "http://www.cvnis.net/bigdata2018/";
        List<String> urlLit = new ArrayList<>();
        urlLit.add("http://icatse.org/icisa2018/");
        urlLit.add("http://www.indiaconf.org/index.html");
        urlLit.add("http://semstats.org/2018");
        urlLit.add("http://icacie.com/2018/");
        urlLit.add("http://www.icmnpe.org");
        urlLit.add("http://www.icwsnuca.com/");
        urlLit.add("http://www.icivc.org/");
        urlLit.add("http://aiaat.org/");
        urlLit.add("http://www.ismse.net/");
        urlLit.add("https://fedcsis.org/2018/");
        urlLit.add("http://cscn2018.ieee-cscn.org/");
        urlLit.add("http://ieee-hpec.org/");
        urlLit.add("http://icaest.org/");

        for (int i = 0; i < urlLit.size(); i++) {
           try{
               String url = urlLit.get(i);
               System.out.println("url = " + url);
               String sourceCode = downloadSourceCode(url);
               parseHtml(sourceCode);
           }catch(Exception e){
               e.printStackTrace();
           }
            System.out.println("*   *    *    *    *    *    *    *    *    *    *    *    *  ");
        }
    }
}

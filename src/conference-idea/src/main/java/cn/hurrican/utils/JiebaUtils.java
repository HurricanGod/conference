package cn.hurrican.utils;

import com.huaban.analysis.jieba.JiebaSegmenter;
import com.huaban.analysis.jieba.SegToken;
import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;

public class JiebaUtils {

    @Test
    public void testMethod0(){
        String keywords = "The 13th World Congress on       Engineering Asset ";
        String[] s = keywords.split(" +");
        for (int i = 0; i < s.length; i++) {
            System.out.println("s = " + s[i]);
        }
    }

    public static void main(String[] args) {
        JiebaSegmenter segmenter = new JiebaSegmenter();
        String sentence = "The 13th World Congress on Engineering Asset Management 2018";
        List<String> words = segmenter.process(sentence, JiebaSegmenter.SegMode.SEARCH).stream()
                .filter(e -> e.word.length() > 1)
                .map(e -> e.word)
                .sorted((o1, o2) -> o2.length() - o1.length())
                .collect(Collectors.toList());


        words.forEach(System.out::println);
        System.out.println();
        List<SegToken> segTokens = segmenter.process(sentence, JiebaSegmenter.SegMode.INDEX);
        segTokens.forEach(System.out::println);
    }
}

//package com.xukeer.test.server;
//
//import de.l3s.boilerpipe.BoilerpipeProcessingException;
//import de.l3s.boilerpipe.document.TextBlock;
//import de.l3s.boilerpipe.document.TextDocument;
//import de.l3s.boilerpipe.extractors.*;
//import de.l3s.boilerpipe.sax.BoilerpipeSAXInput;
//import org.apache.html.dom.HTMLTitleElementImpl;
//import org.xml.sax.InputSource;
//
//import javax.imageio.ImageIO;
//import java.awt.image.RenderedImage;
//import java.io.*;
//import java.net.MalformedURLException;
//import java.net.URL;
//import java.net.URLConnection;
//import java.util.List;
//import java.util.Vector;
//
///**
// * @author xqw
// * @description
// * @date 9:33 2022/1/27
// **/
//public class HtmlTest {
//    public static void main(String[] args) throws Exception {
//        URL url= new URL("https://mp.weixin.qq.com/s?__biz=MzA3OTk0MjcxMg==&mid=2647825043&idx=1&sn=d0dbd34a9e496ffadbc88fd14f6656e3&chksm=878dffc4b0fa76d2b30d27785706d57a29edaf2dd26e14b97e43524141fb5edc982baa714886");
//        InputStream in = url.openConnection().getInputStream();
//        BufferedReader br = new BufferedReader(new InputStreamReader(in, "utf-8"));
//        StringBuilder sb = new StringBuilder();
//        String reads;
//        while ((reads = br.readLine()) != null) {
//            sb.append(reads);
//        }
//        String html = sb.toString();
//        String content;
////        String content = ArticleExtractor.INSTANCE.getText(html);
////        System.out.println(content);
//
//        BoilerpipeSAXInput boilerpipeSAXInput =   new BoilerpipeSAXInput(new InputSource(
//                new StringReader(html)));
//        TextDocument textDocument= boilerpipeSAXInput.getTextDocument();
//
//        List<TextBlock> textBlockList= textDocument.getTextBlocks();
//        textBlockList.forEach(textBlock -> {
//            System.out.println(textBlock.getText());
//        });
//
//        // content =  KeepEverythingExtractor.INSTANCE.getText(html);
//        // System.out.println(content);
//        // content = ArticleSentencesExtractor.getInstance().getText(html);
//        // System.out.println(content);
//    }
//
//}

package com.wantdo.stat.template;

import com.lowagie.text.pdf.BaseFont;
import org.w3c.dom.Document;
import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.io.OutputStream;

/**
 * @ Date : 16/4/6
 * @ From : stat
 * @ Author : luanx@wantdo.com
 */
public class PDFHelper {

    /**
     * Output a pdf to the specified outputstream
     *
     * @param htmlStr the htmlstr
     * @param out     the specified outputstream
     * @throws Exception
     */
    public static void generate(String htmlStr, OutputStream out)
            throws Exception {
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document doc = builder.parse(new ByteArrayInputStream(htmlStr.getBytes()));
        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocument(doc, null);
        // 解决中文问题
        ITextFontResolver fontResolver = renderer.getFontResolver();
        renderer.getFontResolver().addFont("/template/simsun.ttc",
                BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
        renderer.layout();
        renderer.createPDF(out);
        out.close();
    }

}

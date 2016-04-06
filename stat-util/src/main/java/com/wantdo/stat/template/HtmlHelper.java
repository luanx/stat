package com.wantdo.stat.template;

import freemarker.template.Configuration;
import freemarker.template.Template;

import java.io.BufferedWriter;
import java.io.StringWriter;
import java.util.Map;

/**
 * @ Date : 16/4/6
 * @ From : stat
 * @ Author : luanx@wantdo.com
 */
public class HtmlHelper {

    /**
     * Generate html string.
     *
     * @param template  the name of freemarker teamlate.
     * @param variables the data of teamlate.
     * @return htmlStr
     * @throws Exception
     */
    public static String generate(String template, Map<String, Object> variables) throws Exception {
        Configuration config = FreemarkerConfiguration.getConfiguation();
        Template tp = config.getTemplate(template);
        StringWriter stringWriter = new StringWriter();
        BufferedWriter writer = new BufferedWriter(stringWriter);
        tp.setEncoding("UTF-8");
        tp.process(variables, writer);
        String htmlStr = stringWriter.toString();
        writer.flush();
        writer.close();
        return htmlStr;
    }

}

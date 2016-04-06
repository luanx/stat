package com.wantdo.stat.utils;

import com.wantdo.stat.entity.account.User;
import com.wantdo.stat.template.HtmlHelper;
import com.wantdo.stat.template.PDFHelper;
import org.junit.Test;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ Date : 16/4/5
 * @ From : stat
 * @ Author : luanx@wantdo.com
 */
public class TemplateTest {

    @Test
    public void generatePDF() throws Exception{

        try {
            String outputFile = "template.pdf";
            Map<String, Object> variables = new HashMap<String, Object>();

            List<User> userList = new ArrayList<User>();

            User tom = new User();
            tom.setName("tom");
            User amy = new User();
            amy.setName("amy");

            userList.add(tom);
            userList.add(amy);

            variables.put("userList", userList);

            String htmlStr = HtmlHelper.generate("template.ftl", variables);

            OutputStream out = new FileOutputStream(outputFile);
            PDFHelper.generate(htmlStr, out);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}

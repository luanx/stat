package com.wantdo.stat.email;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;

/**
 * MIME邮件服务类
 *
 * @Date : 2015-9-23
 * @From : stat
 * @Author : luanx@wantdo.com
 */
public class MimeMailService {

    private static final String DEFAULT_ENCODING = "utf-8";

    private JavaMailSender mailSender;

    private Template template;

    private static Logger logger = LoggerFactory.getLogger(MimeMailService.class);

    public void sendNotificationMail(String userName){
        try {
            MimeMessage msg = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(msg, true, DEFAULT_ENCODING);

            helper.setFrom("luanx@wantdo.com");
            helper.setTo("luanx@wantdo.com");
            helper.setSubject("用户修改通知");

            String content = generateContent(userName);
            helper.setText(content, true);

            File attachment = generateAttachment();
            helper.addAttachment("mailAttachment.txt", attachment);

            mailSender.send(msg);
            logger.info("HTML版邮件已发送至luanx@wantdo.com");

        } catch (MessagingException e){
            logger.error("构造邮件失败", e);
        } catch (Exception e){
            logger.error("发送邮件失败", e);
        }
    }

    private File generateAttachment() throws MessagingException{
        try {
            Resource resource = new ClassPathResource("/email/mailAttachment.txt");
            return resource.getFile();
        } catch (IOException e){
            logger.error("构造邮件失败，附件文件不存在", e);
            throw new MessagingException("附件文件不存在", e);
        }
    }

    private String generateContent(String userName) throws MessagingException{

        try {
            Map context = Collections.singletonMap("userName", userName);
            return FreeMarkerTemplateUtils.processTemplateIntoString(template, context);
        } catch (IOException e){
            logger.error("生成邮件内容失败，Freemarker模板不存在", e);
            throw new MessagingException("FreeMarker模板不存在", e);
        } catch (TemplateException e){
            logger.error("生成邮件内容失败，Freemarker处理失败", e);
            throw new MessagingException("FreeMarker处理失败", e);
        }

    }

    /**
     * Spring的MailSender
     */
    public void setMailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void setFreemarkerConfiguration(Configuration freemarkerConfiguration) throws IOException{
        template = freemarkerConfiguration.getTemplate("mailTemplate.ftl", DEFAULT_ENCODING);
    }

}

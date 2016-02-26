package com.wantdo.stat.email;


import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.Date;

/**
 * @Date : 2015-9-23
 * @From : stat
 * @Author : luanx@wantdo.com
 */
public class SimpleMailService {

    private static Logger logger = LoggerFactory.getLogger(SimpleMailService.class);

    private JavaMailSender mailSender;
    private String textTemplate;

    public void sendNotificationMail(String userName){
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom("luanx@wantdo.com");
        msg.setTo("luanx@wantdo.com");
        msg.setSubject("用户修改通知");

        String content = String.format(textTemplate, userName, new Date());
        msg.setText(content);

        try {
            mailSender.send(msg);
            if (logger.isInfoEnabled()){
                logger.info("纯文本邮件已发送至{}", StringUtils.join(msg.getTo(), ","));
            }
        } catch (Exception e){
            logger.error("发送邮件失败", e);
        }
    }

    /**
     * Spring的MailSender
     */
    public void setMailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void setTextTemplate(String textTemplate) {
        this.textTemplate = textTemplate;
    }
}

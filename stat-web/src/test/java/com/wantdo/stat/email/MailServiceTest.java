package com.wantdo.stat.email;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.GreenMailUtil;
import com.wantdo.stat.test.spring.SpringContextTestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @Date : 2015-9-23
 * @From : stat
 * @Author : luanx@wantdo.com
 */

@ContextConfiguration(locations = {"/applicationContext.xml", "/email/applicationContext-email.xml"})
public class MailServiceTest extends SpringContextTestCase{

    @Autowired
    private SimpleMailService simpleMailService;

    @Autowired
    private MimeMailService mimeMailService;

    @Autowired
    private GreenMail greenMail;

    @Test
    public void sendSimpleMail() throws MessagingException, InterruptedException, IOException{
        simpleMailService.sendNotificationMail("luanx");

        greenMail.waitForIncomingEmail(2000, 1);

        MimeMessage[] messages = greenMail.getReceivedMessages();
        MimeMessage message = messages[messages.length - 1];

        assertThat(message.getFrom()[0].toString()).isEqualTo("luanx@wantdo.com");
        assertThat(message.getSubject()).isEqualTo("用户修改通知");
        System.out.println(message.getContent());
    }

    @Test
    public void sendMimeMail() throws MessagingException, InterruptedException, IOException{
        mimeMailService.sendNotificationMail("luanx");

        greenMail.waitForIncomingEmail(2000, 1);
        MimeMessage[] messages = greenMail.getReceivedMessages();
        MimeMessage message = messages[messages.length - 1];

        assertThat(message.getFrom()[0].toString()).isEqualTo("luanx@wantdo.com");

        MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();

        assertThat(message.getSubject()).isEqualTo("用户修改通知");

        assertThat(mimeMultipart.getCount()).isEqualTo(2);

        //HTML格式的主邮件
        String mainPartText = getMainPartText(mimeMultipart.getBodyPart(0));
        System.out.println(mainPartText);
        assertThat(mainPartText).contains("<h1>用户luanx被修改</h1>");

        //附件
        assertThat(GreenMailUtil.getBody(mimeMultipart.getBodyPart(1)).trim()).isEqualTo("Hello,i am a attachment.");

    }

    private String getMainPartText(Part mainPart) throws MessagingException, IOException{
        return (String)((Multipart)mainPart.getContent()).getBodyPart(0).getContent();
    }

}

package com.wantdo.stat.email;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * @Date : 2015-9-23
 * @From : stat
 * @Author : luanx@wantdo.com
 */
public class MailServerSimulator implements InitializingBean, DisposableBean, FactoryBean<GreenMail> {

    public static final String DEFAULT_ACCOUNT = "greenmail@localhost.com";
    public static final String DEFAULT_PASSWORD = "greenmail";
    public static final int DEFAULT_PORT = 3025;

    private String account = DEFAULT_ACCOUNT;
    private String password = DEFAULT_PASSWORD;
    private int port = DEFAULT_PORT;

    private GreenMail greenMail;

    public void setAccount(String account) {
        this.account = account;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        greenMail = new GreenMail(new ServerSetup(port, null, ServerSetup.PROTOCOL_SMTP));
        greenMail.setUser(account, password);
        greenMail.start();
    }


    @Override
    public void destroy() throws Exception {
        if (greenMail != null){
            greenMail.stop();
        }
    }

    @Override
    public GreenMail getObject() throws Exception {
        return greenMail;
    }

    @Override
    public Class<?> getObjectType() {
        return GreenMail.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}

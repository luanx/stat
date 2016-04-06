package com.wantdo.stat.template;

import freemarker.template.Configuration;

/**
 * @ Date : 16/4/6
 * @ From : stat
 * @ Author : luanx@wantdo.com
 */
public class FreemarkerConfiguration {

    private static Configuration config = null;

    /**
     * Static initialization.
     *
     * Initialize the configuration of Freemarker.
     */
    static {
        config = new Configuration();
        config.setClassForTemplateLoading(FreemarkerConfiguration.class, "/template");
    }

    public static Configuration getConfiguation() {
        return config;
    }
}

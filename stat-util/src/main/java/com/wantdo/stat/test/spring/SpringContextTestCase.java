/*******************************************************************************
 * Copyright (c) 2005, 2014 springside.github.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *******************************************************************************/
package com.wantdo.stat.test.spring;


import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

@ActiveProfiles(Profiles.UNIT_TEST)
public abstract class SpringContextTestCase extends AbstractJUnit4SpringContextTests {
}

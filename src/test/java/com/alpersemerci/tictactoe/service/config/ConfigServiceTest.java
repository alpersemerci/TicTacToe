package com.alpersemerci.tictactoe.service.config;

import org.junit.Assert;
import org.junit.Test;
import org.junit.internal.runners.JUnit4ClassRunner;
import org.junit.runner.RunWith;

@RunWith(JUnit4ClassRunner.class)
public class ConfigServiceTest {

    private ConfigService configService = ConfigService.getInstance();

    @Test
    public void test_non_existing_getConfigValue() {
        String banner = configService.getConfigValue("non_existing_");
        Assert.assertTrue("Value should be null", banner == null);
    }

    @Test
    public void test_getConfigValue() {
        String banner = configService.getConfigValue("banner");
        Assert.assertTrue("Value should not be null", banner != null);
    }


}
package com.cqx.coasterrider.util;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class BuildMapUtilTest {
    private BuildMapUtil buildMapUtil;

    @Before
    public void setUp() throws Exception {
        buildMapUtil = new BuildMapUtil();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void buildTanke() throws IOException {
        buildMapUtil.buildTanke("d:\\tmp\\data\\show\\示意图.xlsx", 0, 0);
    }
}
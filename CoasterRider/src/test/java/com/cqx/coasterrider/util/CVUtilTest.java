package com.cqx.coasterrider.util;

import org.junit.Test;

public class CVUtilTest {

    @Test
    public void fetchFrame() throws Exception {
        CVUtil.fetchFrame("d:\\tmp\\data\\player\\_LED灯光秀_分段1.mp4"
                , "d:\\tmp\\data\\player\\%s.jpg", 100);
    }
}
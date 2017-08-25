package com.huotu.scrm.web.controller.util;

import com.huotu.scrm.common.utils.EncryptUtils;
import com.huotu.scrm.web.CommonTestBase;
import org.junit.Assert;
import org.junit.Test;

import java.util.Random;

/**
 * Created by hxh on 2017-08-25.
 */
public class EncryptUtilsTest extends CommonTestBase {
    private static final String USER_ID_SECRET_KEY = "XjvDhKLvCsm9y7G7";

    @Test
    public void test() throws Exception {
        Random rand = new Random();
        for (int i = 0; i < 100; i++) {
            int userId = rand.nextInt();
            String aesEncrypt = EncryptUtils.aesEncrypt(String.valueOf(userId), USER_ID_SECRET_KEY);
            String aesDecrypt = EncryptUtils.aesDecrypt(aesEncrypt, USER_ID_SECRET_KEY);
            Assert.assertEquals(userId, Integer.parseInt(aesDecrypt));
        }
    }
}

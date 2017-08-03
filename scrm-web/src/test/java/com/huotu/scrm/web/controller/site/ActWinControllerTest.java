package com.huotu.scrm.web.controller.site;

import com.huotu.scrm.web.CommonTestBase;
import org.junit.Test;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

/**
 * Created by hxh on 2017-08-03.
 */
public class ActWinControllerTest extends CommonTestBase {
    @Test
    public void downloadAllWinDetail() throws Exception {
        byte[] contentAsByteArray = mockMvc.perform(get("/site/downloadWinDetail")
        )
                .andReturn().getResponse().getContentAsByteArray();


        File dir = new File("D:\\");
        if (!dir.exists() && dir.isDirectory()) {//判断文件目录是否存在
            dir.mkdirs();
        }
        File file = new File(dir + "中奖信息.xls");
        FileOutputStream fos = new FileOutputStream(file);
        BufferedOutputStream bos = new BufferedOutputStream(fos);
        bos.write(contentAsByteArray);
        bos.flush();
    }
}

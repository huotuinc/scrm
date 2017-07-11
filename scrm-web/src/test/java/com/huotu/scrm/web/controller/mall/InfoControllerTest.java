package com.huotu.scrm.web.controller.mall;

import com.huotu.scrm.service.entity.info.Info;
import com.huotu.scrm.service.service.InfoServer;
import com.huotu.scrm.web.CommonTestBase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import static org.junit.Assert.*;

/**
 * Created by luohaibo on 2017/7/11.
 */
public class InfoControllerTest extends CommonTestBase{


    private Log logger = LogFactory.getLog(InfoControllerTest.class);

    @Autowired
    InfoServer infoServer;


    @Test
    public void searchInfoListTitleLike() throws Exception {

    }

    @Test
    public void getInfoListsAccount() throws Exception {

    }

    @Test
    public void getInfoListPageable() throws Exception {

    }

    @Test
    public void saveInfo() throws Exception {

        Info info = new Info();
//        info.setTitle("李克强崎岖山路颠簸一小时考察脱贫攻坚");
        info.setTitle("李");
//        info.setContent("【李克强崎岖山路颠簸一小时考察脱贫攻坚】沿着崎岖山路，李克强10日乘车颠簸一小时，深入陕西宝鸡坪头镇大湾河村考察脱贫攻坚。总理走访两户人家询问收入、医保等情况，坐在院子里与村民交流。该村已被列入易地扶贫搬迁计划，村民们都盼着尽快搬迁。总理说，期待你们搬迁后生活有奔头，过上好日子。");
        info.setContent("【李");
        Info saveInfo =  infoServer.infoSave(info);
        logger.info(saveInfo);
    }

}
package com.huotu.scrm.web.controller.mall;

import com.huotu.scrm.service.entity.info.InfoBrowse;
import com.huotu.scrm.service.service.InfoBrowseServer;
import com.huotu.scrm.web.CommonTestBase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import static org.junit.Assert.*;

/**
 * Created by luohaibo on 2017/7/12.
 */
public class InfoBrowseControllerTest extends CommonTestBase{


    @Autowired
    InfoBrowseServer infoBrowseServer;

    @Test
    @Rollback(false)
    public void insertBrowse(){
        InfoBrowse infoBrowse = new InfoBrowse();
        infoBrowse.setInfoId(new Long(123));
        infoBrowse.setReadUserId(new Long(1234));
        infoBrowse.setSourceUserId(new Long(123456));
        infoBrowseServer.infoBroseSave(infoBrowse);
    }

}
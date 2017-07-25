package com.huotu.scrm.web.controller.mall;

import com.huotu.scrm.service.entity.info.InfoBrowse;
import com.huotu.scrm.service.model.info.InfoBrowseAndTurnSearch;
import com.huotu.scrm.service.service.info.InfoBrowseServer;
import com.huotu.scrm.web.CommonTestBase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;

import java.util.List;
import java.util.function.Consumer;

/**
 * Created by luohaibo on 2017/7/12.
 */
public class InfoBrowseControllerTest extends CommonTestBase{


    @Autowired
    InfoBrowseServer infoBrowseServer;

//    @Test
//    @Rollback(false)
//    public void insertBrowse(){
//        InfoBrowse infoBrowse = new InfoBrowse();
//        infoBrowse.setInfoId(new Long(123));
//        infoBrowse.setReadUserId(new Long(1234));
//        infoBrowse.setSourceUserId(new Long(123456));
//        infoBrowseServer.infoBroseSave(infoBrowse);
//    }

    @Test
    public void turnRecord(){

        InfoBrowse infoBrowse = new InfoBrowse();
        infoBrowse.setInfoId(1232L);
        Pageable pageable = new PageRequest(0,20);
        InfoBrowseAndTurnSearch infoBrowseAndTurnSearch = new InfoBrowseAndTurnSearch();
        infoBrowseAndTurnSearch.setSourceUserId(2L);
        Page<InfoBrowse> list = infoBrowseServer.infoTurnRecord(infoBrowseAndTurnSearch);


        list.getContent().stream()
                .forEach(System.out::println);


    }

}
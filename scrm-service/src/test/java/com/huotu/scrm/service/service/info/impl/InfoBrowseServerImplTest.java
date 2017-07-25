package com.huotu.scrm.service.service.info.impl;

import com.huotu.scrm.service.CommonTestBase;
import com.huotu.scrm.service.entity.info.InfoBrowse;
import com.huotu.scrm.service.repository.info.InfoBrowseRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;

import static java.awt.SystemColor.info;
import static org.junit.Assert.*;

/**
 * Created by luohaibo on 2017/7/25.
 */
public class InfoBrowseServerImplTest extends CommonTestBase{
    @Autowired
    private InfoBrowseRepository infoBrowseRepository;

    @Test
    public void deleteInfoTurnRecord() throws Exception {
        Long infoId=1232L,sourceUserId=1L;
        infoBrowseRepository.updateInfoTurn(infoId,sourceUserId,true);
    }


    @Test
    public void infoBrowseRecord() throws Exception {
        Pageable pageable = new PageRequest(0, 20);
        Page<InfoBrowse> page = infoBrowseRepository.findAllBrowseRecord(1232L, 4421L, false, pageable);

        page.getContent()
                .stream().forEach(System.out::println);

    }
}
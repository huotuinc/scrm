package com.huotu.scrm.service.service.info.impl;

import com.huotu.scrm.service.CommonTestBase;
import com.huotu.scrm.service.entity.info.InfoBrowse;
import com.huotu.scrm.service.repository.info.InfoBrowseRepository;
import com.huotu.scrm.service.repository.info.InfoRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

/**
 * Created by luohaibo on 2017/7/25.
 */
public class InfoBrowseServerImplTest extends CommonTestBase{
    @Autowired
    private InfoBrowseRepository infoBrowseRepository;
    @Autowired
    private InfoRepository infoRepository;

    @Test
    public void deleteInfoTurnRecord() throws Exception {
        Long infoId=1232L,sourceUserId=1L;
        infoBrowseRepository.updateInfoTurn(infoId,sourceUserId,true);
    }


    @Test
    public void infoBrowseRecord() throws Exception {
        Pageable pageable = new PageRequest(0, 20);
        Page<InfoBrowse> page = infoBrowseRepository.findAllBrowseRecord(1232L, 4421L, pageable);

        page.getContent()
                .stream().forEach(System.out::println);

    }


    @Test
    public void deleteBrowseRecord(){
        int result =  infoBrowseRepository.updateBrowseInfo(1232L,1L,237L,true);
        int b = 0;
    }


    @Test
    public void infoSiteBrowseRecord(){
        Pageable pageable = new PageRequest(0, 6);
        Page<InfoBrowse> page =  infoBrowseRepository.findAllBrowseRecordByLimit(10L,4421L,pageable);
        int b = 0;
    }
}
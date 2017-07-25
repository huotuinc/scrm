package com.huotu.scrm.service.service.info.impl;

import com.huotu.scrm.service.CommonTestBase;
import com.huotu.scrm.service.repository.info.InfoBrowseRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import static org.junit.Assert.*;

/**
 * Created by luohaibo on 2017/7/25.
 */
public class InfoBrowseServerImplTest extends CommonTestBase{
    @Autowired
    private InfoBrowseRepository infoBrowseRepository;

    @Test
    @Rollback(value = false)
    public void deleteInfoTurnRecord() throws Exception {
        Long infoId=1232L,sourceUserId=1L;
        infoBrowseRepository.updateInfoTurn(infoId,sourceUserId,true);
    }

}
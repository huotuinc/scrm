package com.huotu.scrm.service.service.info;

import com.huotu.scrm.service.CommonTestBase;
import com.huotu.scrm.service.entity.info.Info;
import com.huotu.scrm.service.model.info.InfoExcelModel;
import com.huotu.scrm.service.model.info.InformationSearch;
import com.huotu.scrm.service.repository.info.InfoRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by xyr on 2017/10/24.
 */
public class InfoServiceTest extends CommonTestBase {

    @Autowired
    private InfoService infoService;
    @Autowired
    private InfoRepository repository;

    @Test
    public void queryInfoWithBrowse() throws Exception {
        InformationSearch search = new InformationSearch();
        search.setCustomerId(Long.parseLong("4421"));
        List<Object[]> infoList = infoService.queryInfoWithBrowse(search);
        assertNotEquals(null, infoList);
    }

}
package com.huotu.scrm.service.service.Impl;

import com.huotu.scrm.service.repository.InforListRepository;
import com.huotu.scrm.service.service.InforServer;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by luohaibo on 2017/7/5.
 */
public class InforServerImpl implements InforServer {

    @Autowired
    private InforListRepository inforListRepository;


    public int inforListAllCount() {
        return inforListRepository.findAll().size();
    }
}

package com.huotu.scrm.service.service.Impl;

import com.huotu.scrm.service.repository.InfoRepository;
import com.huotu.scrm.service.service.InforServer;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by luohaibo on 2017/7/5.
 */
public class InforServerImpl implements InforServer {

    @Autowired
    private InfoRepository infoRepository;


    public int inforListAllCount() {
        return infoRepository.findAll().size();
    }
}

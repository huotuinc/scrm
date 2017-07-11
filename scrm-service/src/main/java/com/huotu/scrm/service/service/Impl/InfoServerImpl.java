package com.huotu.scrm.service.service.Impl;

import com.huotu.scrm.service.repository.InfoRepository;
import com.huotu.scrm.service.service.InfoServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by luohaibo on 2017/7/5.
 */
@Service
public class InfoServerImpl implements InfoServer {

    @Autowired
    private InfoRepository infoRepository;


    public long infoListsCount() {
        return infoRepository.count();
    }
}

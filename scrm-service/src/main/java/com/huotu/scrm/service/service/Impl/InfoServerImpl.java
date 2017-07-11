package com.huotu.scrm.service.service.Impl;

import com.huotu.scrm.service.entity.Information.Info;
import com.huotu.scrm.service.repository.InfoRepository;
import com.huotu.scrm.service.service.InfoServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    public Page<Info> infoSList(byte disable, Pageable pageable) {
        return null;
    }

    public void infoSave(Info info) {

    }

    public void deleteInfoById(Long id) {

    }

    public void updateInfoExtendStatusById(Long id) {

    }
}

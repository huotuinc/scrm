package com.huotu.scrm.service.service.info.impl;
import com.huotu.scrm.service.entity.info.InfoBrowse;
import com.huotu.scrm.service.model.info.InfoBrowseAndTurnSearch;
import com.huotu.scrm.service.repository.info.InfoBrowseRepository;
import com.huotu.scrm.service.service.info.InfoBrowseServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


/**
 * Created by luohaibo on 2017/7/12.
 */
@Service
public class InfoBrowseServerImpl implements InfoBrowseServer {


    @Autowired
    InfoBrowseRepository infoBrowseRepository;

    @Override
    public void infoTurnInSave(InfoBrowse infoBrowse,Long customerId) {
        InfoBrowse infoBrowseDate =  infoBrowseRepository.findOneByInfoIdAndSourceUserIdAndReadUserId(infoBrowse.getInfoId(),
                infoBrowse.getSourceUserId(),infoBrowse.getReadUserId());
        if(infoBrowseDate==null){
            infoBrowseDate = new InfoBrowse();
            infoBrowseDate.setTurnTime(LocalDateTime.now());
            infoBrowseDate.setCustomerId(customerId);
            infoBrowseDate.setInfoId(infoBrowse.getInfoId());
            infoBrowseDate.setReadUserId(infoBrowse.getReadUserId());
            infoBrowseDate.setSourceUserId(infoBrowse.getSourceUserId());
            infoBrowseRepository.save(infoBrowseDate);
        }
    }

    @Override
    public Page<InfoBrowse> infoTurnAndBrowseList(InfoBrowseAndTurnSearch infoBrowseAndTurnSearch) {
        return null;
    }


}

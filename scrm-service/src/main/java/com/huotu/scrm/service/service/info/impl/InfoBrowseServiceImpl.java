package com.huotu.scrm.service.service.info.impl;
import com.huotu.scrm.service.entity.info.InfoBrowse;
import com.huotu.scrm.service.model.info.InfoBrowseAndTurnSearch;
import com.huotu.scrm.service.repository.info.InfoBrowseRepository;
import com.huotu.scrm.service.service.info.InfoBrowseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;


/**
 * Created by luohaibo on 2017/7/12.
 */
@Service
public class InfoBrowseServiceImpl implements InfoBrowseService {


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
            infoBrowseDate.setBrowseTime(LocalDateTime.now());
            infoBrowseRepository.save(infoBrowseDate);
        }
    }

    @Override
    public Page<InfoBrowse> infoTurnRecord(InfoBrowseAndTurnSearch infoBrowseAndTurnSearch) {
        Pageable pageable = new PageRequest(infoBrowseAndTurnSearch.getPageNo()-1, infoBrowseAndTurnSearch.getPageSize());
       return infoBrowseRepository.findAllTurnRecordAndCustomerId(infoBrowseAndTurnSearch.getInfoId(),infoBrowseAndTurnSearch.getCustomerId(),false,pageable);
    }

    @Override
    public int updateInfoTurnRecord(InfoBrowseAndTurnSearch infoBrowseAndTurnSearch) {
        infoBrowseRepository.updateInfoTurn(infoBrowseAndTurnSearch.getInfoId(),infoBrowseAndTurnSearch.getSourceUserId(),true);
        return 0;
    }

    @Override
    public Page<InfoBrowse> infoBrowseRecord(InfoBrowseAndTurnSearch infoBrowseAndTurnSearch) {
        Pageable pageable = new PageRequest(infoBrowseAndTurnSearch.getPageNo()-1, infoBrowseAndTurnSearch.getPageSize());
        return infoBrowseRepository.findAllBrowseRecord(infoBrowseAndTurnSearch.getInfoId(),infoBrowseAndTurnSearch.getCustomerId(),false,pageable);
    }


}

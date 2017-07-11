package com.huotu.scrm.service.service.Impl;

import com.huotu.scrm.service.entity.info.Info;
import com.huotu.scrm.service.repository.InfoRepository;
import com.huotu.scrm.service.service.InfoServer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.criteria.Predicate;
import java.util.Date;
import java.util.List;

/**
 * Created by luohaibo on 2017/7/5.
 */
@Service
public class InfoServerImpl implements InfoServer {


    private Log logger = LogFactory.getLog(InfoServerImpl.class);

    @Autowired
    private InfoRepository infoRepository;


    public long infoListsCount(boolean disable) {
        return infoRepository.countByDisable(disable);
    }

    public List<Info> findListsByWord(String title) {
        return infoRepository.findByTitleLike(title);
    }


    public Info infoSave(Info info) {
        Info newInfo;
        logger.info(info.getId());
         if (info.getId() != null && info.getId() != 0) {
            logger.info("test");
            newInfo = infoRepository.findOne(info.getId());
        } else {
            newInfo = new Info();
        }
        newInfo.setTitle(info.getTitle());
        newInfo.setIntroduce(info.getIntroduce());
        newInfo.setContent(info.getContent());
        newInfo.setImageUrl(info.getImageUrl());
        newInfo.setThumbnailImageUrl(info.getThumbnailImageUrl());
        newInfo.setCreateTime(info.getCreateTime());
        newInfo.setStatus(info.isStatus());
        newInfo.setDisable(info.isDisable());
        newInfo.setExtend(info.isExtend());
        return infoRepository.save(newInfo);

    }


    public Page<Info> infoSList(boolean disable, Pageable pageable) {

        return infoRepository.findAll((root, query, cb) -> {
            Predicate predicate = cb.isTrue(cb.literal(true));
            predicate = cb.and(predicate, cb.equal(root.get("disable").as(boolean.class), disable));
            return predicate;
        }, pageable);

    }


}

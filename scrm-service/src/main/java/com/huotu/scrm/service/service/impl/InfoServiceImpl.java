package com.huotu.scrm.service.service.impl;

import com.huotu.scrm.common.utils.InformationSearch;
import com.huotu.scrm.service.entity.info.Info;
import com.huotu.scrm.service.repository.InfoRepository;
import com.huotu.scrm.service.service.InfoService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by luohaibo on 2017/7/5.
 */
@Service
public class InfoServiceImpl implements InfoService {


    private Log logger = LogFactory.getLog(InfoServiceImpl.class);

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
        if (info.getId() != null && info.getId() != 0) {
            newInfo = infoRepository.findOne(info.getId());
        } else {
            newInfo = new Info();
            newInfo.setCustomerId(info.getCustomerId());
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

    public Page<Info> infoSList(InformationSearch informationSearch) {
        Pageable pageable = new PageRequest(informationSearch.getPageNo()-1, informationSearch.getPageSize());
        return infoRepository.findAll((root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> list = new ArrayList<>();
            if (!StringUtils.isEmpty(informationSearch.getSearchCondition())){
                list.add(criteriaBuilder.like(root.get("title").as(String.class), "%" + informationSearch.getSearchCondition() + "%"));
            }
            list.add(criteriaBuilder.equal(root.get("customerId").as(Long.class), informationSearch.getCustomerId()));
            list.add(criteriaBuilder.equal(root.get("disable").as(boolean.class), informationSearch.getDisable()));
            Predicate[] p = new Predicate[list.size()];
            return criteriaBuilder.and(list.toArray(p));
        }, pageable);
    }


}

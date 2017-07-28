package com.huotu.scrm.service.service.info.impl;

import com.huotu.scrm.service.model.info.InformationSearch;
import com.huotu.scrm.service.entity.info.Info;
import com.huotu.scrm.service.repository.info.InfoRepository;
import com.huotu.scrm.service.service.info.InfoService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.time.LocalDateTime;
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
        return infoRepository.countByIsDisable(disable);
    }

    public List<Info> findListsByWord(String title) {
        return infoRepository.findByTitleLike(title);
    }


    @Override
    public Info findOneByIdAndCustomerId(Long id,Long customerId){
        Info info;
        if (id != null && id != 0){
            info = infoRepository.findOneByIdAndCustomerIdAndIsDisableFalse(id,customerId);
        }else {
            info = new Info();
        }
        return info;
    }

    public Info infoSave(Info info) {
        Info newInfo;
        if (info.getId() != null && info.getId() != 0) {
            newInfo = infoRepository.findOne(info.getId());
        } else {
            newInfo = new Info();
            newInfo.setCustomerId(info.getCustomerId());
            newInfo.setCreateTime(LocalDateTime.now());
        }
        newInfo.setTitle(info.getTitle());
        newInfo.setIntroduce(info.getIntroduce());
        newInfo.setContent(info.getContent());
        if(!StringUtils.isEmpty(info.getImageUrl()) && !StringUtils.containsWhitespace("http")){
            newInfo.setImageUrl(info.getImageUrl());
        }
        newInfo.setThumbnailImageUrl(info.getThumbnailImageUrl());
        newInfo.setStatus(info.isStatus());
        newInfo.setDisable(info.isDisable());
        newInfo.setExtend(info.isExtend());
        return infoRepository.save(newInfo);

    }

    @Override
    public boolean deleteInfo(Long customerId, Long id) {
        Info info = infoRepository.findOneByIdAndCustomerIdAndIsDisableFalse(id,customerId);
        if(info != null){
            info.setDisable(true);
            infoRepository.save(info);
            return true;
        }
        return false;
    }

    public Page<Info> infoList(InformationSearch informationSearch) {
        Pageable pageable = new PageRequest(informationSearch.getPageNo()-1, informationSearch.getPageSize(),new Sort(
            new Sort.Order(Sort.Direction.DESC,"createTime")
        ));
        return infoRepository.findAll((root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> list = new ArrayList<>();
            if (!StringUtils.isEmpty(informationSearch.getSearchCondition())){
                list.add(criteriaBuilder.like(root.get("title").as(String.class), "%" + informationSearch.getSearchCondition() + "%"));
            }
            list.add(criteriaBuilder.equal(root.get("customerId").as(Long.class), informationSearch.getCustomerId()));
            list.add(criteriaBuilder.equal(root.get("isDisable").as(boolean.class), informationSearch.getDisable()));
            Predicate[] p = new Predicate[list.size()];
            return criteriaBuilder.and(list.toArray(p));
        }, pageable);
    }


}

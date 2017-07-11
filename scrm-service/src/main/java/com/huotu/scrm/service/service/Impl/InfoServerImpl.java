package com.huotu.scrm.service.service.Impl;

import com.huotu.scrm.service.entity.info.Info;
import com.huotu.scrm.service.repository.InfoRepository;
import com.huotu.scrm.service.service.InfoServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import javax.persistence.criteria.Predicate;
import java.util.List;

/**
 * Created by luohaibo on 2017/7/5.
 */
@Service
public class InfoServerImpl implements InfoServer {

    @Autowired
    private InfoRepository infoRepository;


    public long infoListsCount(boolean disable) {
        return infoRepository.countByDisable(disable);
    }

    public List<Info> findListsByWord(String title) {
        return infoRepository.findByTitleLike(title);
    }


    public void infoSave(Info info) {
        infoRepository.saveAndFlush(info);
    }




    public Page<Info> infoSList(boolean disable, Pageable pageable) {

        return infoRepository.findAll((root, query, cb) -> {
            Predicate predicate = cb.isTrue(cb.literal(true));
            predicate = cb.and(predicate, cb.equal(root.get("disable").as(boolean.class), disable));
            return predicate;
        }, pageable);

    }



}

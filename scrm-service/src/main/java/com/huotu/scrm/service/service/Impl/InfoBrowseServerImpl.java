package com.huotu.scrm.service.service.Impl;
import com.huotu.scrm.service.entity.info.InfoBrowse;
import com.huotu.scrm.service.repository.InfoBrowseRepository;
import com.huotu.scrm.service.service.InfoBrowseServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import javax.persistence.criteria.Predicate;
import java.util.List;



/**
 * Created by luohaibo on 2017/7/12.
 */
@Service
public class InfoBrowseServerImpl implements InfoBrowseServer {


    @Autowired
    InfoBrowseRepository infoBrowseRepository;


    @Override
    public Page<InfoBrowse> infoBrowseLists(Long infoId, int page, int pageSize) {
        Pageable pageable = new PageRequest(page,pageSize);
        return infoBrowseRepository.findAll((root, query, cb) -> {
            Predicate predicate = cb.isTrue(cb.literal(true));
            predicate = cb.and(predicate, cb.equal(root.get("infoId").as(Long.class), pageable));
            return predicate;
        }, pageable);

    }

    @Override
    public long infoListsCount(Long infoId) {
        return infoBrowseRepository.countByInfoId(infoId);
    }

    @Override
    public InfoBrowse infoBroseSave(InfoBrowse infoBrowse) {

        InfoBrowse infoBrowse1;

        if (infoBrowse.getId() != null && infoBrowse.getId() != 0) {
            infoBrowse1 = infoBrowseRepository.findOne(infoBrowse.getId());
        } else {
            infoBrowse1 = new InfoBrowse();
        }
        infoBrowse1.setBrowseTime(infoBrowse.getBrowseTime());
        infoBrowse1.setCustomerId(infoBrowse.getCustomerId());
        infoBrowse1.setReadUserId(infoBrowse.getReadUserId());
        infoBrowse1.setSourceUserId(infoBrowse.getSourceUserId());
        infoBrowse1.setInfoId(infoBrowse.getInfoId());
        return infoBrowseRepository.save(infoBrowse1);
    }


    public List<InfoBrowse> InfoBrowseByInfoId(Long infoId){

        return infoBrowseRepository.findByInfoId(infoId);
    }
}

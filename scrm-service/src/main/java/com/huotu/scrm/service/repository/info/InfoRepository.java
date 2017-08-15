package com.huotu.scrm.service.repository.info;

import com.huotu.scrm.service.entity.info.Info;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


/**
 * Created by luohaibo on 2017/7/5.
 */
public interface InfoRepository extends JpaRepository<Info, Long>, JpaSpecificationExecutor<Info> {


    long countByIsDisable(boolean state);

    Info findOneByIdAndCustomerIdAndIsDisableFalse(Long id, Long customerId);

    List<Info> findByTitleLike(String title);

    List<Info> findByCustomerIdAndIsStatusTrueAndIsDisableFalseOrderByCreateTimeDesc(Long customerId);

    List<Info> findByCustomerIdAndIsExtendTrueAndIsDisableFalseOrderByCreateTimeDesc(Long customerId);

    @Query("select t from Info t where t.id  in ?1")
    List<Info> findInfoList(List<Long> infoIdList);
}


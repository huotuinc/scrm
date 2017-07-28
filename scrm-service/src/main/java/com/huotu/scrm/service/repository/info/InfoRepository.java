package com.huotu.scrm.service.repository.info;

import com.huotu.scrm.service.entity.info.Info;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;


/**
 * Created by luohaibo on 2017/7/5.
 */
public interface InfoRepository extends JpaRepository<Info, Long>, JpaSpecificationExecutor<Info> {


    long countByIsDisable(boolean state);

    Info findOneByIdAndCustomerIdAndIsDisableFalse(Long id,Long customerId);

    List<Info> findByTitleLike(String title);

    List<Info> findByCustomerIdAndIsStatusTrueAndIsDisableFalse(Long customerId);

    List<Info> findByCustomerIdAndIsExtendTrueAndIsDisableFalse(Long customerId);
}


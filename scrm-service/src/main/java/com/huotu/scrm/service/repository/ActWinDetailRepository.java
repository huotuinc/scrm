package com.huotu.scrm.service.repository;

import com.huotu.scrm.service.entity.activity.ActWinDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Created by montage on 2017/7/12.
 */

@Repository
public interface ActWinDetailRepository extends JpaRepository<ActWinDetail,Long>,JpaSpecificationExecutor<ActWinDetail>{

}

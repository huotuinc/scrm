package com.huotu.scrm.service.repository;

import com.huotu.scrm.service.entity.activity.Activity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * Created by montage on 2017/7/12.
 */

@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long> ,JpaSpecificationExecutor<Activity>{

    @Modifying
    @Query("update Activity a set a.isDelete = ?1 where a.actId =?2")
    void updateActivityByActId(boolean isDelete , long actId );

    /**
     * 分页查询所有活动
     *
     * @param isDelete
     * @param pageable
     * @return
     */
    Page<Activity> findByIsDelete(boolean isDelete, Pageable pageable);
}

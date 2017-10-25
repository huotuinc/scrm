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

    /**
     * 通过id查找对应资讯
     * @param id
     * @return
     */
    Info findOneByIdAndIsDisableFalse(Long id);

    List<Info> findByTitleLike(String title);

    List<Info> findByCustomerIdAndIsStatusTrueAndIsDisableFalseOrderByCreateTimeDesc(Long customerId);

    List<Info> findByCustomerIdAndIsExtendTrueAndIsDisableFalseOrderByCreateTimeDesc(Long customerId);

    @Query("select t from Info t where t.id  in ?1 order by t.createTime desc ")
    List<Info> findInfoList(List<Long> infoIdList);

    /*@Query("SELECT i.title, i.introduce, i.createTime, i.isStatus, i.isExtend, count(*) AS infoBrowseNum\n" +
            "FROM SCRM_Info i LEFT JOIN SCRM_InfoBrowseLog b ON i.id = b.infoId\n" +
            "WHERE i.customerId = ?1 and i.title like CONCAT('%',?2,'%') and i.Create_Time >= ?3 AND i.Create_Time <= ?4 \n" +
            "GROUP BY  i.Title, i.Introduce, i.Create_Time, i.Status, i.Extend")
    List<Info> queryInfoWithBrower();*/
}


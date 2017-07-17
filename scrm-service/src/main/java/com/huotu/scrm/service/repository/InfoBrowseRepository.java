package com.huotu.scrm.service.repository;

import com.huotu.scrm.service.entity.info.InfoBrowse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by luohaibo on 2017/7/11.
 */
public interface InfoBrowseRepository extends JpaRepository<InfoBrowse, Long>, JpaSpecificationExecutor<InfoBrowse> {


    long countByInfoId(Long infoId);


    List<InfoBrowse> findByInfoId(Long infoId);

    //根据转发用户ID和转发日期查询转发咨询的访问量
    long countBySourceUserIdAndBrowseTime(Long sourceUserId, LocalDate date);

    @Query("select distinct t.sourceUserId from  InfoBrowse t")
    List<Long> findBySourceUserId();

    //根据转发用户ID和转发时间查询咨询转发量(去掉重复浏览)
    @Query("select count(distinct t.infoId) from InfoBrowse t where t.sourceUserId=?1 and t.browseTime=?2")
    int findForwardNumBySourceUserId(Long userId, LocalDate date);

    //查询咨询的转发量
    @Query("select count(distinct t.infoId) from InfoBrowse t")
    int findInfoForwardNum(Long infoId);

}

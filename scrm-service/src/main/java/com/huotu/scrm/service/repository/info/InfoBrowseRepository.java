package com.huotu.scrm.service.repository.info;

import com.huotu.scrm.service.entity.info.InfoBrowse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by luohaibo on 2017/7/11.
 */
public interface InfoBrowseRepository extends JpaRepository<InfoBrowse, Long>, JpaSpecificationExecutor<InfoBrowse> {


    long countByInfoId(Long infoId);


    List<InfoBrowse> findByInfoId(Long infoId);

    //根据转发用户ID和转发日期查询转发咨询的访问量
    @Query("select count (t) from InfoBrowse t where  t.browseTime>=?2 and t.browseTime<?3 and t.sourceUserId=?1 ")
    int countBySourceUserIdAndBrowseTime(Long sourceUserId, LocalDateTime minDate, LocalDateTime maxDate);

    @Query("select distinct t.sourceUserId from  InfoBrowse t where t.browseTime>=?1 and t.browseTime<=?2")
    List<Long> findSourceUserIdList(LocalDateTime minDate, LocalDateTime maxDate);

    //根据转发用户ID和转发时间查询咨询转发量(去掉重复浏览)
    @Query("select count(distinct t.infoId) from InfoBrowse t where t.browseTime>=?1 and t.browseTime<?2 and t.sourceUserId=?3")
    int findForwardNumBySourceUserId(LocalDateTime minDate, LocalDateTime maxDate, Long userId);

    //查询咨询的转发量
    @Query("select  count(distinct t.sourceUserId) from InfoBrowse t where t.infoId=?1")
    int findInfoForwardNum(Long infoId);

}

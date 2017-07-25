package com.huotu.scrm.service.repository.info;

import com.huotu.scrm.service.entity.info.InfoBrowse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by luohaibo on 2017/7/11.
 */
public interface InfoBrowseRepository extends JpaRepository<InfoBrowse, Long>, JpaSpecificationExecutor<InfoBrowse> {



    InfoBrowse findOneByInfoIdAndSourceUserIdAndReadUserId(Long infoId, Long sourceId, Long readId);


    @Query("select new com.huotu.scrm.service.entity.info.InfoBrowse(t.infoId,t.sourceUserId,MIN(t.browseTime),u.weixinImageUrl,u.wxNickName) from InfoBrowse t left join User u  on  u.id = t.sourceUserId where t.infoId=?1 and t.customerId=?2 and t.isDisable=?3 group by t.infoId,t.sourceUserId,u.weixinImageUrl,u.wxNickName")
    Page<InfoBrowse> findAllBrowseRecordAndCustomerId(Long infoId, Long customerId ,boolean disable,Pageable pageable);


    @Query("update InfoBrowse t set t.isDisable=?3 where t.infoId=?1 and t.sourceUserId=?2")
    @Modifying
    void updateInfoTurn(Long infoId, Long sourceUserId,boolean disable);


    long countByInfoId(Long infoId);


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

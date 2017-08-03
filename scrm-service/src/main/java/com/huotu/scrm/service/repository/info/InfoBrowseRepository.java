package com.huotu.scrm.service.repository.info;

import com.huotu.scrm.service.entity.info.InfoBrowse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * Created by luohaibo on 2017/7/11.
 */
public interface InfoBrowseRepository extends JpaRepository<InfoBrowse, Long>, JpaSpecificationExecutor<InfoBrowse> {


    //查找资讯
    InfoBrowse findOneByInfoIdAndSourceUserIdAndReadUserId(Long infoId, Long sourceId, Long readId);

    //查询转发记录
    @Query("select new com.huotu.scrm.service.entity.info.InfoBrowse(t.infoId,t.sourceUserId,MIN(t.browseTime),u.weixinImageUrl,u.wxNickName) from InfoBrowse t left join User u  on  u.id = t.sourceUserId where t.infoId=?1 and t.customerId=?2 and t.turnDisable=?3 group by t.infoId,t.sourceUserId,u.weixinImageUrl,u.wxNickName")
    Page<InfoBrowse> findAllTurnRecordAndCustomerId(Long infoId, Long customerId, boolean disable, Pageable pageable);

    //查询浏览记录
    @Query("select new com.huotu.scrm.service.entity.info.InfoBrowse(t.infoId,t.sourceUserId,t.readUserId,t.browseTime,u.weixinImageUrl," +
            "u.wxNickName, t.customerId) from InfoBrowse t left join User u  on  u.id = t.readUserId where t.infoId=?1 and t.customerId=?2 and t.browseDisable=?3 ")
    Page<InfoBrowse> findAllBrowseRecord(Long infoId, Long customerId, boolean disable, Pageable pageable);


    /**
     * 查找前端资讯头像和昵称
     *
     * @param infoId
     * @param customerId
     * @param pageable
     * @return
     */
    @Query("select new com.huotu.scrm.service.entity.info.InfoBrowse(t.infoId,u.weixinImageUrl," +
            "u.wxNickName,t.customerId) from InfoBrowse t left join User u  on  u.id = t.readUserId where t.infoId=?1 and t.customerId=?2 order by t.browseTime")
    Page<InfoBrowse> findAllBrowseRecordByLimit(Long infoId, Long customerId, Pageable pageable);


    //删除转发记录
    @Query("update InfoBrowse t set t.turnDisable=?3 where t.infoId=?1 and t.sourceUserId=?2")
    @Modifying
    void updateInfoTurn(Long infoId, Long sourceUserId, boolean disable);

    //删除浏览记录
    @Query("update InfoBrowse t set t.browseDisable=?4 where t.infoId=?1 and t.readUserId=?2 and t.sourceUserId=?3")
    @Modifying
    int updateBrowseInfo(Long infoId, Long readUserId, Long sourceUserId, boolean disable);

    //获取资讯转发量
    @Query("SELECT COUNT(DISTINCT t.sourceUserId) from InfoBrowse t WHERE t.infoId=?1")
    int totalTurnCount(Long InfoId);

    //获取资讯浏览浏览量
    int countByInfoId(Long infoId);


    /**
     * 根据转发用户ID和转发日期查询转发咨询的访问量
     *
     * @param sourceUserId 转发来源用户ID
     * @param minTime      起始时间
     * @param maxTime      结束日时间
     * @return
     */
    int countBySourceUserIdAndBrowseTimeBetween(Long sourceUserId, LocalDateTime minTime, LocalDateTime maxTime);

    /**
     * 根据某一商户和时间获取所有的转发用户
     *
     * @param minTime 起始时间
     * @param maxTime 结束时间
     * @return
     */
    @Query("select distinct (t.sourceUserId) from  InfoBrowse t where t.browseTime>=?1 and t.browseTime<?2")
    List<Long> findSourceUserIdList(LocalDateTime minTime, LocalDateTime maxTime);

    /**
     * 根据转发用户ID和转发时间查询咨询转发量(去掉重复浏览)
     *
     * @param minDate 起始日期
     * @param maxDate 结束日期
     * @param userId  用户ID
     * @return
     */
    @Query("select new com.huotu.scrm.service.entity.info.InfoBrowse(t.infoId,t.sourceUserId)  from InfoBrowse t group by t.infoId,t.sourceUserId having min (t.browseTime)>=?1 and min (t.browseTime)<?2 and t.sourceUserId=?3 ")
    List<InfoBrowse> findForwardNumBySourceUserId(Date minDate, Date maxDate, Long userId);

    /**
     * 查询咨询的转发量
     *
     * @param infoId 咨询ID
     * @return
     */
    @Query("select new com.huotu.scrm.service.entity.info.InfoBrowse(t.infoId,t.sourceUserId)  from InfoBrowse t group by t.infoId,t.sourceUserId having  t.infoId=?1 ")
    List<InfoBrowse> findInfoForwardNum(Long infoId);

    /**
     * 查询某个商户下的所有用户（去除重复）
     *
     * @param customerId 商户ID
     * @return
     */
    @Query("select distinct (t.sourceUserId) from InfoBrowse t where t.customerId=?1")
    List<Long> findByCustomerId(Long customerId);

}

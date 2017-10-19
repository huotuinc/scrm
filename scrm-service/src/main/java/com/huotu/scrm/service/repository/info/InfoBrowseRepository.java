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

    //查找资讯
    InfoBrowse findOneByInfoIdAndSourceUserIdAndReadUserId(Long infoId, Long sourceId, Long readId);

    /**
     * 查询是否有转发记录
     *
     * @param sourceUserId 转发用户id
     * @param infoId       资讯id
     * @return
     */

    int countBySourceUserIdAndInfoId(Long sourceUserId, Long infoId);

    //查询转发记录
    @Query("select new com.huotu.scrm.service.entity.info.InfoBrowse(t.infoId,t.sourceUserId,MIN(t.browseTime),u.weixinImageUrl,u.wxNickName,u.nickName) " +
            "from InfoBrowse t left join User u  on  u.id = t.sourceUserId " +
            "where t.infoId=?1 and t.customerId=?2 and t.turnDisable=false " +
            "group by t.infoId,t.sourceUserId,u.weixinImageUrl,u.wxNickName,u.nickName")
    Page<InfoBrowse> findAllTurnRecordAndCustomerId(Long infoId, Long customerId, Pageable pageable);

    //查询浏览记录
    @Query("select new com.huotu.scrm.service.entity.info.InfoBrowse(t.infoId,t.sourceUserId,t.readUserId,t.browseTime,u.weixinImageUrl," +
            "u.wxNickName,u.nickName, t.customerId) " +
            "from InfoBrowse t left join User u  on  u.id = t.readUserId " +
            "where t.infoId=?1 and t.customerId=?2 and t.browseDisable=false ")
    Page<InfoBrowse> findAllBrowseRecord(Long infoId, Long customerId, Pageable pageable);


    /**
     * 查找前端资讯头像和昵称
     *
     * @param infoId
     * @param customerId
     * @param pageable
     * @return
     */
    @Query("select new com.huotu.scrm.service.entity.info.InfoBrowse(t.infoId,u.weixinImageUrl," +
            "u.wxNickName,t.customerId) " +
            "from InfoBrowse t left join User u  on  u.id = t.readUserId " +
            "where t.infoId=?1 and t.customerId=?2 order by t.browseTime DESC ")
    Page<InfoBrowse> findAllBrowseRecordByLimit(Long infoId, Long customerId, Pageable pageable);


    /**
     * 查找前端资讯头像和昵称通过用户
     * @param infoId
     * @param customerId
     * @param pageable
     * @return
     */
    @Query("select new com.huotu.scrm.service.entity.info.InfoBrowse(t.infoId,u.weixinImageUrl," +
            "u.wxNickName,t.customerId) " +
            "from InfoBrowse t left join User u  on  u.id = t.readUserId " +
            "where t.infoId=?1 and t.customerId=?2 and t.sourceUserId=?3 order by t.browseTime DESC")
    Page<InfoBrowse> findAllBrowseRecordBySourceUserIdByLimit(Long infoId, Long customerId, Long sourceUserId,Pageable pageable);

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

    //获取某个用户资讯浏览量
    int countByInfoIdAndSourceUserId(Long infoId,Long sourceUserId);

    /**
     * 根据转发用户ID和转发日期查询转发咨询的访问量
     *
     * @param sourceUserId 转发来源用户ID
     * @param beginTime    起始时间
     * @param endTime      结束日时间
     * @return
     */
    int countBySourceUserIdAndBrowseTimeBetween(Long sourceUserId, LocalDateTime beginTime, LocalDateTime endTime);

    /**
     * 根据某段时间获取所有的转发用户
     *
     * @param beginTime 起始时间
     * @param endTime   结束时间
     * @return
     */
    @Query("select distinct (t.sourceUserId) from  InfoBrowse t where t.browseTime>=?1 and t.browseTime<?2")
    List<Long> findSourceUserIdList(LocalDateTime beginTime, LocalDateTime endTime);

    /**
     * 根据转发用户ID和转发时间查询咨询转发量(去掉重复浏览)
     *
     * @param beginTime 起始日期
     * @param endTime   结束日期
     * @param userId    用户ID
     * @return
     */
    @Query("select new com.huotu.scrm.service.entity.info.InfoBrowse(t.infoId,t.sourceUserId)  " +
            "from InfoBrowse t " +
            "where t.sourceUserId = ?3 " +
            "group by t.infoId,t.sourceUserId " +
            "having min (t.browseTime)>=?1 and min (t.browseTime)<?2")
    List<InfoBrowse> findForwardNumBySourceUserId(LocalDateTime beginTime, LocalDateTime endTime, Long userId);

    /**
     * 查询某个用户转发的资讯
     *
     * @param userId
     * @return
     */
    @Query("select new com.huotu.scrm.service.entity.info.InfoBrowse(t.infoId,t.sourceUserId,min (t.browseTime)) from InfoBrowse t " +
            "group by t.infoId,t.sourceUserId having t.sourceUserId =?1 order by min (t.browseTime) desc")
    List<InfoBrowse> findUserForwardInfo(Long userId);

    /**
     * 查询咨询的转发量
     *
     * @param infoId 咨询ID
     * @return
     */
    @Query("select new com.huotu.scrm.service.entity.info.InfoBrowse(t.infoId,t.sourceUserId)  " +
            "from InfoBrowse t group by t.infoId,t.sourceUserId having  t.infoId=?1 ")
    List<InfoBrowse> findInfoForwardNum(Long infoId);

    /**
     * 查询某个商户某段时间下的所有用户（去除重复）
     *
     * @param customerId 商户ID
     * @return
     */
    @Query("select distinct (t.sourceUserId) from InfoBrowse t where t.customerId=?1 and t.browseTime>=?2 and t.browseTime<?3")
    List<Long> findSourceIdByCustomerId(Long customerId, LocalDateTime beginTime, LocalDateTime endTime);

    /**
     * 根据某段时间获取所有的转发用户
     *
     * @param beginTime 起始时间
     * @param endTime   结束时间
     * @return
     */
    @Query("select distinct (t.customerId) from  InfoBrowse t where t.browseTime>=?1 and t.browseTime<?2")
    List<Long> findCustomerIdList(LocalDateTime beginTime, LocalDateTime endTime);

}

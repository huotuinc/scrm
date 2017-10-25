package com.huotu.scrm.service.service.info.impl;

import com.huotu.scrm.common.ienum.IntegralTypeEnum;
import com.huotu.scrm.common.ienum.UserType;
import com.huotu.scrm.service.entity.info.InfoBrowse;
import com.huotu.scrm.service.entity.info.InfoConfigure;
import com.huotu.scrm.service.model.info.InfoBrowseAndTurnSearch;
import com.huotu.scrm.service.model.mall.UserModel;
import com.huotu.scrm.service.repository.info.InfoBrowseRepository;
import com.huotu.scrm.service.repository.info.InfoConfigureRepository;
import com.huotu.scrm.service.repository.mall.UserRepository;
import com.huotu.scrm.service.service.api.ApiService;
import com.huotu.scrm.service.service.info.InfoBrowseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


/**
 * Created by luohaibo on 2017/7/12.
 */
@Service
public class InfoBrowseServiceImpl implements InfoBrowseService {


    @Autowired
    private InfoBrowseRepository infoBrowseRepository;
    @Autowired
    private InfoConfigureRepository infoConfigureRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ApiService apiService;

    @Override
    public void infoTurnInSave(InfoBrowse infoBrowse, Long customerId) throws UnsupportedEncodingException {
        boolean flag = false;
        // 判断浏览记录表里：用户转发的资讯是否有浏览记录 有：不用添加转发积分奖励 没有：添加转发积分奖励
        int count = infoBrowseRepository.countBySourceUserIdAndInfoId(infoBrowse.getSourceUserId(), infoBrowse.getInfoId());
        if (count == 0) {
            InfoConfigure infoConfigure = infoConfigureRepository.findOne(customerId);
            //转发开关
            if (infoConfigure !=null && infoConfigure.isRewardSwitch()) {
                //转发能获取的积分
                int score = infoConfigure.getRewardScore();
                //转发奖励限制开启
                if (infoConfigure.isRewardLimitSwitch()) {
                    LocalDateTime today = LocalDate.now().atStartOfDay();
                    List list = infoBrowseRepository.findForwardNumBySourceUserId(today, today.plusDays(1), infoBrowse.getSourceUserId());
                    if (list.size() < infoConfigure.getRewardLimitNum()) {
                        addMallScore(customerId, infoBrowse, infoConfigure, score);
                    }
                } else {
                    addMallScore(customerId, infoBrowse, infoConfigure, score);
                }
            }
            flag = true;
        } else {
            InfoBrowse infoBrowseData = infoBrowseRepository.findOneByInfoIdAndSourceUserIdAndReadUserId(infoBrowse.getInfoId(),
                    infoBrowse.getSourceUserId(), infoBrowse.getReadUserId());
            if (infoBrowseData == null) {
                flag = true;
            }
        }
        if (flag) {
            InfoBrowse infoBrowseData = new InfoBrowse();
            infoBrowseData.setTurnTime(LocalDateTime.now());
            infoBrowseData.setCustomerId(customerId);
            infoBrowseData.setInfoId(infoBrowse.getInfoId());
            infoBrowseData.setReadUserId(infoBrowse.getReadUserId());
            infoBrowseData.setSourceUserId(infoBrowse.getSourceUserId());
            infoBrowseData.setBrowseTime(LocalDateTime.now());
            infoBrowseRepository.save(infoBrowseData);
        }
    }

    /**
     * 转发添加积分
     *
     * @param customerId
     * @param infoBrowse
     * @param infoConfigure
     * @param score
     */
    private void addMallScore(Long customerId, InfoBrowse infoBrowse, InfoConfigure infoConfigure, int score) throws UnsupportedEncodingException {
        //没开启
        UserType userType = userRepository.findUserTypeById(infoBrowse.getSourceUserId());
        //转发奖励获取对象 0 会员;2:会员+小伙伴
        if (infoConfigure.getRewardUserType() == userType.ordinal() || infoConfigure.getRewardUserType() == 2) {
            //调商城接口扣除积分
            apiService.rechargePoint(customerId, infoBrowse.getSourceUserId(), 0L + score, IntegralTypeEnum.TURN_INFO);
        }
    }


    @Override
    public Page<InfoBrowse> infoTurnRecord(InfoBrowseAndTurnSearch infoBrowseAndTurnSearch) {
        Pageable pageable = new PageRequest(infoBrowseAndTurnSearch.getPageNo() - 1, infoBrowseAndTurnSearch.getPageSize());
        return infoBrowseRepository.findAllTurnRecordAndCustomerId(infoBrowseAndTurnSearch.getInfoId(), infoBrowseAndTurnSearch.getCustomerId(), pageable);
    }

    @Override
    public int updateInfoTurnRecord(InfoBrowseAndTurnSearch infoBrowseAndTurnSearch) {
        infoBrowseRepository.updateInfoTurn(infoBrowseAndTurnSearch.getInfoId(), infoBrowseAndTurnSearch.getSourceUserId(), true);
        return 0;
    }

    @Override
    public Page<InfoBrowse> infoBrowseRecord(InfoBrowseAndTurnSearch infoBrowseAndTurnSearch) {
        Pageable pageable = new PageRequest(infoBrowseAndTurnSearch.getPageNo() - 1, infoBrowseAndTurnSearch.getPageSize());
        return infoBrowseRepository.findAllBrowseRecord(infoBrowseAndTurnSearch.getInfoId(), infoBrowseAndTurnSearch.getCustomerId(), pageable);
    }

    @Override
    public List<UserModel> infoBrowseRecordList(InfoBrowseAndTurnSearch infoBrowseAndTurnSearch) {
        return infoBrowseRepository.findAllBrowseRecordList(infoBrowseAndTurnSearch.getInfoId(), infoBrowseAndTurnSearch.getCustomerId());
    }

    @Override
    public int updateInfoBrowse(InfoBrowseAndTurnSearch infoBrowseAndTurnSearch) {

        return infoBrowseRepository.updateBrowseInfo(infoBrowseAndTurnSearch.getInfoId(), infoBrowseAndTurnSearch.getReadUserId(), infoBrowseAndTurnSearch.getSourceUserId(), true);

    }

    @Override
    public int countByTurn(Long infoId) {
        return infoBrowseRepository.totalTurnCount(infoId);
    }

    @Override
    public int countByBrowse(Long infoId) {
        return infoBrowseRepository.countByInfoId(infoId);
    }

    @Override
    public int countBrowseByInfoIdAndSourceUserId(Long infoId, Long sourceUserId) {
        return infoBrowseRepository.countByInfoIdAndSourceUserId(infoId,sourceUserId);
    }

    @Override
    public Page<InfoBrowse> infoSiteBrowseRecord(InfoBrowseAndTurnSearch infoBrowseAndTurnSearch) {
        Pageable pageable;
        if (infoBrowseAndTurnSearch.getSourceType() == 0) {
            pageable = new PageRequest(infoBrowseAndTurnSearch.getPageNo() - 1, 6);
        } else {
            pageable = new PageRequest(infoBrowseAndTurnSearch.getPageNo() - 1, 12);
        }
        return infoBrowseRepository.findAllBrowseRecordByLimit(infoBrowseAndTurnSearch.getInfoId(), infoBrowseAndTurnSearch.getCustomerId(), pageable);

    }

    @Override
    public Page<InfoBrowse> infoSiteBrowseRecordBySourceUserId(InfoBrowseAndTurnSearch infoBrowseAndTurnSearch) {
        Pageable pageable;
        if (infoBrowseAndTurnSearch.getSourceType() == 0) {
            pageable = new PageRequest(infoBrowseAndTurnSearch.getPageNo() - 1, 6);
        } else {
            pageable = new PageRequest(infoBrowseAndTurnSearch.getPageNo() - 1, 12);
        }
        return infoBrowseRepository.findAllBrowseRecordBySourceUserIdByLimit(infoBrowseAndTurnSearch.getInfoId(), infoBrowseAndTurnSearch.getCustomerId(), infoBrowseAndTurnSearch.getSourceUserId(),pageable);
    }


}

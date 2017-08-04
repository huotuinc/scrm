package com.huotu.scrm.service.service;

import com.huotu.scrm.service.CommonTestBase;
import com.huotu.scrm.service.entity.activity.ActPrize;
import com.huotu.scrm.service.model.prizeTypeEnum;
import com.huotu.scrm.service.service.activity.ActPrizeService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Created by hxh on 2017-08-01.
 */
public class ActPrizeServiceTest extends CommonTestBase {

    @Autowired
    private ActPrizeService actPrizeService;

    /**
     * 保存奖品
     */
    @Test
    public void saveActPrize() {
        ActPrize actPrize = new ActPrize();
        actPrize.setPrizeName("手机");
        actPrize.setPrizeImageUrl("dasdsa");
        actPrize.setPrizeType(prizeTypeEnum.PRIZE_TYPE_ENTITY_PRIZE);
        actPrize.setPrizeCount(12);
        actPrize.setRemainCount(10);
        actPrize.setWinRate(20);
        actPrize.setSort(20);
        ActPrize prize = actPrizeService.saveActPrize(actPrize);
        System.out.println(prize.toString());
    }

    @Test
    public void findAllByPage() {
        Page<ActPrize> pageActPrize = actPrizeService.getPageActPrize(1, 20);
        List<ActPrize> prizeList = pageActPrize.getContent();
        prizeList.forEach(actPrize -> {
            System.out.println(actPrize.toString());
        });
    }
}

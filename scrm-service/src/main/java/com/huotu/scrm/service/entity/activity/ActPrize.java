package com.huotu.scrm.service.entity.activity;

import com.huotu.scrm.service.model.prizeTypeEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

/**
 * 活动奖品表
 * <p>
 * Created by montage on 2017/7/11.
 */
@Entity
@Getter
@Setter
@ToString
@Table(name = "SCRM_ActPrize")
public class ActPrize {

    /**
     * 奖品Id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Prize_Id")
    private Long prizeId;


    /**所属活动*/
    @ManyToOne(cascade={CascadeType.MERGE,CascadeType.REFRESH,CascadeType.PERSIST},optional=false)
    @JoinColumn(name="Act_Id")
    private Activity activity;

    /**
     * 奖品名称
     */
    @Column(name ="Prize_Name")
    private String prizeName;

    /**
     * 奖品图片
     */
    @Column(name = "Prize_Image_Url")
    private String prizeImageUrl;


    /**
     * 奖品详细图片
     */
    @Transient
    private String mallPrizeImageUrl;

    /**
     * 奖品类型
     * 0:谢谢惠顾,1:奖品
     */
    @Column(name = "Prize_Type")
    private prizeTypeEnum prizeType;

    /**
     * 奖品总数量
     */
    @Column(name = "Prize_Count")
    private int prizeCount;

    /**
     * 奖品剩余数量
     */
    @Column(name = "Remain_Count")
    private int remainCount;

    /**
     * 中奖概率
     */
    @Column(name = "Win_Rate")
    private int winRate;

    /**
     * 排序字段 越大越靠前
     */
    @Column(name = "Sort")
    private int sort;
}

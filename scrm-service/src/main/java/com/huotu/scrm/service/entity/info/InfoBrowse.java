package com.huotu.scrm.service.entity.info;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

/**
 * Created by luohaibo on 2017/7/11.
 */
@Entity
@Getter
@Setter
@Table(name = "SCRM_InfoBrowseLog")
public class InfoBrowse {

    /**
     * 资讯主键
     */
    @Id
    @Column(name = "Info_Id")
    private Long infoId;

    /**
     * 资讯转发来源用户
     */
    @Id
    @Column(name = "Info_Source_UserId")
    private Long sourceUserId;


    /**
     * 资讯查看用户
     */
    @Id
    @Column(name = "Info_Read_UserId")
    private Long readUserId;


    /**
     * 商户Id
     */
    @Column(name = "Customer_Id")
    private Long customerId;


    /**
     * 资讯查看时间
     */
    @Column(name = "Brose_Time", columnDefinition = "datetime")
    private LocalDateTime browseTime;

    /**
     * 资讯转发
     */
    @Column(name = "Turn_Time", columnDefinition = "datetime")
    private LocalDateTime turnTime;

}

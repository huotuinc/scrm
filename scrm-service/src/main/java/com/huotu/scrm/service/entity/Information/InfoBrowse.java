package com.huotu.scrm.service.entity.Information;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by luohaibo on 2017/7/11.
 */
@Entity
@Getter
@Setter
@Table(name = "SCRM_InfoBrowseLog")
public class InfoBrowse {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Long id;

    /**
     * 资讯主键
     */
    @Column(name = "Info_Id")
    private int info_Id;

    /**
     * 资讯转发来源用户
     */
    @Column(name = "Info_Source_UserId")
    private int sourceUserId;


    /**
     * 资讯查看用户
     */
    @Column(name = "Info_Read_UserId")
    private String readUserId;


    /**
     * 商户Id
     */
    @Column(name = "Customer_Id")
    private String customId;


    /**
     * 资讯创建时间
     */
    @Column(name = "Brose_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date broseTime;

}

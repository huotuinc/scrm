package com.huotu.scrm.service.entity.mall;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

/**
 * Created by xyr on 2017/10/25.
 */
@Entity
@Data
@Table(name = "Mall_UserBanner")
public class UserBanner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MUB_Id")
    private Integer id;

    @Column(name = "MUB_Name")
    private String name;

    @Column(name = "MUB_Image")
    private String image;

    /**
     * 商户号
     */
    @Column(name = "MUB_CustomerId")
    private Long customerId;

    @Column(name = "MUB_Order")
    private Integer order;

    /**
     * 创建时间
     */
    @Column(name = "MUB_Time" , columnDefinition = "datetime")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime time;

    @Column(name = "MUB_LinkUrl")
    private String linkUrl;

    @Column(name = "MUB_Type")
    private Integer type;

    @Column(name = "MUB_UserType")
    private Integer userType;

    @Column(name = "MUB_Enabled")
    private Boolean enabled;

}

package com.huotu.scrm.service.entity.info;

import com.huotu.scrm.service.entity.support.LocalDateAttributeConverter;
import lombok.Getter;
import lombok.Setter;
import org.eclipse.persistence.annotations.Converter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;

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
    private Long infoId;

    /**
     * 资讯转发来源用户
     */
    @Column(name = "Info_Source_UserId")
    private Long sourceUserId;


    /**
     * 资讯查看用户
     */
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
    @Column(name = "Brose_Time",columnDefinition = "date")
    @Converter(name = "",converterClass = LocalDateAttributeConverter.class)
    private LocalDate browseTime;

}

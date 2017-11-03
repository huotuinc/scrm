package com.huotu.scrm.service.model.info;

import com.huotu.scrm.common.utils.Constant;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;


/**
 * Created by luohaibo on 2017/7/13.
 */
@Getter
@Setter
public class InformationSearch {

    private int extend = 0;
    private int status = 1;
    private int disable = 0;
    private int pageNo = 1;
    private int pageSize = Constant.PAGE_SIZE;
    private String searchCondition;
    private Long customerId;
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startDate;
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime  endDate;


}

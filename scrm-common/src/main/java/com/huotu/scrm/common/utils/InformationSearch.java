package com.huotu.scrm.common.utils;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;


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
    @Override
    public String toString() {
        return "InformationSearch{" +
                "extend=" + extend +
                ", status=" + status +
                ", disable=" + disable +
                ", pageNo=" + pageNo +
                ", pageSize=" + pageSize +
                '}';
    }
}

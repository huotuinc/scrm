package com.huotu.scrm.common.utils;

import lombok.Data;


/**
 * Created by luohaibo on 2017/7/13.
 */
@Data
public class InformationSearch {

    private int extend = 0;
    private int status = 1;
    private int disable = 0;
    private int pageNo = 1;
    private int pageSize = 5;
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

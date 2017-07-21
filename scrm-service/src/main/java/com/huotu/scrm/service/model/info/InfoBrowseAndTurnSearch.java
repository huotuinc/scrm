package com.huotu.scrm.service.model.info;

import com.huotu.scrm.common.utils.Constant;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by luohaibo on 2017/7/21.
 */
@Getter
@Setter
public class InfoBrowseAndTurnSearch {

    private int pageNo = 1;
    private int pageSize = Constant.PAGE_SIZE;
    private Long customerId;
    private Long sourceUserId;
    private Long readUserId;
}

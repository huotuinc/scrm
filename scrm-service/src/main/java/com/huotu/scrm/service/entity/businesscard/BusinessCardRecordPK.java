package com.huotu.scrm.service.entity.businesscard;

import lombok.Data;

import java.io.Serializable;

@Data
public class BusinessCardRecordPK implements Serializable{

    private Long userId;
    private Long customerId;
    private Long followId;
}

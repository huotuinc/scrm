package com.huotu.scrm.service.entity.businesscard;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
public class BusinessCardRecordPK implements Serializable{
    private static final long serialVersionUID = 1L;
    private Long userId=0L;
    private Long customerId=0L;
    private Long followId=0L;

    public BusinessCardRecordPK(){
        this.userId=0L;
        this.customerId=0L;
        this.followId=0L;
    }

    public BusinessCardRecordPK(Long userId , Long customerId , Long followId){
        this.userId = userId;
        this.customerId= customerId;
        this.followId = followId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        BusinessCardRecordPK that = (BusinessCardRecordPK) o;

        if (userId != null ? !userId.equals(that.userId) : that.userId != null) return false;
        if (customerId != null ? !customerId.equals(that.customerId) : that.customerId != null) return false;
        return followId != null ? followId.equals(that.followId) : that.followId == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        result = 31 * result + (customerId != null ? customerId.hashCode() : 0);
        result = 31 * result + (followId != null ? followId.hashCode() : 0);
        return result;
    }
}

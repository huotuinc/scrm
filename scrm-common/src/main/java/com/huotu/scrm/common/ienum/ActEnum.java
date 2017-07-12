package com.huotu.scrm.common.ienum;

/**
 * 大转盘活动枚举
 * Created by montage on 2017/7/12.
 */
public interface ActEnum {

    /**
     * 活动名称
     */
    enum Activity implements ICommonEnum {

        ACT_TURNTABLE(0,"大转盘");

        private int code;
        private String value;

        Activity(int code, String value) {
            this.code = code;
            this.value = value;
        }

        @Override
        public Object getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        @Override
        public Object getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    enum ActOpenStatus implements ICommonEnum{

        ACT_OPEN(0,"开启"),
        ACT_END(1,"关闭");

        private int code;
        private String value;

        ActOpenStatus(int code, String value) {
            this.code = code;
            this.value = value;
        }

        @Override
        public Object getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        @Override
        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}

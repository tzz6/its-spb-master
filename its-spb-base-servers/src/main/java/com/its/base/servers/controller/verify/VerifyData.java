package com.its.base.servers.controller.verify;

import java.io.Serializable;

/**
 * 验证码实体
 *
 * @author 89003389
 */
public class VerifyData implements Serializable {

    private static final long serialVersionUID = -3116397801767584103L;

    private StringBuilder chineseChars;

    private StringBuilder realPointsOffset;

    private String combined;

    private String type;

    public StringBuilder getChineseChars() {
        return chineseChars;
    }

    public void setChineseChars(StringBuilder chineseChars) {
        this.chineseChars = chineseChars;
    }


    public StringBuilder getRealPointsOffset() {
        return realPointsOffset;
    }

    public void setRealPointsOffset(StringBuilder realPointsOffset) {
        this.realPointsOffset = realPointsOffset;
    }

    public String getCombined() {
        return combined;
    }

    public void setCombined(String combined) {
        this.combined = combined;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}

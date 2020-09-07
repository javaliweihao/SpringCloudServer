package com.marving.boot.infra.vo;

import java.io.Serializable;

public class JwtRequest implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -1579726131609654740L;

    private String tenant;
    private String wxAccessCode;
    private String phone;
    private String verificationCode;

    public JwtRequest() {

    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    public String getTenant() {
        return tenant;
    }

    public void setTenant(String tenant) {
        this.tenant = tenant;
    }

    public String getWxAccessCode() {
        return wxAccessCode;
    }

    public void setWxAccessCode(String wxAccessCode) {
        this.wxAccessCode = wxAccessCode;
    }
}
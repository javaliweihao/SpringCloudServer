package com.marving.boot.base;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.util.Date;

@MappedSuperclass
public class AbstractUser extends AbstractTenantEntity{

    @Column
    private String phone;

    @Column
    private boolean phoneVaild;

    @Column
    private String verificationCode;

    @Column
    private Date verificationCodeExpireDate;

    @Column
    private String unionId;

    @Column
    private String openId;

    @Column(columnDefinition = "VARCHAR(1024)")
    private String token;

    @Column
    private String realname;

    @Column
    private String nickName;

    /**
     * 用户类型：0 - ，1 - ，2 -
     */
    @Column
    private String userType;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isPhoneVaild() {
        return phoneVaild;
    }

    public void setPhoneVaild(boolean phoneVaild) {
        this.phoneVaild = phoneVaild;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    public Date getVerificationCodeExpireDate() {
        return verificationCodeExpireDate;
    }

    public void setVerificationCodeExpireDate(Date verificationCodeExpireDate) {
        this.verificationCodeExpireDate = verificationCodeExpireDate;
    }

    public String getUnionId() {
        return unionId;
    }

    public void setUnionId(String unionId) {
        this.unionId = unionId;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }
}

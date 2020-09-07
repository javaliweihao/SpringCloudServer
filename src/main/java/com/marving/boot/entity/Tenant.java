package com.marving.boot.entity;

import com.marving.boot.base.AbstractEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "g_tenant")
public class Tenant extends AbstractEntity {


    interface ModuleFlag {
        int SHOWALL = 0b111;
        int HIDEALL = 0b000;
        int SHOWDESC = 0b001;
        int SHOWTEACHER = 0b010;
        int SHOWCOURSE = 0b100;
    }


    @Column(unique = true)
    private String code;

    @Column(unique = true)
    private String name;

    @Column(nullable = false, columnDefinition = "Integer default 7")
    private Integer moduleFlag;// 小程序端控制模块显示与否的标志

    @Column
    private String address;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getModuleFlag() {
        return moduleFlag;
    }

    public void setModuleFlag(Integer moduleFlag) {
        this.moduleFlag = moduleFlag;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}
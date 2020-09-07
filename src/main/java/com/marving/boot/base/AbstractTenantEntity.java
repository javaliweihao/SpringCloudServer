package com.marving.boot.base;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class AbstractTenantEntity extends AbstractEntity {

    @Column
    private String tenantCode;//from global schema

    public String getTenantCode() {
        return tenantCode;
    }
    public void setTenantCode(String tenantCode) {
        this.tenantCode = tenantCode;
    }

}

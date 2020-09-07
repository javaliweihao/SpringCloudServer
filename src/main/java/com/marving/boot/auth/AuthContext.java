package com.marving.boot.auth;

import com.marving.boot.base.AbstractEntity;
import com.marving.boot.base.AbstractTenantEntity;
import com.marving.boot.entity.Tenant;
import org.apache.log4j.MDC;

public class AuthContext {

    public static void init(AbstractEntity entity) {
        entity.setCreatedBy(Long.valueOf(getUser().getId()));
        entity.setCreatorMode(getMode());
        if(entity instanceof AbstractTenantEntity) {
            AbstractTenantEntity te = (AbstractTenantEntity)entity;
            te.setTenantCode(getCurrentTenant().getCode());
        }
    }

    public static final int MODE_SYS_ADMIN = 1;
    public static final int MODE_TENANT_ADMIN = 2;
    public static final int MODE_TENANT_STAFF = 3;
    public static final int MODE_TENANT_STAFF_WX = 4;
    public static final int MODE_TENANT_CLIENT_WX = 5;

    private static ThreadLocal<Integer> authMode = new ThreadLocal<>();

    private static ThreadLocal<JwtUser> currentUser = new ThreadLocal<>();

    private static ThreadLocal<Tenant> currentTenant = new ThreadLocal<>();

    public static Integer getMode() {
        return authMode.get();
    }

    public static void setMode(Integer mode) {
        MDC.put("MODE", String.valueOf(mode));
        authMode.set(mode);
    }

    public static JwtUser getUser() {
        return currentUser.get();
    }

    public static void setUser(JwtUser user) {
        MDC.put("USER", user.getUsername());
        currentUser.set(user);
    }

    public static void setTenant(Tenant tenant) {
        if(tenant == null) {
            MDC.put("TENANT", "SYSADMIN");
        }else {
            MDC.put("TENANT", tenant.getCode());
        }
        currentTenant.set(tenant);
    }

    public static Tenant getCurrentTenant() {
        return currentTenant.get();
    }
    public static void clear() {
        authMode.set(null);
        currentUser.set(null);
        currentTenant.set(null);
        MDC.clear();
    }
}
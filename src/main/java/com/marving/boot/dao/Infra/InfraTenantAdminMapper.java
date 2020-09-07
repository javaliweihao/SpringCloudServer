package com.marving.boot.dao.Infra;

import com.marving.boot.entity.tenant.TenantAdmin;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface InfraTenantAdminMapper {

    String findValidPhoneByUnionId(String tenantCode, String unionId);

    TenantAdmin findByPhone(@Param("tenantCode") String tenantCode, @Param("phone") String phone);

    void updateVerificationCode(@Param("tenantCode") String tenantCode, @Param("phone")String phone,@Param("code") String code);
}

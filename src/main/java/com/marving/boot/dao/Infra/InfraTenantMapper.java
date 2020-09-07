package com.marving.boot.dao.Infra;

import com.marving.boot.entity.Tenant;
import org.springframework.stereotype.Repository;

@Repository
public interface InfraTenantMapper {

    Tenant findByCode(String code);

}

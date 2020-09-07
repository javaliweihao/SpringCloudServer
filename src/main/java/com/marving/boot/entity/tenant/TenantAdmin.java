package com.marving.boot.entity.tenant;

import com.marving.boot.base.AbstractUser;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "t_tenant_admin")
public class TenantAdmin extends AbstractUser {



}

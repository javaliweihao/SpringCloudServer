package com.marving.boot.entity.tenant;

import com.marving.boot.base.AbstractUser;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "t_client_info")
public class ClientInfo extends AbstractUser {



}

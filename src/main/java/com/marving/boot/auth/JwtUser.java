package com.marving.boot.auth;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import com.marving.boot.base.EntityStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class JwtUser implements UserDetails {


    private static final long serialVersionUID = -5771063528948828375L;
    private final String id;
    private final String username;
    private final int status;
    private final String password;
    private final Date passwordExpireDate;
    private final Collection<? extends GrantedAuthority> authorities;

    public JwtUser(
            String id,
            String username,
            int status,
            String password,
            Date passwordExpireDate,
            List<String> permissions) {
        this.id = id;
        this.username = username;
        this.status = status;
        this.password = password;
        this.passwordExpireDate = passwordExpireDate;
        this.authorities = populateAuthorities(permissions);
    }

    private static List<GrantedAuthority> populateAuthorities(List<String> permissions){
        List<GrantedAuthority> list = new ArrayList<>();
        if(permissions != null) {
            for (String perm : permissions) {
                list.add(new SimpleGrantedAuthority(perm));
            }
        }
        return list;
    }
    // 返回分配给用户的角色列表
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @JsonIgnore
    public String getId() {
        return id;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    // 账户是否未过期
    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return status == EntityStatus.ACTIVE;
    }

    // 账户是否未锁定
    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return status == EntityStatus.ACTIVE;
    }

    // 密码是否未过期
    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
//        return passwordExpireDate.after(new Date());
    }

    // 账户是否激活
    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return status == EntityStatus.ACTIVE;
    }

}

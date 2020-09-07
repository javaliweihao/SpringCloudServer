package com.marving.boot.auth;

import com.marving.boot.dao.Infra.InfraTenantMapper;
import com.marving.boot.entity.Tenant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class AuthService {

    private static Logger logger = LoggerFactory.getLogger(AuthService.class.getName());

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private InfraTenantMapper infraTenantMapper;

    @Autowired
    private JwtUserDetailsService userDetailsService;


    public boolean validateTenant(String tenantCode) {
        Tenant tenant = infraTenantMapper.findByCode(tenantCode);
        if(tenant == null) {
            logger.info("Invalid tenantCode "+tenantCode);
            return false;
        }
        AuthContext.setTenant(tenant);
        return true;
    }

    public String authenticate(int mode, String tenantCode,  String phone, String verificationCode){
        AuthContext.setMode(mode);
        try {
            authenticate(phone, verificationCode);
        } catch (Exception e) {
            logger.error("Err when authenticating "+phone+" as "+mode, e);
            return "";
        }

        userDetailsService.refreshUserCredentials(tenantCode, phone);
        final UserDetails userDetails = userDetailsService.loadUserByUsername(phone);
        final String token = jwtTokenUtil.generateToken(userDetails);
        return token;

    }

    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }

    public String authenticateSysAdmin(String phone, String verificationCode) {
        return authenticate(AuthContext.MODE_SYS_ADMIN, "", phone, verificationCode);
    }

    public String authenticateTenantAdmin(String tenantCode, String phone, String verificationCode) {
        return authenticate(AuthContext.MODE_TENANT_ADMIN, tenantCode, phone, verificationCode);
    }

    public String authenticateTenantStaff(String tenantCode, String phone, String verificationCode) {
        return authenticate(AuthContext.MODE_TENANT_STAFF, tenantCode, phone, verificationCode);
    }

}
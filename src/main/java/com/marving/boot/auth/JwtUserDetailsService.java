package com.marving.boot.auth;

import com.marving.boot.base.AbstractUser;
import com.marving.boot.dao.Infra.InfraTenantAdminMapper;
import com.marving.boot.entity.enums.UserTypeEnum;
import com.marving.boot.infra.BizException;
import com.marving.boot.integration.sms.VerificationCodeService;
import io.jsonwebtoken.lang.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.StringUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    private InfraTenantAdminMapper tenantAdminMapper;

    @Autowired
    private VerificationCodeService verificationCodeService;

    public void refreshUserCredentials(String tenantCode, String phone) {
        String code = verificationCodeService.genCode();

        switch (AuthContext.getMode()) {
//            case AuthContext.MODE_SYS_ADMIN:
//                sysAdminMapper.updateVerificationCode(phone, code);
//                break;
            case AuthContext.MODE_TENANT_ADMIN:
                tenantAdminMapper.updateVerificationCode(tenantCode, phone, code);
                break;
//            case AuthContext.MODE_TENANT_STAFF:
//                tenantStaffMapper.updateVerificationCode(tenantCode, phone, code);
//                break;
            default:
                throw new BizException(BizException.Code.ERP_INVALID_ACCESS);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        JwtUser ju = AuthContext.getUser();
        if (ju != null && ju.getUsername().equals(username)) {
            return ju;
        }
        AbstractUser user = null;
        List<String> permissions = new ArrayList<>();
        switch (AuthContext.getMode()) {
//            case AuthContext.MODE_SYS_ADMIN:
//                user = sysAdminMapper.findByPhone(username);
//                break;
            case AuthContext.MODE_TENANT_ADMIN:
                user = tenantAdminMapper.findByPhone(AuthContext.getCurrentTenant().getCode(), username);
                break;
//            case AuthContext.MODE_TENANT_STAFF:
//            case AuthContext.MODE_TENANT_STAFF_WX:
//                user = tenantStaffMapper.findByPhone(AuthContext.getCurrentTenant().getCode(), username);
//                break;
//            case AuthContext.MODE_TENANT_CLIENT_WX:
//                user = tenantClientMapper.findByPhone(AuthContext.getCurrentTenant().getCode(), username);
//                break;
        }
        if (user == null) {
            return null;
        }

        //加入权限
        permissions = genPermissions(user);

        ju = new JwtUser(
                Long.toString(user.getId()),
                user.getPhone(),
                user.getStatus(),
                user.getVerificationCode(),
                user.getVerificationCodeExpireDate(),
                permissions);

        AuthContext.setUser(ju);
        return ju;

    }

    public List<String> genPermissions(AbstractUser user) {
        List<String> permissions = new ArrayList<>();
        String userPermissions = "";
        switch (AuthContext.getMode()) {
            case AuthContext.MODE_SYS_ADMIN:
            case AuthContext.MODE_TENANT_ADMIN:
                userPermissions = "0,1,2";
                break;
            case AuthContext.MODE_TENANT_STAFF:
            case AuthContext.MODE_TENANT_STAFF_WX:
                userPermissions = user.getUserType();
                break;
            case AuthContext.MODE_TENANT_CLIENT_WX:
                break;

        }
        final String userType = userPermissions;
        final String[] userTypeArr = StringUtils.splitByWholeSeparatorPreserveAllTokens(userType, ",");

        if (userTypeArr == null || userTypeArr.length < 1) {

//             throw new RuntimeException("用户职位不存在");
        }

        @SuppressWarnings("unchecked") final List<String> userTypeList = Collections.arrayToList(userTypeArr);

        if (userTypeList.contains(UserTypeEnum.TEACHER.getCode())) {
            permissions.add("permission-teacher");
        }

        if (userTypeList.contains(UserTypeEnum.SELLER.getCode())) {
            permissions.addAll(Arrays.asList("erp-overview", "enroll-no-del", "marketing"));
        }

        if (userTypeList.contains(UserTypeEnum.BUSINESS.getCode())) {
            permissions.addAll(Arrays.asList("erp-overview", "edu-admin", "handle", "marketing", "refund"));
        }
        return permissions;
    }
}
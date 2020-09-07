package com.marving.boot.infra.controller;

import com.marving.boot.auth.AuthContext;
import com.marving.boot.auth.AuthService;
import com.marving.boot.infra.vo.JwtRequest;
import com.marving.boot.infra.vo.JwtResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tenant/admin")
public class TenantAdminController {

    @Autowired
    private AuthService authService;

    @PostMapping(value = "/login")
    public ResponseEntity<?> login(@RequestBody JwtRequest req) {
        if(!authService.validateTenant(req.getTenant())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        String token = authService.authenticateTenantAdmin(req.getTenant(), req.getPhone(), req.getVerificationCode());
        if(StringUtils.isEmpty(token)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(new JwtResponse(token));
    }

    @PostMapping(value = "/test")
    public ResponseEntity<?> testController(){
        return ResponseEntity.ok(AuthContext.getCurrentTenant().getCode());
    }


}

package com.marving.boot.integration.sms;

import org.springframework.stereotype.Component;
import java.util.Calendar;
import java.util.Date;

@Component
public class VerificationCodeService {

    public String genCode() {
        String code = ((int)((Math.random()*9+1)*1000)) + "";
        return "8888";
    }

    public Date genExpiredDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 5);// Expired in 5 mins
        Date expireDate = calendar.getTime();
        return expireDate;
    }

}

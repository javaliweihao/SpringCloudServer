package com.marving.boot.infra.vo;

import java.io.Serializable;

public class JwtResponse implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -1035117135762644113L;

    private final String token;
    private boolean flag;//用户是否授权flag

    public JwtResponse(String token) {
        this.token = token;
    }

    public JwtResponse(String token, boolean flag) {
        super();
        this.token = token;
        this.flag = flag;
    }

    public String getToken() {
        return token;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

}
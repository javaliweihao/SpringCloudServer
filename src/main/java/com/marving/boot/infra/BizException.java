package com.marving.boot.infra;

public class BizException extends RuntimeException{

    private static final long serialVersionUID = -7905327443556825171L;

    public BizException(String code) {
        super(code);
    }

    public static class Code{
        public static final String WX_INVALID_ACCESS="1001";
        public static final String WX_ERROR_ON_CODE2SESSION="1002";
        public static final String WX_INVALID_PARAM="1003";

        public static final String ERP_INVALID_ACCESS="2001";
    }
}
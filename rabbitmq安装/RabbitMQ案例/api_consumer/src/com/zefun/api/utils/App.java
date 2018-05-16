package com.zefun.api.utils;



public interface App {
    public static final long DELIVERIED_TAG = -1;

    /** 服务器域名 */
    public static final String SERVER_HOST = "www.mockdream.com";
    /** 服务器基础地址 */
    public static final String SERVER_BASE_URL = "http://" + SERVER_HOST + "/zefun";

    class MsgErr {
        public static final String MQ_MSG_ERR_RECORD_KEY = "mq_msg_err_record";
    }

    
    /** redis常量key */
    class Redis {
        /** 门店对应微信id的hash key */
        public static final String STORE_WECHAT_APP_ID_KEY_HASH = "store_wechat_app_id_key_hash";
        /** 门店对应微信secret的hash key */
        public static final String STORE_WECHAT_APP_SECRET_KEY_HASH = "store_wechat_app_secret_key_hash";
        /** 门店对应微信accessToken的hash key */
        public static final String STORE_WECHAT_ACCESS_TOKEN_KEY_HASH = "store_wechat_access_token_key_hash";
        /** 门店对应微信JS接口的临时票据 */
        public static final String STORE_WECHAT_JSAPI_TICKET_KEY_HASH = "store_wechat_jsapi_ticket_key_hash";
        
        /** 微信用户关注状态的hash key */
        public static final String WECHAT_SUBSCRIBE_KEY_HASH = "wechat_subscribe_key_hash";
        /** 微信openid对应用户id(包括会员跟员工)的hash key前缀 */
        public static final String WECHAT_OPENID_TO_USERID_KEY_HASH = "wechat_openid_to_userid_key_hash";
        /** 会员标识对应微信openid的hash key */
        public static final String WECHAT_MEMBERID_TO_OPENID_KEY_HASH = "wechat_memberid_to_openid_key_hash";
        /** 员工标识对应微信openid的hash key */
        public static final String WECHAT_EMPLOYEEID_TO_OPENID_KEY_HASH = "wechat_employeeid_to_openid_key_hash";
        
        
        /** 会员基本信息的hash key，key为会员标识，value为会员基本信息，对应memberBaseDto */
        public static final String MEMBER_BASE_INFO_KEY_HASH = "member_base_info_key_hash";
        
        /** 手机验证码 */
        public static final String PHONE_VERIFY_CODE_KEY_HASH = "phone_verify_code_key_hash";
        
        /**员工打卡状态hash key，空表示未打卡，1表示已打上班卡，2表示上班下班都已打卡*/
        public static final String EMPLOYEE_ATTENDANCE_STATUS_HASH = "employee_attendance_status_hash";
    }
    
    /** 微信常量 */
    public static class Wechat {
        /** 获取access_token的url */
        public static final String GET_ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?"
                + "grant_type=client_credential&appid=%s&secret=%s";
        
        /** 获取jsapi ticket的url */
        public static final String GET_JSAPI_TICKET_URL = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=%s&type=jsapi";
        
        /** 获取关注用户的信息 */
        public static final String GET_USER_INFO_URL = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=%s&openid=%s&lang=zh_CN";
        
        /** 获取微信素材资源 */
        public static final String FETCH_MEDIA_URL = "http://file.api.weixin.qq.com/cgi-bin/media/get?access_token=%s&media_id=%s";
    }
}

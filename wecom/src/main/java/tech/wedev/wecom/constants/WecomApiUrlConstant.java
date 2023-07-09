package tech.wedev.wecom.constants;

public class WecomApiUrlConstant {
    /**
     * GET
     * 获取access_token
     */
    public static final String GET_TOKEN = "/cgi-bin/gettoken?corpid=%1$s&corpsecret=%2$s";

    /**
     * POST
     * 客户联系-客户管理-批量获取客户详情
     */
    public static final String BATCH_GET_BY_USER_CONTEXT = "/cgi-bin/externalcontact/batch/get_by_user";
    public static final String BATCH_GET_BY_USER = "/cgi-bin/externalcontact/batch/get_by_user?access_token=%1$s";

    /**
     * GET
     * 客户联系-客户管理-获取客户列表
     */

    public static final String EXTERNAL_CONTACT_LIST_CONTEXT = "/cgi-bin/externalcontact/list";
    public static final String EXTERNAL_CONTACT_LIST = "/cgi-bin/externalcontact/list?access_token=%1$s&userid=%2$s";

    /**
     * GET
     * 客户联系-客户管理-获取客户详情
     */
    public static final String EXTERNAL_CONTACT_GET_CONTEXT = "/cgi-bin/externalcontact/get";
    public static final String EXTERNAL_CONTACT_GET = "/cgi-bin/externalcontact/get?access_token=%1$s&external_userid=%2$s";

    /**
     * GET
     * 身份验证-网页授权登录-获取访问用户身份
     */
    public static final String AUTH_GET_USER_INFO_CONTEXT = "/cgi-bin/auth/getuserinfo";
    public static final String AUTH_GET_USER_INFO = "/cgi-bin/auth/getuserinfo?access_token=%1$s&code=%2$s";

    /**
     * GET
     * 通讯录管理-成员管理-二次验证
     */
    public static final String USER_AUTH_SUCC_CONTEXT = "/cgi-bin/user/authsucc";
    public static final String USER_AUTH_SUCC = "/cgi-bin/user/authsucc?access_token=%1$s&userid=%2$s";

    /**
     * POST
     * 消息推送-发送应用消息
     */
    public static final String SEND_MESSAGE_APIURL_CONTEXT = "/cgi-bin/message/send";
    public static final String SEND_MESSAGE_APIURL = "/cgi-bin/message/send?access_token=%1$s";


    /**
     * POST
     * 客户联系-消息推送-发送新客户欢迎语
     */
    public static final String SEND_WELCOME_MSG_CONTEXT = "/cgi-bin/externalcontact/send_welcome_msg";
    public static final String SEND_WELCOME_MSG = "/cgi-bin/externalcontact/send_welcome_msg?access_token=%1$s";

    /**
     * 上传临时素材
     */
    public static final String MEDIA_UPLOAD_CONTEXT = "/cgi-bin/media/upload";
    public static final String MEDIA_UPLOAD = "/cgi-bin/media/upload?access_token=%1$s&type=%2$s";

}

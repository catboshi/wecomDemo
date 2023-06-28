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

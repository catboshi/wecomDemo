package tech.wedev.wecom.tools;

/**
 * @see <a href="https://juejin.cn/post/7090327624970895397">Java中的三种校验注解的使用说明！分析@Valid和@Validated以及@PathVariable的的具体使用</a>
 */
public interface ValidatorGroup {
    interface Insert{}
    interface Update{}
    interface Select{}
    interface Delete{}
    interface WecomTxtMsg{}
    interface WecomCardMsg{}
    interface CustInfo{}
    interface CustList{}
    interface FriendAuth{}

    /**
     * QywxStatisticsController 层的 QueryListPage 方法的校验分组
     */
    interface QywxStatisticsControllerQueryListPageQuery{}

    /**
     *CustMgrController 层的 CustMgrQueryName 方法的校验分组
     */
    interface CustMgrControllerCustMgrQueryName{}

    /**
     * ProductController 层的 qimQueryProduct 方法的校验分组
     */
    interface ProductControllerQimQueryProduct{}

    /**
     * 根据产品代码修改总行产品的总行营销话术ID和分行营销话术ID
     */
    interface ProductControllerUpdateQrCodeScriptId{}

    /**
     * MarketVO 的分页参数校验
     */
    interface MgtMarketJobControllerJobList{}

    /**
     * 根据营销话术ID列表查询营销话术
     */
    interface WecomMarketScriptControllerSelectBranchProdScripts{}

    /**
     * 根据营销素材ID列表查询营销素材
     */
    interface WecomSysFileControllerSelectBranchProdScripts{}

    /**
     * 客户分析内部生成链接接口校验
     */
    interface AddUrlInner{}

    /**
     * 客户分析内部生成链接接口校验
     */
    interface UpdateUrlInner{}

    /**
     * 客户修改按钮修改备注信息
     */
    interface RemarkUpdateUrl{}

    /**
     * 同花顺入参校验
     */
    interface TongHuaShun{}

    /**
     * 【联系我】二维码生成入参校验
     */
    interface addContactWay{}

    /**
     * 新增初始用户入参校验
     */
    interface initialUserAdd{}
}

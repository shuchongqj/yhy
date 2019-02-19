package com.yhy.network

enum class YhyCode( val i: Long, val text : String) {
    NETWORK_ERROR(-9999999999, "请求异常"),
    RISK_USER_LOCKED(-380, "用户被锁定"),
    USER_LOCKED(-360, "用户被锁定"),
    TOKEN_ERROR(-370, "token错误"),
    NO_ACTIVE_DEVICE(-340, "您的账户在其他设备上登录"),
    NO_TRUSTED_DEVICE(-320, "不是用户的受信设备"),
    TOKEN_EXPIRE(-300, "token已过期"),

    APPID_NOT_EXIST(-280, "应用id不存在"),
    UPLINK_SMS_NOT_RECEIVED(-270, "上行短信尚未收到"),
    DYNAMIC_CODE_ERROR(-260, "手机动态密码错误"),
    /* 手机号未绑定 */
    MOBILE_NOT_REGIST(-250, "手机号未绑定"),
    API_UPGRADE(-220, "接口已升级"),
    REQUEST_PARSE_ERROR(-200, "请求解析错误"),
    SIGNATURE_ERROR(-180, "签名错误"),
    UNKNOW_TOKEN_DENIED(-164, "UNKNOW_TOKEN_DENIED"),
    USER_CHECK_FAILED(-161, "USER_CHECK_FAILED"),
    ACCESS_DENIED(-160, "访问被拒绝"),
    PARAMETER_ERROR(-140, "参数错误"),
    UNKNOWN_METHOD(-120, "mt参数服务端无法识别"),
    DUBBO_SERVICE_TIMEOUT_ERROR(-108, "DUBBO_SERVICE_TIMEOUT_ERROR"),
    DUBBO_SERVICE_NOTFOUND_ERROR(-107, "DUBBO_SERVICE_NOTFOUND_ERROR"),
    SECURITY_SERVICE_ERROR(-106, "SECURITY_SERVICE_ERROR"),
    WEB_ACCESS_FAILED(-105, "WEB_ACCESS_FAILED"),
    FATAL_ERROR(-104, "FATAL_ERROR"),
    IP_DENIED(-103, "IP_DENIED"),
    SERIALIZE_FAILED(-102, "SERIALIZE_FAILED"),
    INTERNAL_SERVER_ERROR(-101, "INTERNAL_SERVER_ERROR"),
    UNKNOWN_ERROR(-100, "服务端返回未知错误"),
    SUCCESS(0, "成功"),
    DATA_NOT_FOUND_1003000(1003000, "数据未找到"),
    USERNAME_OR_PASSWORD_ERROR_1003010(1003010, "用户名或密码错误"),
    USER_NOT_FOUND_1003020(1003020, "用户不存在"),
    ACCOUNT_NOT_FOUND_1003021(1003021, "账号不存在"),
    LOGIN_FAILED_1003030(1003030, "用户登录失败"),
    USER_LOCKED_TO_MANY_TIMES_FAILED_1003031(1003031,"密码多次错误，账户已被锁住" ),
    USER_EXIST_1003040(1003040, "用户已存在"),

    //    /* 不支持此手机号 */
    UNSUPPORTED_PHONE_NUM_1003050(1003050, "不支持此手机号"),
    //    /* 密码不能于原密码相同 */
    SAME_PASSWORD_ERROR_1003060(1003060, "密码不能于原密码相同"),
    //    /* 短信密码输入错误 */
    SMS_PASSWORD_ERROR_1003070(1003070, "短信密码输入错误"),
    //    /* 未知场景号 */
    UNRECOGNIZED_SCENARIO_1003080(1003080, "未知场景号"),
    //    /* 安全问题回答不合格 */
    SAFETYEXAM_NOT_QUALIFIED_1003090(1003090, "安全问题回答不合格"),
    //    /* 没有这个验证码 */
    NO_SUCH_SMSPASSWORD_1003110(1003110, "没有这个验证码"),
    //    /* 用户不能获取 */
    USER_NOT_AQUIRABLE_1003120(1003120, "用户不能获取"),
    //    /* 用户不能获取 */
    STATIC_PASSWORD_NOT_SET_1003130(1003130, "用户不能获取"),
    //    /* 用户获取token失败 */
    RENEW_USERTOKEN_FAILED_1003140(1003140, "用户获取token失败"),
    //    /* 不同应用切换token失败 */
    SWITCH_USERTOKEN_FAILED_1003150(1003150, "不同应用切换token失败"),
    //    /* 密码错误 */
    PASSWORD_ERROR_1003160(1003160, "密码错误"),
    //    /* 参数错误 */
    PARAMETER_ERROR_1003170(1003170, "参数错误"),
    //    /* 服务器内部错误 */
    INTERNAL_SERVER_ERROR_1003180(1003180, "服务器内部错误"),
    //    /* 写tair失败 */
    TAIR_WRITE_FAILED_1003181(1003181, "写tair失败"),
    //    /* 调用IM接口失败 */
    IM_INVOKE_FAILED_1003182(1003182, "调用IM接口失败"),
    //    /* 发送mq消息出错 */
    SEND_MQ_FAILED_1003190(1003190, "发送mq消息出错"),
    //    /* 万里通ID已经绑定到另外一个用户 */
    WANLIID_BINDED_TO_DIFFERENT_USER_1005000(1005000, "万里通ID已经绑定到另外一个用户"),
    //    /* 昵称已被他人使用 */
    NICKNAME_TAKEN_1006000(1006000, "昵称已被他人使用"),
    //    /* 不允许使用的昵称 */
    NICKNAME_FORBIDDEN_1006403(1006403, "不允许使用的昵称"),
    //    /* 禁止操作 */
    RISK_CONTROL_FORBIDDEN_1007000(1007000, "禁止操作"),
    //    /* 对普通用户无效 */
    NOT_APPLICABLE_TO_NORMAL_USERS_1008000(1008000, "对普通用户无效"),
    //    /* 超过收货地址最大限制 */
    ADDRESS_MAX_NUM_ERROR_1009000(1009000, "超过收货地址最大限制"),
    //    /* 昵称不允许为空 */
    NICKNAME_IS_NULL_ERROR_1010000(1010000, "昵称不允许为空"),
    //    /* 读数据库出错 */
    READ_DB_ERROR_1011000(1011000, "读数据库出错"),
    //    /* 写数据库出错 */
    WRITE_DB_ERROR_1011001(1011001, "写数据库出错"),
    //    /* 生成token失败 */
    GNERATE_TOKEN_FAILED_1012000(1012000, "生成token失败"),
    //    /* 未找到需要更新的记录 */
    NO_DATA_UPDATED_1013000(1013000, "未找到需要更新的记录"),
    //    /* 未找到需要删除的记录 */
    NO_DATA_DELETED_1013001(1013001, "未找到需要删除的记录"),
    //    /* 未找到记录 */
    NO_DATA_FOUND_1013002(1013002, "未找到记录"),
    //    /* 删除标签失败 */
    DELETE_TAG_FAILED_1014000(1014000, "删除标签失败"),
    //    /* 微信用户已存在 */
    REGISTER_WX_OPEN_EXIST_1014001(1014001, "微信用户已存在"),
    //    /* 省未找到 */
    PROVOINCE_NOT_FOUND_1015000(1015000, "省未找到"),
    //    /* 市未找到 */
    CITY_NOT_FOUND_1015001(1015001, "市未找到"),
    //    /* 用户已经有手机号了 */
    USER_HAS_MOBILE_1015002(1015002, "用户已经有手机号了"),
    //    /* 手机号码已注册 */
    MOBILE_REGISTED_1015003(1015003, "手机号码已注册"),
    //    /* 短信发送失败 */
    SMS_SEND_FAILED_1015004(1015004, "短信发送失败"),
    //    /* 短信发送中 */
    SMS_SEND_ING_1015005(1015005, "短信发送中"),
    //    /* 短信已发送 */
    SMS_ALREADY_SEND_1015006(1015006, "短信已发送"),
    //    /* 短信验证码错误 */
    SMS_VERIFY_CODE_ERROR_1015007(1015007, "短信验证码错误"),
    //    /* 不允许修改 */
    MODIFY_DISABLE_1015008(1015008, "不允许修改"),
    //    /* 用户昵称已经存在 */
    NICKNAME_EXISTED_1015009(1015009, "用户昵称已经存在"),
    //    /* 店铺名称已经存在 */
    EXISTED_MERCHANT_NAME_1016000(1016000, "店铺名称已经存在"),
    //    /* 写入数据库错误 */
    WRITE_DB_FAILED_2000001(2000001, "写入数据库错误"),
    //    /* 数据库读取操作失败 */
    READ_DB_FAILED_2000002(2000002, "数据库读取操作失败"),
    //    /* 系统错误 */
    SYSTEM_EXCEPTION_CODE_2000003(2000003, "系统错误"),
    //    /* 风控 */
    RISK_CODE_2000004(2000004, "风控"),
    //    /* 内容不能为空 */
    CONTENT_CAN_NOT_BE_NULL_2000005(2000005, "内容不能为空"),
    //    /* 参数错误 */
    PARAMETER_ERROR_2000006(2000006, "参数错误"),
    //    /* 评论未找到 */
    COMMENT_NOT_FOUND_2000007(2000007, "评论未找到"),
    //    /* 没有权限 */
    PERMISSION_DENY_2000008(2000008, "没有权限"),
    //    /* 设备不存在 */
    DEVICE_NOT_EXIST_2002000(2002000, "设备不存在"),
    //    /* 设备被拒绝访问 */
    DEVICE_DENIED_2002010(2002010, "设备被拒绝访问"),
    //    /* 挑战码过期 */
    DEVICE_CHALLENGE_EXPIRED_2002020(2002020, "挑战码过期"),
    //    /* 设备证书损坏 */
    DEVICE_CERT_BROKEN_2002030(2002030, "设备证书损坏"),
    //    /* 应用id不存在 */
    DEVICE_APPID_NOT_EXIST_2003000(2003000, "应用id不存在"),
    //    /* 无法生成新的设备id */
    DEVICE_DEVICEID_GENERATE_FAILED_2003010(2003010, "无法生成新的设备id"),
    //    /* 证书生成错误 */
    DEVICE_CERT_GENERATE_FAILED_2003020(2003020, "证书生成错误"),
    //    /* 验证证书生成错误 */
    DEVICE_CERT_VERITY_FAILED_2003030(2003030, "验证证书生成错误"),
    //    /* 写入数据库错误 */
    WRITE_DB_FAILED_3000001(3000001, "写入数据库错误"),
    //    /* 数据库读取操作失败 */
    READ_DB_FAILED_3000002(3000002, "数据库读取操作失败"),
    //    /* 系统错误 */
    SYSTEM_EXCEPTION_CODE_3000003(3000003, "系统错误"),
    //    /* 风控 */
    RISK_CODE_3000004(3000004, "风控"),
    //    /* 参数错误 */
    CONTENT_CAN_NOT_BE_NULL_3000005(3000005, "参数错误"),
    //    /* 活动未找到 */
    ACTIVTIY_NOT_FOUND_3000006(3000006, "活动未找到"),
    //    /* 俱乐部未找到 */
    CLUB_NOT_FOUND_3000007(3000007, "俱乐部未找到"),
    //    /* 俱乐部成员未找到 */
    CLUB_MEMBER_NOT_FOUND_3000008(3000008, "俱乐部成员未找到"),
    //    /* 没有权限 */
    PERMISSION_DENY_3000009(3000009, "没有权限"),
    //    /* 话题未找到 */
    SUBJECT_NOT_FOUND_3000011(3000011, "话题未找到"),
    //    /* 游记未找到 */
    TRAVELSPECIAL_NOT_FOUND_3000012(3000012, "游记未找到"),
    //    /* 参数错误 */
    PARAMETER_ERROR_3000013(3000013, "参数错误"),
    //    /* 超过最大关注数量 */
    EXCEED_MAX_FOLLOW_SIZE_3000020(3000020, "超过最大关注数量"),
    //    /* 已关注 */
    CHECK_ATTENTION_STATUS_3000021(3000021, "已关注"),
    //    /* 服务内部错误 */
    INTERNAL_SERVER_ERROR_4000001(4000001, "服务内部错误"),
    //    /* 参数错误 */
    PARAM_ERROR_4000002(4000002, "参数错误"),
    //    /* 商品类型错误 */
    ITEM_TYPE_WRONG_4000003(4000003, "商品类型错误"),
    //    /* 入住日期已过期 */
    ENTER_DATE_EXPIRED_4000004(4000004, "入住日期已过期"),
    //    /* 请先完成当前咨询服务 */
    PROCESS_ORDER_NOT_FINISH_4000005(4000005, "请先完成当前咨询服务"),
    //    /* 目前没有可提供咨询服务的咨询师~换个时间再来咨询吧 */
    CONSULT_ONLINE_TALENT_NOT_FOUND_4000006(4000006, " 目前没有可提供咨询服务的咨询师~换个时间再来咨询吧"),
    //    /* 没有找到符合条件的咨询师~换个咨询内容试试吧 */
    FAST_CONSULT_ITEM_NOT_FOUND_4000007(4000007, "没有找到符合条件的咨询师~换个咨询内容试试吧"),
    //    /* 商品不存在 */
    ITEM_NOT_FOUND_4000100(4000100, "商品不存在"),
    //    /* 未查询到卖家 */
    SELLER_NOT_FOUND_4000101(4000101, "未查询到卖家"),
    //    /* 写入数据库错误 */
    WRITE_DB_FAILED_5000001(5000001, "写入数据库错误"),
    //    /* 数据库读取操作失败 */
    READ_DB_FAILED_5000002(5000002, "数据库读取操作失败"),
    //    /* 系统错误 */
    SYSTEM_EXCEPTION_CODE_5000003(5000003, "系统错误"),
    //    /* 风控 */
    RISK_CODE_5000004(5000004, "风控"),
    //    /* 参数错误 */
    CONTENT_CAN_NOT_BE_NULL_5000005(5000005, "参数错误"),
    //    /* 话题未找到 */
    SUBJECT_NOT_FOUND_5000006(5000006, "话题未找到"),
    //    /* 评论未找到 */
    COMMENT_NOT_FOUND_5000007(5000007, "评论未找到"),
    //    /* 没有权限 */
    PERMISSION_DENY_5000008(5000008, "没有权限"),
    //    /* 未知错误 */
    PERUNKNOWN_DENY_5000009(5000009, "未知错误"),
    //    /* 点赞未找到 */
    SUPPORTUNKNOWN_DENY_5000010(5000010, "点赞未找到"),
    //    /* 没有权限查看 */
    NO_PERMISSION_TO_VIEW_6000000(6000000, "没有权限查看"),
    //    /* 获取所有省级行政区信息出错 */
    GET_ALL_PROVINCE_LIST_ERROR_6000001(6000001, "获取所有省级行政区信息出错"),
    //    /* 通过省级行政区编码获取所有市级行政区信息出错 */
    GET_ALL_CITY_LIST_BY_PROVINCE_CODE_ERROR_6000002(6000002, "通过省级行政区编码获取所有市级行政区信息出错"),
    //    /* 通过省级行政区编码获取所有区县级行政区信息出错 */
    GET_ALL_AREA_LIST_BY_PROVINCE_CODE_ERROR_6000003(6000003, "通过省级行政区编码获取所有区县级行政区信息出错"),
    //    /* 通过市级行政区编码获取所有区县级行政区信息出错 */
    GET_ALL_AREA_LIST_BY_CITY_CODE_ERROR_6000004(6000004, "通过市级行政区编码获取所有区县级行政区信息出错"),
    //    /* 获取我的收货地址信息出错 */
    GET_ALL_MY_ADDRESS_ERROR_6000005(6000005, "获取我的收货地址信息出错"),
    //    /* 更新我的收货地址信息出错 */
    UPDATE_MYADDRESS_ERROR_6000006(6000006, "更新我的收货地址信息出错"),
    //    /* 新增我的收货地址信息出错 */
    ADD_MYADDRESS_ERROR_6000007(6000007, "新增我的收货地址信息出错"),
    //    /* 删除我的收货地址信息出错 */
    DEL_MYADDRESS_ERROR_6000008(6000008, "删除我的收货地址信息出错"),
    //    /* 获取我的受检人信息出错 */
    GET_ALL_MY_PEOPLE_ERROR_6000009(6000009, "获取我的受检人信息出错"),
    //    /* 新增我的受检人信息出错 */
    ADD_MY_PEOPLE_ERROR_6000010(6000010, "新增我的受检人信息出错"),
    //    /* 更新我的受检人信息出错 */
    UPDATE_MY_PEOPLE_ERROR_6000011(6000011, "更新我的受检人信息出错"),
    //    /* 删除我的受检人信息出错 */
    DEL_MY_PEOPLE_ERROR_6000012(6000012, "删除我的受检人信息出错"),
    //    /* 获取当前登录用户所有受检人报告列表信息出错 */
    GET_MY_TEST_REPORT_LIST_ERROR_6000014(6000014, "获取当前登录用户所有受检人报告列表信息出错"),
    //    /* 通过联系人id获取该受检人报告列表信息出错 */
    GET_TEST_REPORT_LIST_BY_PEOPLE_ID_ERROR_6000015(6000015, "通过联系人id获取该受检人报告列表信息出错"),
    //    /* 获取所有城市数据出错 */
    GET_ALL_CITY_LIST_ERROR_6000016(6000016, "获取所有城市数据出错"),
    //    /* 查询订单列表出错 */
    PAGE_QUERY_BIZ_ORDER_FOR_BUYER_ERROR_6000017(6000017, "查询订单列表出错"),
    //    /* 查询订单详情出错 */
    QUERY_BIZ_ORDER_FOR_BUYER_ERROR_6000018(6000018, "查询订单详情出错"),
    //    /* 积分兑换商品出错 */
    CREDITS_EXCHANGE_ERROR_6000019(6000019, "积分兑换商品出错"),
    //    /* 使用基因检测服务出错 */
    USEGENE_GOODS_ERROR_6000020(6000020, "使用基因检测服务出错"),
    //    /* 买家确认收货出错 */
    BUYER_CONFIRM_GOODS_DELIVERIED_ERROR_6000021(6000021, "买家确认收货出错"),
    //    /* 商品已下架 */
    ITEM_NOT_AVAILABLE_6000022(6000022, "商品已下架"),
    //    /* 无法关闭订单 */
    ORDER_CANNOT_CLOSE_6000023(6000023, "无法关闭订单"),
    //    /* 获取体检报告出错 */
    GET_TESTREPORTBYID_ERROR_6000024(6000024, "获取体检报告出错"),
    //    /* 新增我的受检人信息出错 */
    ADD_EXAMINEE_COUNT_ERROR_6000030(6000030, "新增我的受检人信息出错"),
    //    /* 需要收货地址信息。 */
    NEED_ADDRESS_6000040(6000040, "需要收货地址信息"),
    //    /* 发票信息不全。 */
    INVOICE_INFO_INCOMPLETE_6000041(6000041, "发票信息不全"),
    //    /* 卡不存在。 */
    CARD_NOT_FOUND_6000042(6000042, "卡不存在"),
    //    /* 卡已经被激活。 */
    CARD_ACTIVATED_6000043(6000043, "卡已经被激活"),
    //    /* 很遗憾，您的优惠券已过期 */
    WRONG_VOUCHER_6000050(6000050, "很遗憾，您的优惠券已过期"),
    //    /* 提交订单参数不合法。 */
    CREATE_ORDER_ERROR_6000070(6000070, "提交订单参数不合法"),
    //    /* 订单已提交 */
    CREATE_ORDER_EXIST_6000071(6000071, "订单已提交"),
    //    /* 提交订单商品过期 */
    CREATE_ORDER_ITEMSKU_EXPIRED_6000072(6000072, "提交订单商品过期"),
    //    /* 提交订单失败 */
    CREATE_ORDER_PARAM_ERROR_6000073(6000073, "提交订单失败"),
    //    /* 提交订单时间错误 */
    CREATE_ORDER_TIME_PARAM_ERROR_6000074(6000074, "提交订单时间错误"),
    //    /* 商品不存在。 */
    ITEM_NOT_FOUND_6000100(6000100, "商品不存在"),
    //    /* 没有库存了。 */
    SOLD_OUT_6000101(6000101, "没有库存了"),
    //    /* 商品没有最小销售单元 */
    ITEM_SKU_NOT_FOUND_6000102(6000102, "商品没有最小销售单元"),
    //    /* 用户有效的收货地址过多 */
    UPDATE_MYADDRESS_COUNT_ERROR_6000106(6000106, "用户有效的收货地址过多"),
    //    /* 买家没有足够积分。 */
    BUYER_NOT_HAVE_ENOUGH_CREDIT_6000200(6000200, "买家没有足够积分"),
    //    /* 买家没有足够金币。 */
    BUYER_NOT_HAVE_ENOUGH_GOLD_6000201(6000201, "买家没有足够金币"),
    //    /* 不是未付款状态。 */
    PAY_STATUS_IS_NOT_WAIT_PAY_6000202(6000202, "不是未付款状态"),
    //    /* 支付系统已经支付，不能关闭。 */
    ORDER_CAN_NOT_CLOSE_6000203(6000203, "支付系统已经支付，不能关闭"),
    //    /* 卡过期。 */
    CARD_EXPIRED_6000204(6000204, "卡过期"),
    //    /* 订单找不到。 */
    BIZ_ORDER_NOT_FOUND_6000300(6000300, "订单找不到"),
    //    /* 不能删除其他人的数据。 */
    ADDRESS_REMOVE_OTHER_ERROR_6000400(6000400, "不能删除其他人的数据"),
    //    /* 收货地址不存在 */
    ADDRESS_NOT_FOUND_6000401(6000401, "收货地址不存在"),
    //    /* 地址信息中缺少省的信息 */
    ADDRESS_MISSING_PROVINCE_6000402(6000402, "地址信息中缺少省的信息"),
    //    /* 地址信息中缺少市的信息 */
    ADDRESS_MISSING_CITY_6000403(6000403, "地址信息中缺少市的信息"),
    //    /* 地址信息中缺少区的信息 */
    ADDRESS_MISSING_AREA_6000404(6000404, "地址信息中缺少区的信息"),
    //    /* 省信息不存在 */
    PROVINCE_NOT_EXIT_6000405(6000405, "省信息不存在"),
    //    /* 市信息不存在 */
    CITY_NOT_EXIT_6000406(6000406, "市信息不存在"),
    //    /* 区信息不存在 */
    AREA_NOT_EXIT_6000407(6000407, "区信息不存在"),
    //    /* 地址信息中的city code错误，不在当前省中 */
    ADDRESS_CITY_CODE_ERROR_6000408(6000408, "地址信息中的city code错误，不在当前省中"),
    //    /* 地址信息中的area code错误，不在当前城市中 */
    ADDRESS_AREA_CODE_ERROR_6000409(6000409, "地址信息中的area code错误，不在当前城市中"),
    //    /* 收货地址数目超过限制 */
    ADDRESS_TO_MUCH_ERROR_6000410(6000410, "收货地址数目超过限制"),
    //    /* 不能修改他人的地址 */
    ADDRESS_MODIFIED_OTHER_DATA_ERROR_6000411(6000411, "不能修改他人的地址"),
    //    /* 受检人信息未找到 */
    EXAMINEE_NOT_FOUND_6000500(6000500, "受检人信息未找到"),
    //    /* 不能修改他人的受检人信息 */
    EXAMINEE_UPDATE_OTHER_DATA_ERROR_6000501(6000501, "不能修改他人的受检人信息"),
    //    /* 不能删除他人的受检人信息 */
    EXAMINEE_DELETE_OTHER_DATA_ERROR_6000502(6000502, "不能删除他人的受检人信息"),
    //    /* 受检人数目超过最大限制 */
    EXAMINEE_TO_MUCH_ERROR_6000503(6000503, "受检人数目超过最大限制"),
    //    /* 目前医生没有排队用户 */
    DOCTOR_QUEUE_EMPTY_6000600(6000600, "目前医生没有排队用户"),
    //    /* 医生不存在 */
    DOCTOR_ITEM_NOT_FOUND_6000601(6000601, "医生不存在"),
    //    /* 转诊医生在同一个组中，不能转诊 */
    REFERRAL_FAILED_DOCTOR_IN_SAME_GROUP_6000603(6000603, "转诊医生在同一个组中，不能转诊"),
    //    /* 用户未找到 */
    USER_NOT_FOUND_6000604(6000604, "用户未找到"),
    //    /* 用户详细信息未找到 */
    USER_PROFILE_NOT_FOUND_6000605(6000605, "用户详细信息未找到"),
    //    /* 未找到医生 */
    ENTRANCE_DOCTOR_NOT_FOUND_6000606(6000606, "未找到医生"),
    //    /* 用户提交主诉失败 */
    USER_SUBMIT_CHIEF_ERROR_6000701(6000701, "用户提交主诉失败"),
    //    /* 用户提交评论失败 */
    USER_POST_COMMENT_ERROR_6000801(6000801, "用户提交评论失败"),
    //    /* 基因盒编号不存在，请核对编号 */
    GENE_SN_NOT_EXITS_ERROR_6000811(6000811, "基因盒编号不存在，请核对编号"),
    //    /* 该编号已使用请输入未使用的基因盒编号 */
    GENE_SN_ALREADY_USED_ERROR_6000812(6000812, "该编号已使用请输入未使用的基因盒编号"),
    //    /* 导医不在线 */
    DOCTOR_APPOINTMENT_GUIDE_OFFLINE_6000901(6000901, "导医不在线"),
    //    /* 问诊提交主诉参数不合法 */
    CONSULT_CHECK_SUBMIT_CHIEF_PARAM_ERROR_6000902(6000902, "问诊提交主诉参数不合法"),
    //    /* 问诊的服务履行记录不合法 */
    CONSULT_CHECK_SERVICE_ORDER_ITEMID_ERROR_6000903(6000903, "问诊的服务履行记录不合法"),
    //    /* 问诊医生不在线 */
    CONSULT_DOCTOR_OFFLINE_ERROR_6000904(6000904, "问诊医生不在线"),
    //    /* 问诊服务履行状态不一致 */
    CONSULT_CHECK_STATUS_NOT_MATCH_6000905(6000905, "问诊服务履行状态不一致"),
    //    /* 用户同意问诊失败 */
    CONSULT_USER_AGREE_CONSULT_ERROR_6000906(6000906, "用户同意问诊失败"),
    //    /* 转诊卡片过期 */
    CONSULT_TRANSFER_CARD_EXPIRED_6000907(6000907, "转诊卡片过期"),
    //    /* 续费的状态不合法，不能续费 */
    CONSULT_RENEWAL_SERVICE_STATUS_ERROR_6000908(6000908, "续费的状态不合法，不能续费"),
    //    /* 医生工作台参数不合法 */
    DOCTOR_PLATFORM_PARAMETER_ILLEGAL_6001001(6001001, "医生工作台参数不合法"),
    //    /* 医生工作台调用方法发生异常 */
    DOCTOR_PLATFORM_FUNCTION_ERROR_6001002(6001002, "医生工作台调用方法发生异常"),
    //    /* 医生工作台获取队列中第一个排队用户失败 */
    DOCTOR_PLATFORM_GETFIRST_FROMWAITINGQUEUE_FAIL_6001003(6001003, "医生工作台获取队列中第一个排队用户失败"),
    //    /* 医生工作台获取队列数量失败 */
    DOCTOR_PLATFORM_GET_WAITINGQUEUE_SIZE_FAIL_6001004(6001004, "医生工作台获取队列数量失败"),
    //    /* 医生工作台拉用户进去问诊失败 */
    DOCTOR_PLATFORM_PULL_USER_INTO_CONSULT_FAIL_6001005(6001005, "医生工作台拉用户进去问诊失败"),
    //    /* 医生工作台拉用户进去问诊(旧流程)失败 */
    DOCTOR_PLATFORM_PULL_USER_INTO_CONSULT_OLD_FAIL_6001006(6001006, "医生工作台拉用户进去问诊(旧流程)失败"),
    //    /* 医生工作台引导问诊结束失败 */
    DOCTOR_PLATFORM_PULL_GUIDE_FINISH_CONSULT_FAIL_6001007(6001007, "医生工作台引导问诊结束失败"),
    //    /* 医生工作台关闭问诊失败 */
    DOCTOR_PLATFORM_CLOSE_CONSULT_FAIL_6001008(6001008, "医生工作台关闭问诊失败"),
    //    /* 医生工作台关闭问诊失败 */
    DOCTOR_PLATFORM_GET_CONSULT_QUEUE_SERVICEORDERITEMS_FAIL_6001009(6001009, "医生工作台关闭问诊失败"),
    //    /* 医生工作台推卡片失败 */
    DOCTOR_PLATFORM_PUSH_DEPARTMENT_CARD_FAIL_6001010(6001010, "医生工作台推卡片失败"),
    //    /* 时间参数转化失败 */
    ORDER_QUERY_PARAM_PARSE_DATE_ERROR_6001011(6001011, "时间参数转化失败"),
    //    /* 经纬度不符合要求 */
    MY_ADDRESS_LNG_LAT_ERROR_6001012(6001012, "经纬度不符合要求"),
    //    /* 地址列表为空 */
    MY_ADDRESS_EMPTY_6001013(6001013, "地址列表为空"),
    //    /* tc服务没有正常返回值 */
    MY_ADDRESS_TC_ERROR_6001014(6001014, "tc服务没有正常返回值"),
    //    /* 订单状态不是未付款状态 */
    ORDER_STATUS_ERROR_6002000(6002000, "订单状态不是未付款状态"),
    //    /* 用户提交评价失败 */
    USER_PUBLISH_RATE_ERROR_6002001(6002001, "用户提交评价失败"),
    //    /* 请先完成当前咨询服务 */
    CONSULTION_CONTINUE_ERROR_6003000(6003000, "请先完成当前咨询服务"),
    //    /* 目前该时间段,没有可提供服务的咨询师,换个时间再来吧 */
    CONSULTION_NO_ITEM_ERROR_6003001(6003001, "目前该时间段,没有可提供服务的咨询师,换个时间再来吧"),
    //    /* 所选商品不存在 */
    CONSULTION_ITEM_EXIST_ERROR_6003002(6003002, "所选商品不存在"),
    //    /* 咨询师暂不提供服务 */
    CONSULTION_ITEM_OFFLINE_ERROR_6003003(6003003, "咨询师暂不提供服务"),
    //    /* 订单不存在 */
    CONSULTION_PROCESS_ORDER_NOT_FIND_6003004(6003004, "订单不存在"),
    //    /* 订单查询失败 */
    CONSULTION_PROCESS_ORDER_QUERY_ERROR_6003005(6003005, "订单查询失败"),
    //    /* 提交流程单失败 */
    CREATE_PROCESS_ORDER_ERROR_6003006(6003006, "提交流程单失败"),
    //    /* 提交流程单参数错误 */
    CREATE_PROCESS_ORDER_PARAM_ERROR_6003007(6003007, "提交流程单参数错误"),
    //    /* 错误的流程单类型 */
    UNKNOWN_PROCESS_ORDER_TYPE_6003008(6003008, "错误的流程单类型"),
    //    /* 咨询师不在线 */
    CONSULT_TALENT_OFFLINE_6003009(6003009, "咨询师不在线"),
    //    /* 其他咨询尚未完成 */
    CONSULT_ORDER_NOT_FINISHED_6003010(6003010, "其他咨询尚未完成"),
    //    /* 不能对自己的商品下单 */
    BUYER_AND_SELLER_CANNOT_BE_SAMEONE_6003011(6003011, "不能对自己的商品下单"),
    //    /* 用户已取消,接单失败 */
    BUYER_CANCELED_PROCESS_ORDER_6003012(6003012, "用户已取消,接单失败"),
    //    /* 包裹信息未找到 */
    PACKAGE_NOT_FOUND_6003013(6003013, "包裹信息未找到"),
    //    /* 禁止使用激活 */
    RISK_FORBID_ERROR_6999997(6999997, "禁止使用激活"),
    //    /* 系统错误 */
    SYSTEM_ERROR_6999998(6999998, "系统错误"),
    //    /* 缺少参数 */
    MISSING_PARAM_6999999(6999999, "缺少参数"),
    //    /* 系统异常 */
    SYSTEM_EXCEPTION_CODE_7000000(7000000, "系统异常"),
    //    /* 请不要重复操作 */
    REPEATED_OPERATIONS_7000001(7000001, "请不要重复操作"),
    //    /* 缺少参数 */
    MISSING_PARAMETER_7000002(7000002, "缺少参数"),
    //    /* 请求超时 */
    TIME_OUT_7000003(7000003, "请求超时"),
    //    /* 写数据库失败 */
    WRITE_DB_FAILED_7000004(7000004, "写数据库失败"),
    //    /* 空对象 */
    EMPTY_OBJECT_7000005(7000005, "空对象"),
    //    /* 写数据库失败 */
    READ_DB_FAILED_7000006(7000006, "写数据库失败"),
    //    /* 发送IM消息失败 */
    SEND_IM_MESSAGE_FAILED_7000007(7000007, "发送IM消息失败"),
    //    /* 数据订正的sql非法 */
    DB_SQL_INVALID_7000100(7000100, "数据订正的sql非法"),
    //    /* 数据订正的sql非法,不能执行删除表操作 */
    DB_SQL_DROP_NOT_ALLOWED_7000101(7000101, "数据订正的sql非法,不能执行删除表操作"),
    //    /* 数据订正的sql非法,不能一次插入多条数据 */
    DB_SQL_INSERT_MULTI_NOT_ALLOWED_7000102(7000102, "数据订正的sql非法,不能一次插入多条数据"),
    //    /* 数据订正的sql非法,不能执行删除数据操作 */
    DB_SQL_DELETE_NOT_ALLOWED_7000103(7000103, "数据订正的sql非法,不能执行删除数据操作"),
    //    /* 订正数据的sql执行失败 */
    DB_SQL_EXECUTE_FAILED_7000104(7000104, "订正数据的sql执行失败"),
    //    /* 数据库记录不存在 */
    DB_DATA_NOTFOUND_7000105(7000105, "数据库记录不存在"),
    //    /* 文章已下架 */
    DB_DATA_OFFLINE_7000106(7000106, "文章已下架"),
    //    /* booth不存在 */
    BOOTH_NOT_EXIST_7000200(7000200, "booth不存在"),
    //    /* 同版本的booth已存在 */
    SAME_VERSION_BOOTH_EXIST_7000201(7000201, "同版本的booth已存在"),
    //    /* article处于下架状态 */
    ARTICLE_OFFLINE_7000300(7000300, "article处于下架状态"),
    //    /* article未找到 */
    ARTICLE_NOT_FOUND_7000301(7000301, "article未找到"),
    //    /* 服务内部错误 */
    INTERNAL_SERVER_ERROR_11000000(11000000, "服务内部错误"),
    //    /* 未找到店铺 */
    SELLER_NOT_FOUND_11000001(11000001, "未找到店铺"),
    //    /* 很遗憾，券领取失败 */
    BIND_VOUCHER_ERROR_11000002(11000002, "很遗憾，券领取失败"),
    //    /* 您已领取过了哦！ */
    HAS_BIND_VOUCHER_11000003(11000003, "您已领取过了哦！"),
    //    /* 券已经下架 */
    VOUCHER_OVERDUE_11000004(11000004, "券已经下架"),
    //    /* 未找到商品 */
    ITEM_NOT_FOUND_11000005(11000005, "未找到商品"),
    //    /* 缺少参数 */
    MISSING_PARAM_11000006(11000006, "缺少参数"),
    //    /* 系统错误 */
    SYSTEM_ERROR_14000000(14000000, "系统错误"),
    //    /* 写数据库失败 */
    DB_WRITE_FAILED_14000001(14000001, "写数据库失败"),
    //    /* 读数据库失败 */
    DB_READ_FAILED_14000002(14000002, "读数据库失败"),
    //    /* 参数 错误 */
    PARAMTER_ERROR_14000003(14000003, "参数 错误"),
    //    /* 写Tair失败 */
    TAIR_WRITE_FAILED_14000004(14000004, "写Tair失败"),
    //    /* 请勿重复提交，请稍后再试！ */
    REPEATED_SUBMISSION_14000005(14000005, "请勿重复提交，请稍后再试！"),
    //    /* 发送mq消息失败 */
    SEND_MQ_MSG_FAILED_14000006(14000006, "发送mq消息失败"),
    //    /* 远程调用失败 */
    RPC_CALL_FAILED_14000007(14000007, "远程调用失败"),
    //    /* 重复操作 */
    REPEATED_OPTION_14000008(14000008, "重复操作"),
    //    /* tair中不存在数据 */
    TAIR_DATA_NOT_EXIT_14000009(14000009, "tair中不存在数据"),
    //    /* 签名数据错误 */
    SIG_DATA_ERROR_14000010(14000010, "签名数据错误"),
    //    /* 活动不存在 */
    ACTIVITY_NOT_FOUND_14001000(14001000, "活动不存在"),
    //    /* 活动已结束，下次早点来哦！ */
    ACTIVITY_INVALID_14001001(14001001, "活动已结束，下次早点来哦！"),
    //    /* 活动未开始 */
    ACTIVITY_NOT_START_YET_14001002(14001002, "活动未开始"),
    //    /* 活动已经结束 */
    ACTIVITY_ALREADY_END_14001003(14001003, "活动已经结束"),
    //    /* 活动邀请码生成失败 */
    ACTIVITY_INVITE_GENERATE_FAILED_14001004(14001004, "活动邀请码生成失败"),
    //    /* 用户还未参加活动 */
    USER_NOT_JOINED_ACTIVITY_14001005(14001005, "用户还未参加活动"),
    //    /* 之前已经申请发奖了，目前正等待发奖中 */
    USER_WAITING_AWARDS_14001006(14001006, "之前已经申请发奖了，目前正等待发奖中"),
    //    /* 用户邀请码错误 */
    USER_INVITE_CODE_ERROR_14001007(14001007, "用户邀请码错误"),
    //    /* 用户已被其他用户邀请 */
    USER_ALREAD_INVITED_14001008(14001008, "用户已被其他用户邀请"),
    //    /* 用户未被邀请 */
    USER_NOT_INVITED_14001009(14001009, "用户未被邀请"),
    //    /* 手机号码错误 */
    MOBILE_ERROR_14001010(14001010, "手机号码错误"),
    //    /* 手机号码已经注册 */
    MOBILE_HAS_REG_14001011(14001011, "手机号码已经注册"),
    //    /* 非指定活动 */
    NOT_SPECIFY_ACTIVTTY_14001012(14001012, "非指定活动"),
    //    /* 用户不存在 */
    USER_NOT_FOUND_ERROR_14001013(14001013, "用户不存在"),
    //    /* 老用户不予许参加活动 */
    OLD_USER_NOT_ALLOW_JOIN_ACTIVITY_14001014(14001014, "老用户不予许参加活动"),
    //    /* 不是彩虹节活动，邀请好友不奖励电影券 */
    NOT_RAINBOW_NOT_REWARD_TICKET_FOR_INVITE_14001015(14001015, "不是彩虹节活动，邀请好友不奖励电影券"),
    //    /* 抽奖活动未找到 */
    LOTTERY_ACTIVITY_NOT_FOUND_14001030(14001030, "抽奖活动未找到"),
    //    /* 添加关注关系失败 */
    ADDFOLLOW_ERROR_14001031(14001031, "添加关注关系失败"),
    //    /* 没中奖，呜呜呜，再接再厉哟! */
    LOTTERY_NOT_WIN_14002001(14002001, "没中奖，呜呜呜，再接再厉哟!"),
    //    /* 奖品发完了，下次要积极哦! */
    PRIZE_NOT_FOUND_14002002(14002002, "奖品发完了，下次要积极哦!"),
    //    /* 减库存失败 */
    PRIZE_REDUCE_STOCK_FAILED_14002003(14002003, "减库存失败"),
    //    /* 奖品发完了，下次要积极哦! */
    PRIZE_NOT_EXIST_14002004(14002004, "奖品发完了，下次要积极哦! "),
    //    /* 中奖纪录未找到! */
    WIN_RECORD_NOT_FOUND_14002005(14002005, "中奖纪录未找到!"),
    //    /* 已兑奖，不能重复兑奖! */
    WIN_RECORD_EXCHANGED_14002006(14002006, "已兑奖，不能重复兑奖!"),
    //    /* 对不起，没有找到您的中奖纪录! */
    NOT_WIN_14002007(14002007, "对不起，没有找到您的中奖纪录!"),
    //    /* 请填写生日信息！ */
    NEED_BIRTHDAY_14002008(14002008, "请填写生日信息！"),
    //    /* 未兑换，不能使用! */
    WIN_RECORD_NOT_EXCHANGED_14002009(14002009, "未兑换，不能使用!"),
    //    /* 未兑换，不能使用! */
    WIN_RECORD_TO_LIMIT_ERROR_14002010(14002010, "未兑换，不能使用!"),
    //    /* 奖品类型未找到 */
    PRIZE_TYPE_NOT_FOUND_14002011(14002011, "奖品类型未找到"),
    //    /* 奖品类型未找到 */
    GRANT_PRIZE_FAILED_14002012(14002012, "奖品类型未找到"),
    //    /* win record out type错误 */
    WIN_RECORD_OUT_TYPE_ERROR_14002013(14002013, "win record out type错误"),
    //    /* 谜编码不正确，请重新输入! */
    TICKET_NOT_FOUND_14003000(14003000, "谜编码不正确，请重新输入!"),
    //    /* 谜编号已被使用! */
    TICKET_ALREADY_USED_14003001(14003001, "谜编号已被使用!"),
    //    /* 谜编号不正确，请重新输入! */
    TICKET_ERROR_14003002(14003002, "谜编号不正确，请重新输入!"),
    //    /* 抽奖码不正确，请重新输入! */
    TICKET_LOTTERY_CODE_ERROR_14003003(14003003, "抽奖码不正确，请重新输入!"),
    //    /* 抽奖码已使用! */
    TICKET_LOTTERY_CODE_USED_14003004(14003004, "抽奖码已使用!"),
    //    /* 手机号码已存在! */
    SPREAD_MOBILE_EXIST_14003005(14003005, "手机号码已存在!"),
    //    /* 谜编码已使用! */
    SPREAD_TICKET_EXIST_14003006(14003006, "谜编码已使用!"),
    //    /* 身份证已存在! */
    SPREAD_ID_CARD_EXIST_14003007(14003007, "身份证已存在!"),
    //    /* 身份证号码错误! */
    SPREAD_ID_CARD_ERROR_14003008(14003008, "身份证号码错误!"),
    //    /* 姓名非法! */
    SPREAD_USERNAME_INVALIDE_14003009(14003009, "姓名非法!"),
    //    /* 券的数量不足 */
    TICKET_SOLD_OUT_14003010(14003010, "券的数量不足"),
    //    /* 活动太火爆了，请稍后重试 */
    ACTIVITY_TO_HOT_14003011(14003011, "活动太火爆了，请稍后重试"),
    //    /* 活动已参加 */
    ACTIVITY_JOIND_14003012(14003012, "活动已参加"),
    //    /* 发送短信失败 */
    SMS_SEND_FAILED_14004001(14004001, "发送短信失败"),
    //    /* 短信已发送，请稍等! */
    SMS_ALREADY_SEND_14004002(14004002, "短信已发送，请稍等!"),
    //    /* 手机号码不正确，请重新输入! */
    SMS_MOBILE_ERROR_14004003(14004003, "手机号码不正确，请重新输入!"),
    //    /* 手机验证码不正确，请重新输入! */
    MOBILE_VERIFY_CODE_ERROR_14004004(14004004, "手机验证码不正确，请重新输入!"),
    //    /* 短信发送中，请稍后! */
    SMS_SEND_ING_14004005(14004005, "短信发送中，请稍后!"),
    //    /* push消息，设备未绑定 */
    PUSH_SEND_UNBIND_ERROR_14004007(14004007, "push消息，设备未绑定"),
    //    /* 第三方应用配置错误 */
    THIRD_APP_INFO_ERROR_14100001(14100001, "第三方应用配置错误"),
    //    /* 邀请好友参数错误 */
    USER_INVITE_ERROR_14101001(14101001, "邀请好友参数错误"),
    //    /* 邀请好友列表手机参数为空 */
    USER_INVITE_PHONE_ERROR_14101002(14101002, "邀请好友列表手机参数为空"),
    //    /* 已发送邀请短信 */
    USER_INVITE_SENDSMS_ERROR_14101003(14101003, "已发送邀请短信"),
    //    /* 短信提醒异常 */
    USER_INVITE_SENDSMS_EXCETPION_14101004(14101004, "短信提醒异常"),
    //    /* 每天最多可提醒5个好友 */
    USER_INVITE_SENDSMS_MAX_ERROR_14101005(14101005, "每天最多可提醒5个好友"),
    //    /* 调用短信接口失败 */
    USER_INVITE_SENDSMS_CALL_ERROR_14101006(14101006, "调用短信接口失败"),
    //    /* 用户信息错误 */
    USER_INVITE_USER_INFO_ERROR_14101007(14101007, "用户信息错误"),
    //    /* 添加邀请好友信息失败 */
    USER_INVITE_USER_APP_ERROR_14101008(14101008, "添加邀请好友信息失败"),
    //    /* 注册用户不才参与计步器活动 */
    USER_INVITE_USER_APP_SUCCES_14101009(14101009, "注册用户不才参与计步器活动"),
    //    /* 活动数据配置错误 */
    USER_INVITE_USER_ACT_CODE_ERROR_14101010(14101010, "活动数据配置错误"),
    //    /* 用户code错误 */
    USER_INVITE_USER_CODE_ERROR_14101011(14101011, "用户code错误"),
    //    /* rpc调用错误 */
    USER_INVITE_RPC_ERROR_14101012(14101012, "rpc调用错误"),
    //    /* 正在注册，请稍后重试 */
    USER_REGISTER_ING_ERROR_14101013(14101013, "正在注册，请稍后重试"),
    //    /* 生成token失败 */
    GNERATE_TOKEN_FAILED_14101014(14101014, "生成token失败"),
    //    /* 没有此类型活动 */
    ACTIVITY_STATUS_ERROR_14200001(14200001, "没有此类型活动"),
    //    /* 没有参与活动用户 */
    ACTIVITY_USER_NONE_14200002(14200002, "没有参与活动用"),
    //    /* 积分查询参数错误 */
    ACTIVITY_CALL_POINT_PARAM_ERROR_14200003(14200003, "积分查询参数错误"),
    //    /* 调用积分接口异常 */
    ACTIVITY_CALL_POINT_EXCEPTION_14200004(14200004, "调用积分接口异常"),
    //    /* 添加中奖记录失败 */
    ACTIVITY_ADD_WIN_ERROR_14200005(14200005, "添加中奖记录失败"),
    //    /* 积分累加错误,与预期值不符 */
    ACTIVITY_CALL_POINT_ERROR_14200006(14200006, "积分累加错误,与预期值不符"),
    //    /* 更新中奖记录状态失败 */
    ACTIVITY_UPDATE_WIN_ERROR_14200007(14200007, "更新中奖记录状态失败"),
    //    /* 手机推送提醒异常 */
    USER_INVITE_SENDPUSH_EXCEPTION_14200008(14200008, "手机推送提醒异常"),
    //    /* 用户积分不足 */
    ACTIVITY_CALL_POINT_NOT_ENOUGH_14200009(14200009, "用户积分不足"),
    //    /* 图片验证码错误 */
    IMG_VALIDATE_CODE_ERROR_14300000(14300000, "图片验证码错误"),
    //    /* 图片验证码的key错误 */
    IMG_VALIDATE_CODE_KEY_ERROR_14300001(14300001, "图片验证码的key错误"),
    //    /* enable key错误 */
    ENABLE_KEY_ERROR_14300002(14300002, "enable key错误"),
    //    /* 未找到此礼包 */
    PACKAGE_NOT_FOUND_ERROR_14300100(14300100, "未找到此礼包"),
    //    /* 礼包配置错误 */
    PACKAGE_CONFIG_ERROR_14300101(14300101, "礼包配置错误"),
    //    /* 礼包积分发放错误 */
    PACKAGE_POINT_ISSUE_ERROR_14300102(14300102, "礼包积分发放错误"),
    //    /* 没有达到领取礼包的要求 */
    PACKAGE_LIMIT_ERROR_14300103(14300103, "没有达到领取礼包的要求"),
    //    /* 已经参加过接机活动 */
    SHUTTLE_REPEAT_ERROR_14500000(14500000, "已经参加过接机活动"),
    //    /* 积分重复提交 */
    LOTTERY_POINT_REPEAT_ERROR_14600001(14600001, "积分重复提交"),
    //    /* 执行成功 */
    PARAM_SUCCESS_14999999(14999999, "执行成功"),
    //    /* 系统错误 */
    SYSTEM_ERROR_16000000(16000000, "系统错误"),
    //    /* 写数据库失败 */
    DB_WRITE_FAILED_16000001(16000001, "写数据库失败"),
    //    /* 读数据库失败 */
    DB_READ_FAILED_16000002(16000002, "读数据库失败"),
    //    /* 参数错误 */
    PARAMTER_ERROR_16000003(16000003, "参数错误"),
    //    /* dubbo接口失败 */
    DUBBO_ERROR_16000004(16000004, "dubbo接口失败"),
    //    /* 无审核数据 */
    EXAMIN_DATA_ERROR_16000005(16000005, "无审核数据"),
    //    /* 更新数据库失败 */
    DB_UPDATE_FAILED_16000006(16000006, "更新数据库失败"),
    //    /* 已通过审核 */
    DB_EXAMINE_FAILED_16000007(16000007, "已通过审核"),
    //    /* 店铺名称已存在 */
    DB_MERCHANTNAME_FAILED_16000008(16000008, "店铺名称已存在"),
    //    /* 用户非咨询师 */
    DB_TALENT_FAILED_16000009(16000009, "用户非咨询师"),
    //    /* 未获取到银行列表 */
    DB_BANK_FAILED_16000010(16000010, "未获取到银行列表"),
    //    /* 已经审核通过拒绝再次审核 */
    DB_EXAMINE_REFUSE_16000011(16000011, "已经审核通过拒绝再次审核"),
    //    /* 非审核进行中状态无法进行审核 */
    DB_EXAMINE_NOT_ING_16000012(16000012, "非审核进行中状态无法进行审核"),
    //    /* 用户昵称已存在 */
    DB_NICKNAME_FAILED_16000013(16000013, "用户昵称已存在"),
    //    /* 订单未找到 */
    BIZ_ORDER_NOT_FOUND_16001000(16001000, "订单未找到"),
    //    /* 分页查询订单失败 */
    PAGE_QUERY_ORDER_FAILED_16001001(16001001, "分页查询订单失败"),
    //    /* 用户未找到 */
    USER_NOT_FOUND_16002001(16002001, "用户未找到"),
    //    /* 用户错误 */
    USER_ERROR_16002002(16002002, "用户错误"),
    //    /* 用户已注册 */
    USER_REGISTERED_16002003(16002003, "用户已注册"),
    //    /* 用户未注册 */
    USER_NOT_REGISTER_16002004(16002004, "用户未注册"),
    //    /* 更新特权状态失败 */
    UPTATE_PRIVILEGE_STATUS_ERROR_16003004(16003004, "更新特权状态失败"),
    //    /* 商家不存在 */
    MERCHANT_NOT_FOUND_ERROR_16004000(16004000, "商家不存在"),
    //    /* 会员信息未找到 */
    MEMBER_NOT_FOUND_16005000(16005000, "会员信息未找到"),
    //    /* 分页查询商品信息失败 */
    PAGE_QUERY_ITEM_FAILED_16006000(16006000, "分页查询商品信息失败"),
    //    /* 用户角色没有任何权限 */
    USER_ROLE_NON_URL_ERROR_16007000(16007000, "用户角色没有任何权限"),
    //    /* 获取菜单权限失败 */
    PAGE_QUERY_USER_MENU_FAILED_16007001(16007001, "获取菜单权限失败"),
    //    /* 获取URL权限失败 */
    PAGE_QUERY_USER_URL_FAILED_16007002(16007002, "获取URL权限失败"),
    //    /* 没有给用户分配角色 */
    USER_NON_ROLE_ERROR_16007003(16007003, "没有给用户分配角色"),
    //    /* 获取全部菜单权限失败 */
    PAGE_QUERY_ALL_MENU_FAILED_16007004(16007004, "获取全部菜单权限失败"),
    //    /* 未查询到该商户的经营范围 */
    MERCHANT_SCOPE_FAILED_16007005(16007005, "未查询到该商户的经营范围"),
    //    /* 未查询到该商户类目的经营范围 */
    CATEGORY_BUSINESS_SCOPE_FAILED_16007006(16007006, "未查询到该商户类目的经营范围"),
    //    /* 未查询到该商户类目需要的资质 */
    CATEGORY_QUALIFICATION_FAILED_16007007(16007007, "未查询到该商户类目需要的资质"),
    //    /* 未查询到需要的资质 */
    QUALIFICATION_FAILED_16007008(16007008, "未查询到需要的资质"),
    //    /* 未查询到经营范围 */
    BUSINESS_SCOPE_FAILED_16007009(16007009, "未查询到经营范围"),
    //    /* 未查询到该商户的资质 */
    MERCHANT_QUALIFICATION_FAILED_16007010(16007010, "未查询到该商户的资质"),
    //    /* 未查询到该商户的身份 */
    BUSINESS_CATEGORY_FAILED_16007011(16007011, "未查询到该商户的身份"),
    //    /* 保存入驻信息失败 */
    SAVE_MERCHANT_FAILED_16007012(16007012, "保存入驻信息失败"),
    //    /* 店铺名称已存在 */
    MERCHANT_NAME_EXIST_16007013(16007013, "店铺名称已存在"),
    //    /* 商家经营类目不存在 */
    SCOPE_ITEM_CATEGORY_NOT_FOUND_ERROR_16008001(16008001, "商家经营类目不存在"),
    //    /* 草稿名称已存在 */
    DRAFTNAME_EXISTS_FAILED_16009001(16009001, "草稿名称已存在"),
    //    /* 写入数据库错误 */
    WRITE_DB_FAILED_20000001(20000001, "写入数据库错误"),
    //    /* 数据库读取操作失败 */
    READ_DB_FAILED_20000002(20000002, "数据库读取操作失败"),
    //    /* 系统错误 */
    SYSTEM_EXCEPTION_CODE_20000003(20000003, "系统错误"),
    //    /* 风控 */
    RISK_CODE_20000004(20000004, "风控"),
    //    /* 参数错误 */
    CONTENT_CAN_NOT_BE_NULL_20000005(20000005, "参数错误"),
    //    /* 活动未找到 */
    ACTIVTIY_NOT_FOUND_20000006(20000006, "活动未找到"),
    //    /* 俱乐部未找到 */
    CLUB_NOT_FOUND_20000007(20000007, "俱乐部未找到"),
    //    /* 俱乐部成员未找到 */
    CLUB_MEMBER_NOT_FOUND_20000008(20000008, "俱乐部成员未找到"),
    //    /* 没有权限 */
    PERMISSION_DENY_20000009(20000009, "没有权限"),
    //    /* 话题未找到 */
    SUBJECT_NOT_FOUND_20000011(20000011, "话题未找到"),
    //    /* 游记未找到 */
    TRAVELSPECIAL_NOT_FOUND_20000012(20000012, "游记未找到"),
    //    /* 参数错误 */
    PRAM_ERROR_24005000(24005000, "参数错误"),
    //    /* 系统错误 */
    SYSTEM_ERROR_24005001(24005001, "系统错误"),
    //    /* 上架失败 */
    ON_SALE_ERROR_24005002(24005002, "上架失败"),
    //    /* 下架失败 */
    OFF_SALE_ERROR_24005003(24005003, "下架失败"),
    //    /* 删除失败 */
    DELETE_ERROR_24005004(24005004, "删除失败"),
    //    /* 上架成功 */
    ON_SALE_SUCCESS_24005005(24005005, "上架成功"),
    //    /* 下架成功 */
    OFF_SALE_SUCCESS_24005006(24005006, "下架成功"),
    //    /* 删除成功 */
    DELETE_SUCCESS_24005007(24005007, "删除成功"),
    //    /* 发布失败 */
    ADD_ITEM_ERROR_24005008(24005008, "发布失败"),
    //    /* 更新失败 */
    UPDATE_ITEM_ERROR_24005009(24005009, "更新失败"),
    //    /* 获取该店铺信息失败 */
    QUERY_MERCHANT_ERROR_24005010(24005010, "获取该店铺信息失败"),
    //    /* 获取该店铺资质信息失败 */
    QUERY_MERCHANT_QUALIFICATION_ERROR_24005011(24005011, "获取该店铺资质信息失败"),
    //    /* 获取属性失败 */
    QUERY_PROPERTY_ERROR_24005012(24005012, "获取属性失败"),
    //    /* 参数错误 */
    PRAM_ERROR_25000000(25000000, "参数错误"),
    //    /* 系统错误 */
    SYSTEM_ERROR_25000001(25000001, "系统错误"),
    //    /* 第三方应用配置未找到 */
    THIRD_APP_NOT_FOUND_25001000(25001000, "第三方应用配置未找到"),
    //    /* 第三方应用配置错误 */
    THIRD_APP_INFO_ERROR_25001001(25001001, "第三方应用配置错误"),
    //    /* 参数错误 */
    PRAM_ERROR_26000000(26000000, "参数错误"),
    //    /* 系统错误 */
    SYSTEM_ERROR_26000001(26000001, "系统错误"),
    //    /* mq调用错误 */
    MQ_SEND_ERROR_26000002(26000002, "mq调用错误"),
    //    /* dubbo接口失败 */
    DUBBO_ERROR_26000004(26000004, "dubbo接口失败"),
    //    /* 用户信息错误,用户不存在 */
    USER_USER_INFO_ERROR_26000005(26000005, "用户信息错误,用户不存在"),
    //    /* 签到失败 */
    SIGN_IN_FALIURE_26000102(26000102, "签到失败"),
    //    /* 获取签到明细失败 */
    QUERY_SIGN_IN_FALIURE_26000103(26000103, "获取签到明细失败"),
    //    /* 邀请好友参数错误 */
    USER_INVITE_ERROR_26001001(26001001, "邀请好友参数错误"),
    //    /* 邀请好友列表手机参数为空 */
    USER_INVITE_PHONE_ERROR_26001002(26001002, "邀请好友列表手机参数为空"),
    //    /* 已发送邀请短信 */
    USER_INVITE_SENDSMS_ERROR_26001003(26001003, "已发送邀请短信"),
    //    /* 短信提醒异常 */
    USER_INVITE_SENDSMS_EXCETPION_26001004(26001004, "短信提醒异常"),
    //    /* 每天最多可提醒5个好友 */
    USER_INVITE_SENDSMS_MAX_ERROR_26001005(26001005, "每天最多可提醒5个好友"),
    //    /* 调用短信接口失败 */
    USER_INVITE_SENDSMS_CALL_ERROR_26001006(26001006, "调用短信接口失败"),
    //    /* 添加邀请好友信息失败 */
    USER_INVITE_USER_APP_ERROR_26001008(26001008, "添加邀请好友信息失败"),
    //    /* 数据错误 */
    DATA_ERROR_26002001(26002001, "数据错误"),
    //    /* 排行榜远程调用错误 */
    FOLLOW_RPC_ERROR_26002002(26002002, "排行榜远程调用错误"),
    //    /* 用户不存在 */
    USER_INFO_ERROR_26003001(26003001, "用户不存在"),
    //    /* 昨日分数已领取 */
    YESTERDAY_ERROR_26004001(26004001, "昨日分数已领取"),
    //    /* 起步基金已领取 */
    START_UP_ERROR_26004002(26004002, "起步基金已领取"),
    //    /* 昨日无积分可以领取 */
    NOPOINT_ERROR_26004003(26004003, "昨日无积分可以领取"),
    //    /* 添加积分RPC调用失败 */
    ADDPOINT_RPC_ERROR_26004004(26004004, "添加积分RPC调用失败"),
    //    /* 添加积分更新数据库状态失败 */
    ADDPOINT_UPDATE_ERROR_26004005(26004005, "添加积分更新数据库状态失败"),
    //    /* 添加关注关系RPC失败 */
    ADDFOLLOW_ERROR_26005001(26005001, "添加关注关系RPC失败"),
    //    /* 执行成功 */
    PARAM_SUCCESS_26999999(26999999, "执行成功"),
    //    /* 写入数据库错误 */
    WRITE_DB_FAILED_29000000(29000000, "写入数据库错误"),
    //    /* 数据库读取操作失败 */
    READ_DB_FAILED_29000001(29000001, "数据库读取操作失败"),
    //    /* 系统错误 */
    SYSTEM_EXCEPTION_CODE_29000002(29000002, "系统错误"),
    //    /* 参数错误 */
    CONTENT_CAN_NOT_BE_NULL_29000003(29000003, "参数错误"),
    //    /* 商品不支持加入购物车 */
    ITEMTYPE_NOT_SUPPORT_29000004(29000004, "商品不支持加入购物车"),
    //    /* 购物车数量超过50 */
    CART_NUM_29000005(29000005, "购物车数量超过50"),
    //    /* 没有权限 */
    PERMISSION_DENY_29000006(29000006, "没有权限"),
    //    /* 未分配返回值 */
    NO_ASSIGN(-2147483648, "未分配返回值");


    companion object {
        fun parseCode(code : Long): YhyCode? {

            return values().find {
                code == it.i
            } ?: return UNKNOWN_ERROR
        }
    }
}

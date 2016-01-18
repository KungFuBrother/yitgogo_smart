package yitgogo.smart.tools;

public interface API {

    /**
     * 检查更新
     */
    String API_UPDATE = "http://updatePhone.yitos.net/android/version_smart.js";
    String API_DOWNLOAD = "http://updatePhone.yitos.net/android/yitgogo_smart.apk";

    String IP_PUBLIC = "http://yitos.net";
    //    String IP_PUBLIC = "http://192.168.8.8:8050";
//    String IP_PUBLIC = "http://192.168.8.8:9001";
//    String IP_PUBLIC = "http://42.96.249.111";
    //    String IP_SUNING = "http://gcapi.suning.com";
    String IP_SUNING = "http://gcapi.suning.com";
//    String IP_SUNING = "http://58.240.86.161";

    /**
     * 获取服务器系统时间
     */
    String API_GET_SYSTEM_TIME = IP_PUBLIC + "/api/agency/mechanismManage/equipment/findSystemTime";

    /**
     * 说明：根据手机号码和面值（为慢充时还须到账时间）查询充值信息 参数： phoneno 手机号码 必须 pervalue 面值 必须
     * 快充可选面值（1、2、5、10、20、30、50、100、300、500） 慢充可选面值（30、50、100）
     * <p/>
     * mctype 慢充到账时间 慢充才传 0.5（半小时到账）、4（4小时到账）、12（12小时到账）、
     * 24（24小时到账）、48（48小时到账）、72（72小时到账） 默认为24
     */
    String API_BIANMIN_PHONE_CHARGE_INFO = IP_PUBLIC
            + "/api/facilitate/recharge/findPhoneInfo";
    /**
     * 说明：生成手机充值订单 参数： phoneno 手机号码 必须 pervalue 面值 必须 mctype 慢充到账时间 慢充才传
     */
    String API_BIANMIN_PHONE_CHARGE = IP_PUBLIC
            + "/api/facilitate/recharge/addOrder/addPhoneOrder";

    /**
     * 说明：根据固话号码，面值，运营商，充值类型查询充值信息 参数： phoneno 固话/宽带号码 必须 格式：021-88888888
     * <p/>
     * pervalue 面值 必须 电信：10、20、30、50、100、300 联通：50、100
     * <p/>
     * teltype 运营商 必须 1、电信 2、联通（默认选中1）
     * <p/>
     * chargeType 充值类型 必须 1：固话；2：宽带（默认选中1）
     */
    String API_BIANMIN_TELEPHONE_CHARGE_INFO = IP_PUBLIC
            + "/api/facilitate/recharge/fixtelquery";
    /**
     * 说明：生成固话/宽带充值订单 参数： phoneno 固话/宽带号码 必须 pervalue 面值 必须 teltype 运营商编号 必须
     * chargeType 充值类型编号 必须 memberAccount 会员账号 登录才传 返回结果：map orderNumber 订单编号
     */
    String API_BIANMIN_TELEPHONE_CHARGE = IP_PUBLIC
            + "/api/facilitate/recharge/addOrder/addFixtelOrder";

    /**
     * 说明：根据手机号码和面值（为慢充时还须到账时间）查询充值信息 参数： game_userid 加油卡卡号 必须 chargeType 加油卡类型
     * 必须 1:中石化、2:中石油（默认选中1） 返回结果：充值卡号对象 username 卡主姓名
     */
    String API_BIANMIN_OIL_CARD_INFO = IP_PUBLIC
            + "/api/facilitate/recharge/queryCardInfo";

    String API_BIANMIN_TRAFFIC_PROVINCE = IP_PUBLIC
            + "/api/facilitate/recharge/trafficFines/getProvinceList";

    String API_BIANMIN_TRAFFIC_CITY = IP_PUBLIC
            + "/api/facilitate/recharge/trafficFines/getCityList";

    String API_BIANMIN_TRAFFIC_CARTYPE = IP_PUBLIC
            + "/api/facilitate/recharge/trafficFines/getVehicleType";

    String API_BIANMIN_TRAFFIC_HISTORY = IP_PUBLIC
            + "/api/facilitate/recharge/trafficFines/queryViolationInfo";

    String API_BIANMIN_GAME_LIST = IP_PUBLIC
            + "/api/facilitate/recharge/findAllGameNames";
    String API_BIANMIN_GAME_AREA = IP_PUBLIC
            + "/api/facilitate/recharge/findAllAreas";
    String API_BIANMIN_GAME_SERVER = IP_PUBLIC
            + "/api/facilitate/recharge/findAllServers";
    String API_BIANMIN_GAME_QQ_CHARGE = IP_PUBLIC
            + "/api/facilitate/recharge/addOrder/addGameOrder";
    String API_BIANMIN_QQ_INFO = IP_PUBLIC
            + "/api/facilitate/recharge/findCoinInfo";

    String API_BIANMIN_CARD_INFO = IP_PUBLIC
            + "/api/facilitate/recharge/querycardinfo";

    /**
     * 判断是否已经签到
     */
    String API_USER_SIGN_STATE = IP_PUBLIC
            + "/member/memberManage/Sign/findSign";

    /**
     * 签到送积分
     */
    String API_USER_SIGN = IP_PUBLIC + "/member/memberManage/Sign/addSign";

    /**
     * 获取会员等级
     */
    String API_MEMBER_GRADE = IP_PUBLIC
            + "/api/member/memberManage/member/getGrade";

    /**
     * 获取所有收货地址
     */
    String API_USER_ADDRESS_LIST = IP_PUBLIC
            + "/member/addressManage/MemberAddress/findAddressAll";
    /**
     * 添加收货地址
     */
    String API_USER_ADDRESS_ADD = IP_PUBLIC
            + "/member/addressManage/MemberAddress/addAddress";
    /**
     * 删除收货地址
     */
    String API_USER_ADDRESS_DELETE = IP_PUBLIC
            + "/member/addressManage/MemberAddress/deleteAddress";
    /**
     * 设为默认地址
     */
    String API_USER_ADDRESS_SET_DEAFULT = IP_PUBLIC
            + "/member/addressManage/MemberAddress/setDefaultAddress";
    /**
     * 收货地址详情
     */
    String API_USER_ADDRESS_DETAIL = IP_PUBLIC
            + "/member/addressManage/MemberAddress/getUpdateAddress";
    /**
     * 修改收货地址
     */
    String API_USER_ADDRESS_MODIFY = IP_PUBLIC
            + "/member/addressManage/MemberAddress/updateAddress";

    /**
     * 说明：根据区域id查询下级区域 授权访问：否 参数：aid：区域id（如果不传，则查询所有省份）
     * 返回结果：regionalValue（区域对象）list集合
     */
    String API_STORE_AREA = IP_PUBLIC
            + "/api/agency/mechanismManage/regional/findArea";
    /**
     * 说明：查询区域下面所有加盟商 授权访问：否 参数：areaId，区域ID 返回结果：serviceProviders(机构对象)的list集合
     */
    String API_STORE_LIST = IP_PUBLIC
            + "/api/agency/mechanismManage/mechanism/findStoreByArea";

    /**
     * 广告
     */
    String API_ADS = IP_PUBLIC
            + "/api/product/productManage/advertisement/findAdver";

    /**
     * 查询所有上级区域
     */
    String API_AREA = IP_PUBLIC
            + "/api/agency/mechanismManage/regional/findAllOnRegional";

    /*
     * 消费者手机端查询所有促销分类
     */
    String API_SALE_CLASS = IP_PUBLIC
            + "/api/product/promotionManage/promotionTheme/findPromotionclass";
    /**
     * 获取全部的品牌信息
     * <p/>
     * 参数：无
     * <p/>
     * 返回结果：{brandLogo:LoGo图片,brandName:品牌名称,brandId:品牌ID}
     */
    String API_HOME_BRAND = IP_PUBLIC
            + "/api/MobileArea/MobileManager/brand/MobileAreaBrandInit";
    /**
     * 获取全部的推荐分类信息
     * <p/>
     * 参数：无
     * <p/>
     * 返回结果：{classLogo:LoGo图片,className:分类名称,ClassId:品牌ID}
     */
    String API_HOME_CLASS = IP_PUBLIC
            + "/api/MobileArea/MobileManager/class/MobileAreaClassInit";

    /**
     * 查询商品列表
     */
    String API_PRODUCT_LIST = IP_PUBLIC
            + "/api/product/productManage/product/findByItemOpenProduct";
    /**
     * 查询商品列表
     */
    String API_PRODUCT_DETAIL = IP_PUBLIC
            + "/api/product/productManage/product/findByOpenProductId";
    /**
     * 添加商品浏览记录
     */
    String API_PRODUCT_BROWSE_HISTORY = IP_PUBLIC
            + "/api/product/productManage/browse/addBrowse";
    /**
     * 查询所有大分类
     */
    String API_PRODUCT_CLASS_MAIN = IP_PUBLIC
            + "/api/product/productManage/classValue/findAllClass";
    /**
     * 根据上级分类ID查询对应的分类
     */
    String API_PRODUCT_CLASS_MID = IP_PUBLIC
            + "/api/product/productManage/classValue/findBySuperiorClassId";

    /**
     * 根据最新分类id查询分类属性
     */
    String API_PRODUCT_CLASS_ATTR = IP_PUBLIC
            + "/api/product/productManage/classValue/findByMinClassId";

    /**
     * 商品热搜
     */
    String API_PRODUCT_SEARCH_HOT = IP_PUBLIC
            + "/api/product/productManage/topsearch/findTopSearchQueries";

    /**
     * 查询商品列表价格
     */
    String API_PRICE_LIST = IP_PUBLIC
            + "/api/product/productManage/product/findByOpenPorudctLXPrice";
    /**
     * 查询商品属性价格
     */
    String API_PRICE_BY_ATTR = IP_PUBLIC
            + "/api/product/productManage/product/findOpenSelectedAttPro";

    /**
     * 查询商品属性价格
     */
    String API_PRICE_CAR = IP_PUBLIC
            + "/api/product/productManage/product/findSelectedAttPriceInTF";

    /**
     * 登录
     */
    String API_USER_LOGIN = IP_PUBLIC
            + "/api/member/memberManage/member/loginMember";
    /**
     * 注册
     *
     * @Parameters String phone 电话号码 String password 密码 String smsCode 验证码
     * String equipMachine 机器码 String spNo 机构编号 refereeCode
     * 推荐我的人的推荐码
     */
    String API_USER_REGISTER = IP_PUBLIC
            + "/api/member/memberManage/member/findOrAddMember";
    /**
     * 注册时获取短信
     */
    String API_USER_REGISTER_SMSCODE = IP_PUBLIC
            + "/api/member/memberManage/member/getSmsCode";
    /**
     * 找回密码
     */
    String API_USER_FIND_PASSWORD = IP_PUBLIC
            + "/api/member/memberManage/member/getPassword";

    /**
     * 找回密码时获取短信验证码
     */
    String API_USER_FIND_PASSWORD_SMSCODE = IP_PUBLIC
            + "/api/member/memberManage/member/getSmsCodes";

    /**
     * 获取用户信息
     */
    String API_USER_INFO_GET = IP_PUBLIC
            + "/api/member/memberManage/member/findMemberByPhone";
    /**
     * 修改用户资料
     */
    String API_USER_INFO_UPDATE = IP_PUBLIC
            + "/member/memberManage/member/updateMember";

    /**
     * 修改密码参数：useraccount 账号，oldpassword旧密码，newpassword新密码
     */
    String API_USER_MODIFY_SECRET = IP_PUBLIC
            + "/member/memberManage/member/updateMemberPassword";
    /**
     * 修改手机号 参数：account账号，newphone新手机号
     */
    String API_USER_MODIFY_PHONE = IP_PUBLIC
            + "/member/memberManage/member/updateMemberPhone";
    /**
     * 修改手机号获取验证码
     */
    String API_USER_MODIFY_PHONE_SMSCODE = IP_PUBLIC
            + "/member/memberManage/member/VerificationCode";
    /**
     * 修改身份证号 参数：account账号，idcard身份证号
     */
    String API_USER_MODIFY_IDCARD = IP_PUBLIC
            + "/member/memberManage/member/upadteMemberIdcard";

    /**
     * 查询用户积分余额
     */
    String API_USER_JIFEN = IP_PUBLIC
            + "/member/bonusManage/MemberBonus/findByAccount";

    // 查询积分详情
    String API_USER_JIFEN_DETAIL = IP_PUBLIC
            + "/member/bonusDetailManage/bonusDetail/MemberBonusDetail/findByItem";
    /**
     * 上传用户位置
     */
    String API_USER_UPDATE_LOCATION = IP_PUBLIC
            + "/member/locationManage/MemberLocation/addMemberLocation";
    /**
     * 获取我推荐的会员列表
     */
    String API_USER_RECOMMEND_LIST = IP_PUBLIC
            + "/member/memberManage/member/findRecPersons";
    /**
     * 统计所推荐会员的数据
     */
    String API_USER_RECOMMEND_STATISTICS = IP_PUBLIC
            + "/member/memberManage/member/recommendData";

    /**
     * 下单
     */
    String API_ORDER_ADD = IP_PUBLIC
            + "/api/order/unified/orderManage/addYiShopOrOCOrder";

    /**
     * 下单至运营中心
     */
    String API_ORDER_ADD_CENTER = IP_PUBLIC
            + "/api/order/franchiseeOrder/orderManage/addOrder";

    /**
     * 下单至易店
     */
    String API_ORDER_ADD_YISTORE = IP_PUBLIC
            + "/api/order/yiShopOrder/orderYS/addOrderYS";

    /**
     * 查询订单列表
     */
    String API_ORDER_LIST = IP_PUBLIC
            + "/api/order/franchiseeOrder/orderManage/findByOrderUserNumber";
    /**
     * 查询订单详情
     */
    String API_ORDER_DETAIL = IP_PUBLIC
            + "/api/order/franchiseeOrder/orderManage/findByOrderNumber";
    /**
     * 查询订单详情
     */
    String API_ORDER_WULIU = IP_PUBLIC
            + "/api/mobilePhone/deliveryRecord/logisticsManage/findDeliveryRecords";

    /**
     * 查询店铺街的店铺列表及信息 参数：加盟店编号（storeId）
     */
    String API_LOCAL_STORE_LIST = IP_PUBLIC
            + "/api/agency/mechanismManage/mechanism/findShopByStore";

    /**
     * 本地产品列表
     */
    String API_LOCAL_BUSINESS_GOODS = IP_PUBLIC
            + "/api/localEBusiness/retail/productManager/queryRPManager";

    /**
     * 本地产品详情
     * <p/>
     * String retailProductManagerID
     */
    String API_LOCAL_BUSINESS_GOODS_DETAIL = IP_PUBLIC
            + "/api/localEBusiness/retail/productManager/viewRPManager";

    /**
     * 本地产品大分类
     * <p/>
     * serviceProviderID
     */
    String API_LOCAL_BUSINESS_GOODS_CLASS_PRIMARY = IP_PUBLIC
            + "/api/localEBusiness/retail/productClassType/queryRPBigClassType";
    /**
     * 本地产品小分类
     * <p/>
     * productTypeValueID
     */
    String API_LOCAL_BUSINESS_GOODS_CLASS_SECOND = IP_PUBLIC
            + "/api/localEBusiness/retail/productClassType/queryRPNextClassType";
    /**
     * 本地产品订单添加
     *
     * @test http://192.168.8.80:8081/api/localEBusiness/retail/orderManager/
     * addRetailOrderMan
     * ?customerName=累夏鸥&customerPhone=13032889558&deliveryAddress
     * =四川省成都市金牛区解放路二段六号
     * &retailOrderPrice=300&paymentType=1&serviceProvidID
     * =1068&memberAccount
     * =13032889558&data=[{"retailProductManagerID":"1",
     * "shopNum":"3","productPrice" :"300"}]
     */
    String API_LOCAL_BUSINESS_GOODS_ORDER_ADD = IP_PUBLIC
            + "/api/localEBusiness/retail/orderManager/addRetailOrderMan";
    /**
     * 本地产品订单查询
     */
    String API_LOCAL_BUSINESS_GOODS_ORDER_LIST = IP_PUBLIC
            + "/api/localEBusiness/retail/orderManager/queryRetailOrderMan";
    /**
     * 本地服务分类
     */
    String API_LOCAL_BUSINESS_SERVICE_CLASS = IP_PUBLIC
            + "/api/localService/productManage/localClassValue/findAllMyLocalClassValue";

    /**
     * 本地服务列表
     */
    String API_LOCAL_BUSINESS_SERVICE = IP_PUBLIC
            + "/api/localService/productManage/localProduct/findByItemOpenLocalProduct";
    /**
     * 农副产品
     */
    String API_LOCAL_BUSINESS_NONGFU = IP_PUBLIC
            + "/api/localService/productManage/reverseRelease/findByItemOpenRRlocalProduct";

    /**
     * 查询爱新鲜产品
     */
    String API_LOCAL_BUSINESS_SERVICE_FRESH = IP_PUBLIC
            + "/api/localService/productManage/localProduct/findByAixinxianProduct";

    /**
     * 本地服务详情
     */
    String API_LOCAL_BUSINESS_SERVICE_DETAIL = IP_PUBLIC
            + "/api/localService/productManage/localProduct/findByIdOpenLocalProduct";
    /**
     * 本地促销-特价
     * <p/>
     * 传递参数: jqm 机器码
     */
    String API_LOCAL_SALE_TEJIA = IP_PUBLIC
            + "/api/product/promotionManage/LocalPromotion/findShopPromotionJQM";
    /**
     * 本地促销-特价详情
     * <p/>
     * 传递参数 : id 促销ID
     */
    String API_LOCAL_SALE_TEJIA_DETAIL = IP_PUBLIC
            + "/api/product/promotionManage/LocalPromotion/findShopPromotionId";
    /**
     * 本地促销-秒杀
     * <p/>
     * 传递参数: jqm 机器码
     */
    String API_LOCAL_SALE_MIAOSHA = IP_PUBLIC
            + "/api/product/promotionManage/LocalPromotion/findSpikePromotionJQM";
    /**
     * 本地促销-秒杀详情
     * <p/>
     * 传递参数 : id 促销ID
     */
    String API_LOCAL_SALE_MIAOSHA_DETAIL = IP_PUBLIC
            + "/api/product/promotionManage/promotion/findspikePromotionDetais";
    /**
     * 本地服务订单添加
     *
     * @LocalTest http://192.168.8.80:8081/api/localService/orderManage/localOrder
     * / addLocalOrderPos
     * ?customerName=累夏鸥&customerPhone=13032889558&deliveryAddress
     * =四川省成都市金牛区解放路二段六号
     * &orderPrice=300&providerId=1068&memberNumber=13032889558
     * &deliveryType=送货上门&orderType=服务&productId=1&productNum=1
     */
    String API_LOCAL_BUSINESS_SERVICE_ORDER_ADD = IP_PUBLIC
            + "/api/localService/orderManage/localOrder/addLocalOrderPos";
    /**
     * 本地服务订单列表
     *
     * @LocalTest http://192.168.8.80:8081/api/localService/orderManage/localOrder
     * /findByItemMemberLocalOrder?memberNumber=13032889558&pageNo=1&
     * pageSize=2
     */
    String API_LOCAL_BUSINESS_SERVICE_ORDER_LIST = IP_PUBLIC
            + "/api/localService/orderManage/localOrder/findByItemMemberLocalOrder";
    /**
     * 本地服务订单详情
     */
    String API_LOCAL_BUSINESS_SERVICE_ORDER_DETAIL = IP_PUBLIC
            + "/api/localService/orderManage/localOrder/findByLocalOrderId";
    /**
     * 查询限时促销商品详情
     */
    String API_SALE_TIME_DETAIL = IP_PUBLIC
            + "/api/product/promotionManage/promotion/findPromotionDetails";
    /**
     * 查询限时促销商品列表
     */
    String API_SALE_TIME_LIST = IP_PUBLIC
            + "/api/product/promotionManage/promotion/findAllpromotion";
    /**
     * 查询限时促销商品列表
     */
    String API_SALE_PRICE = IP_PUBLIC
            + "/api/product/promotionManage/promotion/findPromotionPrice";

    /**
     * 查询秒杀商品
     * <p/>
     * strno
     */
    String API_SALE_MIAOSHA = IP_PUBLIC
            + "/api/product/promotionManage/seckillActivity/findSeckillActivity";

    /**
     * 查询秒杀商品详情
     */
    String API_SALE_MIAOSHA_DETAIL = IP_PUBLIC
            + "/api/product/promotionManage/promotion/findSeckillPromotionDetais";

    /**
     * 查询特价商品
     * <p/>
     * strno
     */
    String API_SALE_TEJIA = IP_PUBLIC
            + "/api/product/promotionManage/promotionTheme/findSalePromotion";
    /**
     * 查询特价促销商品详情
     */
    String API_SALE_TEJIA_DETAIL = IP_PUBLIC
            + "/api/product/promotionManage/promotion/findSalePromotionDetails";
    /**
     * 运费计算
     */
    String API_PRODUCT_FREIGHT = IP_PUBLIC + "/api/order/orderManage/freightTemplate/calculationOfFreight";

    /**
     * 查询主题活动
     * <p/>
     * strno
     */
    String API_SALE_ACTIVITY = IP_PUBLIC
            + "/api/product/promotionManage/Theme/findAllThmem";

    /**
     * 查询积分商城列表
     */
    String API_SCORE_PRODUCT_LIST = IP_PUBLIC
            + "/api/product/integralMall/IntegralMallAction/findIntegralMallJQM";
    /**
     * 查询积分商品详情
     */
    String API_SCORE_PRODUCT_DETAIL = IP_PUBLIC
            + "/api/product/integralMall/IntegralMallAction/findIntegrallMallIds";

    /**
     * 支付宝支付结果异步返回给服务器地址
     */
    String API_ALIPAY_NOTIFY = IP_PUBLIC
            + "/api/settlement/pay/addPayMentData";

    /**
     * 付款后修改订单状态
     */
    String API_ORDER_STATE_UPDATE = IP_PUBLIC
            + "/api/order/franchiseeOrder/orderManage/fAmessages";

    /**
     * 说明：更新设备状态 授权访问：否 参数;machine，机器码 必传,status:状态（ONLINE或者OFFLINE）
     * 必传，version，版本号 返回结果：无 注意：无
     */
    String API_MACHINE_AREA = IP_PUBLIC
            + "/api/agency/mechanismManage/regional/areas";

    /**
     * 说明：绑定加盟商设备 授权访问：否 参数：activetion（激活码），machine（机器码） 返回结果：无 注意：无
     */
    String API_MACHINE_BIND = IP_PUBLIC
            + "/api/agency/mechanismManage/equipment/updateMachine";

    /**
     * 说明：根据机器码查询设备状态 授权访问：否 参数：machine，机器码 返回结果：无
     * 注意：判断状态根据抛出的异常来判断，异常有noexit(表示没有查到该设备)，stop（表示设备停用）
     */
    String API_MACHINE_STATE = IP_PUBLIC
            + "/api/agency/mechanismManage/equipment/findByMachine";

    /**
     * 说明：更新设备状态 授权访问：否 参数;machine，机器码 必传,status:状态（ONLINE或者OFFLINE）
     * 必传，version，版本号 返回结果：无 注意：无
     */
    String API_MACHINE_UPDATE_STATE = IP_PUBLIC
            + "/api/agency/mechanismManage/equipment/updateStatus";

    String API_LOCAL_SALE_MIAOSHA_COUNT = IP_PUBLIC
            + "/api/order/otherOrder/LocalSpikePurchaseApi/queryRetailOrder";
    /**
     * 传机构编号：no
     */
    String API_STORE_SEND_FEE = IP_PUBLIC
            + "/api/agency/mechanismManage/ConfigurationFile/findSpPostage";

    //苏宁
    //分类
    String API_SUNING_PRODUCT_CALSSES = IP_PUBLIC + "/api/order/cloudMallOrder/CloudMallOrderSerachApi/findBySNClass";
    //列表
    String API_SUNING_PRODUCT_LIST = IP_PUBLIC + "/api/order/cloudMallOrder/CloudMallOrderSerachApi/findSnProductByPage";

    String API_SUNING_PRODUCT_DETAIL = IP_SUNING + "/esbadapter/GEProductMgmt_GCMS/getProductDetail";

    String API_SUNING_PRODUCT_IMAGES = IP_SUNING + "/esbadapter/GEProductMgmt_GCMS/getProductImage";

    String API_SUNING_PRODUCT_STOCK = IP_SUNING + "/esbadapter/GEProductMgmt_GCMS/getProductInventory";

    String API_SUNING_PRODUCT_PRICE = IP_SUNING + "/esbadapter/GEProductMgmt_GCMS/getProductSNPrice";

    String API_SUNING_AREA_PROVINCE = IP_SUNING + "/esbadapter/GEBaseInfoMgmt_GCMS/getProvinceInfo";

    String API_SUNING_AREA_CITY = IP_SUNING + "/esbadapter/GEBaseInfoMgmt_GCMS/getCityInfo";

    String API_SUNING_AREA_DISTRICT = IP_SUNING + "/esbadapter/GEBaseInfoMgmt_GCMS/getDistrictInfo";

    String API_SUNING_AREA_TOWN = IP_SUNING + "/esbadapter/GEBaseInfoMgmt_GCMS/getTownInfo";

    String API_SUNING_ORDER_WULIU = IP_SUNING + "/esbadapter/GEOrderBaseMgmt_GCMS/getOrderLogisticsStatus";

    String API_SUNING_SIGNATURE = IP_PUBLIC
            + "/api/order/cloudMallOrder/CloudMallAction/queryAccessToken";

    String API_SUNING_ORDER_ADD = IP_PUBLIC
            + "/api/order/cloudMallOrder/CloudMallAction/CreatSuNingOrder";

    String API_SUNING_ORDER_PAY = IP_PUBLIC
            + "/api/order/cloudMallOrder/CloudMallAction/updateCloudmallOrderYFK";

    String API_SUNING_ORDER_LIST = IP_PUBLIC
            + "/api/order/cloudMallOrder/CloudMallAction/findMenberOrder";

    String API_SUNING_ORDER_RECEIVE_RETURN = IP_PUBLIC
            + "/api/order/cloudMallOrder/CloudMallAction/updateOrderComplete";

}

package config;

/**
 * Created by 孙明明 on 2017/5/16.
 */

public interface NetConfig {

    //搜索记录
    public static final String searchLogUrl = "http://www.gangjianwang.com/mobile/index.php?act=index&op=search_key_list";
    //城市列表
    public static final String cityUrl = "http://www.gangjianwang.com/shop/index.php?act=index&op=getWapAreaCities";
    //首页碎片
    public static final String homeUrl = "http://www.gangjianwang.com/mobile/index.php?act=index";
    //钢建特卖会
    public static final String gjSpecialSaleUrl = "http://www.gangjianwang.com/mobile/index.php?act=index&op=special&special_id=1";
    //店铺列表
    public static final String shopListUrl = "http://www.gangjianwang.com/mobile/index.php?act=store&op=store_list&page=10&curpage=1";
    //承揽工程
    public static final String contractprojectUrl = "http://www.gangjianwang.com/mobile/index.php?act=store&op=store_contract_list&page=10&curpage=";
    //承揽工程详情头
    public static final String contractprojectDetailHeadUrl = "http://www.gangjianwang.com/mobile/index.php?act=goods&op=goods_detail&goods_id=";
    //承揽工程详情尾
    public static final String contractprojectDetailFootUrl = "&key=";
    //省列表
    public static final String provinceListUrl = "http://www.gangjianwang.com/mobile/index.php?act=index&op=search_adv";
    //分类左
    public static final String classifyLeftUrl = "http://www.gangjianwang.com/mobile/index.php?act=goods_class";
    //分类右
    public static final String classifyRightUrl = "http://www.gangjianwang.com/mobile/index.php?act=goods_class&op=get_child_all&gc_id=";
    //底购商城
    public static final String digouStoreUrl = "http://eshop.gangjianwang.com/mobile/";
    //采购单头
    public static final String purchaseHeadUrl = "http://www.gangjianwang.com/mobile/index.php?act=member_purchase&op=purchase_list&page=10&curpage=1&key=";
    //采购单尾
    public static final String purchaseFootUrl = "&member_id=5&purchase_key=";
    //采购单详情
    public static final String purchaseDetailUrl = "http://www.gangjianwang.com/mobile/index.php?act=member_purchase&op=purchase_info&key=fc54fe6ad977b6f8c16f05d3aad5edbc&purchase_id=";
    //采购单所属类目头
    public static final String ofKindHeadUrl = "http://www.gangjianwang.com/mobile/index.php?act=goods_class&op=index&callback=jsonp1&gc_id=";
    //采购单所属类目尾
    public static final String ofKindFootUrl = "&_=1498116253258";
    //指定供货商头
    public static final String specialSupplyHeadUrl = "http://www.gangjianwang.com/shop/index.php?act=index&op=json_seller&callback=jsonp4&gc_id=";
    //指定供货商尾
    public static final String specialSupplyFootUrl = "&_=1498182762846";
    //收货地头
    public static final String receiveAreaUrl = "http://www.gangjianwang.com/mobile/index.php?act=area&op=area_list&area_id=";
    //注册
    public static final String registerUrl = "http://www.gangjianwang.com/mobile/index.php?act=login&op=register";
    //登录
    public static final String loginUrl = "http://www.gangjianwang.com/mobile/index.php?act=login";
    //登录后
    public static final String loginAfterUrl = "http://www.gangjianwang.com/mobile/index.php?act=member_index";
    //用户协议
    public static final String agreeMentUrl = "http://www.gangjianwang.com/wap/tmpl/member/document.html";
    //商品收藏头
    public static final String goodsCollectHeadUrl = "http://www.gangjianwang.com/mobile/index.php?act=member_favorites&op=favorites_list&key=";
    //商品收藏尾
    public static final String goodsCollectFootUrl = "&curpage=1&page=10";
    //商品删除(post) key fav_id
    public static final String goodsDeleteUrl = "http://www.gangjianwang.com/mobile/index.php?act=member_favorites&op=favorites_del";
    //店铺收藏头
    public static final String storeCollectHeadUrl = "http://www.gangjianwang.com/mobile/index.php?act=member_favorites_store&op=favorites_list&key=";
    //店铺收藏尾
    public static final String storeCollectFootUrl = "&curpage=1&page=10";
    //店铺删除(post) key store_id
    public static final String storeDeleteUrl = "http://www.gangjianwang.com/mobile/index.php?act=member_favorites_store&op=favorites_del";
    //足迹头
    public static final String footHeadUrl = "http://www.gangjianwang.com/mobile/index.php?act=member_goodsbrowse&op=browse_list&key=";
    //足迹尾页头
    public static final String footFootPageHeadUrl = "&curpage=";
    //足迹尾页尾
    public static final String footFootPageFootUrl = "&page=10";
    //我的财产头
    public static final String propertyHeadUrl = "http://www.gangjianwang.com/mobile/index.php?act=member_index&op=my_asset&key=";
    //积分头(总)
    public static final String integrateNumHeadUrl = "http://www.gangjianwang.com/mobile/index.php?act=member_index&op=my_asset&key=";
    //积分尾(总)
    public static final String integrateNumFootUrl = "&fields=point";
    //积分头
    public static final String integrateHeadUrl = "http://www.gangjianwang.com/mobile/index.php?act=member_points&op=pointslog&key=";
    //积分尾页头
    public static final String integrateFootPageHeadUrl = "&curpage=";
    //积分尾页尾
    public static final String integrateFootPageFootUrl = "&page=10";
    //地址管理(HomePost) 字段key
    public static final String addressManageUrl = "http://www.gangjianwang.com/mobile/index.php?act=member_address&op=address_list";
    //地址新增 key,true_name,mob_phone,address,city_id,area_id,area_info,is_default
    public static final String addressAddUrl = "http://www.gangjianwang.com/mobile/index.php?act=member_address&op=address_add";
    //地址管理删除(HomePost) 字段 address_id key
    public static final String addressManageDeleteUrl = "http://www.gangjianwang.com/mobile/index.php?act=member_address&op=address_del";
    //实物订单全部订单(post)头 key state_type order_key
    public static final String realallorderUrl = "http://www.gangjianwang.com/mobile/index.php?act=member_order&op=order_list&page=10&curpage=";
    //实物订单移除 order_id key
    public static final String realorderRemoveUrl = "http://www.gangjianwang.com/mobile/index.php?act=member_order&op=order_delete";
    //实物订单取消 order_id key
    public static final String realorderCancelUrl = "http://www.gangjianwang.com/mobile/index.php?act=member_order&op=order_cancel";
    //虚拟订单 key state_type order_key
    public static final String virtualorderUrl = "http://www.gangjianwang.com/mobile/index.php?act=member_vr_order&op=order_list&page=10&curpage=1";
    //浏览记录 删除头 get
    public static final String footDelHeadUrl = "http://www.gangjianwang.com/mobile/index.php?act=member_goodsbrowse&op=browse_list&key=";
    //浏览记录 删除尾 get
    public static final String footDelFootUrl = "&curpage=1&page=10";
    //商品详情分类 store_id goods_id
    public static final String goodsDetailClassifyUrl = "http://www.gangjianwang.com/mobile/index.php?act=store&op=store_goods_second_class";
    //购物车 key
    public static final String shopCarUrl = "http://www.gangjianwang.com/mobile/index.php?act=member_cart&op=cart_list";
    //购物车删除 key cart_id
    public static final String shopCarDeleteUrl = "http://www.gangjianwang.com/mobile/index.php?act=member_cart&op=cart_del";
    //确认订单 key cart_id 123|1,124|1,125|1 quotation_id ifcart address_id
    public static final String sureOrderUrl = "http://www.gangjianwang.com/mobile/index.php?act=member_buy&op=buy_step1";
    //店铺详情 key,store_id
    public static final String storeDetailUrl = "http://www.gangjianwang.com/mobile/index.php?act=store&op=store_info";
    //店铺详情 排行 store_id,收藏：ordertype = collectdesc,销量：ordertype = salenumdesc,num = 3
    public static final String storeDetailCollectUrl = "http://www.gangjianwang.com/mobile/index.php?act=store&op=store_goods_rank";
    //店铺详情 全部商品头
    public static final String storeDetailAllGoodsHeadUrl = "http://www.gangjianwang.com/mobile/index.php?act=store&op=store_goods&store_id=";
    //店铺详情 全部商品尾页头
    public static final String storeDetailAllGoodsFootPageHeadUrl = "&curpage=";
    //店铺详情 全部商品尾页尾
    public static final String storeDetailAllGoodsFootPageFootUrl = "&page=10";
    //店铺详情分类跳转详情 http://www.gangjianwang.com/mobile/index.php?act=goods&op=goods_list&gc_id=4&store_id=6&page=10&curpage=1&gc_id=4
    public static final String classifyDetailUrl1 = "http://www.gangjianwang.com/mobile/index.php?act=goods&op=goods_list&gc_id=";
    public static final String classifyDetailUrl2 = "&store_id=";
    public static final String classifyDetailUrl3 = "&page=10&curpage=";
    public static final String classifyDetailUrl4 = "&gc_id=";
    //店铺介绍 key,store_id
    public static final String storeIntroUrl = "http://www.gangjianwang.com/mobile/index.php?act=store&op=store_intro";
    //店铺介绍收藏 key,store_id
    public static final String storeIntroCollectUrl = "http://www.gangjianwang.com/mobile/index.php?act=member_favorites_store&op=favorites_add";
    //店铺介绍取消收藏 key,store_id
    public static final String storeIntroCollectCancelUrl = "http://www.gangjianwang.com/mobile/index.php?act=member_favorites_store&op=favorites_del";
    //购物车数量 key
    public static final String cartCountUrl = "http://www.gangjianwang.com/mobile/index.php?act=member_cart&op=cart_count";
    //询价单 key
    public static final String inquiryHeadUrl = "http://www.gangjianwang.com/mobile/index.php?act=quotation&op=getQuotationList&page=10&curpage=";
    public static final String inquiryFootHeadUrl = "&state_type=";
    public static final String inquiryFootFootUrl = "&quotation_sn=";
    //询价单 删除 quotation_id,key
    public static final String inquiryDelUrl = "http://www.gangjianwang.com/mobile/index.php?act=quotation&op=quotation_del";
}

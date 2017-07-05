package config;

/**
 * Created by 孙明明 on 2017/5/16.
 */

public interface NetConfig {

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
    //采购单
    public static final String purchaseUrl = "http://www.gangjianwang.com/mobile/index.php?act=member_purchase&op=purchase_list&page=10&curpage=1&key=fc54fe6ad977b6f8c16f05d3aad5edbc&member_id=5&purchase_key=";
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
    //足迹尾
    public static final String footFootUrl = "&curpage=1&page=10";
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
    //地址管理(Post) 字段key
    public static final String addressManageUrl = "http://www.gangjianwang.com/mobile/index.php?act=member_address&op=address_list";
    //地址管理删除(Post) 字段 address_id key
    public static final String addressManageDeleteUrl = "http://www.gangjianwang.com/mobile/index.php?act=member_address&op=address_del";
    //实物订单全部订单(post) key state_type order_key
    public static final String realallorderUrl = "http://www.gangjianwang.com/mobile/index.php?act=member_order&op=order_list&page=10&curpage=1";

}

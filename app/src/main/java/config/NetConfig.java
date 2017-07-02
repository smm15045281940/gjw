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
}

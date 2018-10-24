package com.monamedia.vmt.common.interfaces;

import com.android.volley.Request;

public interface Statics {
    // pc: https://detail.tmall.hk/hk/item.htm?id=42496288663&spm=875.7931836/B.2017041.8.66144265SIyi8R&scm=1007.12144.81309.73133_0&pvid=1a8ffbf4-d31c-4638-82fd-6086fa391cda&utparam={%22x_hestia_source%22:%2273133%22,%22x_object_type%22:%22item%22,%22x_mt%22:8,%22x_src%22:%2273133%22,%22x_pos%22:5,%22x_pvid%22:%221a8ffbf4-d31c-4638-82fd-6086fa391cda%22,%22x_object_id%22:42496288663}&skuId=3220925169190
    // mobile: https://detail.m.tmall.com/item.htm?id=42496288663&toSite=main&skuId=3220925169190

    // pc:https://detail.tmall.hk/hk/item.htm?id=559193157478&spm=875.7931836/B.2017041.4.66144265J3QDYm&scm=1007.12144.81309.73133_0&pvid=b3e18dd1-4367-46f4-95e9-3ab7bad4afe7&utparam={%22x_hestia_source%22:%2273133%22,%22x_object_type%22:%22item%22,%22x_mt%22:8,%22x_src%22:%2273133%22,%22x_pos%22:1,%22x_pvid%22:%22b3e18dd1-4367-46f4-95e9-3ab7bad4afe7%22,%22x_object_id%22:559193157478}
    // mobile: https://detail.m.tmall.hk/item.htm?id=559193157478&toSite=main&sku_properties=1627207:878644924&decision=sku
    String tmall = "https://detail.m.tmall.hk/item.htm?id=559193157478&toSite=main&sku_properties=1627207:878644924&decision=sku";

    // pc: https://item.taobao.com/item.htm?spm=a21wu.241046-global.4691948847.37.41ca55e5Wg44cb&scm=1007.15423.84311.100200300000005&id=45356208084&pvid=8047671a-4c8a-4181-a45c-a5db3a322660
    // mobile : https://m.intl.taobao.com/detail/detail.html?spm=a21wu.241046-global.4691948847.37.41ca55e5Wg44cb&scm=1007.15423.84311.100200300000005&id=45356208084&pvid=8047671a-4c8a-4181-a45c-a5db3a322660#&modal=sku

    //pc: https://item.taobao.com/item.htm?spm=a21bo.7929913.198967.35.548041743KJYqQ&id=531008075626
    //m: https://m.intl.taobao.com/detail/detail.html?spm=a21bo.7929913.198967.35.548041743KJYqQ&id=531008075626
    String taobao = "https://m.intl.taobao.com/detail/detail.html?spm=a21wu.241046-global.4691948847.37.41ca55e5Wg44cb&scm=1007.15423.84311.100200300000005&id=45356208084&pvid=8047671a-4c8a-4181-a45c-a5db3a322660#&modal=sku";

    // 1688
    // pc: https://detail.1688.com/offer/573579586529.html?spm=a262r.11939639.jl3fhedn.3.27882f0ao3RG30
    // mobile: https://m.1688.com/offer/573579586529.html?spm=a262r.11939639.jl3fhedn.3.27882f0ao3RG30
    // https://m.1688.com/offer/575849388753.html?spm=a260k.10548425.j9tk8ui2.3.2bba29ddQvU8lN
    String m1688 = "https://m.1688.com/offer/575849388753.html?spm=a260k.10548425.j9tk8ui2.3.2bba29ddQvU8lN";


    String CN_color = "颜色";
    String CN_color_v2 = "颜色分类";
    String CN_number = "购买数量";
    String CN_size = "尺码";

    String VN_color = "Màu sắc";
    String VN_number = "Số lượng";
    String VN_size = "Kích thước";

    String DATE_FORMAT_DD_MM_YYYY = "dd/MM/yyyy";
    String yyyy_MM_dd_T_HH_mm_ss = "yyyy-dd-MM'T'HH:mm:ss";

    int HTTP_SUCCESS = 102;
    int HTTP_FAIL = 101;
    int TIMEOUT_MS = 7000;
    int POST = Request.Method.POST;
    int GET = Request.Method.GET;

    String ROOT_URL_FOR_IMAGE = "http://demo.vominhthien.com";
    String ROOT_URL = ROOT_URL_FOR_IMAGE + "/";
    String API = ROOT_URL + "webservice1.asmx/";
    String Register = API + "Register";
    String Login = API + "Login";
    String ForgotPassword = API + "ForgotPassword";
    String Get_Info = API + "Get_Info";
    String GetAllMenu = API + "GetAllMenu";
    String UpdateProfile = API + "UpdateProfile";
    String Upload = API + "Upload";
    String GetMenuMain = API + "GetMenuMain";
    String ChangePassword = API + "ChangePassword";
    String LogOut = API + "LogOut";
    String GetAllNoti = API + "GetAllNoti";
    String UpdateNoti = API + "UpdateNoti";


    String ABOUT_BLANK = "about:blank";
    String DATE_FORMAT_PICTURE = "yyyyMMdd_HHmmss";
    int MEDIA_TYPE_IMAGE = 1;
    String pathDownload = "/DolEnglish/";
    String IMAGE_JPG = ".jpg";
    int TIME_OUT_UPLOAD_IMAGE = 20000;
}

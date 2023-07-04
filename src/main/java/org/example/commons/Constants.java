package org.example.commons;

public class Constants {
    /**
     * 京东
     */
    public static final String JD_URL = "https://api.jd.com/routerjson";
    public static final String JD_METHOD = "jd.union.open.goods.jingfen.query";

    public final static String APP_KEY_JD = "8724dd097e248b402993f9550b287e0d";//京东
    public final static String APP_SECRET_JD = "c7b290141c2145ba9d052a563b5a03b6";//密钥
    public final static String APP_ID = "4100876757";//APP ID

    /**
     * 淘宝
     * 淘宝客id
     * mm_529810053_2700800303_114443250154
     * 529810053 淘宝联盟账号的ID
     * 2700800303 备案推广类型的ID
     * 114443250154 推广位的ID
     */
    public final static String TB_URL = "https://eco.taobao.com/router/rest";//
    public final static String TB_METHOD = "taobao.tbk.dg.optimus.material";//

    public final static String APP_KEY_TB = "33976106";//淘宝
    public final static String APP_SECRET_TB = "a617a035736f95becd2a9c8db14962cf";//密钥
    public final static String ADZONE_ID = "114443250154";//推广位  mm_529810053_2700800303_114443250154
    //双色球
    public static final String SSQ_HIS_URL = "http://www.cwl.gov.cn/cwl_admin/front/cwlkj/search/kjxx/findDrawNotice";
    /**
     * application.properties
     */
    public final static String VERSION_APPURL = "version.appUrl";
    public final static String VERSION_VERSION = "version.version";
    public final static String VERSION_VERSIONNAME = "version.versionName";
    public final static String VERSION_UPDATECONTENT = "version.updateContent";
}

package com.zhuangbudong.ofo.net;

/**
 * Retrofit api 请求接口定义
 *
 * 希望所有请求全部使用 Get 方式。
 *
 * Created by wangxu on 16/11/24.
 */

public interface ApiService {


//    /**
//     * Login
//     *
//     * @param appKey   appkey
//     * @param userName 用户名
//     * @param password 登录密码
//     * @return 用户信息
//     */
//
//    @FormUrlEncoded
//    @POST("demo/v1.0/userLogin.do")
//    Observable<User> getUser(@Field("appKey") String appKey, @Field("userName") String userName, @Field("password") String password);
//
//
//    /**
//     * 根据用户获取对应的商场信息（列表）
//     *
//     * @param appKey appkey
//     * @param userId 用户id
//     * @return 用户名下所有支持的商场信息
//     */
//    @FormUrlEncoded
//    @POST("demo/mall/v1.0/queryMallList.do")
//    Observable<Mall> getMallList(@Field("appKey") String appKey, @Field("userId") String userId);
//
//
//    /**
//     * 下载文件
//     *
//     * @param url 下载地址
//     * @return
//     */
//    @Streaming
//    @GET
//    Observable<ResponseBody> download(@Url String url);
//
//    /**
//     * 获取楼层信息
//     *
//     * @param appKey appkey
//     * @param mallId 对应楼编号
//     * @return
//     */
//    @FormUrlEncoded
//    @POST("nexd/v1.0/getFloorsInfo.do")
//    Observable<Floor> getFloors(@Field("appKey") String appKey, @Field("buildingCode") long mallId);
//
//    @FormUrlEncoded
//    @POST("nexd/v1.0/checkProp.do")
//    Observable<JSONObject> checkUpdate(@Field("appKey") String appKey, @Field("md5") String md5, @Field("buildingCode") String buildingCode);
}

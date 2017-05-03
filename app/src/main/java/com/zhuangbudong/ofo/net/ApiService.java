package com.zhuangbudong.ofo.net;

import com.lawrence.core.lib.core.net.HttpResult;
import com.zhuangbudong.ofo.model.Issue;
import com.zhuangbudong.ofo.model.User;

import org.json.JSONObject;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Retrofit api 请求接口定义
 * <p>
 * 希望所有请求全部使用 POST 方式（除特殊请求外）.
 * <p>
 * Created by wangxu on 16/11/24.
 */

public interface ApiService {


    /**
     * 用户登录
     *
     * @param userName 用户名
     * @param password 用户密码
     * @return 用户信息
     */

    @FormUrlEncoded
    @POST("login.do")
    Observable<HttpResult<User>> login(@Field("userName") String userName, @Field("password") String password);


    /**
     * 用户注册
     *
     * @param userName 用户名
     * @param password 密码
     * @return 注册成功信息
     */
    @FormUrlEncoded
    @POST("register.do")
    Observable<HttpResult<JSONObject>> register(@Field("userName") String userName, @Field("password") String password);


    /**
     * 修改用户详细信息
     *
     * @param user
     * @return
     */
    @FormUrlEncoded
    @POST("userDetail.do")
    Observable<HttpResult<JSONObject>> updateUserDeatil(@Body User user);


    /**
     * 用户发布信息接口
     *
     * @param issue
     * @return
     */
    @FormUrlEncoded
    @POST("issue.do")
    Observable<HttpResult<JSONObject>> issue(@Body Issue issue);


    /**
     * 用户查询自己的发布信息
     *
     * @param userName
     * @param id
     * @return
     */
    @FormUrlEncoded
    @POST("myIssue.do")
    Observable<HttpResult<List<Issue>>> myIssue(@Field("userName") String userName, @Field("id") String id);

    /**
     * 获取所有发布信息
     *
     * @return
     */
    @FormUrlEncoded
    @POST("allIssue.do")
    Observable<HttpResult<List<Issue>>> allIssue();


//    /**
//     * 处理照片上传
//     *
//     * @return
//     */
//    @Multipart
//    @PUT("")
//    Observable uploadImage();

}

package com.lawrence.core.lib.core.net;

import rx.functions.Func1;

/**
 * 用来统一处理Http的resultCode,并将HttpResult的Data部分剥离出来返回给subscriber
 *
 * @param <T> Subscriber真正需要的数据类型，也就是Data部分的数据类型
 *            <p>
 *            create by wangxu
 */

class HttpResultFunc<T> implements Func1<HttpResult<T>, T> {

    @Override
    public T call(HttpResult<T> tHttpResult) {
        if (tHttpResult.getError() == 1) {
            throw new ApiException(tHttpResult.getMsg());
        }
        return tHttpResult.getData();
    }
}

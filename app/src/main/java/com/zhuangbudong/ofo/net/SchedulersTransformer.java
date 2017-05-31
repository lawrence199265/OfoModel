//package com.zhuangbudong.ofo.net;
//
//
//import io.reactivex.ObservableSource;
//import io.reactivex.ObservableTransformer;
//import rx.android.schedulers.AndroidSchedulers;
//import rx.schedulers.Schedulers;
//
///**
// * Created by xxx on 17/5/3.
// */
//
//public class SchedulersTransformer<T> implements ObservableTransformer<T, T> {
//    @Override
//    public ObservableSource<T> apply(io.reactivex.Observable<T> upstream) {
//        return upstream
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeOn(Schedulers.io())
//                .unsubscribeOn(Schedulers.io());
//    }
//
//}

package com.zhuangbudong.ofo.model;


import io.reactivex.Observable;
import io.reactivex.subjects.ReplaySubject;
import io.reactivex.subjects.Subject;

/**
 * Created by xxx on 17/5/8.
 */

public class UserRepository {
    private User user;
    private Subject<User> subject = ReplaySubject.createWithSize(1);

    public Observable<User> update() {
        return subject;
    }

    public void updateUser(User user) {
        this.user = user;
        subject.onNext(user);
    }
}

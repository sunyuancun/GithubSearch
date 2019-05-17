package com.syc.githubsearch.mvp.base;

import android.content.Context;

/**
 * Created by Administrator on 2019/5/16.
 */

public abstract class BasePresenter<V extends IBaseView> {

    //Presenter持有的View
    protected V mView;
    //上下文
    protected Context mContext;

    //构造方法让Presenter层持有View层的引用
    public BasePresenter(Context context,V view){
        this.mContext = context;
        this.mView = view;
    }
}
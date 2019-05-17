package com.syc.githubsearch.mvp;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import com.syc.githubsearch.GithubSearchResult;
import com.syc.githubsearch.mvp.base.BasePresenter;
import com.syc.githubsearch.utils.RetrofitUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2019/5/16.
 */

public class GithubPresenter extends BasePresenter<GithubView> {

    protected GithubService mService = RetrofitUtils.getInstance().create(GithubService.class);
    private GithubView mGithubView;
    private ProgressDialog mProgressDialog;

    public GithubPresenter(Context context, GithubView view) {
        super(context, view);
        mGithubView = view;
        mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setMessage("正在加载中");
    }

    public void searchUsers(String mUsersKeyWords) {
        mProgressDialog.show();
        try {

            final List<GithubSearchResult.UserItem> emptyResultList = new ArrayList<>();

            mService.searchUsers(mUsersKeyWords)
                    .flatMap(new Function<GithubSearchResult, ObservableSource<List<GithubSearchResult.UserItem>>>() {
                        @Override
                        public ObservableSource<List<GithubSearchResult.UserItem>> apply(GithubSearchResult githubSearchResult) throws Exception {
                            if (githubSearchResult.total_count == 0)
                                return Observable.just(emptyResultList);
                            List<GithubSearchResult.UserItem> items = githubSearchResult.userItems;
                            return searchUserrepos(items);
                        }
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<List<GithubSearchResult.UserItem>>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(List<GithubSearchResult.UserItem> userItems) {
                            mGithubView.getSearchListResult(userItems);
                        }

                        @Override
                        public void onError(Throwable e) {
                            mGithubView.getResultError(e.getMessage().toString());
                            mProgressDialog.dismiss();
                        }

                        @Override
                        public void onComplete() {
                            mProgressDialog.dismiss();
                        }
                    });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Observable<List<GithubSearchResult.UserItem>> searchUserrepos(List<GithubSearchResult.UserItem> list) {

        return Observable.fromIterable(list)
                .concatMap(new Function<GithubSearchResult.UserItem, ObservableSource<GithubSearchResult.UserItem>>() {
                    @Override
                    public ObservableSource<GithubSearchResult.UserItem> apply(final GithubSearchResult.UserItem userItem) throws Exception {

                        String path = userItem.repos_url.split("users/")[1].split("/repos")[0];

                        return mService.searchUsersrepos(path)
                                .flatMap(new Function<List<GithubSearchResult.Language>, Observable<GithubSearchResult.UserItem>>() {
                                    @Override
                                    public Observable<GithubSearchResult.UserItem> apply(List<GithubSearchResult.Language> languages) throws Exception {
                                        HashMap<String, Integer> tempmap = new HashMap<>();
                                        for (GithubSearchResult.Language item : languages) {
                                            String key_language = item.language;
                                            if (tempmap.containsKey(key_language)) {
                                                tempmap.get(key_language);
                                                tempmap.put(key_language, tempmap.get(key_language) + 1);
                                            } else {
                                                tempmap.put(key_language, 1);
                                            }
                                        }
                                        userItem.languagesMap = tempmap;
                                        return Observable.just(userItem);
                                    }
                                })
                                .onErrorResumeNext(new Function<Throwable, Observable< GithubSearchResult.UserItem>>() {
                                    @Override
                                    public Observable< GithubSearchResult.UserItem> apply(Throwable throwable) throws Exception {
                                        userItem.languagesMap = null;
                                        return Observable.just(userItem);
                                    }
                                });

                    }
                })
                .buffer(list.size());


    }

}

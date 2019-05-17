package com.syc.githubsearch.mvp;

import com.syc.githubsearch.GithubSearchResult;
import com.syc.githubsearch.mvp.base.IBaseView;

import java.util.List;

/**
 * Created by Administrator on 2019/5/16.
 */

public interface GithubView extends IBaseView {
    void getResultError(String s);

    void getSearchListResult(List<GithubSearchResult.UserItem> userItems);
}

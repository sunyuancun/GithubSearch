package com.syc.githubsearch.mvp;

import com.syc.githubsearch.GithubSearchResult;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Administrator on 2019/5/16.
 */

public interface  GithubService {

    @GET("users/{usersReposKeyWords}/repos")
    Observable<List<GithubSearchResult.Language>> searchUsersrepos(@Path("usersReposKeyWords") String usersReposKeyWords);

    @GET("/search/users")
    Observable<GithubSearchResult> searchUsers(@Query("q") String usersKeyWords);
}

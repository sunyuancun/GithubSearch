package com.syc.githubsearch;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2019/5/16.
 */

public class GithubSearchResult implements Serializable {

    @SerializedName("total_count")
    public  int total_count;
    @SerializedName("incomplete_results")
    public  boolean incomplete_results;

    @SerializedName("items")
    public List<UserItem> userItems;

    public class UserItem {
        @SerializedName("login")
        public  String login;
        @SerializedName("avatar_url")
        public  String avatar_url;
        @SerializedName("repos_url")
        public  String repos_url;

        public HashMap<String,Integer> languagesMap;
    }

    public class Language {
        @SerializedName("language")
        public String language;
    }

}

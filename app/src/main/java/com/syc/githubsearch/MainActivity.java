package com.syc.githubsearch;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.syc.githubsearch.mvp.base.BaseActivity;
import com.syc.githubsearch.mvp.GithubPresenter;
import com.syc.githubsearch.mvp.GithubView;
import com.syc.githubsearch.utils.MapUtils;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends BaseActivity<GithubPresenter> implements GithubView {

    private Context mContext;
    private ListView mListView;
    private EditText et_keyword;
    private Button bt_search;

    private CommonAdapter mResultAdapter;
    private ArrayList<GithubSearchResult.UserItem> mResultList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        et_keyword = findViewById(R.id.et_keyword);
        bt_search = findViewById(R.id.bt_search);
        mListView = findViewById(R.id.list_view);
        mResultAdapter = new CommonAdapter<GithubSearchResult.UserItem>(this, R.layout.list_view_item, mResultList) {
            @Override
            protected void convert(ViewHolder viewHolder, GithubSearchResult.UserItem item, int position) {
                try {
                    ImageView iv_avatar = viewHolder.getView(R.id.iv_avatar);
                    Glide.with(MainActivity.this).load(item.avatar_url).asBitmap().into(iv_avatar);
                    viewHolder.setText(R.id.tv_name, item.login);

                    String likeLanguage = "";
                    if (item.languagesMap != null) {
                        Object max_language_value = MapUtils.gerMaxValue(item.languagesMap);
                        ArrayList<String> max_language_keys = MapUtils.getMaxKeys(item.languagesMap, Integer.valueOf(max_language_value.toString()));
                        likeLanguage = String.format("偏好语言:%s,repos次数:%d", max_language_keys.get(0), max_language_value);
                    } else {
                        likeLanguage = "repos 无法访问";
                    }

                    viewHolder.setText(R.id.tv_language, likeLanguage);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        mListView.setAdapter(mResultAdapter);
        mListView.setEmptyView(findViewById(R.id.empty_line));

        bt_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String keyWords = et_keyword.getText().toString().trim();
                if (keyWords.isEmpty()) {
                    Toast.makeText(MainActivity.this, "关键字不能空", Toast.LENGTH_SHORT).show();
                    return;
                }
                mPresenter.searchUsers(keyWords);
            }
        });

    }

    @Override
    protected void initPresenter() {
        mContext = this;
        mPresenter = new GithubPresenter(mContext, this);

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void getResultError(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void getSearchListResult(List<GithubSearchResult.UserItem> userItems) {
        mResultList.clear();
        mResultList.addAll(userItems);
        mResultAdapter.notifyDataSetChanged();
    }
}

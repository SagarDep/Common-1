package cn.lemon.jcourse.module.java;

import android.os.Bundle;

import cn.lemon.common.base.presenter.SuperPresenter;
import cn.lemon.common.net.ServiceResponse;
import cn.lemon.jcourse.model.JavaCourseModel;
import cn.lemon.jcourse.model.bean.JavaCourse;

/**
 * Created by linlongxin on 2016/8/16.
 */

public class StarListPresenter extends SuperPresenter<StarListActivity> {

    private int mPage = 0;

    @Override
    public void onCreate(Bundle b) {
        getData(true);
    }

    public void refreshData() {
        getData(true);
    }

    public void loadMoreData() {
        getData(false);
    }

    private void getData(final boolean isRefresh) {
        if(isRefresh){
            mPage = 0;
        }else {
            mPage++;
        }
        JavaCourseModel.getInstance().getStarJCourseList(mPage, new ServiceResponse<JavaCourse[]>() {
            @Override
            public void onNext(JavaCourse[] javaCourses) {
                super.onNext(javaCourses);
                if (isRefresh) {
                    getView().showContent();
                    getView().getAdapter().clear();
                }
                getView().getAdapter().addAll(javaCourses);
                if (javaCourses.length == 0) {
                    if (mPage == 0) {
                        getView().showEmpty();
                    } else {
                        getView().getRecyclerView().showNoMore();
                    }
                } else if (javaCourses.length < 10) {
                    getView().getRecyclerView().showNoMore();
                }
                if (getView().getRecyclerView().getSwipeRefreshLayout().isRefreshing()) {
                    getView().getRecyclerView().getSwipeRefreshLayout().setRefreshing(false);
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                getView().showError();
            }
        });
    }
}

package com.phannguyen.githubgraphqlsample

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.support.annotation.RequiresApi

class TrendingRepositoriesViewModel: ViewModel() {

    private val dataRepository = DataRepository()

    var reposResult = MutableLiveData<Pair<List<GetLatestTrendingRepositoriesInLastWeekQuery.Edge>?, Error?>>()

    init {
        loadRepos()
    }


    fun loadRepos() {
        dataRepository.getLatestTrendingRepositoriesInLastWeek {
            val handler = Handler(Looper.getMainLooper())
            handler.post {
                reposResult.value = it
            }
        }
    }

}
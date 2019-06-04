package com.phannguyen.graphqlsample

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.support.annotation.RequiresApi

@RequiresApi(Build.VERSION_CODES.O)
class TrendingRepositoriesViewModel: ViewModel() {

    private val dataRepository = DataRepository()

    var reposResult = MutableLiveData<Pair<List<GetLatestTrendingRepositoriesInLastWeekQuery.Edge>?, Error?>>()

    init {
        loadRepos()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun loadRepos() {
        dataRepository.getLatestTrendingRepositoriesInLastWeek {
            val handler = Handler(Looper.getMainLooper())
            handler.post {
                reposResult.value = it
            }
        }
    }

}
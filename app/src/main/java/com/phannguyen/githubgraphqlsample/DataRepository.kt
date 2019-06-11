package com.phannguyen.githubgraphqlsample

import android.os.Build
import android.support.annotation.RequiresApi
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.exception.ApolloException
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import type.SearchType
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.TimeUnit

class DataRepository {
    @RequiresApi(Build.VERSION_CODES.O)
    fun getLatestTrendingRepositoriesInLastWeek(completion: (result: Pair<List<GetLatestTrendingRepositoriesInLastWeekQuery.Edge>?, Error?>) -> Unit) {
        val lastWeekDate = LocalDate.now().minusDays(7)
        val formattedDateText = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH).format(lastWeekDate)
        val queryCall = GetLatestTrendingRepositoriesInLastWeekQuery
            .builder()
            .query("created:>$formattedDateText sort:stars-desc")
            .first(25)
            .type(SearchType.REPOSITORY)
            .build()

        apolloClient.query(queryCall).enqueue(object: ApolloCall.Callback<GetLatestTrendingRepositoriesInLastWeekQuery.Data>() {
            override fun onFailure(e: ApolloException) {
                completion(Pair(null, Error(e.message)))
            }

            override fun onResponse(response: com.apollographql.apollo.api.Response<GetLatestTrendingRepositoriesInLastWeekQuery.Data>) {
                val errors = response.errors()
                if (!errors.isEmpty()) {
                    val message = errors[0]?.message() ?: ""
                    completion(Pair(null, Error(message)))
                } else {
                    completion(Pair(response.data()?.search()?.edges() ?: listOf(), null))
                }
            }
        })
    }

    companion object {

        private val GITHUB_GRAPHQL_ENDPOINT = "https://api.github.com/graphql"

        private val httpClient: OkHttpClient by lazy {
            OkHttpClient.Builder()
                .writeTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .addNetworkInterceptor(NetworkInterceptor())
                .build()
        }


        private val apolloClient: ApolloClient by lazy {
            ApolloClient.builder()
                .serverUrl(GITHUB_GRAPHQL_ENDPOINT)
                .okHttpClient(httpClient)
                .build()
        }

        private class NetworkInterceptor: Interceptor {

            override fun intercept(chain: Interceptor.Chain?): Response {
                return chain!!.proceed(chain.request().newBuilder().header("Authorization", "Bearer 6a371b8d15593562d52c6baab0efe665daf52fad").build())
            }
        }

    }
}
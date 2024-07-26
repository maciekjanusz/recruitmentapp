package dev.mjanusz.recruitmentapp.data.remote

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val token: String) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val builder = request.newBuilder()
            if(token.isNotEmpty()) {
                builder.addHeader("Authorization", "Bearer $token")
            } else {
                Log.d("AuthInterceptor", "Empty token, proceeding without authorization header.")
            }
        return chain.proceed(builder.build())
    }
}
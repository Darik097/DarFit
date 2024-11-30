    package com.example.darfit.api

    import android.content.Context
    import retrofit2.Retrofit
    import retrofit2.converter.gson.GsonConverterFactory

    object RetrofitInstance {
        private var retrofit: Retrofit? = null

        public fun getRetrofitInstanceDefault(): Retrofit {
            if (retrofit == null) {
                retrofit = Retrofit.Builder()
                    .baseUrl("http://31.129.102.158:8889/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            return retrofit!!
        }
    }
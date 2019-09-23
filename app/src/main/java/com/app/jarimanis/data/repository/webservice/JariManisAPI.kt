package com.app.jarimanis.data.repository.webservice

import com.app.jarimanis.data.datasource.models.ResponseRegister
import com.app.jarimanis.data.datasource.models.UserRegister
import com.app.jarimanis.data.datasource.models.kategori.Category
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface JariManisAPI {
    @GET("category")
        suspend  fun listCategory() : Response<Category>
}

package hr.fika.formulaone.api

import Season
import retrofit2.Call
import retrofit2.http.GET

const val API_URL = "http://ergast.com/api/f1/"
interface FormulaApi {
    @GET("2021/results.json?limit=500")
    fun fetchItems() : Call<Season>
}
package samuele794.it.feature.warframe

import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import samuele794.it.feature.warframe.model.MissionType
import kotlin.reflect.KProperty1
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.memberProperties

class WarframeTableDownloader(private val url: String) {
    private val client = HttpClient(OkHttp)

    suspend fun downloadWebsite(): String {
        val response =client.get(url)

        if (response.status.isSuccess()){
            return response.bodyAsText()
        }else{
            throw Exception()
        }
    }
}
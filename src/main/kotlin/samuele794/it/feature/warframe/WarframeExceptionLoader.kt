package samuele794.it.feature.warframe

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import samuele794.it.feature.warframe.model.WarframeTableException
import java.io.File
import java.net.URI

class WarframeExceptionLoader(
    val jsonDecoder: Json,
    val exceptionFilePath: URI
) {

    @OptIn(ExperimentalSerializationApi::class)
    fun loadExceptionTable(): WarframeTableException {
        return jsonDecoder.decodeFromStream( File(exceptionFilePath).inputStream())

    }
}
package org.example.project

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.example.project.utils.isAndroid

class FileTransferApi {
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(
                Json {
                    ignoreUnknownKeys = true
                    prettyPrint = true
                    isLenient = true
                }
            )
        }
    }
    private val baseUrl = if(isAndroid()) "http://10.0.2.2:3000" else "http://localhost:3000"

    suspend fun uploadFile(request: FileTransferRequest): FileTransferResponse {
        return try {
            val formData = formData {
                append("file", request.fileContent, Headers.build {
                    append(HttpHeaders.ContentDisposition, "filename=\"${request.fileName}\"")
                })
                append("expires", request.expiration)
                append("maxDownloads", request.maxDownloads.toString())
                append("autoDelete", request.autoDelete.toString())
            }

            val response = client.post("$baseUrl/api/upload") {
                setBody(MultiPartFormDataContent(formData))
                header(HttpHeaders.Accept, "application/json")
            }

            if (!response.status.isSuccess()) {
                throw Exception("Server error: ${response.status}")
            }

            Json.decodeFromString<FileTransferResponse>(response.bodyAsText())
        } catch (e: Exception) {
            throw Exception("Upload failed: ${e.message}")
        }
    }
}

@Serializable
data class FileTransferRequest(
    val fileContent: ByteArray,
    val fileName: String,
    val expiration: String,
    val maxDownloads: Int,
    val autoDelete: Boolean
)

@Serializable
data class FileTransferResponse(
    val success: Boolean,
    val link: String,
    val filename: String,
    val expires: String,
    val maxDownloads: String,
    val message: String
)
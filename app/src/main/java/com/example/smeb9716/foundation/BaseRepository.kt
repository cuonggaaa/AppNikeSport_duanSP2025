package com.example.smeb9716.foundation

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.HttpException
import retrofit2.Response
import timber.log.Timber
import java.io.IOException
import java.net.SocketTimeoutException
import kotlin.coroutines.cancellation.CancellationException

open class BaseRepository {
    suspend fun <T> safeCallApi(
        mapper: ((T) -> T)? = null, call: suspend () -> Response<T>
    ): Data<T> {
        return try {
            val response = call.invoke()

            if (response.isSuccessful) {
                val body = response.body()
                Timber.d("[1] Body: $body")

                if (body == null) {
                    Timber.d("[1] Response null")
                    Data.Error("Response null")
                } else {
                    if (body !is BaseResponse) {
                        Data.Error("Expected response to be of type BaseResponse")
                    }

                    val success = (body as BaseResponse).success
                    val message = (body as BaseResponse).message
                    val err = (body as BaseResponse).error

                    if (!success) {
                        Timber.d("[1] Errorrrr: $message")
                        Data.Error(message ?: err ?: "An error occurred")
                    }

                    if (mapper != null) {
                        Data.Success(mapper(body))
                    } else {
                        Data.Success(body)
                    }
                }
            } else {
                val body = parseErrorBody(response)
                Timber.d("[2] Body: $body")

                if (body == null) {
                    Data.Error("Response null")
                } else {
                    val message = body.message
                    val err = body.error

                    Timber.d("Errorrrr: $message")
                    Data.Error(message ?: err ?: "An error occurred")
                }
            }

        } catch (e: HttpException) {
            val message = e.message()
            Timber.d("Errorrrr: $message")
            Data.Error(e.message ?: "An error occurred")
        } catch (e: IOException) {
            Timber.d("Errorrrr: ${e.message}")
            Data.Error(e.message ?: "An error occurred")
        } catch (e: Exception) {
            Timber.d("Errorrrr: ${e.message}")
            Data.Error(e.message ?: "An error occurred")
        } catch (e: SocketTimeoutException) {
            Timber.d("Errorrrr: ${e.message}")
            Data.Error(e.message ?: "An error occurred")
        } catch (e: CancellationException) {
            Timber.d("Errorrrr: ${e.message}")
            Data.Error(e.message ?: "An error occurred")
        }
    }

    private val gson = Gson()

    private fun <T> parseErrorBody(response: Response<T>): BaseResponse? {
        return try {
            val type = object : TypeToken<BaseResponse>() {}.type
            response.errorBody()?.string()?.let {
                gson.fromJson(it, type)
            }
        } catch (e: Exception) {
            Timber.e(e, "Error parsing error body")
            null
        }
    }
}

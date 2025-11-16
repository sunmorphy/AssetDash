package com.andikas.assetdash.utils

import com.andikas.assetdash.domain.model.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

inline fun <T> safeApiCall(
    crossinline apiCall: suspend () -> T
): Flow<Resource<T>> = flow {

    emit(Resource.Loading())

    try {
        val data = apiCall()

        emit(Resource.Success(data))

    } catch (e: HttpException) {
        val message = "Terjadi kesalahan jaringan (Error ${e.code()})"
        emit(Resource.Error(message))

    } catch (e: IOException) {
        emit(Resource.Error("Tidak ada koneksi internet. Mohon periksa kembali."))

    } catch (e: Exception) {
        emit(Resource.Error("Terjadi kesalahan tidak terduga: ${e.message}"))
    }
}
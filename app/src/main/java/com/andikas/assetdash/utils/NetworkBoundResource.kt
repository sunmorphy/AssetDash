package com.andikas.assetdash.utils

import com.andikas.assetdash.domain.model.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

inline fun <ResultType, DbType> networkBoundResource(
    crossinline query: () -> Flow<DbType?>,
    crossinline fetch: suspend () -> Unit,
    crossinline map: (DbType?) -> ResultType?
): Flow<Resource<ResultType>> = flow {

    emit(Resource.Loading())

    val cache = query().first()

    emit(Resource.Loading(data = map(cache)))

    try {
        fetch()

        val newCache = query().first()

        val mappedData = map(newCache)
        if (mappedData != null) {
            emit(Resource.Success(data = mappedData))
        } else {
            emit(Resource.Error("Data tidak ditemukan setelah refresh.", data = null))
        }

    } catch (e: HttpException) {
        emit(
            Resource.Error(
                message = "Terjadi kesalahan jaringan (Error ${e.code()})",
                data = map(cache)
            )
        )
    } catch (_: IOException) {
        emit(
            Resource.Error(
                message = "Tidak ada koneksi internet. Menampilkan data offline.",
                data = map(cache)
            )
        )
    } catch (e: Exception) {
        emit(
            Resource.Error(
                message = "Terjadi kesalahan: ${e.message}",
                data = map(cache)
            )
        )
    }
}
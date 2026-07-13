package com.smach.zapmancer.framework.storage

import com.google.cloud.storage.BlobId
import com.google.cloud.storage.BlobInfo
import com.google.cloud.storage.Storage
import com.google.cloud.storage.StorageOptions
import java.util.concurrent.TimeUnit

/**
 * Google Cloud Storage implementation of the StorageService.
 */
@Suppress("MaxLineLength", "TooGenericExceptionCaught", "SwallowedException")
class GcsStorageService : StorageService {
    private val storage: Storage = StorageOptions.getDefaultInstance().service

    override suspend fun uploadFile(
        bucketName: String,
        path: String,
        content: ByteArray,
        contentType: String,
    ): String {
        val blobId = BlobId.of(bucketName, path)
        val blobInfo = BlobInfo.newBuilder(blobId)
            .setContentType(contentType)
            .build()

        storage.create(blobInfo, content)
        return "https://storage.googleapis.com/$bucketName/$path"
    }

    override suspend fun deleteFile(bucketName: String, path: String): Boolean = storage.delete(BlobId.of(bucketName, path))

    override suspend fun exists(bucketName: String, path: String): Boolean = storage.get(BlobId.of(bucketName, path)) != null

    override suspend fun getDownloadUrl(
        bucketName: String,
        path: String,
        expirationMinutes: Int,
    ): String {
        val blobInfo = BlobInfo.newBuilder(BlobId.of(bucketName, path)).build()
        return storage.signUrl(blobInfo, expirationMinutes.toLong(), TimeUnit.MINUTES).toString()
    }

    override suspend fun getFile(bucketName: String, path: String): ByteArray? = try {
        storage.readAllBytes(BlobId.of(bucketName, path))
    } catch (e: Exception) {
        null
    }
}

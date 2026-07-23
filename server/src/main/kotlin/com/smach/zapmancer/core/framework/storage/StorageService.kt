package com.smach.zapmancer.core.framework.storage

/**
 * Interface for file storage operations across different providers (GCS, S3, etc.).
 */
interface StorageService {
    /**
     * Uploads a file to the storage provider.
     * @param bucketName The name of the storage bucket.
     * @param path The path/filename within the bucket.
     * @param content The file content as a ByteArray.
     * @param contentType The MIME type of the file.
     * @return The public URL of the uploaded file.
     */
    suspend fun uploadFile(
        bucketName: String,
        path: String,
        content: ByteArray,
        contentType: String,
    ): String

    /**
     * Deletes a file from the storage provider.
     * @param bucketName The name of the storage bucket.
     * @param path The path/filename to delete.
     * @return True if the file was deleted, false otherwise.
     */
    suspend fun deleteFile(bucketName: String, path: String): Boolean

    /**
     * Checks if a file exists in the storage provider.
     * @param bucketName The name of the storage bucket.
     * @param path The path/filename to check.
     * @return True if the file exists, false otherwise.
     */
    suspend fun exists(bucketName: String, path: String): Boolean

    /**
     * Generates a temporary, signed URL for downloading a private file.
     * @param bucketName The name of the storage bucket.
     * @param path The path/filename to access.
     * @param expirationMinutes How long the URL remains valid (default 15 mins).
     * @return A temporary URL string.
     */
    suspend fun getDownloadUrl(
        bucketName: String,
        path: String,
        expirationMinutes: Int = 15,
    ): String

    /**
     * Downloads a file's content into memory.
     * @param bucketName The name of the storage bucket.
     * @param path The path/filename to download.
     * @return The file content as a ByteArray, or null if not found.
     */
    suspend fun getFile(bucketName: String, path: String): ByteArray?
}

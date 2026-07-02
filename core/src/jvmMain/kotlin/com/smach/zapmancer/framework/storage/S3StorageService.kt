package com.smach.zapmancer.framework.storage

import aws.sdk.kotlin.runtime.auth.credentials.StaticCredentialsProvider
import aws.sdk.kotlin.services.s3.S3Client
import aws.sdk.kotlin.services.s3.deleteObject
import aws.sdk.kotlin.services.s3.headObject
import aws.sdk.kotlin.services.s3.model.GetObjectRequest
import aws.sdk.kotlin.services.s3.model.PutObjectRequest
import aws.sdk.kotlin.services.s3.presigners.presignGetObject
import aws.smithy.kotlin.runtime.content.ByteStream
import aws.smithy.kotlin.runtime.content.toByteArray
import aws.smithy.kotlin.runtime.net.url.Url
import kotlin.time.Duration.Companion.minutes

/**
 * S3-compatible implementation of the StorageService (works with RustFS, MinIO, AWS S3).
 */
@Suppress("TooGenericExceptionCaught", "SwallowedException")
class S3StorageService(
    private val endpointUrl: String?,
    private val region: String,
    private val accessKey: String,
    private val secretKey: String,
) : StorageService {

    private val s3Client = S3Client {
        region = this@S3StorageService.region
        this@S3StorageService.endpointUrl?.let { endpointUrl = Url.parse(it) }
        forcePathStyle = true // Required for local S3 providers like RustFS/MinIO
        credentialsProvider = StaticCredentialsProvider {
            accessKeyId = this@S3StorageService.accessKey
            secretAccessKey = this@S3StorageService.secretKey
        }
    }

    override suspend fun uploadFile(
        bucketName: String,
        path: String,
        content: ByteArray,
        contentType: String,
    ): String {
        val request = PutObjectRequest {
            bucket = bucketName
            key = path
            body = ByteStream.fromBytes(content)
            this.contentType = contentType
        }

        s3Client.putObject(request)

        return endpointUrl?.let { "$it/$bucketName/$path" }
            ?: "https://$bucketName.s3.$region.amazonaws.com/$path"
    }

    override suspend fun deleteFile(bucketName: String, path: String): Boolean = try {
        s3Client.deleteObject {
            bucket = bucketName
            key = path
        }
        true
    } catch (e: Exception) {
        false
    }

    override suspend fun exists(bucketName: String, path: String): Boolean = try {
        s3Client.headObject {
            bucket = bucketName
            key = path
        }
        true
    } catch (e: Exception) {
        false
    }

    override suspend fun getDownloadUrl(
        bucketName: String,
        path: String,
        expirationMinutes: Int,
    ): String {
        val request = GetObjectRequest {
            bucket = bucketName
            key = path
        }
        val presignedRequest = s3Client.presignGetObject(request, expirationMinutes.minutes)
        return presignedRequest.url.toString()
    }

    override suspend fun getFile(bucketName: String, path: String): ByteArray? = try {
        val request = GetObjectRequest {
            bucket = bucketName
            key = path
        }
        s3Client.getObject(request) { response ->
            response.body?.toByteArray()
        }
    } catch (e: Exception) {
        null
    }
}

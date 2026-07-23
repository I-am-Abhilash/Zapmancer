package com.smach.zapmancer.core.framework.di

import com.smach.zapmancer.core.framework.storage.GcsStorageService
import com.smach.zapmancer.core.framework.storage.S3StorageService
import com.smach.zapmancer.core.framework.storage.StorageService
import io.ktor.server.application.Application
import org.koin.dsl.module

/**
 * Koin module for file storage dependencies.
 * It switches between GCS and S3 based on the configuration.
 */
val storageModule = module {
    single<StorageService> {
        val config = get<Application>().environment.config
        val provider = config.propertyOrNull("storage.provider")?.getString() ?: "gcs"

        if (provider == "s3") {
            S3StorageService(
                endpointUrl = config.propertyOrNull("storage.s3.endpointUrl")?.getString(),
                region = config.propertyOrNull("storage.s3.region")?.getString() ?: "us-east-1",
                accessKey = config.property("storage.s3.accessKey").getString(),
                secretKey = config.property("storage.s3.secretKey").getString(),
            )
        } else {
            GcsStorageService()
        }
    }
}

package com.smach.zapmancer.core.common.utils

/**
 * Extension functions to convert DataError to user-friendly messages.
 */
fun DataError.toUserMessage(): String = when (this) {
    DataError.Network.SERVICE_UNAVAILABLE -> "Service is temporarily unavailable. Please try again later."
    DataError.Network.CLIENT_ERROR -> "Something went wrong with your request."
    DataError.Network.NO_INTERNET -> "No internet connection. Please check your network."
    DataError.Network.SERIALIZATION -> "There was an error processing data from the server."
    DataError.Network.UNAUTHORIZED -> "Session expired or invalid credentials. Please log in again."
    DataError.Network.UNKNOWN -> "An unexpected network error occurred."
    DataError.Local.DISK_FULL -> "Your device storage is full."
    DataError.Local.PERMISSION_DENIED -> "Required permissions were denied."
    DataError.Local.INVALID_INPUT -> "The provided input is invalid."
    DataError.Local.UNKNOWN -> "An unexpected local error occurred."
}

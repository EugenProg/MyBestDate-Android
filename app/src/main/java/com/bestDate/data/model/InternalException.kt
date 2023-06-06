package com.bestDate.data.model

import java.io.IOException

sealed class InternalException(override val message: String): IOException(message) {
    class OperationException(message: String?): InternalException(message.orEmpty())
    class NotConnectionException(): InternalException("Connection lost")
    class UnknownException(val original: Exception): InternalException("Unknown error")
    class RequestDuplicateException(requestString: String): InternalException("Duplicate: $requestString")
    class ValidationException(message: String): InternalException(message)
}

class HandleError(val exception: InternalException)
package com.bestDate.data.model

import java.io.IOException

sealed class InternalException(override val message: String): IOException(message) {
    class OperationException(message: String): InternalException(message)
    class NotFoundException(): InternalException("Not founded")
    class UnknownException(val original: Exception): InternalException("Unknown error")
    class LogoutException(): InternalException("Logout error")
    class ValidationException(message: String): InternalException(message)
}

class HandleError(val exception: InternalException)
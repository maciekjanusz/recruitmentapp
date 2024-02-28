package dev.mjanusz.recruitmentapp.common

sealed interface LoadingState<out T: Any> {
    companion object {
        fun <T: Any> success(value: T): LoadingState<T> = Success(value)
        fun <T: Any> failure(throwable: Throwable): LoadingState<T> = Failure(throwable)
        fun <T: Any> loading(): LoadingState<T> = Loading()
    }

}

data class Success<out T : Any> internal constructor(val value: T) : LoadingState<T> {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Success<*>

        return value == other.value
    }

    override fun hashCode(): Int {
        return value.hashCode()
    }
}

data class Failure<out T : Any> internal constructor(val throwable: Throwable) : LoadingState<T> {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Failure<*>

        return throwable == other.throwable
    }

    override fun hashCode(): Int {
        return throwable.hashCode()
    }
}

class Loading<out T : Any> internal constructor(): LoadingState<T> {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        return javaClass == other?.javaClass
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }
}

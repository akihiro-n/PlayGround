package flux

import io.reactivex.Single
import org.koin.core.KoinComponent
import org.koin.core.inject

abstract class ActionCreator : KoinComponent {

    private val dispatcher by inject<Dispatcher>()

    protected fun dispatch(action: Action) {
        dispatcher.onNext(action)
    }

    protected fun <T, A : Action> Single<T>.onSuccessDispatch(mapper: (T) -> A): Single<A> {
        return this@onSuccessDispatch
            .map { type ->
                mapper(type)
            }
            .doOnSuccess { action ->
                dispatch(action)
            }
    }

    protected fun <T, A : ErrorAction> Single<T>.onErrorDispatch(onErrorAction: (Throwable) -> A): Single<T> {
        return this@onErrorDispatch
            .doOnError { throwable ->
                val action = onErrorAction(throwable)
                dispatch(action)
            }
    }
}
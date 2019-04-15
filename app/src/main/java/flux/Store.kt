package flux

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import org.koin.core.KoinComponent
import org.koin.core.inject

abstract class Store : KoinComponent {

    private val dispatcher by inject<Dispatcher>()

    protected val observable: Observable<Action> = dispatcher.observeOn(AndroidSchedulers.mainThread())

    /**
     * 空のエラー
     * このクラスのインスタンスが流れてきた時はエラーとして見なさない
     */
    inner class EmptyError: Throwable()

}
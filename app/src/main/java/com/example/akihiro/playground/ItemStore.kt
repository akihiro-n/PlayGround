package com.example.akihiro.playground

import android.annotation.SuppressLint
import flux.Action
import flux.Store
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

class ItemStore : Store() {

    private var nextPage = 1
    private val itemsSubject = BehaviorSubject.create<List<Item>>()
    private val fetchItemErrorSubject = BehaviorSubject.create<Throwable>()

    val nextPageState: Int
        get() = nextPage
    val itemsState: Observable<List<Item>>
        get() = itemsSubject
    val errorFetchItemsState: Observable<Throwable>
        get() = fetchItemErrorSubject

    init {
        initActionObservables()
    }

    @SuppressLint("CheckResult")
    private fun initActionObservables() {
        observable
            .updateNextPageState()
            .ofType(ItemAction.Initialize::class.java)
            .subscribe {
                nextPage = 1
                itemsSubject.onNext(listOf()) //TODO onNextのタイミングでnullを流せる仕様にしたい
                fetchItemErrorSubject.onNext(EmptyError()) //TODO onNextのタイミングでnullを流せる仕様にしたい
            }

        observable
            .updateNextPageState()
            .ofType(ItemAction.FetchNewItems::class.java)
            .map(ItemAction.FetchNewItems::item)
            .subscribe { items ->
                itemsSubject.apply {
                    value.let { value ->
                        if (value == null) onNext(items)
                        else onNext(value.plus(items))
                    }
                }
            }

        observable
            .updateNextPageState()
            .ofType(ItemAction.FetchItemsForQuery::class.java)
            .map(ItemAction.FetchItemsForQuery::item)
            .subscribe{ items ->
                itemsSubject.apply {
                    value.let { value ->
                        if (value == null) onNext(items)
                        else onNext(value.plus(items))
                    }
                }
            }

        observable
            .ofType(ItemAction.ErrorFetchNewItems::class.java)
            .map(ItemAction.ErrorFetchNewItems::throwable)
            .subscribe(fetchItemErrorSubject::onNext)

        observable
            .ofType(ItemAction.ErrorFetchItemsForQuery::class.java)
            .map(ItemAction.ErrorFetchItemsForQuery::throwable)
            .subscribe(fetchItemErrorSubject::onNext)
    }

    private fun Observable<Action>.updateNextPageState() = doOnNext { nextPage += 1 }

}
package com.example.akihiro.playground

import android.annotation.SuppressLint
import flux.Action
import flux.Store
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

class ItemStore : Store() {

    private var nextPage = 1
    private val itemsSubject = BehaviorSubject.createDefault<List<Item>>(listOf())
    private val fetchItemErrorSubject = BehaviorSubject.createDefault<Throwable>(EmptyError())

    val nextPageState: Int
        get() = nextPage
    val itemsState: Observable<List<Item>>
        get() = itemsSubject
    val errorFetchItemsState: Observable<Throwable>
        get() = fetchItemErrorSubject

    private val newItemsObservable: Observable<List<Item>> = onDispatch
        .updateNextPageState()
        .ofType(ItemAction.FetchNewItems::class.java)
        .map(ItemAction.FetchNewItems::item)

    private val itemsForQueryObservable: Observable<List<Item>> = onDispatch
        .updateNextPageState()
        .ofType(ItemAction.FetchItemsForQuery::class.java)
        .map(ItemAction.FetchItemsForQuery::item)

    private val errorFetchNewItemsObservable: Observable<Throwable> = onDispatch
        .ofType(ItemAction.ErrorFetchNewItems::class.java)
        .map(ItemAction.ErrorFetchNewItems::throwable)


    private val errorFetchItemsForQueryObservable: Observable<Throwable> = onDispatch
        .ofType(ItemAction.ErrorFetchItemsForQuery::class.java)
        .map(ItemAction.ErrorFetchItemsForQuery::throwable)

    init {
        initActionObservables()
    }

    @SuppressLint("CheckResult")
    private fun initActionObservables() {
        onDispatch
            .ofType(ItemAction.Initialize::class.java)
            .subscribe {
                nextPage = 1
                itemsSubject.onNext(listOf()) //TODO onNextのタイミングでnullを流せる仕様にしたい
                fetchItemErrorSubject.onNext(EmptyError()) //TODO onNextのタイミングでnullを流せる仕様にしたい
            }

        Observable
            .merge(newItemsObservable, itemsForQueryObservable)
            .subscribe { items ->
                itemsSubject.apply {
                    value?.let { value ->
                        if (value.isEmpty()) onNext(items)
                        else onNext(value.plus(items))
                    }
                }
            }

        Observable
            .merge(errorFetchNewItemsObservable, errorFetchItemsForQueryObservable)
            .subscribe { throwable ->
                fetchItemErrorSubject.onNext(throwable)
            }
    }

    private fun Observable<Action>.updateNextPageState() = doOnNext { nextPage += 1 }

}
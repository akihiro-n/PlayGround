package flux

import io.reactivex.subjects.PublishSubject

typealias Dispatcher = PublishSubject<Action>
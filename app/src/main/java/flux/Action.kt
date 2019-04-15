package flux

interface Action

interface ErrorAction: Action {
    val throwable: Throwable
}
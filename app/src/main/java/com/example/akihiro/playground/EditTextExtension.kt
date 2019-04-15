package com.example.akihiro.playground

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import io.reactivex.Observable

/**
 * TextWatcherの使いづらさを少しでも軽減するためのExtension集
 * 目的は以下の２点の不満を解消すること
 *  1. 必ず3つのコールバックメソッドを実装しなくてはならない（多くの場合使用するメソッドは１つであることが多いので少し面倒）
 *  2. 入力された文字列がコールバックされるのに引数の型がStringではない。毎度Stringに変換するのは少し手間
 */

/**
 * 入力された文字列を必ずStringで流すよう型を定義
 */
interface TextChangedType {
    val s: String
}

/**
 * EditTextにセットするTextWatcherの3つのコールバックメソッドに対応するクラスを定義
 * After → afterTextChanged(editable: Editable?)
 * Before → beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int)
 * On → onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int)
 */
sealed class TextChanged : TextChangedType {
    class After(override val s: String) : TextChanged()
    class Before(override val s: String, val start: Int, val count: Int, val after: Int) : TextChanged()
    class On(override val s: String, val start: Int, val before: Int, val count: Int) : TextChanged()
}

/**
 * EditTextクラスの拡張
 * 入力された文字列のみを流すObservable<String>を返す
 * 使用するコールバックメソッドは引数の型で分岐させる
 * @param clazz [TextChangedType]
 * @return
 *
 * <使用例>
 * binding.ediText
 *  .toStringObservable(TextChanged.After::class.java)
 *  .subscribe {
 *      //ここで文字列を受け取る
 *  }
 */
inline fun <reified T> EditText.toStringObservable(clazz: Class<T>): Observable<String> where T : TextChangedType {
    return toObservable(clazz).map { it.s }
}

/**
 * EditTextクラスの拡張
 * TextWatcherのコールバックメソッドで受け取った引数の値を流すObservable<T>を返す
 * 使用するコールバックメソッドは引数の型で分岐させる
 * @param clazz [TextChangedType]
 * @return
 *
 * <使用例>
 * binding.ediText
 *  .toObservable(TextChanged.After::class.java)
 *  .subscribe {
 *      //ここでTextWatcherの該当するコールバックメソッドの引数の値を全て含んだデータを受け取る
 *  }
 */
inline fun <reified T> EditText.toObservable(clazz: Class<T>): Observable<T> where T : TextChangedType {
    return Observable.create { emitter ->
        when (clazz) {
            TextChanged.After::class.java ->
                this@toObservable.afterTextChangedListener {
                    emitter.onNext(TextChanged.After(it) as T)
                }
            TextChanged.Before::class.java ->
                this@toObservable.beforeTextChangedListener { s, start, count, after ->
                    emitter.onNext(TextChanged.Before(s, start, count, after) as T)
                }
            TextChanged.On::class.java ->
                this@toObservable.onTextChangedListener { s, start, before, count ->
                    emitter.onNext(TextChanged.On(s, start, before, count) as T)
                }
        }
    }
}

/**
 * EditTextクラスの拡張
 * TextWatcherのafterTextChangedListenerの値だけをcallBackに流す
 * @param callBack
 */
fun EditText.afterTextChangedListener(callBack: (String) -> Unit) {
    this@afterTextChangedListener.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            editable?.let {
                callBack.invoke("$it")
            }
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    })
}

/**
 * EditTextクラスの拡張
 * TextWatcherのbeforeTextChangedの値だけをcallBackに流す
 * @param callBack
 */
fun EditText.beforeTextChangedListener(callBack: (s: String, start: Int, count: Int, after: Int) -> Unit) {
    this@beforeTextChangedListener.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            s?.let {
                callBack.invoke("$it", start, count, after)
            }
        }

        override fun afterTextChanged(editable: Editable?) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    })
}

/**
 * EditTextクラスの拡張
 * TextWatcherのonTextChangedの値だけをcallBackに流す
 * @param callBack
 */
fun EditText.onTextChangedListener(callBack: (s: String, start: Int, before: Int, count: Int) -> Unit) {
    this@onTextChangedListener.addTextChangedListener(object : TextWatcher {
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            s?.let {
                callBack.invoke("$it", start, before, count)
            }
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun afterTextChanged(editable: Editable?) {}
    })
}
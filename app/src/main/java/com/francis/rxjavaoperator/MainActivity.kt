package com.francis.rxjavaoperator

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*

/*
* Flowable
* Observable
* Single
* Completable
* Maybe
* */

@SuppressLint("CheckResult")
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun clickAction(view: View) {
        observer1()
        observer2()
        observer3()
        observer4()
        observer5()
        observer6()
        observer7()
        observer8()
        observer9()
    }

    private fun observer1() {
        Observable.just("Hai Francis")
            .subscribe { println(it) }
    }


    private fun observer2() {
        Observable.just(4).subscribe({ println("data $it") }, { throwable -> println("fail: ${throwable.message}") })
    }


    private fun observer3() {
        Observable.fromArray(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
            .subscribeOn(Schedulers.newThread())
            .filter { item -> item % 2 == 0 }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                println("Item $it")
                txt.text = "$it"
            }
    }

    private fun observer4() {
        Observable.fromArray("Hai", "Hallo", "Dear", "Sir")
            .filter { item -> item.contains("a") }
            .subscribe {
                println("observer4 $it")
                txt.setText(it)
            }
    }

    private fun observer5() {
        val list = arrayListOf<UserDetails>()
        list.add(UserDetails("Francis", 22))
        list.add(UserDetails("Martine", 22))
        list.add(UserDetails("Sonia", 22))
        Observable.just(list)
            .subscribe { item -> println(item.size) }
    }

    private fun observer6() {
        Observable.create<String> { subscriber ->
            try {
                val d = 10 / 2
                //val d = 10 / 0
                subscriber.onNext("" + d)
            } catch (e: Exception) {
                subscriber.onError(e)
            }
            subscriber.onComplete()
        }.subscribe({ onNext -> println("Success calculation: $onNext") },
            { throwable -> println("failed calculation: ${throwable.message}") })
    }

    private fun observer7() {
        Observable.fromCallable {
            //val data = 10 / 0
            val data = 10 / 2
            return@fromCallable data
        }.subscribe({ onNext -> println("data: $onNext") }, { throwable -> println("OnFail: ${throwable.message}") })
    }


    private fun observer8() {
        Observable.defer { Observable.just("Hai Francis") }.subscribe { println(it) }
    }

    private fun observer9() {
        Observable.zip(Observable.just("Hello"), Observable.just("Francis"),
            BiFunction { t1: String, t2: String -> t1 + t2 })
            .subscribe { println(it) }


    }
}

internal data class UserDetails(val name: String?, val age: Int)




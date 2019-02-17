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
import java.util.concurrent.TimeUnit

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
        //filterOpeationWithThread()
        filterOpeation2()
        createOperator()
        fromCallableOperator()
        deferOperator()
        zipOperator1()
        skipOperator()
        takeOperator1()
        takeOperator2()
        concatOperator1()
        concatOperator2()
        mergeOperator1()
        mergeOperator2()
        //combineLatestOperator1()
        mapOperator1()
        flatMapOprator1()
    }

    private fun observer1() {
        Observable.just("Hai Francis")
            .subscribe { println(it) }
    }


    private fun observer2() {
        Observable.just(4).subscribe({ println("data $it") }, { throwable -> println("fail: ${throwable.message}") })
    }

    private fun observer3() {
        val list = arrayListOf<UserDetails>()
        list.add(UserDetails("Francis", 22))
        list.add(UserDetails("Martine", 22))
        list.add(UserDetails("Sonia", 22))
        Observable.just(list)
            .subscribe { item -> println(item.size) }
    }


    private fun filterOpeationWithThread() {
        Observable.fromArray(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
            .subscribeOn(Schedulers.newThread())
            .filter { item -> item % 2 == 0 }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                println("Even number $it")
                txt.text = "$it"
            }
    }

    private fun filterOpeation2() {
        Observable.fromArray("Hai", "Hallo", "Dear", "Sir")
            .filter { item -> item.contains("a") }
            .subscribe {
                println("Contains a: $it")
                txt.setText(it)
            }
    }


    private fun createOperator() {
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

    private fun fromCallableOperator() {
        Observable.fromCallable {
            //val data = 10 / 0
            val data = 10 / 2
            return@fromCallable data
        }.subscribe({ onNext -> println("data: $onNext") }, { throwable -> println("OnFail: ${throwable.message}") })
    }


    private fun deferOperator() {
        Observable.defer { Observable.just("Hai Francis") }.subscribe { println(it) }
    }

    private fun zipOperator1() {
        Observable.zip(Observable.just("Hello", "Age"), Observable.just("Francis", "24    "),
            BiFunction { t1: String, t2: String -> t1 + t2 })
            .subscribe { println("Zip operator1: $it") }


    }


    private fun skipOperator() {
        Observable.just("Hai", "dear", "apple", "orange", "hello")
            .skip(2)
            .subscribe { println("Skip operator: $it") }
    }

    private fun takeOperator1() {
        Observable.just(1, 2, 3, 4)
            .take(2)
            .subscribe { println("Take operator1: $it") }
    }

    private fun takeOperator2() {
        Observable.just(1, 2, 3, 4)
//            .take(1)
            .takeLast(1)
            .subscribe { println("Take operator2: $it") }
    }

    private fun concatOperator1() {
        val observable1 = Observable.just("Hai")
        val observable2 = Observable.just("Francis")
        Observable.concat(observable1, observable2).subscribe { println("Concat Operator1: $it") }
    }

    private fun concatOperator2() {
        val obe1 = Observable.just(UserDetails("Francis", 22))
        val obe2 = Observable.just(ContactDetails("1234567890"))
        Observable.concat(obe1, obe2)
            .subscribe { println("Concat Operator2: $it") }
    }

    private fun mergeOperator1() {
        val observable1 = Observable.just(1, 2, 3)
        val observable2 = Observable.just(4, 5, 6)
        Observable.merge(observable1, observable2).subscribe { println("Merge Operator1: $it") }
    }

    private fun mergeOperator2() {
        val observable1 = Observable.error<String> { RuntimeException("Message") }
        val observable2 = Observable.just(4, 5, 6)
        Observable.mergeDelayError(observable1, observable2).subscribe({ println("Merge Operator2: $it") },
            { throwable -> println("Merge operator2 exception: ${throwable.message}") })
    }

    private fun combineLatestOperator1() {
        val newsRefreshes = Observable.interval(100, TimeUnit.MILLISECONDS)
        val weatherRefreshes = Observable.interval(50, TimeUnit.MILLISECONDS)

        Observable.combineLatest(newsRefreshes, weatherRefreshes,
            BiFunction { t1: Long, t2: Long -> "Refreshed news  $t1  times and weather  $t2" })
            .subscribe({
                println("Combine latest1: $it")
            }, { t -> println("combineLatestOperator1 ${t.message}") })


    }

    private fun observableMap(): Observable<Int> {
        return Observable.just(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
            .filter { it % 2 == 0 }
            .take(1)
            .map { it * 2 }
    }

    private fun mapOperator1() {
        observableMap().subscribe { println("Map operator 1: $it") }
    }

    private fun flatMapOprator1() {
        Observable.just("Hello")
            .flatMap { data ->
                Observable.just("$data Flatmap")
            }.subscribe { println("Flat map operator1 $it") }

    }

}

internal data class UserDetails(val name: String?, val age: Int)
internal data class ContactDetails(val number: String?)

internal class CombineDataEx(val userDetails: UserDetails?, val contactDetails: ContactDetails?)



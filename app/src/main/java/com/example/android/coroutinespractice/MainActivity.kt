package com.example.android.coroutinespractice

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.example.android.coroutinespractice.databinding.ActivityMainBinding
import kotlinx.coroutines.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.ButtonUpdateNumber.setOnClickListener { view: View -> testCoroutines() }

        binding.ButtonEnterLetter.setOnClickListener { view: View -> addText() }
    }

    private val uiScope = CoroutineScope(Dispatchers.Main)
    private val BACKGROUND = Executors.newFixedThreadPool(2)
    private val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")

    //    @Volatile
    var number: Int = 0

    var newText: String = ""


//    private fun changeNumber() {
//        BACKGROUND.submit {
////            val localNumber = number++
////            number++
//            Thread.sleep(returnRandomNumber())
////            Thread.sleep(1_000)
//            number++
//            runOnUiThread { updateText(number) }
//        }
//    }

    ///
    private fun changeNumber() {
        uiScope.launch {
            val localNumber = number++
            delay(returnRandomNumber())
            updateText(localNumber)

        }
    }

    //
    private fun returnRandomNumber(): Long {
        synchronized(this) {
            return when ((1..5).random()) {
                1 -> 1000
                2 -> 3000
                3 -> 4000
                4 -> 5000
                else -> 6000
            }

        }
    }

    //проверка функции repeat которая запускает заданное кол-во корутинов
    private fun launchSomeCoroutines(){
        GlobalScope.launch(Dispatchers.IO) {
            repeat(4){
                val start = sdf.format(Date())
                val finish = sdf.format(Date())
                launch(Dispatchers.Main){
                    newText = binding.UpdatableText.text.toString() + " $start - $finish "
                    binding.UpdatableText.text = newText
                }
            }
        }
    }


    //функия для проверки мешают ли корутины друг другу в потоке
    private fun testCoroutines() {
        // функции имитирующие работу в потоке
        forTestOne()
        forTestTwo()
    }

    private fun forTestOne() {
        GlobalScope.launch(Dispatchers.IO) {
            val start = sdf.format(Date())
            for (i in 1..3_000_000_000) {
                var t = 0
                t++
            }
            val finish = sdf.format(Date())
            showResult(start,finish,"First fun")
        }
    }

    private fun forTestTwo() {
        GlobalScope.launch(Dispatchers.IO) {
            val start = sdf.format(Date())
            for (i in 1..900_000_000) {
                var t = 0
                t++
            }
            val finish = sdf.format(Date())
            showResult(start,finish,"Second fun")
        }
    }
    // отображает результат на экране
    private fun showResult(starTime: String, finishTime: String, funNumber: String) {
        newText = binding.UpdatableText.text.toString() + " $funNumber $starTime - $finishTime "
        binding.UpdatableText.text = newText
    }

    //
     private suspend fun updateText(number: Int) {
         withContext(Dispatchers.IO){
             for (i in 1..1_000_000_000){
                 var t =0
                 t++
             }
         }
        synchronized(this) {
            newText = binding.UpdatableText.text.toString() + ",$number "
            binding.UpdatableText.text = newText
        }
    }

    private fun addText() {
        binding.UpdatableText.text =
            binding.UpdatableText.text.toString() + binding.inputLine.text.toString()
    }

}

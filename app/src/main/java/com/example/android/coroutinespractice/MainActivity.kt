package com.example.android.coroutinespractice

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.example.android.coroutinespractice.databinding.ActivityMainBinding
import kotlinx.coroutines.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.ButtonUpdateNumber.setOnClickListener { view: View -> changeNumber() }

        binding.ButtonEnterLetter.setOnClickListener { view: View -> addText() }
    }

    private val uiScope = CoroutineScope(Dispatchers.Main)
    private val BACKGROUND = Executors.newFixedThreadPool(2)

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
//        CoroutineScope(Dispatchers.Main).launch {
        uiScope.launch {
            val localNumber = number++
            delay(returnRandomNumber())
//            delay(1_000)
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


//    suspend fun loop(){
//        for (i in 1..1_000_000_000){
//            var t =0
//            t++
//        }
//    }

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

//    private fun updateScreenText() {
//        doAsync {
//            BACKGROUND.submit {
//                Thread.sleep(1_000)
//                number++
//                uiThread { updateText(number) }
//            }
//        }
//
//
//    }
}

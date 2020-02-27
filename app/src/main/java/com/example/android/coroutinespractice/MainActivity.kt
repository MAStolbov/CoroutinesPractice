package com.example.android.coroutinespractice

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.example.android.coroutinespractice.databinding.ActivityMainBinding
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

    private val BACKGROUND = Executors.newFixedThreadPool(2)

//    @Volatile
    var number: Int = 0

    var newText: String = ""


    private fun changeNumber() {
        BACKGROUND.submit {
//            val localNumber = number++
//            number++
            Thread.sleep(returnRandomNumber())
//            Thread.sleep(1_000)
            number++
            runOnUiThread { updateText(number) }
        }
    }

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

    private fun updateText(number: Int) {
        synchronized(this) {
            newText = binding.UpdatableText.text.toString() + ",$number "
            binding.UpdatableText.text = newText
        }
    }

    private fun addText() {
        binding.UpdatableText.text =
            binding.UpdatableText.text.toString() + binding.inputLine.text.toString()
    }

    private fun updateScreenText() {
        doAsync {
            BACKGROUND.submit {
                Thread.sleep(1_000)
                number++
                uiThread { updateText(number) }
            }
        }


    }
}

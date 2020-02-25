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

        binding.ButtonEnterLetter.setOnClickListener { view:View -> addText() }
    }

    private val BACKGROUND = Executors.newFixedThreadPool(2)

    @Volatile
    var number: Int = 0

    var newText: String = ""


    private fun changeNumber() {
        BACKGROUND.submit {
            Thread.sleep(returnRandomNumber())
            number++
            runOnUiThread { updateText(number) }
        }
    }

    private fun returnRandomNumber():Long{
        return (1_000..10_000).random().toLong()
    }

    private fun updateText(number: Int) {
        synchronized(this) {
            newText = binding.UpdatableText.text.toString() + ",$number "
            binding.UpdatableText.text = newText
        }
    }

    private fun addText(){
        binding.UpdatableText.text = binding.UpdatableText.text.toString() + binding.inputLine.text.toString()
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

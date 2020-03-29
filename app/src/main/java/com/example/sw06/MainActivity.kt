package com.example.sw06

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.annotation.MainThread
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val WAITING_TIME_MILIS: Long = 7000
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val btn_block = findViewById<Button>(R.id.btn_gui_block)

        btn_block.setOnClickListener {
            freeze7Seconds(it)
        }


    }

    @Suppress("UNUSED_PARAMETER")
    private fun freeze7Seconds(view: View?) {
        try {
            Thread.sleep(WAITING_TIME_MILIS)
        } catch (e: InterruptedException) {

        }
    }


}

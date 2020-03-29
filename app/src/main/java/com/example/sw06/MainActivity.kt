package com.example.sw06

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val WAITING_TIME_MILIS: Long = 7000
    private var demoThread: Thread = createDemoThread()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val btn_block = findViewById<Button>(R.id.btn_gui_block)

        btn_block.setOnClickListener {
            freeze7Seconds(it)
        }

        val btn_demo = findViewById<Button>(R.id.btn_thread_start)

        btn_demo.setOnClickListener {
            startDemoThread(it)
        }
    }

    @Suppress("UNUSED_PARAMETER")
    private fun freeze7Seconds(view: View?) {
        try {
            Thread.sleep(WAITING_TIME_MILIS)
        } catch (e: InterruptedException) {
            //TODO
        }
    }

    private fun startDemoThread(view: View?) {
        if (!demoThread.isAlive) {
            demoThread = createDemoThread()
            demoThread.start()
            btn_thread_start.text = "[Demo-Thread läuft...]"
        } else {
            Toast.makeText(this, "DemoThread läuft schon!", Toast.LENGTH_LONG)
        }
    }

    private fun createDemoThread(): Thread {
        return object : Thread("HSLUDemoThread") {
            override fun run() = try {
                sleep(WAITING_TIME_MILIS)
                runOnUiThread {
                    btn_thread_start.text = "Demo-Thread starten"
                    Toast.makeText(this@MainActivity, "Demo Thread beendet!", Toast.LENGTH_LONG)
                }
            } catch (e: InterruptedException) {
                //TODO
            }
        }
    }
}

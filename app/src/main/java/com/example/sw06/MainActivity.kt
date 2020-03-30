package com.example.sw06

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val WAITING_TIME_MILIS: Long = 7000
    private var demoThread: Thread = createDemoThread()
    private val bandsViewModel: BandsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val btnBlock = findViewById<Button>(R.id.btn_gui_block)

        btnBlock.setOnClickListener {
            freeze7Seconds(it)
        }

        val btnDemo = findViewById<Button>(R.id.btn_thread_start)

        btnDemo.setOnClickListener(this::startDemoThread)

        val btnWorker = findViewById<Button>(R.id.btn_worker_start)

        btnWorker.setOnClickListener {
            startDemoWorker(it)
        }
        val btn_request = findViewById<Button>(R.id.btn_api_request)

        btn_request.setOnClickListener {
            bandsViewModel.getBandinfo()
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

    @Suppress("UNUSED_PARAMETER")
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

    @Suppress("UNUSED_PARAMETERS")
    fun startDemoWorker(v: View?) {
        val workManager = WorkManager.getInstance(application)
        val demoWorkerRequest = OneTimeWorkRequestBuilder<DemoWorker>()
            .setInputData(
                Data.Builder()
                    .putLong(DemoWorker.WAITING_TIME_KEY, WAITING_TIME_MILIS)
                    .build()
            )
            .build()
        workManager.enqueue(demoWorkerRequest)
    }
}

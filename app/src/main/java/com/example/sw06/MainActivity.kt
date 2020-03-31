package com.example.sw06

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val WAITING_TIME_MILIS: Long = 7000
    private var demoThread: Thread = createDemoThread()
    private val bandsViewModel: BandsViewModel by viewModels()
    private var imageView: ImageView? = null
    private var txtBandInfo: TextView? = null
    private var txtBandName: TextView? = null

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        makeUnvisible()

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
        val btn_request = findViewById<Button>(R.id.btn_get_json)

        btn_request.setOnClickListener {
            bandsViewModel.getBands()
        }

        val btnReset = findViewById<Button>(R.id.btn_reset_view_model)

        btnReset.setOnClickListener {
            bandsViewModel.resetViewModel()
        }

        val btn_chooseBand = findViewById<Button>(R.id.btn_choose_band)

        btn_chooseBand.setOnClickListener {
            chooseBand()
        }

        bandsViewModel.bands?.observe(this, Observer {
            main_nubmer_of_bands.text = "#Bands = ${bandsViewModel.bands?.value?.size}"
        })

        bandsViewModel.currentBand?.observe(this, Observer {
            makeVisible()
            main_current_band_name.text = "${bandsViewModel.currentBand?.value?.name}"
            main_current_band_info.text = "${bandsViewModel.currentBand?.value?.homeCountry}"
            if (bandsViewModel.currentBand?.value?.bestOfCdCoverImageUrl != null) {
                Picasso.get().load(bandsViewModel.currentBand?.value?.bestOfCdCoverImageUrl)
                    .into(img_band_picture)
            }
        })
    }

    @Suppress("UNUSED_PARAMETER")
    private fun freeze7Seconds(view: View?) {
        try {
            Thread.sleep(WAITING_TIME_MILIS)
        } catch (e: InterruptedException) {
            //TODO
        }
    }

    @SuppressLint("SetTextI18n", "ShowToast")
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
            @SuppressLint("ShowToast")
            override fun run() = try {
                sleep(WAITING_TIME_MILIS)
                runOnUiThread {
                    btn_thread_start.text = "Demo-Thread starten"
                    Toast.makeText(this@MainActivity, "Demo Thread beendet!", Toast.LENGTH_LONG)
                        .show()
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
    
    private fun makeVisible() {
        imageView?.visibility = VISIBLE
        txtBandInfo?.visibility = VISIBLE
        txtBandName?.visibility = VISIBLE
    }

    private fun makeUnvisible() {
        imageView?.visibility = GONE
        txtBandInfo?.visibility = GONE
        txtBandName?.visibility = GONE
    }

    private fun chooseBand() {
        bandsViewModel.getBands()
        var builder = AlertDialog.Builder(this)
        val bandsToChoose: Array<String>? = bandsViewModel.getArrayListBands()
        builder.setTitle("Wählen Sie eine Band aus")
        builder?.setItems(bandsToChoose) { _, which ->
            bandsViewModel.getCurrentBand(bandsViewModel.bands?.value?.get(which)?.code)
        }
        builder.create()
        builder.show()
    }
}

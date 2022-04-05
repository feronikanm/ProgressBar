package com.ferodev.progressbar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import com.ferodev.progressbar.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    var isStarted = false
    var progressStatus = 0
    var handler: Handler? = null
    var secondaryHandler: Handler? = Handler()
    var primaryProgressStatus = 0
    var secondaryProgressStatus = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        handler = Handler(Handler.Callback {
            if (isStarted) {
                progressStatus++
            }
            binding.progressBarHorizontal.progress = progressStatus
            binding.textViewHorizontalProgress.text = "${progressStatus}/${binding.progressBarHorizontal.max}"
            handler?.sendEmptyMessageDelayed(0, 100)

            true
        })

        handler?.sendEmptyMessage(0)


        binding.btnProgressBarSecondary.setOnClickListener {
            primaryProgressStatus = 0
            secondaryProgressStatus = 0

            Thread(Runnable {
                while (primaryProgressStatus < 100) {
                    primaryProgressStatus += 1

                    try {
                        Thread.sleep(1000)
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }

                    startSecondaryProgress()
                    secondaryProgressStatus = 0

                    secondaryHandler?.post {
                        binding.progressBarSecondary.progress = primaryProgressStatus
                        binding.textViewPrimary.text = "Complete $primaryProgressStatus% of 100"

                        if (primaryProgressStatus == 100) {
                            binding.textViewPrimary.text = "All tasks completed"
                        }
                    }
                }
            }).start()
        }

    }

    fun startSecondaryProgress() {
        Thread(Runnable {
            while (secondaryProgressStatus < 100) {
                secondaryProgressStatus += 1

                try {
                    Thread.sleep(10)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }

                secondaryHandler?.post {
                    binding.progressBarSecondary.setSecondaryProgress(secondaryProgressStatus)
                    binding.textViewSecondary.setText("Current task progress\n$secondaryProgressStatus% of 100")

                    if (secondaryProgressStatus == 100) {
                        binding.textViewSecondary.setText("Single task complete.")
                    }
                }
            }
        }).start()
    }

    fun horizontalDeterminate(view: View) {
        isStarted = !isStarted
    }
}
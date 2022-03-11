package hr.fika.formulaone

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.work.WorkManager
import hr.fika.formulaone.databinding.ActivitySplashScreenBinding
import hr.fika.formulaone.framework.callDelayed
import hr.fika.formulaone.framework.isOnline
import hr.fika.formulaone.framework.startAnimation

private const val DELAY = 3000L
public const val DATA_LOADED = "hr.fika.formulaone.data_imported"

class SplashScreenActivity : AppCompatActivity() {


    private lateinit var binding: ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        startAnimation()
        redirect()
    }

    private fun redirect() {


        if (isOnline()) {

            val exampleWorkRequest = FormulaWorker.buildWorkRequest()
            WorkManager.getInstance(this).enqueue(exampleWorkRequest)
        } else {
            binding.tvSplashInfo.text = "No Connection"
            callDelayed(DELAY) { finish() }
        }


    }

    private fun startAnimation() {
        binding.ivSplashIcon.startAnimation(R.anim.slide)
        binding.ivLeftTyre.startAnimation(R.anim.spin)
        binding.ivRightTyre.startAnimation(R.anim.spin)

    }
}
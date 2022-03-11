package hr.fika.formulaone

import android.content.Context
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.Worker
import androidx.work.WorkerParameters
import hr.fika.formulaone.api.FormulaFetcher
import hr.fika.formulaone.framework.getBooleanPreference

class FormulaWorker(context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {


    companion object {


        fun buildWorkRequest(): OneTimeWorkRequest {
            return OneTimeWorkRequestBuilder<FormulaWorker>().build()
        }
    }

    override fun doWork(): Result {


        if (!applicationContext.getBooleanPreference(DATA_LOADED)) {
            FormulaFetcher(applicationContext).fetchItems()
        }

        FormulaFetcher(applicationContext).getDrivers()



        return Result.success()
    }


}
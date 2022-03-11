package hr.fika.formulaone

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import hr.fika.formulaone.framework.startActivity

class FormulaReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        context.startActivity<HostActivity>()
    }
}
package it.egolab.mensasava

import android.net.Uri
import android.os.Bundle
import android.util.TypedValue
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent

class MainActivity : AppCompatActivity() {
    private fun dp(v:Int)=TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,v.toFloat(),resources.displayMetrics).toInt()

    private fun open(url:String) {
        val colors = CustomTabColorSchemeParams.Builder()
            .setToolbarColor(0xFF075CA8.toInt())
            .setNavigationBarColor(0xFFF4F7FB.toInt())
            .build()

        val builder = CustomTabsIntent.Builder()
            .setDefaultColorSchemeParams(colors)
            .setShowTitle(true)

        // Partial Custom Tab: supported browsers show it as a panel over the app.
        builder.setInitialActivityHeightPx((resources.displayMetrics.heightPixels * 0.78).toInt(),
            CustomTabsIntent.ACTIVITY_HEIGHT_ADJUSTABLE)
        builder.setToolbarCornerRadiusDp(22)

        builder.build().launchUrl(this, Uri.parse(url))
    }

    override fun onCreate(savedInstanceState:Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<Button>(R.id.login).setOnClickListener { open("https://www.comune.sava.ta.it/mensascolastica") }
        findViewById<Button>(R.id.assenze).setOnClickListener { open("https://www.comune.sava.ta.it/mensascolastica_assenze") }
        findViewById<Button>(R.id.info).setOnClickListener { open("https://www.comune.sava.ta.it/mensascolastica_info/") }
        findViewById<Button>(R.id.dashboard).setOnClickListener { open("https://www.comune.sava.ta.it/mensascolastica") }
    }
}
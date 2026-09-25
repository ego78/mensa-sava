package it.egolab.mensasava

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent

class MainActivity : AppCompatActivity() {

    private fun open(url: String) {
        val uri = Uri.parse(url)
        try {
            val colors = CustomTabColorSchemeParams.Builder()
                .setToolbarColor(0xFF075CA8.toInt())
                .setNavigationBarColor(0xFFF4F7FB.toInt())
                .build()

            val builder = CustomTabsIntent.Builder()
                .setDefaultColorSchemeParams(colors)
                .setShowTitle(true)

            // Prova il pannello parziale. Se il browser non lo gestisce,
            // il Custom Tab viene comunque aperto normalmente.
            try {
                val h = (resources.displayMetrics.heightPixels * 0.78).toInt()
                builder.setInitialActivityHeightPx(h)
            } catch (_: Throwable) { }

            builder.build().launchUrl(this, uri)
        } catch (_: Throwable) {
            // Fallback di sicurezza: nessun pulsante deve chiudere l'app.
            try {
                startActivity(Intent(Intent.ACTION_VIEW, uri))
            } catch (_: ActivityNotFoundException) { }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.login).setOnClickListener {
            open("https://www.comune.sava.ta.it/mensascolastica")
        }
        findViewById<Button>(R.id.assenze).setOnClickListener {
            open("https://www.comune.sava.ta.it/mensascolastica_assenze")
        }
        findViewById<Button>(R.id.info).setOnClickListener {
            open("https://www.comune.sava.ta.it/mensascolastica_info/")
        }
        findViewById<Button>(R.id.dashboard).setOnClickListener {
            open("https://www.comune.sava.ta.it/mensascolastica")
        }
    }
}

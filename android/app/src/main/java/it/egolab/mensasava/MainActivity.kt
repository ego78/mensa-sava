package it.egolab.mensasava

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent

class MainActivity : AppCompatActivity() {
    private var authDialog: AlertDialog? = null
    private var authStarted = false

    private fun launchSecurePortal(url: String) {
        val uri = Uri.parse(url)
        try {
            val colors = CustomTabColorSchemeParams.Builder()
                .setToolbarColor(0xFF075CA8.toInt())
                .setNavigationBarColor(0xFFF4F7FB.toInt())
                .build()
            CustomTabsIntent.Builder()
                .setDefaultColorSchemeParams(colors)
                .setShowTitle(true)
                .build()
                .launchUrl(this, uri)
        } catch (_: Throwable) {
            startActivity(Intent(Intent.ACTION_VIEW, uri))
        }
    }

    private fun showAuthPanel() {
        authStarted = false
        val view = LayoutInflater.from(this).inflate(R.layout.dialog_auth, null)
        val dialog = AlertDialog.Builder(this)
            .setView(view)
            .setCancelable(true)
            .create()

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        view.findViewById<Button>(R.id.startAuth).setOnClickListener {
            authStarted = true
            launchSecurePortal("https://www.comune.sava.ta.it/mensascolastica")
        }
        view.findViewById<Button>(R.id.closeAuth).setOnClickListener {
            dialog.dismiss()
        }
        authDialog = dialog
        dialog.show()
    }

    private fun open(url: String) = launchSecurePortal(url)

    override fun onResume() {
        super.onResume()
        // Quando l'utente torna dal browser/CIE, il pannello resta sopra la Home
        // e mostra il comando esplicito per chiuderlo.
        if (authStarted && authDialog?.isShowing == true) {
            authDialog?.findViewById<android.widget.TextView>(R.id.authMessage)?.text =
                "Se hai completato l’autenticazione CIE/SPID, chiudi questa finestra per tornare a Mensa Sava."
            authDialog?.findViewById<Button>(R.id.closeAuth)?.isEnabled = true
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<Button>(R.id.login).setOnClickListener { showAuthPanel() }
        findViewById<Button>(R.id.assenze).setOnClickListener { open("https://www.comune.sava.ta.it/mensascolastica_assenze") }
        findViewById<Button>(R.id.info).setOnClickListener { open("https://www.comune.sava.ta.it/mensascolastica_info/") }
        findViewById<Button>(R.id.dashboard).setOnClickListener { open("https://www.comune.sava.ta.it/mensascolastica") }
    }
}
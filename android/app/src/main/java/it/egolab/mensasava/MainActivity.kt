package it.egolab.mensasava
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
class MainActivity:AppCompatActivity(){
 private fun open(u:String){CustomTabsIntent.Builder().setShowTitle(true).build().launchUrl(this,Uri.parse(u))}
 override fun onCreate(b:Bundle?){super.onCreate(b);setContentView(R.layout.activity_main)
 findViewById<Button>(R.id.login).setOnClickListener{open("https://www.comune.sava.ta.it/mensascolastica")}
 findViewById<Button>(R.id.assenze).setOnClickListener{open("https://www.comune.sava.ta.it/mensascolastica_assenze")}
 findViewById<Button>(R.id.info).setOnClickListener{open("https://www.comune.sava.ta.it/mensascolastica_info/")}
 findViewById<Button>(R.id.dashboard).setOnClickListener{open("https://www.comune.sava.ta.it/mensascolastica")}
 }}

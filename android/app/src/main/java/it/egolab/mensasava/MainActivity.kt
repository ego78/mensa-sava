package it.egolab.mensasava
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Window
import android.webkit.*
import android.widget.Button
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
class MainActivity : AppCompatActivity() {
 private var authDialog: Dialog?=null; private var authWebView: WebView?=null
 private fun openExternal(uri:Uri)=try{startActivity(Intent(Intent.ACTION_VIEW,uri).addCategory(Intent.CATEGORY_BROWSABLE));true}catch(_:ActivityNotFoundException){false}
 @SuppressLint("SetJavaScriptEnabled") private fun showWebAuth(url:String){
  val dialog=Dialog(this);dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);dialog.setContentView(R.layout.dialog_web_auth)
  val web=dialog.findViewById<WebView>(R.id.authWebView);val progress=dialog.findViewById<ProgressBar>(R.id.webProgress)
  CookieManager.getInstance().setAcceptCookie(true);CookieManager.getInstance().setAcceptThirdPartyCookies(web,true)
  web.settings.javaScriptEnabled=true;web.settings.domStorageEnabled=true;web.settings.databaseEnabled=true;web.settings.loadsImagesAutomatically=true;web.settings.javaScriptCanOpenWindowsAutomatically=true;web.settings.setSupportMultipleWindows(false);web.settings.userAgentString=WebSettings.getDefaultUserAgent(this);web.settings.useWideViewPort=true;web.settings.loadWithOverviewMode=true;web.settings.builtInZoomControls=true;web.settings.displayZoomControls=false;web.settings.textZoom=85
  web.webChromeClient=object:WebChromeClient(){override fun onProgressChanged(v:WebView?,p:Int){progress.progress=p;progress.visibility=if(p>=100) android.view.View.GONE else android.view.View.VISIBLE}}
  web.webViewClient=object:WebViewClient(){override fun shouldOverrideUrlLoading(v:WebView?,r:WebResourceRequest?):Boolean{val u=r?.url?:return false;val s=u.scheme?.lowercase()?:return false;if(s=="https"||s=="http")return false;if(s=="intent")return try{startActivity(Intent.parseUri(u.toString(),Intent.URI_INTENT_SCHEME));true}catch(_:Throwable){true};return openExternal(u)};override fun onPageFinished(v:WebView?,u:String?){super.onPageFinished(v,u);CookieManager.getInstance().flush()}}
  dialog.findViewById<Button>(R.id.closeWeb).setOnClickListener{web.stopLoading();dialog.dismiss()};dialog.setOnDismissListener{authWebView?.destroy();authWebView=null;authDialog=null};dialog.window?.setBackgroundDrawableResource(android.R.color.transparent);dialog.window?.setDimAmount(.32f);dialog.window?.addFlags(android.view.WindowManager.LayoutParams.FLAG_DIM_BEHIND);dialog.show();dialog.window?.setLayout((resources.displayMetrics.widthPixels*.94).toInt(),(resources.displayMetrics.heightPixels*.88).toInt());authDialog=dialog;authWebView=web;web.loadUrl(url)
 }
 override fun onResume(){super.onResume();authWebView?.onResume()};override fun onPause(){authWebView?.onPause();super.onPause()}
 @Deprecated("Deprecated in Java") override fun onBackPressed(){val w=authWebView;if(authDialog?.isShowing==true&&w!=null&&w.canGoBack())w.goBack() else if(authDialog?.isShowing==true)authDialog?.dismiss() else super.onBackPressed()}
 override fun onCreate(savedInstanceState:Bundle?){super.onCreate(savedInstanceState);setContentView(R.layout.activity_main);findViewById<android.widget.TextView>(R.id.heroAnim).apply{alpha=0f;translationX=45f;animate().alpha(1f).translationX(0f).setDuration(700).start()}
  fun go(b:Button,u:String){b.setOnClickListener{it.animate().scaleX(.97f).scaleY(.97f).setDuration(70).withEndAction{it.animate().scaleX(1f).scaleY(1f).setDuration(120).start();showWebAuth(u)}.start()}}
  go(findViewById(R.id.login),"https://www.comune.sava.ta.it/mensascolastica");go(findViewById(R.id.info),"https://www.comune.sava.ta.it/mensascolastica_info/");go(findViewById(R.id.recharge),"https://www.comune.sava.ta.it/pagamentiDovutiCittadino/259");go(findViewById(R.id.diets),"https://www.comune.sava.ta.it/mensascolastica_dietespeciali");go(findViewById(R.id.assenze),"https://www.comune.sava.ta.it/mensascolastica_assenze");go(findViewById(R.id.dashboard),"https://www.comune.sava.ta.it/mensascolastica")
 }
}

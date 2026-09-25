if('serviceWorker' in navigator){window.addEventListener('load',()=>navigator.serviceWorker.register('./sw.js'))}
let deferredPrompt;const btn=document.getElementById('installBtn');
window.addEventListener('beforeinstallprompt',e=>{e.preventDefault();deferredPrompt=e;btn.hidden=false});
btn.addEventListener('click',async()=>{if(!deferredPrompt)return;deferredPrompt.prompt();await deferredPrompt.userChoice;deferredPrompt=null;btn.hidden=true});
const isiOS=/iphone|ipad|ipod/i.test(navigator.userAgent);const standalone=window.navigator.standalone;
if(isiOS&&!standalone)document.getElementById('iosHelp').hidden=false;
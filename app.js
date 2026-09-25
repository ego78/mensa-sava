if('serviceWorker'in navigator)addEventListener('load',()=>navigator.serviceWorker.register('./sw.js'));
const login=document.querySelector('#loginBtn'), box=document.querySelector('#returnBox'), ret=document.querySelector('#returnMenu');
let authWindow=null;
login.addEventListener('click',()=>{
  localStorage.setItem('mensaLoginStarted','1');
  box.hidden=false;
  authWindow=window.open('https://www.comune.sava.ta.it/mensascolastica','mensaLogin','popup=yes,width=520,height=760');
  if(!authWindow){ location.href='https://www.comune.sava.ta.it/mensascolastica'; }
});
ret.addEventListener('click',()=>{box.hidden=true;document.querySelector('#menu').scrollIntoView({behavior:'smooth'});});
function resumed(){
 if(localStorage.getItem('mensaLoginStarted')==='1') box.hidden=false;
}
document.addEventListener('visibilitychange',()=>{if(!document.hidden)resumed()});
addEventListener('pageshow',resumed);
resumed();
if('serviceWorker'in navigator)addEventListener('load',()=>navigator.serviceWorker.register('./sw.js'));
const modal=document.querySelector('#modal'),frame=document.querySelector('#loginFrame');
const URL='https://www.comune.sava.ta.it/mensascolastica';
function openModal(){modal.hidden=false;document.body.style.overflow='hidden';frame.src=URL}
function closeModal(){modal.hidden=true;document.body.style.overflow='';frame.src='about:blank'}
document.querySelector('#openLogin').onclick=openModal;
document.querySelector('#x').onclick=closeModal;
document.querySelector('#done').onclick=closeModal;
modal.addEventListener('click',e=>{if(e.target===modal)closeModal()});

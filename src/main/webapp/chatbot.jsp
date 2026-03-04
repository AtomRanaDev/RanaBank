<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<style>
.bot-msg{
  background:#334155;
  color:#e2e8f0;
  margin:6px 0;
  padding:10px 14px;
  border-radius:14px;
  max-width:80%;
  width:fit-content;
  font-size:14px;
}
.user-msg{
  background:#0ea5e9;
  color:white;
  margin:6px 0;
  margin-left:auto;
  padding:10px 14px;
  border-radius:14px;
  max-width:80%;
  width:fit-content;
  font-size:14px;
}
</style>

<!-- Floating Chat Icon -->

<div id="chatbot-icon"
     onclick="toggleChat()"
     style="position:fixed;bottom:25px;right:25px;
            background:#0ea5e9;color:white;
            border-radius:50%;width:60px;height:60px;
            display:flex;align-items:center;justify-content:center;
            cursor:pointer;box-shadow:0 4px 12px rgba(0,0,0,0.3);
            z-index:9999;">
<i class="fa-solid fa-comments fa-lg"></i>
</div>

<!-- Chat Window -->

<div id="chatbot-container"
     style="position:fixed;bottom:95px;right:25px;
            width:360px;height:450px;
            background:#1e293b;
            border-radius:16px;
            box-shadow:0 8px 20px rgba(0,0,0,0.35);
            display:none;flex-direction:column;
            overflow:hidden;z-index:9999;">

<!-- Header -->

<div style="background:#0ea5e9;color:white;padding:15px;font-weight:600;">
<i class="fa-solid fa-robot me-2"></i> RanaBank Assistant
<button onclick="toggleChat()"
        style="float:right;border:none;background:none;color:white;">
<i class="fa-solid fa-xmark"></i>
</button>
</div>

<!-- Chat Body -->

<div id="chat-body"
     style="flex:1;padding:12px;overflow-y:auto;background:#0f172a;">
</div>

<!-- Input -->

<div style="padding:10px;border-top:1px solid #334155;background:#1e293b;">
<div style="display:flex;gap:8px;">

<input id="userInput"
    type="text"
    placeholder="Type a message..."
    style="flex:1;border-radius:20px;padding:8px 12px;
           background:#334155;color:white;
           border:1px solid #475569;outline:none;">

<button onclick="sendMessage()"
     style="border:none;border-radius:20px;
            padding:8px 14px;background:#0ea5e9;color:white;"> <i class="fa-solid fa-paper-plane"></i> </button>

</div>
</div>
</div>

<script>
(function(){

const chatBody=document.getElementById("chat-body");
const chatContainer=document.getElementById("chatbot-container");

/* Load previous chat */
loadChatHistory();

window.toggleChat=function(){
chatContainer.style.display=
chatContainer.style.display==="flex"?"none":"flex";
}

function appendMessage(sender,text){

const msg=document.createElement("div");
msg.className=sender==="bot"?"bot-msg":"user-msg";
msg.innerHTML=text;

chatBody.appendChild(msg);
chatBody.scrollTop=chatBody.scrollHeight;

saveChatHistory();
}

window.sendMessage=async function(){

const input=document.getElementById("userInput");
const text=input.value.trim();

if(!text)return;

appendMessage("user",text);
input.value="";

const thinking=document.createElement("div");
thinking.className="bot-msg";
thinking.innerHTML="<i>Thinking...</i>";

chatBody.appendChild(thinking);
chatBody.scrollTop=chatBody.scrollHeight;

try{

const res = await fetch("ChatServlet", {
    method: "POST",
    credentials: "same-origin",
    headers: {
        "Content-Type": "application/x-www-form-urlencoded"
    },
    body: new URLSearchParams({ message: text })
});

const data=await res.json();

chatBody.removeChild(thinking);

appendMessage("bot",data.reply);

}catch(err){

chatBody.removeChild(thinking);

appendMessage("bot","⚠ Unable to connect to assistant.");

}
}

/* Save chat */
function saveChatHistory(){
localStorage.setItem("ranaBankChat",chatBody.innerHTML);
}

/* Load chat */
function loadChatHistory(){

const history=localStorage.getItem("ranaBankChat");

if(history){
chatBody.innerHTML=history;
}else{

appendMessage("bot",
"👋 Hello! I'm your RanaBank assistant.<br><small>Try: balance, deposit 500, withdraw 200</small>"
);

}

}

/* Optional clear chat */
window.clearChat=function(){
localStorage.removeItem("ranaBankChat");
chatBody.innerHTML="";
}

})();
</script>

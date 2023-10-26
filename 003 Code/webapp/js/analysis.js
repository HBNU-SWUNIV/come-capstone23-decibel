
const audioContext = new (window.AudioContext || window.webkitAudioContext)(); 
// const mediaNode = new MediaElementAudioSourceNode(audioContext, {mediaElement: audio});



////////// PlayList 저장
var mp3NameVal = ["Attention.mp3", "Cant_Get_Over_You.mp3", "Dont_Wanna_Know.mp3", 
"kick_back.mp3","Merry_Christmas_Mr_Lawrence.mp3", "Mr_Hollywood.mp3", "Nice_Boys.mp3",
"Ocean_Man.mp3", "Steal_The_Show.mp3", "TEST_DRIVE.mp3", "Tick_Tock.mp3", "나를_슬프게_하는_사람들.mp3"]

var mp3Num = 4
var audio = new Audio(mp3NameVal[mp3Num])

const mediaSource = audioContext.createMediaElementSource(audio);
const gainNode = audioContext.createGain();
var average = 0;
var count = 0;
var canvas = document.getElementById('canvas');
var canvasCtx = canvas.getContext('2d');


var WIDTH = canvas.width;
var HEIGHT = canvas.height;


const analyser = audioContext.createAnalyser();
analyser.fftSize = 32768; // 고속 푸리에 변환을 할 때 얼마나 많은 샘플을 샘플링할지를 결정함, 값이 높을 수록 주파수 영역을 자세히 표현할 수 있음.

var bufferLength = analyser.frequencyBinCount;
var dataArray = new Uint8Array(bufferLength);

canvasCtx.clearRect(0, 0, WIDTH, HEIGHT);

const button = document.getElementById("btn_play");     

mediaSource.connect(gainNode).connect(analyser).connect(audioContext.destination);

     document.getElementById("btn_play").onclick = (event) => {
      if(button.innerText != "재생") button.innerText = "재생";
      else button.innerText = "정지";
      
      if(audio.paused) {
          audioContext.resume();
          audio.play();
          MusicHandler();
      }
      else {
        audio.pause();
      }
     }    


function VolumeValue() { // 볼륨값을 합하여 평균값을 계산함
  
  let total = 0.0;

  for(var i = 0; i<bufferLength; i+=1){ // fftsize의 절반, 주파수의 빈도의 길이만큼 반복.
    total += dataArray[i];
  }

  var value = total / bufferLength;

  if(value != 0 && dataArray[0] != 0){
    average += value;
    count+=1;
  }
  
  return value;
}

function Draw() {
  

  analyser.getByteFrequencyData(dataArray);

  canvasCtx.fillStyle = 'rgb(0, 0, 0)';
  canvasCtx.fillRect(0, 0, WIDTH, HEIGHT);

  var barWidth = (WIDTH / bufferLength) -1;
  document.getElementById('time').innerText = Math.floor(audio.currentTime)
  
  
  var barHeight;
  var x = 0;
  
  for(var i = 0; i < bufferLength; i++) {
    barHeight = dataArray[i];

     canvasCtx.fillStyle = 'rgb(' + (barHeight+80) + ',50,50)';
    canvasCtx.fillRect(x,(HEIGHT-barHeight/2)-(HEIGHT/2),barWidth,barHeight);
    x += barWidth + 1;
  }
};


Draw();

function SetVolume(){
  var setter = 0.0;
  if(count != 0 ){
    setter = average / count * 0.05;
    gainNode.gain.value = Math.log(setter);
    
  }
}

function MusicHandler(){
  var musicHandler = requestAnimationFrame(MusicHandler);

  if(audio.ended){
    button.innerText = "재생";
  }
  
  Draw();
  VolumeValue();
  SetVolume();
}

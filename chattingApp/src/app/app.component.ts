import { Component } from '@angular/core';
import Stomp from 'stompjs';
import SockJS from 'sockjs-client';
import $ from 'jquery';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  public serverUrl = 'http://localhost:9026/socket';
  private title = 'WebSockets chat';
  public stompClient;

  constructor(){
    this.initializeWebSocketConnection();
  }

  initializeWebSocketConnection(){
    const ws = new SockJS(this.serverUrl);
    this.stompClient = Stomp.over(ws);
    const that = this;
    this.stompClient.connect({}, function(frame) {
      that.stompClient.subscribe('/chat', (message) => {
        if(message.body) {
          $('.chat').append('<div class=\'sendMessage\'>'+message.body+'</div>')
          console.log(message.body);
        }
      });
    });
  }

  sendMessage(message){
    this.stompClient.send("/app/send/message" , {}, message);
    $('#input').val('');
  }
}
package com.lostad.app.demo.imcloud;

import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;
import com.avos.avoscloud.im.v2.AVIMTypedMessageHandler;

import org.greenrobot.eventbus.EventBus;

import com.lostad.app.demo.event.ImTypeMessageEvent;

public class MessageHandler extends AVIMTypedMessageHandler<AVIMTypedMessage> {

    public MessageHandler(){

    }

    @Override
    public void onMessage(AVIMTypedMessage message, AVIMConversation conversation, AVIMClient client){

        String clientID = "";

        try{
            clientID = AVImClientManager.getInstance().getClientId();
            if(client.getClientId().equals(clientID)){

                //过滤自己发的消息
                if(!message.getFrom().equals(clientID)){

                    sendEvent(message, conversation);
                }
            }else {
                client.close(null);
            }
        }catch(IllegalStateException e){
            client.close(null);
            e.printStackTrace();
        }
    }

    private void sendEvent(AVIMTypedMessage message, AVIMConversation conversation) {
        ImTypeMessageEvent event = new ImTypeMessageEvent();
        event.message = message;
        event.conversation = conversation;
        EventBus.getDefault().post(event);
    }
}

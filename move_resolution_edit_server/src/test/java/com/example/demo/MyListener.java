package com.example.demo;

import javax.jms.*;
public class MyListener implements MessageListener {

    public void onMessage(Message m) {
        try{
            TextMessage msg=(TextMessage)m;
            //System.out.println("following message is received:"+msg.getText());
            String emssage = msg.getText();
            Operator.compress_video(emssage);
        }catch(JMSException e){System.out.println(e);}
    }
}
package com.example.unorthobox;

import android.content.Context;
import android.widget.Toast;

import org.eclipse.paho.mqttv5.client.MqttAsyncClient;
import org.eclipse.paho.mqttv5.client.MqttConnectionOptions;
import org.eclipse.paho.mqttv5.client.IMqttToken;
import org.eclipse.paho.mqttv5.client.persist.MemoryPersistence;
import org.eclipse.paho.mqttv5.common.MqttException;
import org.eclipse.paho.mqttv5.common.MqttMessage;


public class MqttConnection {
    private static MqttConnection instance = null;
    private static MqttAsyncClient userClient;
    private static final int qos             = 1;
    private static final String broker       = "tcp://broker.emqx.io:1883";
    private static final String clientId     = "AndroidApp";
    private static final MemoryPersistence persistence = new MemoryPersistence();

    //Singleton design pattern
    private MqttConnection() {}

    /**
     * Get an instance of the Mqtt Connection Object
     * @param context The application context. This is to print exception errors in the app's toast.
     * @return An instance of a Mqtt Connection Object
     */
    public static MqttConnection getInstance(Context context){
        if(instance == null){
            instance = new MqttConnection();
            instance.connect(context);
        }
        return MqttConnection.instance;
    }

    /**
     * Connects to the Mqtt client
     * @param context The application context. This is to print exception errors in the app's toast.
     * After reconsideration, this method is now made private, so idk if this comment is still useful
     */
    private void connect(Context context){
        try{
            MqttConnectionOptions connOpts = new MqttConnectionOptions();
            connOpts.setCleanStart(false);
            userClient = new MqttAsyncClient(broker, clientId, persistence);
//            System.out.println("Connecting to broker: " + broker);
            IMqttToken token = userClient.connect(connOpts);
            token.waitForCompletion(); //Pauses the app until the user is connected. Might be bad.
//            System.out.println("Connected");
        } catch(MqttException me){
            exceptionMessage(me, context);
        }
    }

    /**
     * Publishes a message to m=Mqtt
     * @param topic Which "channel" we publish to in the broker
     * @param content Contents of what we publish... the message
     * @param context The application context. This is to print exception errors in the app's toast.
     */
    public void publish(String topic, String content, Context context){
        try{
//            System.out.println("Publishing message: "+content);
            MqttMessage message = new MqttMessage(content.getBytes());
            message.setQos(qos);
            IMqttToken token = userClient.publish(topic, message);
//            token.waitForCompletion(); //Don't spam the button thx
        } catch(MqttException me){
            exceptionMessage(me, context);
        }
    }

    public void close(Context context){
        try{
//            System.out.println("Disconnected");
//            System.out.println("Close client.");
            userClient.close();
        } catch(MqttException me){
            exceptionMessage(me, context);
        }
    }

    //Prints the exceptionMessage to the app
    private static void exceptionMessage(MqttException me, Context context){
        CharSequence text = "reason "+me.getReasonCode();
        text += " | msg "+me.getMessage();
        text += " | loc "+me.getLocalizedMessage();
        text += " | cause "+me.getCause();
        text += " | excep "+me;

        int duration = Toast.LENGTH_LONG;
        Toast.makeText(context, text, duration).show();
    }
}

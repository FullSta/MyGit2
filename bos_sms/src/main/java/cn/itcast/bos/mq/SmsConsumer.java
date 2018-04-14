package cn.itcast.bos.mq;

import cn.itcast.bos.utils.SmsUtils;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import org.springframework.stereotype.Service;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

@Service("smsConsumer")
public class SmsConsumer implements MessageListener {

    @Override
    public void onMessage(Message message) {
        MapMessage mapMessage = (MapMessage)message;
        // 调用sms服务发送短信
        SendSmsResponse responce;
        try {
            responce = SmsUtils.sendSms(mapMessage.getString("telephone"),
                    mapMessage.getString("checkcode"));
            if(responce.getCode().equals("OK")){
                // 发送成功
            }else {
                // 发送失败
                throw new RuntimeException("短信发送失败,信息码:"+responce.getCode());
            }
            System.out.println(mapMessage.getString("telephone")+mapMessage.getString("checkcode")) ;
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

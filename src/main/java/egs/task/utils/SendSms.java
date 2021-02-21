package egs.task.utils;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SendSms {

    private static String twilioNumber;
    private static String accountSid;
    private static String authId;

    @Value("${twilio.number}")
    public void setTwilioNumber(String twilioNumber) {
       SendSms.twilioNumber = twilioNumber;
    }

    @Value("${twilio.account.sid}")
    public void setAccountSid(String privateAccountSid) {
        SendSms.accountSid = privateAccountSid;
    }

    @Value("${twilio.auth.id}")
    public void setAuthId(String privateAuthId) {
        SendSms.authId = privateAuthId;
    }

    public static void sendCodeToPhone(String toNumber, String code) {
        Twilio.init(accountSid, authId);
        Message.creator(new PhoneNumber(toNumber), new PhoneNumber(twilioNumber),
                code).create();
    }
}
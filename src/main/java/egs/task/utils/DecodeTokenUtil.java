package egs.task.utils;

import org.apache.commons.codec.binary.Base64;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class DecodeTokenUtil {
    public static String getRoleFromToken(String jwtToken) {
        //Decode JW
        String[] split_string = jwtToken.split("\\.");
        String base64EncodedHeader = split_string[0];
        String base64EncodedBody = split_string[1];
        String base64EncodedSignature = split_string[2];

        //JWT Header
        Base64 base64Url = new Base64(true);
        String header = new String(base64Url.decode(base64EncodedHeader));

        //JWT Body
        String body = new String(base64Url.decode(base64EncodedBody));

        //Get role from body
        String role = null;
        try {
            role = (String) new JSONObject(body).get("scopes");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return role;
    }
}

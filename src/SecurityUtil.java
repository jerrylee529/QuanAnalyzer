
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;  
import java.security.NoSuchAlgorithmException;  
import java.util.Arrays;  

import net.sf.json.JSONObject;



public class SecurityUtil {
	// 与接口配置信息中的Token要一致  
    private static String token = "jerrylee529";  
    
    // 测试appid 和 appkey
    //private static String appid = "wxddaf179c565fc876";
    
    //private static String appkey = "075002f92c3a0ce854b65bd4d56b58c9";
  
    // 公众号 appid 和 appkey
    private static String appid = "wx9ac14d5576687eee";
    
    private static String appkey = "6b325d0ce852df7b067bf06a6518d168";
    
    /** 
     * 验证签名 
     *  
     * @param signature 
     * @param timestamp 
     * @param nonce 
     * @return 
     */  
    public static boolean checkSignature(String signature, String timestamp,  
            String nonce) {  
        String[] arr = new String[] { token, timestamp, nonce };  
        // 将token、timestamp、nonce三个参数进行字典序排序  
        Arrays.sort(arr);  
        StringBuilder content = new StringBuilder();  
        for (int i = 0; i < arr.length; i++) {  
            content.append(arr[i]);  
        }  
        MessageDigest md = null;  
        String tmpStr = null;  
  
        try {  
            md = MessageDigest.getInstance("SHA-1");  
            // 将三个参数字符串拼接成一个字符串进行sha1加密  
            byte[] digest = md.digest(content.toString().getBytes());  
            tmpStr = byteToStr(digest);  
        } catch (NoSuchAlgorithmException e) {  
            e.printStackTrace();  
        }  
  
        content = null;  
        // 将sha1加密后的字符串可与signature对比，标识该请求来源于微信  
        return tmpStr != null ? tmpStr.equals(signature.toUpperCase()) : false;  
    }  
    
    /**

     * 获得ACCESS_TOKEN

     * 

     * @Title: getAccess_token

     * @Description: 获得ACCESS_TOKEN

     * @param @return 设定文件

     * @return String 返回类型

     * @throws

     */

    public static AccessToken getAccess_token() {

        String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="

                + appid + "&secret=" + appkey;

        AccessToken accessToken = null;

        try {

            URL urlGet = new URL(url);

            HttpURLConnection http = (HttpURLConnection)urlGet.openConnection();

            http.setRequestMethod("GET"); // 必须是get方式请求

            http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            http.setDoOutput(true);

            http.setDoInput(true);

            System.setProperty("sun.net.client.defaultConnectTimeout", "30000");// 连接超时30秒

            System.setProperty("sun.net.client.defaultReadTimeout", "30000"); // 读取超时30秒

            http.connect();

            InputStream is = http.getInputStream();

            int size = is.available();

            byte[] jsonBytes = new byte[size];

            is.read(jsonBytes);

            String message = new String(jsonBytes, "UTF-8");

            JSONObject demoJson = JSONObject.fromObject(message); //new JSONObject(message);
            
            accessToken = new AccessToken();

            accessToken.setAccess_token(demoJson.getString("access_token"));
            accessToken.setExpire_in(demoJson.getInt("expires_in"));

            System.out.println(accessToken);

            is.close();

        } catch (Exception e) {

            e.printStackTrace();

        }

        return accessToken;
    }
  
    /** 
     * 将字节数组转换为十六进制字符串 
     *  
     * @param byteArray 
     * @return 
     */  
    private static String byteToStr(byte[] byteArray) {  
        String strDigest = "";  
        for (int i = 0; i < byteArray.length; i++) {  
            strDigest += byteToHexStr(byteArray[i]);  
        }  
        return strDigest;  
    }  
  
    /** 
     * 将字节转换为十六进制字符串 
     *  
     * @param mByte 
     * @return 
     */  
    private static String byteToHexStr(byte mByte) {  
        char[] Digit = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A',  
                'B', 'C', 'D', 'E', 'F' };  
        char[] tempArr = new char[2];  
        tempArr[0] = Digit[(mByte >>> 4) & 0X0F];  
        tempArr[1] = Digit[mByte & 0X0F];  
  
        String s = new String(tempArr);  
        return s;  
    }  
}

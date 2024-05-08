package com.example.spb.utils;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;

public class SendSMSUtil {
    /**
     * 你的accessKeyId
     */
    private static final String accessKeyId="LTAI5tH1TcTYoPwN6evfQbdr";
    /**
     * 你的accessKeySecret
     */
    private static final String accessKeySecret="UryngwMag3xQiRlvvty3TUeIhgWigt";
    /**
     * 签名
     */
    private static final String signName="智慧党建414";
    /**
     * 短信模板
     */
    private static final String templateCode="SMS_465891060";
    /**
     * 验证码
     */
    private static int code;

    /**
     * @Descirption:发送手机验证码
     * @param phoneNumber：需要发送的手机号码
     * @return OK表示成功，失败则返回失败消息
     */
    public static String sendSmsUtil(String phoneNumber){
        //设置超时时间-可自行调整
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");

        // 初始化ascClient需要的几个参数

        // 短信API产品名称（短信产品名固定，无需修改）
        final String product = "Dysmsapi";
        // 短信API产品域名（接口地址固定，无需修改）
        final String domain = "dysmsapi.aliyuncs.com";

        // 初始化ascClient,暂时不支持多region（请勿修改）
        IClientProfile profile= DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
        try {
            DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
        } catch (ClientException e) {
            e.printStackTrace();
        }
        IAcsClient acsClient=new DefaultAcsClient(profile);

        // 组装请求对象
        SendSmsRequest request=new SendSmsRequest();
        //使用post提交
        request.setMethod(MethodType.POST);

        // 必填:待发送手机号。支持以逗号分隔的形式进行批量调用，批量上限为1000个手机号码,批量调用相对于单条调用及时性稍有延迟,
        // 验证码类型的短信推荐使用单条调用的方式；发送国际/港澳台消息时，接收号码格式为国际区号+号码，如“85200000000”
        request.setPhoneNumbers(phoneNumber);
        request.setSignName(signName);
        // 必填:短信模板-可在短信控制台中找到，发送国际/港澳台消息时，请使用国际/港澳台短信模版
        request.setTemplateCode(templateCode);

        //随机生成6位验证码
        code = (int) ((Math.random() * 9 + 1) * 100000);

        // 可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
        // 友情提示:如果JSON中需要带换行符,请参照标准的JSON协议对换行符的要求,比如短信内容中包含\r\n的情况在JSON中需要表示成\\r\\n,否则会导致JSON在服务端解析失败
        request.setTemplateParam("{code:"+code+"}");

        // 请求失败这里会抛ClientException异常
        SendSmsResponse sendSmsResponse = null;

        try {
            sendSmsResponse = acsClient.getAcsResponse(request);
        } catch (ClientException e) {
            e.printStackTrace();
            return "请求失败";
        }

        assert sendSmsResponse.getCode() != null;
        // 发送不成功
        if (sendSmsResponse.getCode() == null || !sendSmsResponse.getCode().equals("OK")) {
            return sendSmsResponse.getMessage();
        }

        // 请求成功
        return "OK";
    }

    public static int getCode(){
        return code;
    }
}

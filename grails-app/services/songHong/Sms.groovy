package songHong

class Sms {
    public void execute() throws Exception{
        StringBuilder sb = new StringBuilder();
        sb.append("accountSid").append("=").append(Config.ACCOUNT_SID);
        sb.append("&to").append("=").append("13529134722");
        sb.append("&param").append("=").append(URLEncoder.encode("","UTF-8"));
		sb.append("&smsContent").append("=").append( URLEncoder.encode("【秒嘀科技】您的验证码为6156164，该验证码5分钟内有效。请勿泄漏于他人。","UTF-8"));
        String body = sb.toString() + HttpUtil.createCommonParam(Config.ACCOUNT_SID, Config.AUTH_TOKEN);
        String result = HttpUtil.post(Config.BASE_URL, body);
        System.out.println(result);
    }
    public static void main(String[] args) {
        Sms am = new Sms();
        try {
            am.execute();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

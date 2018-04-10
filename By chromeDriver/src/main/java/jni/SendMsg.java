package jni;

public class SendMsg {

    public native void getMsg(String msg);
    public native String returnMsg();
    SendMsg sendmsg = new SendMsg();

    static {
        System.load("main.so");
    }

    /**
     * 发送消息
     * @param msg
     */
    public void SendMsgToC(String msg){
        sendmsg.getMsg(msg);
    }

    /**
     * 发送消息并获取用户的输入
     * @return code 用户输入的验证码
     */
    public String ReceiveMsgFromC(){
        String code = sendmsg.returnMsg();
        return code;
    }


}

package cn.xa87.constant;

public class SmsConstant {
    public enum Sms_Type {
        /**
         * 注册REGISTER
         * 重置密码
         * 重置支付密码
         * 绑定手机或者邮箱
         * 找回密码
         * 提币
         * <p>
         * 提现
         */
        REGISTER("REGISTER"),
        RESET("RESET"),
        PAYRESET("PAYRESET"),
        SETPHMAIL("SETPHMAIL"),
        RETRIEVE("RETRIEVE"),
        WITHDRAWAL_MONEY("WITHDRAWAL_MONEY"),
        OTCWECHAT("OTCWECHAT"),
        OTCALIAY("OTCALIAY"),
        OTCBANK("OTCBANK"),
        WITHDRAWAL_COIN("WITHDRAWAL_COIN");
        private String smsType;


        Sms_Type(String smsType) {
            this.smsType = smsType;
        }

        public String getSmsType() {
            return smsType;
        }

        public void setSmsType(String smsType) {
            this.smsType = smsType;
        }

        @Override
        public String toString() {
            return smsType;
        }
    }
}

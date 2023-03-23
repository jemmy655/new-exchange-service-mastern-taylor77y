package cn.xa87.constant;

public class MemConstant {
    public enum Register_Type {
        MAIL("MAIL"),
        PHONE("PHONE"),

        ACCOUNT("ACCOUNT");
        private String regType;

        Register_Type(String regType) {
            this.regType = regType;
        }

        public String getRegType() {
            return regType;
        }

        public void setRegType(String regType) {
            this.regType = regType;
        }

        @Override
        public String toString() {
            return regType;
        }
    }

    public enum Card_Sate {
        WAIT("WAIT"),
        PASS("PASS"),
        NO("NO"),
        REFUSE("REFUSE");
        private String state;

        Card_Sate(String state) {
            this.state = state;
        }

        public String getState() {
            return state;
        }

        public void setCardState(String state) {
            this.state = state;
        }

        @Override
        public String toString() {
            return state;
        }
    }

    public enum Fb_Status {
        NORMAL("NORMAL"),
        UNNORMAL("UNNORMAL");

        private String fbStatus;

        Fb_Status(String fbStatus) {
            this.fbStatus = fbStatus;
        }

        public String getFbStatus() {
            return fbStatus;
        }

        public void setFbStatus(String fbStatus) {
            this.fbStatus = fbStatus;
        }

        @Override
        public String toString() {
            return fbStatus;
        }
    }
}
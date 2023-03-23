package cn.xa87.constant;

public class ImConstant {
    public enum Read_Status {
        READ("READ"),
        UNREAD("UNREAD");

        private String readStatus;

        Read_Status(String readStatus) {
            this.readStatus = readStatus;
        }

        public String getReadStatus() {
            return readStatus;
        }

        public void setReadStatus(String readStatus) {
            this.readStatus = readStatus;
        }

        @Override
        public String toString() {
            return readStatus;
        }
    }
}

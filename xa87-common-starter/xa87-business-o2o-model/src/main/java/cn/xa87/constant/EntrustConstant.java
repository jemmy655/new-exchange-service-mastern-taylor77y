package cn.xa87.constant;

public class EntrustConstant {
    public enum Entrust_Type {
        BUY("BUY"),
        SELL("SELL");
        private String type;


        Entrust_Type(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        @Override
        public String toString() {
            return type;
        }
    }

    public enum Method_Type {
        ACTIVE("ACTIVE"),
        PASSIVE("PASSIVE");
        private String type;


        Method_Type(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        @Override
        public String toString() {
            return type;
        }
    }

    public enum Order_State {
        CREATE("CREATE"),
        MATCH("MATCH"),
        CLOSE("CLOSE"),
        FINAL("FINAL");
        private String state;


        Order_State(String state) {
            this.state = state;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        @Override
        public String toString() {
            return state;
        }
    }
}

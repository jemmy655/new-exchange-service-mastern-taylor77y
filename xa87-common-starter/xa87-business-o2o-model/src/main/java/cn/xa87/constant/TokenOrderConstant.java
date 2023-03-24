package cn.xa87.constant;

public class TokenOrderConstant {
    public enum Order_State {
        CREATE("CREATE"), //创建
        MATCH("MATCH"),   //匹配
        CANCEL("CANCEL"),   //取消
        FINAL("FINAL");     //结束
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

    public enum Order_Type {
        POSITIONS("POSITIONS"),//持仓
        ORDERS("ORDERS"),//委托
        PROFITLOSS("PROFITLOSS");//止盈止损
        private String type;


        Order_Type(String type) {
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

    public enum Match_Type {
        TRIGGER("TRIGGER"),//止盈
        MATCH("MATCH"),//止盈
        ORD("ORD");//止损
        private String type;


        Match_Type(String type) {
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

    public enum Close_Type {
        BURST("BURST"),//爆仓
        MATCH("MATCH");//手动
        private String type;


        Close_Type(String type) {
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
}

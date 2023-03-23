package cn.xa87.constant;

public class BalanceConstant {
    public enum Move_Type {
        //比比到合约
        BALANCE_MOVE_TOKEN("BALANCE_MOVE_TOKEN"),
        //合约到比比
        TOKEN_MOVE_BALANCE("TOKEN_MOVE_BALANCE"),
        //法币到比比
        FB_MOVE_BALANCE("FB_MOVE_BALANCE"),
        //比比到法币
        BALANCE_MOVE_FB("BALANCE_MOVE_FB"),
        //合约到发币
        TOKEN_MOVE_FB("TOKEN_MOVE_FB"),
        //法币到合约
        FB_MOVE_TOKEN("FB_MOVE_TOKEN"),
        //币币到充提
        BALANCE_MOVE_ASSETS("BALANCE_MOVE_ASSETS"),
        //充提到币币
        ASSETS_MOVE_BALANCE("ASSETS_MOVE_BALANCE"),
        //法币到充提
        FB_MOVE_ASSETS("FB_MOVE_ASSETS"),
        //充提到法币
        ASSETS_MOVE_FB("ASSETS_MOVE_FB"),
        //合约到充提
        TOKEN_MOVE_ASSETS("TOKEN_MOVE_ASSETS"),
        //充提到合约
        ASSETS_MOVE_TOKEN("ASSETS_MOVE_TOKEN");
        private String moveType;


        Move_Type(String moveType) {
            this.moveType = moveType;
        }

        public String getMoveType() {
            return moveType;
        }

        public void setMoveType(String moveType) {
            this.moveType = moveType;
        }

        @Override
        public String toString() {
            return moveType;
        }
    }

    public enum Balance_Type {
        BALANCE_BB("BALANCE_BB"),
        BALANCE_FB("BALANCE_FB"),
        BALANCE_HY("BALANCE_HY");

        private String balanceType;


        Balance_Type(String balanceType) {
            this.balanceType = balanceType;
        }

        public String getBalanceType() {
            return balanceType;
        }

        public void setBalanceType(String balanceType) {
            this.balanceType = balanceType;
        }

        @Override
        public String toString() {
            return balanceType;
        }
    }

    public enum Extract_State {
        CREATE("CREATE"),
        PASS("PASS"),
        REJECT("REJECT"),
        CLOSE("CLOSE");


        private String extractState;


        Extract_State(String extractState) {
            this.extractState = extractState;
        }

        public String getExtractState() {
            return extractState;
        }

        public void setExtractState(String extractState) {
            this.extractState = extractState;
        }

        @Override
        public String toString() {
            return extractState;
        }
    }
}

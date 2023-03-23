package cn.xa87.constant;

public class CoinConstant {
    public enum Coin_Type {
        MAIN_COIN("MAIN_COIN"),
        PROJECT_COIN("PROJECT_COIN"),
        SPOT("SPOT"),
        FB("FB"),
        ASSETS("ASSETS"),
        CONTRACT("CONTRACT"),
        RELEASE("RELEASE");

        private String coinType;

        Coin_Type(String coinType) {
            this.coinType = coinType;
        }

        public String getCoinType() {
            return coinType;
        }

        public void setCoinType(String coinType) {
            this.coinType = coinType;
        }

        @Override
        public String toString() {
            return coinType;
        }
    }

    public enum Coin_State {
        NORMAL("NORMAL"),
        FROZEN("FROZEN");

        private String coinState;

        Coin_State(String coinState) {
            this.coinState = coinState;
        }

        public String getCoinState() {
            return coinState;
        }

        public void setCoinState(String coinState) {
            this.coinState = coinState;
        }

        @Override
        public String toString() {
            return coinState;
        }
    }

    public enum Get_Coin_Type {
        TOP("TOP"),
        UPDOWN("UPDOWN"),
        VOLUME("VOLUME"),
        TRANSFER("TRANSFER"),
        PROJECT("PROJECT");

        private String getCoinType;


        Get_Coin_Type(String getCoinType) {
            this.getCoinType = getCoinType;
        }

        public String getGetCoinType() {
            return getCoinType;
        }

        public void setGetCoinType(String getCoinType) {
            this.getCoinType = getCoinType;
        }

        @Override
        public String toString() {
            return getCoinType;
        }
    }

    public enum Get_Coin_Sort {
        NAME_UP("NAME_UP"),
        NAME_DOWN("NAME_DOWN"),
        PRICE_UP("PRICE_UP"),
        PRICE_DOWN("PRICE_DOWN"),
        UP("UP"),
        DOWN("DOWN");
        private String getCoinSort;

        Get_Coin_Sort(String getCoinSort) {
            this.getCoinSort = getCoinSort;
        }

        public String getGetCoinSort() {
            return getCoinSort;
        }

        public void setGetCoinSort(String getCoinSort) {
            this.getCoinSort = getCoinSort;
        }

        @Override
        public String toString() {
            return getCoinSort;
        }
    }
}

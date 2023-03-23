package cn.xa87.constant;

public class ContractConstant {
    public enum Contract_Type {
        PERPETUAL("PERPETUAL");
        private String contractType;


        Contract_Type(String contractType) {
            this.contractType = contractType;
        }

        public String getContractType() {
            return contractType;
        }

        public void setContractType(String contractType) {
            this.contractType = contractType;
        }

        @Override
        public String toString() {
            return contractType;
        }
    }

    public enum Trade_Type {
        OPEN_UP("OPEN_UP"),//开多
        OPEN_DOWN("OPEN_DOWN"),//开空
        CLOSE_UP("CLOSE_UP"),//平多
        CLOSE_DOWN("CLOSE_DOWN");//平空
        private String tradeType;


        Trade_Type(String tradeType) {
            this.tradeType = tradeType;
        }

        public String getTradeType() {
            return tradeType;
        }

        public void setTradeType(String tradeType) {
            this.tradeType = tradeType;
        }

        @Override
        public String toString() {
            return tradeType;
        }
    }

    public enum Price_Type {
        CUSTOM_PRICE("CUSTOM_PRICE"),//限价
        MARKET_PRICE("MARKET_PRICE");//市价

        private String priceType;

        Price_Type(String priceType) {
            this.priceType = priceType;
        }

        public String getPriceType() {
            return priceType;
        }

        public void setPriceType(String priceType) {
            this.priceType = priceType;
        }

        @Override
        public String toString() {
            return priceType;
        }
    }
}

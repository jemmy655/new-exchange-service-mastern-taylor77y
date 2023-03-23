package cn.xa87.constant;

public class OtcConstant {

    public enum PAY_TYPE {
        ALIAY("ALIAY"),
        WECHAT("WECHAT"),
        BANKCARD("BANKCARD");

        private String payType;

        PAY_TYPE(String payType) {
            this.payType = payType;
        }

        public String getPayType() {
            return payType;
        }

        public void setPayType(String payType) {
            this.payType = payType;
        }

        @Override
        public String toString() {
            return payType;
        }
    }

    public enum AUTO_STATUS {
        OPEN("OPEN"),
        UNOPEN("UNOPEN");


        private String autoStatus;

        AUTO_STATUS(String autoStatus) {
            this.autoStatus = autoStatus;
        }

        public String getAutoStatus() {
            return autoStatus;
        }

        public void setAutoStatus(String autoStatus) {
            this.autoStatus = autoStatus;
        }

        @Override
        public String toString() {
            return autoStatus;
        }
    }

    public enum ORDER_STATUS {
        NORMAL("NORMAL"),
        BACKOUT("BACKOUT"),
        FINISH("FINISH");

        private String orderStatus;

        ORDER_STATUS(String orderStatus) {
            this.orderStatus = orderStatus;
        }

        public String getOrderStatus() {
            return orderStatus;
        }

        public void setOrderStatus(String orderStatus) {
            this.orderStatus = orderStatus;
        }

        @Override
        public String toString() {
            return orderStatus;
        }
    }


    public enum DIRECTION {
        SELL("SELL"),
        BUY("BUY");

        private String direction;

        DIRECTION(String direction) {
            this.direction = direction;
        }

        public String getDirection() {
            return direction;
        }

        public void setDirection(String direction) {
            this.direction = direction;
        }

        @Override
        public String toString() {
            return direction;
        }
    }

    public enum PLACEANORDER {
        PRICE("PRICE"),
        NUM("NUM");

        private String placeAnOrder;

        PLACEANORDER(String placeAnOrder) {
            this.placeAnOrder = placeAnOrder;
        }

        public String getPlaceAnOrder() {
            return placeAnOrder;
        }

        public void setPlaceAnOrder(String placeAnOrder) {
            this.placeAnOrder = placeAnOrder;
        }

        @Override
        public String toString() {
            return placeAnOrder;
        }
    }


    public enum PRICE_ORDER_STATUS {
        NONPAYMENT("NONPAYMENT"),
        PAYMENT("PAYMENT"),
        FINISH("FINISH"),
        CALLOFF("CALLOFF");

        private String priceOrderStatus;

        PRICE_ORDER_STATUS(String priceOrderStatus) {
            this.priceOrderStatus = priceOrderStatus;
        }

        public String getPriceOrderStatus() {
            return priceOrderStatus;
        }

        public void setPriceOrderStatus(String priceOrderStatus) {
            this.priceOrderStatus = priceOrderStatus;
        }

        @Override
        public String toString() {
            return priceOrderStatus;
        }
    }


    public enum APPEAL_STATUS {
        NORMAL("NORMAL"),
        UNDERWAY("UNDERWAY"),
        SUCCEED("SUCCEED"),
        DEFEATED("DEFEATED"),
        BACK("BACK");

        private String appealStatus;

        APPEAL_STATUS(String appealStatus) {
            this.appealStatus = appealStatus;
        }

        public String getAppealStatus() {
            return appealStatus;
        }

        public void setAppealStatus(String appealStatus) {
            this.appealStatus = appealStatus;
        }

        @Override
        public String toString() {
            return appealStatus;
        }
    }

    public enum APPEAL_TYPE {
        WAIT("WAIT"),
        SUCCEED("SUCCEED"),
        DEFEATED("DEFEATED");

        private String appealType;

        APPEAL_TYPE(String appealType) {
            this.appealType = appealType;
        }

        public String getAppealType() {
            return appealType;
        }

        public void setAppealType(String appealType) {
            this.appealType = appealType;
        }

        @Override
        public String toString() {
            return appealType;
        }
    }
}

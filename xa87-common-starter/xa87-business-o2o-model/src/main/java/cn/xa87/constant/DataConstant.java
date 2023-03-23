package cn.xa87.constant;

public class DataConstant {
    /**
     * banner
     *
     * @author ZhaoZ
     */
    public enum Banner_Type {
        DATA_BANNER("DATA_BANNER"),//主页
        WEL_BANNER("WEL_BANNER"),// 邀请返佣
        PROJECT_BANNER("PROJECT_BANNER");//项目方

        private String bannerType;

        Banner_Type(String bannerType) {
            this.bannerType = bannerType;
        }

        public String getBannerType() {
            return bannerType;
        }

        public void setBannerType(String bannerType) {
            this.bannerType = bannerType;
        }

        @Override
        public String toString() {
            return bannerType;
        }
    }

    /**
     * 语言类型
     *
     * @author ZhaoZ
     */
    public enum Global_Type {
        CHINESE_SIM("CHINESE_SIM"),//中文简体
        ENGLISH("ENGLISH"),//英文
        THAI("THAI"),//泰语
        JAPANESE("JAPANESE"),//日语
        KOREAN("KOREAN"),//韩语
        CHINESE_TRAD("CHINESE_TRAD");//中文繁体

        private String globalType;

        Global_Type(String globalType) {
            this.globalType = globalType;
        }

        public String getGlobalType() {
            return globalType;
        }

        public void setGlobalType(String globalType) {
            this.globalType = globalType;
        }

        @Override
        public String toString() {
            return globalType;
        }
    }

    /**
     * 公告类型
     *
     * @author ZhaoZ
     */
    public enum Notice_Type {
        OFFICIAL("OFFICIAL"),//官方
        NEWS("NEWS");//中文繁体

        private String noticeType;

        Notice_Type(String noticeType) {
            this.noticeType = noticeType;
        }

        public String getNoticeType() {
            return noticeType;
        }

        public void setNoticeType(String noticeType) {
            this.noticeType = noticeType;
        }

        @Override
        public String toString() {
            return noticeType;
        }
    }
}

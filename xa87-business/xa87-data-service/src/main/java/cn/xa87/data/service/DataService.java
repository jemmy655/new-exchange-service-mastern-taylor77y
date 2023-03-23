package cn.xa87.data.service;

import cn.xa87.constant.CoinConstant;
import cn.xa87.constant.DataConstant;
import cn.xa87.data.controller.apidata;
import cn.xa87.model.Banner;
import cn.xa87.model.Notice;
import cn.xa87.model.Pairs;

import java.util.List;


public interface DataService {

    List<Banner> getBanners(DataConstant.Banner_Type bannerType, DataConstant.Global_Type global);

    List<Notice> getNotices(DataConstant.Notice_Type noticeType, DataConstant.Global_Type global);

    List<Pairs> getIndexCoin(CoinConstant.Get_Coin_Type getCoinType);

    String getCoinInfo(String pairsName);

    List<String> getMainCurs();

    List<Pairs> getPairsByMainCur(String pairsMainCur, CoinConstant.Coin_Type type);

    List<Pairs> getCoinQuotation(CoinConstant.Get_Coin_Sort getCoinSort, CoinConstant.Coin_Type coinType);

    void syncKline();

    String apiListen(apidata data);
}
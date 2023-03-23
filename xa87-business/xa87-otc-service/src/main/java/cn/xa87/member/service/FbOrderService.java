package cn.xa87.member.service;


import cn.xa87.model.OtcOrder;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;


public interface FbOrderService extends IService<OtcOrder> {
    Boolean orderReleasing(String memberId, String slidingScales, String currency, String num, String minQuota, String payType, String tradeComment, String autoStatus, String direction, String extremum, String autoMsg);

    Object orderList(String page, String size, String currency, String direction, Integer mccId);

    Object currencyList();

    Object placeAnOrder(String orderId, String memberId, String type, String num, Integer mccId);

    Object orderDetails(String priceOrderId, String memberId);

    Object ownOrderList(String priceOrderStatus, String memberId, String page, String size);

    Boolean payment(String priceOrderId, String memberId, String payType);

    Boolean deliverGoods(String priceOrderId, String memberId);

    Boolean backOrder(String priceOrderId, String memberId);

    Object orderEndTime(String priceOrderId);

    Object appeal(String priceOrderId, String userId, String msg, String file, String file1);

    Object appealBack(String priceOrderId, String userId);

    Object merchantsOrders(String userId, String page, String size);

    Object getNowPrice(String currency);

    Object ratioConfig();

    Object updataOrder(String orderId, String userId, String upDownNumber, String extremum, String num, String minPrice, String payType, String remarks, String autoStatus, String autoMsg);

    Object getAppealList();

    Object orderBack(String orderId, String userId);

    Object fastPlaceAnOrder(String userId, String price, String direction, String currency, String payType, Integer mccId);

    Object fastPlaceAnOrderGetPrice(String userId, String type, String price, String direction, String currency, String payType);
}

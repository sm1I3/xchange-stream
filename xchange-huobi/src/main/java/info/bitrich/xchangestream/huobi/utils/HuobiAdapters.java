package info.bitrich.xchangestream.huobi.utils;

import info.bitrich.xchangestream.huobi.dto.HuobiMarketDepthMessage;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.Order;
import org.knowm.xchange.dto.marketdata.OrderBook;
import org.knowm.xchange.dto.trade.LimitOrder;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class HuobiAdapters {

    public static String adaptCurrencyPair(CurrencyPair currencyPair) {
        return currencyPair.base.toString().toLowerCase() + currencyPair.counter.toString().toLowerCase();
    }

    public static OrderBook adaptOrderBook(CurrencyPair currencyPair, HuobiMarketDepthMessage orderBook) {

        List<LimitOrder> asks = adaptOrderBookLevels(currencyPair, Order.OrderType.ASK, orderBook.getData().getAsks());
        List<LimitOrder> bids = adaptOrderBookLevels(currencyPair, Order.OrderType.BID, orderBook.getData().getBids());
        return new OrderBook(new Date(orderBook.getData().getTs()), asks, bids);
    }

    private static List<LimitOrder> adaptOrderBookLevels(CurrencyPair currencyPair, Order.OrderType orderType,
                                                         List<HuobiMarketDepthMessage.HuobiMarketDepthLevel> levels) {
        return levels.stream()
                .map(level -> adaptOrderBookLevel(currencyPair, orderType, level))
                .collect(Collectors.toList());
    }

    private static LimitOrder adaptOrderBookLevel(CurrencyPair currencyPair, Order.OrderType orderType,
                                                  HuobiMarketDepthMessage.HuobiMarketDepthLevel level) {
        return new LimitOrder.Builder(orderType, currencyPair)
                .limitPrice(level.getPrice())
                .originalAmount(level.getAmount())
                .build();
    }
}

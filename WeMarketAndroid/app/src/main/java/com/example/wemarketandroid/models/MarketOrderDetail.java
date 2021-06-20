package com.example.wemarketandroid.models;

import java.util.List;
import java.util.Objects;

public class MarketOrderDetail implements IDiffable{
    private int id;
    private int marketId;
    private Market market;
    private List<OrderDetail> marketOrderDetails;

    public MarketOrderDetail(int id, int marketId, Market market, List<OrderDetail> marketOrderDetails) {
        this.id = id;
        this.marketId = marketId;
        this.market = market;
        this.marketOrderDetails = marketOrderDetails;
    }

    @Override
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setMarketId(int marketId) {
        this.marketId = marketId;
    }

    public void setMarket(Market market) {
        this.market = market;
    }

    public void setMarketOrderDetails(List<OrderDetail> marketOrderDetails) {
        this.marketOrderDetails = marketOrderDetails;
    }

    public int getMarketId() {
        return marketId;
    }

    public Market getMarket() {
        return market;
    }

    public List<OrderDetail> getMarketOrderDetails() {
        return marketOrderDetails;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MarketOrderDetail that = (MarketOrderDetail) o;
        return id == that.id &&
                marketId == that.marketId &&
                Objects.equals(marketOrderDetails, that.marketOrderDetails);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, marketId, marketOrderDetails);
    }
}

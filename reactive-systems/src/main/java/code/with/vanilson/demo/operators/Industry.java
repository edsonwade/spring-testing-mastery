package code.with.vanilson.demo.operators;

import java.util.List;

public class Industry {

    private String industryCode;
    private List<StockQuote> stockQuoteList;

    public Industry(String industryCode, List<StockQuote> stockQuoteList) {
        this.industryCode = industryCode;
        this.stockQuoteList = stockQuoteList;
    }

    public String getIndustryCode() {
        return industryCode;
    }

    public void setIndustryCode(String industryCode) {
        this.industryCode = industryCode;
    }

    public List<StockQuote> getStockQuoteList() {
        return stockQuoteList;
    }

    public void setStockQuoteList(List<StockQuote> stockQuoteList) {
        this.stockQuoteList = stockQuoteList;
    }

    @Override
    public String toString() {
        return "Industry{" +
                "industryCode='" + industryCode + '\'' +
                ", stockQuoteList=" + stockQuoteList +
                '}';
    }
}




package code.with.vanilson.demo.operators;

public class StockQuote {

    private String industryCode;
    private String languageCode;

    public String getIndustryCode() {
        return industryCode;
    }

    public void setIndustryCode(String industryCode) {
        this.industryCode = industryCode;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    @Override
    public String toString() {
        return "StockQuote{" +
                "industryCode='" + industryCode + '\'' +
                ", languageCode='" + languageCode + '\'' +
                '}';
    }
}

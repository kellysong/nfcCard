package com.sinpo.xnfc.bean;

import java.util.List;

/**
 * 卡片信息
 *
 * @author Kelly
 * @version 1.0.0
 * @filename CardInfo.java
 * @time 2018/6/27 17:26
 * @copyright(C) 2018 song
 */
public class CardInfo {
    private String cardName;//卡片名称
    private String cardBalance;//卡片余额
    private String cardNo;//卡号
    private String cardVersion;//版本号
    private String effectiveDate;//有效日期
    private List<ConsumeRecord> consumeRecords;//消费记录

    public CardInfo() {
    }

    public CardInfo(String cardName, String cardBalance, String cardNo, String cardVersion, String effectiveDate, List<ConsumeRecord> consumeRecords) {
        this.cardName = cardName;
        this.cardBalance = cardBalance;
        this.cardNo = cardNo;
        this.cardVersion = cardVersion;
        this.effectiveDate = effectiveDate;
        this.consumeRecords = consumeRecords;
    }

    //消费日志记录
    public static class ConsumeRecord {
        private String consumeTime;//消费时间
        private String consumeMoney;//消费金额
        private String transactionNo;//流水号

        public String getConsumeTime() {
            return consumeTime;
        }

        public void setConsumeTime(String consumeTime) {
            this.consumeTime = consumeTime;
        }

        public String getConsumeMoney() {
            return consumeMoney;
        }

        public void setConsumeMoney(String consumeMoney) {
            this.consumeMoney = consumeMoney;
        }

        public String getTransactionNo() {
            return transactionNo;
        }

        public void setTransactionNo(String transactionNo) {
            this.transactionNo = transactionNo;
        }
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String getCardBalance() {
        return cardBalance;
    }

    public void setCardBalance(String cardBalance) {
        this.cardBalance = cardBalance;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getCardVersion() {
        return cardVersion;
    }

    public void setCardVersion(String cardVersion) {
        this.cardVersion = cardVersion;
    }

    public String getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(String effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public List<ConsumeRecord> getConsumeRecords() {
        return consumeRecords;
    }

    public void setConsumeRecords(List<ConsumeRecord> consumeRecords) {
        this.consumeRecords = consumeRecords;
    }

    @Override
    public String toString() {
        return "CardInfo{" +
                "cardName='" + cardName + '\'' +
                ", cardBalance='" + cardBalance + '\'' +
                ", cardNo='" + cardNo + '\'' +
                ", cardVersion='" + cardVersion + '\'' +
                ", effectiveDate='" + effectiveDate + '\'' +
                ", consumeRecords=" + consumeRecords +
                '}';
    }
}

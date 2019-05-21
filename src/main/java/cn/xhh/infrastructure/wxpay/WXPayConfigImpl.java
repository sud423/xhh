package cn.xhh.infrastructure.wxpay;

import java.io.InputStream;

public class WXPayConfigImpl extends WXPayConfig{
    @Override
    String getAppID() {
        return null;
    }

    @Override
    String getMchID() {
        return null;
    }

    @Override
    String getKey() {
        return null;
    }

    @Override
    InputStream getCertStream() {
        return null;
    }

    @Override
    public int getHttpConnectTimeoutMs() {
        return super.getHttpConnectTimeoutMs();
    }

    @Override
    public int getHttpReadTimeoutMs() {
        return super.getHttpReadTimeoutMs();
    }

    @Override
    IWXPayDomain getWXPayDomain() {
        return null;
    }

    @Override
    public boolean shouldAutoReport() {
        return super.shouldAutoReport();
    }

    @Override
    public int getReportWorkerNum() {
        return super.getReportWorkerNum();
    }

    @Override
    public int getReportQueueMaxSize() {
        return super.getReportQueueMaxSize();
    }

    @Override
    public int getReportBatchSize() {
        return super.getReportBatchSize();
    }
}

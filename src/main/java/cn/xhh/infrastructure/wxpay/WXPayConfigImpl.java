package cn.xhh.infrastructure.wxpay;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

@Component
public class WXPayConfigImpl extends WXPayConfig{

    private byte[] certData;

    /**
     * 公共号appid
     */
    @Value("${app_id}")
    private String appId;

    /**
     * 商户号
     */
    @Value("${mch_id}")
    private String mchId;

    /**
     * 密钥
     */
    @Value("${paterner_key}")
    private String paternerKey;

    /**
     * 证书地址
     */
    @Value("${cert_path}")
    private String certPath;

    public WXPayConfigImpl() throws Exception
    {
        // 构造方法读取证书, 通过getCertStream 可以使sdk获取到证书
        if(certPath!=null && certPath!=""){
            File file = new File(certPath);
            InputStream certStream = new FileInputStream(file);
            this.certData = new byte[(int) file.length()];
            certStream.read(this.certData); certStream.close();
        }

    }


    @Override
    String getAppID() {
        return appId;
    }

    @Override
    String getMchID() {
        return mchId;
    }

    @Override
    String getKey() {
        return paternerKey;
    }

    @Override
    InputStream getCertStream() {
        return new ByteArrayInputStream(this.certData);
    }

    @Override
    IWXPayDomain getWXPayDomain() {
        IWXPayDomain iwxPayDomain = new IWXPayDomain()
        {
            @Override
            public void report(String domain, long elapsedTimeMillis, Exception ex) { }

            @Override
            public DomainInfo getDomain(WXPayConfig config)
            {
                return new IWXPayDomain.DomainInfo(WXPayConstants.DOMAIN_API, true);
            }
        };
        return iwxPayDomain;

    }

}

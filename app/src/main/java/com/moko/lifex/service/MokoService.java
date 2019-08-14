package com.moko.lifex.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.moko.lifex.AppConstants;
import com.moko.lifex.entity.MQTTConfig;
import com.moko.lifex.utils.SPUtiles;
import com.moko.support.MokoSupport;
import com.moko.support.log.LogModule;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMDecryptorProvider;
import org.bouncycastle.openssl.PEMEncryptedKeyPair;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.bouncycastle.openssl.jcajce.JcePEMDecryptorProviderBuilder;
import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.security.KeyManagementException;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.Security;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.SocketFactory;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;


/**
 * @Date 2017/12/7 0007
 * @Author wenzheng.liu
 * @Description
 * @ClassPath com.moko.lifex.service.MokoService
 */
public class MokoService extends Service {
    private IBinder mBinder = new LocalBinder();

    public class LocalBinder extends Binder {
        public MokoService getService() {
            return MokoService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LogModule.i("启动后台服务");
        String mqttAppConfigStr = SPUtiles.getStringValue(this, AppConstants.SP_KEY_MQTT_CONFIG_APP, "");
        if (!TextUtils.isEmpty(mqttAppConfigStr)) {
            MQTTConfig mqttConfig = new Gson().fromJson(mqttAppConfigStr, MQTTConfig.class);
            if (!mqttConfig.isError(null)) {
                MqttAndroidClient client = MokoSupport.getInstance().creatClient(mqttConfig.host, mqttConfig.port, mqttConfig.clientId, mqttConfig.connectMode > 0);
                MqttConnectOptions connOpts = new MqttConnectOptions();
                connOpts.setAutomaticReconnect(true);
                connOpts.setCleanSession(mqttConfig.cleanSession);
                connOpts.setKeepAliveInterval(mqttConfig.keepAlive);
//                connOpts.setUserName(mqttConfig.username);
//                connOpts.setPassword(mqttConfig.password.toCharArray());
                if (TextUtils.isEmpty(mqttConfig.caPath)) {
                    // 单向不验证
                    try {
                        connOpts.setSocketFactory(getAllTMSocketFactory());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    switch (mqttConfig.connectMode) {
                        case 1:
                            // 单向验证
                            try {
                                connOpts.setSocketFactory(getSingleSocketFactory(mqttConfig.caPath));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                        case 3:
                            // 双向验证
                            try {
                                connOpts.setSocketFactory(getSocketFactory(mqttConfig.caPath, mqttConfig.clientKeyPath, mqttConfig.clientCertPath));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                    }
                }
                try {
                    MokoSupport.getInstance().connectMqtt(connOpts);
                } catch (MqttException e) {
                    e.printStackTrace();
                }
                return;
            }
        }
    }

    @Override
    public void onDestroy() {
        LogModule.i("关闭后台服务");
        super.onDestroy();
        try {
            MokoSupport.getInstance().disconnectMqtt();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }


    static class AllTM implements TrustManager, X509TrustManager {
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        public boolean isServerTrusted(X509Certificate[] certs) {
            LogModule.i("isServerTrusted");
            for (java.security.cert.X509Certificate certificate : certs) {
                LogModule.w("Accepting:" + certificate);
            }
            return true;
        }

        public boolean isClientTrusted(X509Certificate[] certs) {
            LogModule.i("isClientTrusted");
            for (java.security.cert.X509Certificate certificate : certs) {
                LogModule.w("Accepting:" + certificate);
            }
            return true;
        }

        public void checkServerTrusted(X509Certificate[] certs, String authType) throws CertificateException {
            LogModule.i("Server authtype=" + authType);
            for (java.security.cert.X509Certificate certificate : certs) {
                LogModule.w("Accepting:" + certificate);
            }
            return;
        }

        public void checkClientTrusted(X509Certificate[] certs, String authType) throws CertificateException {
            LogModule.i("Client authtype=" + authType);
            for (java.security.cert.X509Certificate certificate : certs) {
                LogModule.w("Accepting:" + certificate);
            }
            return;
        }
    }


    /**
     * 单向不验证
     *
     * @Date 2019/8/5
     * @Author wenzheng.liu
     * @Description
     */
    private SocketFactory getAllTMSocketFactory() {
        TrustManager[] trustAllCerts = new TrustManager[1];
        TrustManager tm = new AllTM();
        trustAllCerts[0] = tm;
        SSLContext sc = null;
        try {
            // 全信任
            sc.init(null, trustAllCerts, null);
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        return sc.getSocketFactory();
    }

    /**
     * 单向验证
     *
     * @return
     * @throws Exception
     */

    private SSLSocketFactory getSingleSocketFactory(String caFile) throws Exception {
        // 中间证书地址
        Security.addProvider(new BouncyCastleProvider());

        X509Certificate caCert = null;

        FileInputStream fis = new FileInputStream(caFile);

        BufferedInputStream bis = new BufferedInputStream(fis);

        CertificateFactory cf = CertificateFactory.getInstance("X.509");

        while (bis.available() > 0) {

            caCert = (X509Certificate) cf.generateCertificate(bis);

        }

        KeyStore caKs =
                KeyStore.getInstance(KeyStore.getDefaultType());

        caKs.load(null, null);

        caKs.setCertificateEntry("ca-certificate", caCert);

        TrustManagerFactory tmf =
                TrustManagerFactory.getInstance("X509");

        tmf.init(caKs);

        SSLContext sslContext = SSLContext.getInstance("TLSv1.2");

        sslContext.init(null, tmf.getTrustManagers(), null);

        return sslContext.getSocketFactory();

    }

    /**
     * 双向验证
     *
     * @return
     * @throws Exception
     */
    private SSLSocketFactory getSocketFactory(String caFile, String clientKeyFile, String clientCertFile) throws Exception {

        FileInputStream ca = new FileInputStream(caFile);
        FileInputStream clientCert = new FileInputStream(clientCertFile);
        FileInputStream clientKey = new FileInputStream(clientKeyFile);
        Security.addProvider(new BouncyCastleProvider());
        // load CA certificate
        X509Certificate caCert = null;

        BufferedInputStream bis = new BufferedInputStream(ca);
        CertificateFactory cf = CertificateFactory.getInstance("X.509");

        while (bis.available() > 0) {
            caCert = (X509Certificate) cf.generateCertificate(bis);
        }

        // load client certificate
        bis = new BufferedInputStream(clientCert);
        X509Certificate cert = null;
        while (bis.available() > 0) {
            cert = (X509Certificate) cf.generateCertificate(bis);
        }

        // load client private key
        PEMParser pemParser = new PEMParser(new InputStreamReader(clientKey));
        Object object = pemParser.readObject();
        PEMDecryptorProvider decProv = new JcePEMDecryptorProviderBuilder()
                .build("".toCharArray());
        JcaPEMKeyConverter converter = new JcaPEMKeyConverter()
                .setProvider("BC");
        KeyPair key;
        if (object instanceof PEMEncryptedKeyPair) {
            LogModule.e("Encrypted key - we will use provided password");
            key = converter.getKeyPair(((PEMEncryptedKeyPair) object)
                    .decryptKeyPair(decProv));
        } else {
            LogModule.e("Unencrypted key - no password needed");
            key = converter.getKeyPair((PEMKeyPair) object);
        }
        pemParser.close();

        // CA certificate is used to authenticate server
        KeyStore caKs = KeyStore.getInstance(KeyStore.getDefaultType());
        caKs.load(null, null);
        caKs.setCertificateEntry("ca-certificate", caCert);
        TrustManagerFactory tmf = TrustManagerFactory.getInstance("X509");
        tmf.init(caKs);

        // client key and certificates are sent to server so it can authenticate
        // us
        KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
        ks.load(null, null);
        ks.setCertificateEntry("certificate", cert);
        ks.setKeyEntry("private-key", key.getPrivate(), "".toCharArray(),
                new java.security.cert.Certificate[]{cert});
        KeyManagerFactory kmf =
                KeyManagerFactory.getInstance(KeyManagerFactory
                        .getDefaultAlgorithm());
        kmf.init(ks, "".toCharArray());

        // finally, create SSL socket factory
        SSLContext context = SSLContext.getInstance("TLSv1.2");
        context.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

        return context.getSocketFactory();
    }
}
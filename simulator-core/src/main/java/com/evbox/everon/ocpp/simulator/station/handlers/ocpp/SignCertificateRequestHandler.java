package com.evbox.everon.ocpp.simulator.station.handlers.ocpp;

import com.evbox.everon.ocpp.simulator.station.StationHardwareData;
import com.evbox.everon.ocpp.simulator.station.StationMessageSender;
import com.evbox.everon.ocpp.simulator.station.StationStore;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.openssl.jcajce.JcaPEMWriter;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.bouncycastle.pkcs.PKCS10CertificationRequestBuilder;
import org.bouncycastle.pkcs.jcajce.JcaPKCS10CertificationRequestBuilder;

import javax.security.auth.x500.X500Principal;
import java.io.IOException;
import java.io.StringWriter;
import java.security.*;

@Slf4j
public class SignCertificateRequestHandler implements Runnable {

    private static final String SIGNATURE_ALG = "SHA256withRSA";

    private StationStore stationStore;
    private StationMessageSender stationMessageSender;

    public SignCertificateRequestHandler(StationStore stationStore, StationMessageSender stationMessageSender) {
        this.stationStore = stationStore;
        this.stationMessageSender = stationMessageSender;
    }

    @Override
    public void run() {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(2048, new SecureRandom());
            KeyPair keypair = keyGen.generateKeyPair();
            PublicKey publicKey = keypair.getPublic();
            PrivateKey privateKey = keypair.getPrivate();
            String csr = generatePKCS10(publicKey, privateKey);

            stationStore.setStationPublicKey(publicKey);
            stationStore.setStationPrivateKey(privateKey);
            stationMessageSender.sendSignCertificateRequest(csr);
        } catch (Exception e) {
            log.debug("Error while creating the CSR", e);
        }

    }

    private String generatePKCS10(PublicKey publicKey, PrivateKey privateKey) throws IOException, OperatorCreationException {
        X500Principal principal = new X500Principal( "CN=" + StationHardwareData.SERIAL_NUMBER);
        PKCS10CertificationRequestBuilder p10Builder = new JcaPKCS10CertificationRequestBuilder(principal, publicKey);
        JcaContentSignerBuilder csBuilder = new JcaContentSignerBuilder(SIGNATURE_ALG);
        ContentSigner signer = csBuilder.build(privateKey);
        PKCS10CertificationRequest csr = p10Builder.build(signer);

        StringWriter stringWriter = new StringWriter();
        JcaPEMWriter pemWriter = new JcaPEMWriter(stringWriter);
        pemWriter.writeObject(csr);
        pemWriter.close();
        stringWriter.close();
        return stringWriter.toString();
    }
}

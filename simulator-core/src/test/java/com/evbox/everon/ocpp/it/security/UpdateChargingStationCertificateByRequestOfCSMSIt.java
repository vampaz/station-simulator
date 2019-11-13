package com.evbox.everon.ocpp.it.security;

import com.evbox.everon.ocpp.common.CiString;
import com.evbox.everon.ocpp.mock.StationSimulatorSetUp;
import com.evbox.everon.ocpp.mock.csms.exchange.SignCertificate;
import com.evbox.everon.ocpp.simulator.message.ActionType;
import com.evbox.everon.ocpp.simulator.message.Call;
import com.evbox.everon.ocpp.v20.message.station.*;
import com.google.common.base.Splitter;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static com.evbox.everon.ocpp.mock.constants.StationConstants.*;
import static org.assertj.core.api.Assertions.assertThat;

public class UpdateChargingStationCertificateByRequestOfCSMSIt extends StationSimulatorSetUp {

    private static final String certificate = "3082035830820240a0030201020205009252d85e300d06092a864886f70d01010b05003075310b3009060355040613024e4c310b300906035504080c024e4c310c300a06035504070c03414d53310e300c060355040a0c055375624341310e300c060355040b0c055375624341310e300c06035504030c055375624341311b301906092a864886f70d010901160c7375626361407375622e6361301e170d3139313130383039333630395a170d3239313130353039333630395a30173115301306035504030c0c30303030303030303030304630820122300d06092a864886f70d01010105000382010f003082010a0282010100dbd865c2c144dbceab605ec2dcec80d8ac5b6e9a3ed98895c569dfffc71b7ab4e074628cf4edfc071ae1bc75f3ce88578719dbd05d8ff27a79883488325d0b7131497b518adf205cd3f16e4698fb116b613f088621eba6c7fd65b9960e882961340bf3882f37f4168a726f1f367821f7856d59658a5508769d8c39401ee349e77edf5a03a1f9b817adc17b7eaa75d560fb3cc0b5e6c3478486e2ae76db3f796be234d6f080937537a61c6a9b85c2ed520028a6c13909f386f3adc726f92fb4c92c8babf3cbc7d7e4c3b1f3b5d54f376726947761ad7aea0d33e6c4537427cf6da12f011b928b119083b6af3b797991ba519a976d22d473d65d84c09a74a722db0203010001a34d304b30090603551d1304023000301d0603551d0e04160414c078da2313232fa6293b4d31253d103f0b856972301f0603551d23041830168014fce4a2ae5d72e6af9f8ada3af4bc15b7668cdeb5300d06092a864886f70d01010b05000382010100a188ec7c5ad444753780b9d59d80c875f032ee49fba2ca9b5cf22a376b5d5cb1e4214b7e311fbeb205ee5794c1638d2edc1df16e5c3327c9bea4d6386e9a8c051cc7df52a42fdaf99e852b84a638e50a5be2a0ecd52a8588067c430f5b367637cf2e8c1693ab4dccc28e9f94006a90911162599eb4ac9cafcdd844190b3253e4216a205f89ffa631f82299efd26bf2800caecbcd8d079661b29cfd56cfa65235fd7557746c0101ab737f549615cde8ffd052ef8f19ff8104dcdf718426cee7cdf4716528780c1600a5251b9cdf4fd7770d4542d0807984df1d12e38b9c8f047a9cef17ec9b0b35784f12afaf62f5b61f0aea191129339d2512dccf2713950010\n" +
            "308203643082024c02021235300d06092a864886f70d0101050500307a310b3009060355040613024e4c310b300906035504080c024e4c310c300a06035504070c03414d53310f300d060355040a0c06526f6f744341310f300d060355040b0c06526f6f744341310f300d06035504030c06526f6f744341311d301b06092a864886f70d010901160e726f6f74636140726f6f742e6361301e170d3139313032313133303633365a170d3230313032303133303633365a3075310b3009060355040613024e4c310b300906035504080c024e4c310c300a06035504070c03414d53310e300c060355040a0c055375624341310e300c060355040b0c055375624341310e300c06035504030c055375624341311b301906092a864886f70d010901160c7375626361407375622e636130820122300d06092a864886f70d01010105000382010f003082010a0282010100b8025e34f372a4957af6b12ebc07475770c8dd1a9b657eeb5e9f1790b8217c3652feef703c4407895d740658a68ce26d890266d27d8115d587f7ee011aa14bd56494cadf81fecf20ba67ace4e96154b0b61a0860a0818a1d65631533e61f07d5857a6d33640e87273d5ee4b5446b55e15fb2dc1f4416ae13a07f64560ba6d51a6dfa52b7dd5f24039a7b8094c84ddb36ae7c037dc306df5b6a914a0409102611ecf0a3a5e390620fb9d6ec0ad48a1ccaf61c0257bebe6186edabeaba9bd3966f69f20a49190cf6dda28d1367fe66882aafed27fdf01bcc8adc8b629048148b84fb76cdbfc68ba5f9736090779160725be68db41822c480a7f3f0c7ea3acc27c70203010001300d06092a864886f70d010105050003820101007dacf7835ca9865f0f146acba45c97ce71386c7afc769f8f05fff59c11e813d7ca63904b1bafba386791f913b6b1be17bdadf2c23438aeb6c1a04bba9a69e0c7c7dd14db0d6c21e395e0aedd310fba5bb3746779cb0a6d4ad64979023fccf170649ac35b2ade0d199f75e6d2e2c1b43c49976298b2d79f5e1004e2cb40274110bde4ebe44f03edd5b6f8707f99ba17038f7971af8d1da21238b014eeb819580cfc33bfc4328050c62589023c79631f82925802a080ab4338fcc199283c924db682806c8cb2d5cd2834bb10a50633d6a0eae0b05ad74c7b2e51b50b06df7955528411f28b1c1f82c469b2b8e230ca63875091d8b64e387132267f66dd18bee54f";

    private static final String pemCertificate = "-----BEGIN CERTIFICATE-----" +
            "MIIDWDCCAkCgAwIBAgIFAJJS2F4wDQYJKoZIhvcNAQELBQAwdTELMAkGA1UEBhMC" +
            "TkwxCzAJBgNVBAgMAk5MMQwwCgYDVQQHDANBTVMxDjAMBgNVBAoMBVN1YkNBMQ4w" +
            "DAYDVQQLDAVTdWJDQTEOMAwGA1UEAwwFU3ViQ0ExGzAZBgkqhkiG9w0BCQEWDHN1" +
            "YmNhQHN1Yi5jYTAeFw0xOTExMDgwOTM2MDlaFw0yOTExMDUwOTM2MDlaMBcxFTAT" +
            "BgNVBAMMDDAwMDAwMDAwMDAwRjCCASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoC" +
            "ggEBANvYZcLBRNvOq2BewtzsgNisW26aPtmIlcVp3//HG3q04HRijPTt/Aca4bx1" +
            "886IV4cZ29Bdj/J6eYg0iDJdC3ExSXtRit8gXNPxbkaY+xFrYT8IhiHrpsf9ZbmW" +
            "DogpYTQL84gvN/QWinJvHzZ4IfeFbVllilUIdp2MOUAe40nnft9aA6H5uBetwXt+" +
            "qnXVYPs8wLXmw0eEhuKudts/eWviNNbwgJN1N6YcapuFwu1SACimwTkJ84bzrccm" +
            "+S+0ySyLq/PLx9fkw7HztdVPN2cmlHdhrXrqDTPmxFN0J89toS8BG5KLEZCDtq87" +
            "eXmRulGal20i1HPWXYTAmnSnItsCAwEAAaNNMEswCQYDVR0TBAIwADAdBgNVHQ4E" +
            "FgQUwHjaIxMjL6YpO00xJT0QPwuFaXIwHwYDVR0jBBgwFoAU/OSirl1y5q+fito6" +
            "9LwVt2aM3rUwDQYJKoZIhvcNAQELBQADggEBAKGI7Hxa1ER1N4C51Z2AyHXwMu5J" +
            "+6LKm1zyKjdrXVyx5CFLfjEfvrIF7leUwWONLtwd8W5cMyfJvqTWOG6ajAUcx99S" +
            "pC/a+Z6FK4SmOOUKW+Kg7NUqhYgGfEMPWzZ2N88ujBaTq03Mwo6flABqkJERYlme" +
            "tKycr83YRBkLMlPkIWogX4n/pjH4Ipnv0mvygAyuy82NB5Zhspz9Vs+mUjX9dVd0" +
            "bAEBq3N/VJYVzej/0FLvjxn/gQTc33GEJs7nzfRxZSh4DBYApSUbnN9P13cNRULQ" +
            "gHmE3x0S44ucjwR6nO8X7JsLNXhPEq+vYvW2HwrqGREpM50lEtzPJxOVABA=" +
            "-----END CERTIFICATE-----";

    @Test
    void shouldRequestNewCertificate() {

        ocppMockServer
                .when(SignCertificate.request())
                .thenReturn(SignCertificate.response());

        stationSimulatorRunner.run();
        ocppMockServer.waitUntilConnected();

        TriggerMessageRequest triggerMessageRequest = new TriggerMessageRequest()
                                                            .withRequestedMessage(TriggerMessageRequest.RequestedMessage.SIGN_CHARGING_STATION_CERTIFICATE);

        Call call = new Call(DEFAULT_CALL_ID, ActionType.TRIGGER_MESSAGE, triggerMessageRequest);
        TriggerMessageResponse triggerResponse = ocppServerClient.findStationSender(STATION_ID).sendMessage(call.toJson(), TriggerMessageResponse.class);

        assertThat(triggerResponse).isNotNull();
        assertThat(triggerResponse.getStatus().value()).isEqualTo(TriggerMessageResponse.Status.ACCEPTED.value());

        CertificateSignedRequest certificateSignedRequest = new CertificateSignedRequest().withCert(stringToCiStringsList(certificate));
        call = new Call(DEFAULT_CALL_ID, ActionType.CERTIFICATE_SIGNED, certificateSignedRequest);
        CertificateSignedResponse certificateResponse = ocppServerClient.findStationSender(STATION_ID).sendMessage(call.toJson(), CertificateSignedResponse.class);

        assertThat(certificateResponse).isNotNull();
        assertThat(certificateResponse.getStatus().value()).isEqualTo(CertificateSignedResponse.Status.ACCEPTED.value());

        String storedCertificate = stationSimulatorRunner.getStation(STATION_ID).getStateView().getStationCertificate();
        assertThat(storedCertificate.replaceAll("\n", "")).isEqualTo(pemCertificate);
    }

    private List<CiString.CiString5500> stringToCiStringsList(String certificate) {
        List<CiString.CiString5500> result = new ArrayList<>();
        Splitter.fixedLength(5500).split(certificate).forEach(c -> result.add(new CiString.CiString5500(c)));
        return result;
    }

}

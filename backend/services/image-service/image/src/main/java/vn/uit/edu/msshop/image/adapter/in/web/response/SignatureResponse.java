package vn.uit.edu.msshop.image.adapter.in.web.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class SignatureResponse {
    private String signature;
    private long timeStamp;
    private final String apiKey;
    private final String cloudName;
    public SignatureResponse(String signature, long timeStamp, String apiKey, String cloudName) {
        this.signature=signature;
        this.timeStamp= timeStamp;
        this.apiKey = apiKey;
        this.cloudName= cloudName;
    }
}

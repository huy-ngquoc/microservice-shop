package vn.uit.edu.msshop.recommendation.application.port.in;

import vn.uit.edu.msshop.recommendation.adapter.in.web.response.AIServerResponse;

public interface GetMLResultUseCase {
    public AIServerResponse getResponse(byte[] imageData);
}

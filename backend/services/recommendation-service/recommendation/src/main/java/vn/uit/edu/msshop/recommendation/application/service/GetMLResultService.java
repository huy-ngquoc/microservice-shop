package vn.uit.edu.msshop.recommendation.application.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import vn.uit.edu.msshop.recommendation.adapter.in.web.response.AIServerResponse;
import vn.uit.edu.msshop.recommendation.adapter.remote.MLServerCaller;
import vn.uit.edu.msshop.recommendation.application.port.in.GetMLResultUseCase;

@Service
@RequiredArgsConstructor
public class GetMLResultService implements GetMLResultUseCase {
    private final MLServerCaller caller;
    @Override
    public AIServerResponse getResponse(byte[] imageData) {
        return caller.getPrediction(imageData).getBody();
    }

}

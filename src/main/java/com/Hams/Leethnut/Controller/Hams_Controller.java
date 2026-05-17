package com.Hams.Leethnut.Controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.Hams.Leethnut.Service.PredictionResult;
import com.Hams.Leethnut.Service.Service1;
import com.Hams.Leethnut.model.Order1;

@RestController
@RequestMapping("/delivery")
public class Hams_Controller {

    private final Service1 service;

    public Hams_Controller(Service1 service) {
        this.service = service;
    }

    // STEP 1 — User places order
    // POST /delivery/predict
    // Body: { "area": "KR PURAM", "slotScore": 18, "pastFail": 2, ... }
    @PostMapping("/predict")
    public PredictionResult predict(@RequestBody Order1 order) {
        return service.predict(order);
    }

    // STEP 2 — User gives feedback after delivery
    // POST /delivery/feedback?orderId=5&status=FAILED
    @PostMapping("/feedback")
    public String feedback(@RequestParam Long orderId,
            @RequestParam String status) {
        return service.submitFeedback(orderId, status);
    }
}

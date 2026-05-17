package com.Hams.Leethnut.Service;

import java.util.List;

public class PredictionResult {

    private final int percent;
    private final String decision;
    private final List<String> reasons;
    private final Long orderId;
    // ← lowercase 'o' (Java convention)

    // ── Add orderId to constructor ──
    public PredictionResult(int percent, String decision, List<String> reasons, Long orderId) {
        this.percent = percent;
        this.decision = decision;
        this.reasons = reasons;
        this.orderId = orderId; // ← set it
    }

    public int getPercent() {
        return percent;
    }

    public String getDecision() {
        return decision;
    }

    public List<String> getReasons() {
        return reasons;
    }

    public Long getOrderId() {
        return orderId;
    } // ← getter for frontend JSON
}

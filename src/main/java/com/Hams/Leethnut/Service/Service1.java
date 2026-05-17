package com.Hams.Leethnut.Service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.Hams.Leethnut.model.Order1;
import com.Hams.Leethnut.repo.Repo;

@Service
public class Service1 {
    private final Repo repo;

    public Service1(Repo repo) {
        this.repo = repo;
    }

    private static final double BIAS = -2.5;
    private static final double W_ADDRESS = 0.2;
    private static final double W_SLOT = 0.2;
    private static final double W_PAST_FAIL = 0.2;
    private static final double W_ZONE_RATE = 0.2;
    private static final double W_COD = 0.2;
    private static final double W_WEATHER = 0.5;
    private static final double W_DAY = 0.3;

    public PredictionResult predict(Order1 order) {

        String areaKey = (order.getArea() == null) ? "" : order.getArea().trim().toUpperCase();

        // ── Pull from DB, not from user input ──────────────────────
        long totalConfirmed = repo.countConfirmedByArea(areaKey);
        long totalFailed = repo.countFailedByArea(areaKey);

        // zoneRate: real failure rate from past feedback
        double zoneRate = (totalConfirmed > 0)
                ? (double) totalFailed / totalConfirmed
                : 0.0; // new area → default safe

        // pastFail: how many times this area saw failures (capped at 3)
        double pastFailScore = Math.min(totalFailed, 3) / 3.0;

        // addressScore: same as zoneRate here (area-level risk)
        double addressScore = zoneRate;

        double slotScore = encodeSlot(order.getSlotScore());
        double codScore = order.isCod() ? 1.0 : 0.0;
        double dayScore = encodeDay(order.getDayOfWeek());
        double weatherScore = order.getWeatherScore();

        double z = BIAS
                + (W_ADDRESS * addressScore)
                + (W_SLOT * slotScore)
                + (W_PAST_FAIL * pastFailScore)
                + (W_ZONE_RATE * zoneRate)
                + (W_COD * codScore)
                + (W_WEATHER * weatherScore)
                + (W_DAY * dayScore);

        double probability = sigmoid(z);
        int percent = (int) (probability * 100);

        // ── DEFAULT = DELIVER unless DB data proves risky ──────────
        String decision = percent <= 35
                ? "deliver first, ask feedback later"
                : "do not deliver without confirmation";

        order.setPredictionResult(decision);
        order.setPredictionPercent(percent);
        order.setDeliveryStatus(null); // pending feedback
        repo.save(order);

        return new PredictionResult(percent, decision,
                buildReasons(order, addressScore, totalFailed, totalConfirmed), order.getId());
    }

    public String submitFeedback(Long orderId, String status) {
        Order1 order = repo.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));

        String normalized = status.trim().toUpperCase();
        if (!normalized.equals("SUCCESS") && !normalized.equals("FAILED")) {
            return "Invalid status. Use SUCCESS or FAILED.";
        }

        order.setDeliveryStatus(normalized);
        repo.save(order);

        String area = order.getArea().trim().toUpperCase();
        long total = repo.countConfirmedByArea(area);
        long failed = repo.countFailedByArea(area);
        int rate = total > 0 ? (int) ((double) failed / total * 100) : 0;

        return "Feedback saved. Area '" + order.getArea() + "' now has "
                + failed + " failures out of " + total
                + " confirmed orders (" + rate + "% failure rate).";
    }

    private double sigmoid(double z) {
        return 1.0 / (1.0 + Math.exp(-z));
    }

    private double encodeSlot(double hour) {
        if (hour >= 23 || hour < 6)
            return 1.0;
        if (hour >= 22 && hour < 23)
            return 0.5;
        return 0.0;
    }

    private double encodeDay(int day) {
        return (day == 6 || day == 7) ? 1.0 : 0.5; // weekend vs weekday
    }

    private List<String> buildReasons(Order1 order, double areaScore,
            long failed, long total) {
        List<String> reasons = new ArrayList<>();

        if (total == 0) {
            reasons.add("Area '" + order.getArea() + "' is new — defaulting to SAFE");
        } else if (areaScore >= 0.5) {
            reasons.add("Area '" + order.getArea() + "' has "
                    + (int) (areaScore * 100) + "% failure rate from past deliveries");
        }

        if (failed >= 2) {
            reasons.add("This area had " + failed + " past failed deliveries");
        }

        if (order.getSlotScore() >= 18)
            reasons.add("Evening delivery slot — higher risk");

        if (order.isCod())
            reasons.add("COD order — customer less committed");

        if (order.getWeatherScore() == 1.0)
            reasons.add("Bad weather expected in delivery window");

        return reasons;
    }
}

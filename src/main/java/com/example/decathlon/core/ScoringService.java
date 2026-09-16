package com.example.decathlon.core;

import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ScoringService {
    public enum Type { TRACK, FIELD }
    public record EventDef(String id, Type type, double A, double B, double C, String unit) {}

    private final Map<String, EventDef> events = Map.ofEntries(
            Map.entry("100m", new EventDef("100m", Type.TRACK, 25.4347, 18.0, 1.81, "s")),
            Map.entry("400m", new EventDef("400m", Type.TRACK, 1.53775, 82.0, 1.81, "s")),
            Map.entry("110mHurdles", new EventDef("110mHurdles", Type.TRACK, 5.74352, 28.5, 1.92, "s")),
            Map.entry("1500m", new EventDef("1500m", Type.TRACK, 0.03768, 480.0, 1.85, "s")),
            Map.entry("longJump", new EventDef("longJump", Type.FIELD, 0.14354, 220.0, 1.4, "cm")),
            Map.entry("highJump", new EventDef("highJump", Type.FIELD, 0.8465, 75.0, 1.42, "cm")),
            Map.entry("poleVault", new EventDef("poleVault", Type.FIELD, 0.2797, 100.0, 1.35, "cm")),
            Map.entry("shotPut", new EventDef("shotPut", Type.FIELD, 51.39, 1.5, 1.05, "m")),
            Map.entry("discusThrow", new EventDef("discusThrow", Type.FIELD, 12.91, 4.0, 1.1, "m")),
            Map.entry("javelinThrow", new EventDef("javelinThrow", Type.FIELD, 10.14, 7.0, 1.08, "m")),
            Map.entry("hep100mHurdles", new EventDef("hep100mHurdles", Type.TRACK, 9.23076, 26.7, 1.835, "s")),
            Map.entry("hep200m", new EventDef("hep200m", Type.TRACK, 4.99087, 42.5, 1.81, "s")),
            Map.entry("hep800m", new EventDef("hep800m", Type.TRACK, 0.11193, 254.0, 1.88, "s")),
            Map.entry("hepLongJump", new EventDef("hepLongJump", Type.FIELD, 0.188807, 210.0, 1.41, "cm")),
            Map.entry("hepHighJump", new EventDef("hepHighJump", Type.FIELD, 1.84523, 75.0, 1.348, "cm")),
            Map.entry("hepShotPut", new EventDef("hepShotPut", Type.FIELD, 56.0211, 1.5, 1.05, "m")),
            Map.entry("hepJavelinThrow", new EventDef("hepJavelinThrow", Type.FIELD, 15.9803, 3.8, 1.04, "m"))
    );

    public EventDef get(String id) { return events.get(id); }

    public int score(String eventId, double raw) {
        EventDef e = events.get(eventId);
        if (e == null) return 0;
        double points;
        if (e.type == Type.TRACK) {
            double x = e.B - raw;
            if (x <= 0) return 0;
            points = e.A * Math.pow(x, e.C);
        } else {
            double x = raw - e.B;
            if (x <= 0) return 0;
            points = e.A * Math.pow(x, e.C);
        }
        return (int)Math.floor(points);
    }
}

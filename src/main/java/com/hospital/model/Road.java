package com.hospital.model;

import java.util.Objects;

public class Road {
    private final String roadId;
    private final String fromLocationId;
    private final String toLocationId;
    private final double distanceKm;
    private final double travelTimeMin;
    private final double roadConditionWeight;

    public Road(String roadId, String fromLocationId, String toLocationId, double distanceKm, double travelTimeMin,
            double roadConditionWeight) {
        this.roadId = roadId;
        this.fromLocationId = fromLocationId;
        this.toLocationId = toLocationId;
        this.distanceKm = distanceKm;
        this.travelTimeMin = travelTimeMin;
        this.roadConditionWeight = roadConditionWeight;
    }

    public String getRoadId() {
        return roadId;
    }

    public String getFromLocationId() {
        return fromLocationId;
    }

    public String getToLocationId() {
        return toLocationId;
    }

    public double getDistanceKm() {
        return distanceKm;
    }

    public double getTravelTimeMin() {
        return travelTimeMin;
    }

    public double getRoadConditionWeight() {
        return roadConditionWeight;
    }

    @Override
    public String toString() {
        return "Road{" +
                "roadId=" + roadId +
                ", fromLocationId=" + fromLocationId +
                ", toLocationId=" + toLocationId +
                ", distanceKm=" + distanceKm +
                ", travelTimeMin=" + travelTimeMin +
                ", roadConditionWeight=" + roadConditionWeight +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Road road))
            return false;
        return Objects.equals(roadId, road.roadId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(roadId);
    }
}

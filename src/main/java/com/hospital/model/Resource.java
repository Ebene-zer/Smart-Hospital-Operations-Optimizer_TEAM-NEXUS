package com.hospital.model;

import java.util.Objects;

public class Resource {
    private final String resourceId;
    private final String type;
    private final String homeLocationId;
    private final Integer capacity;
    private final String availabilityStatus;

    public Resource(String resourceId, String type, String homeLocationId, Integer capacity, String availabilityStatus) {
        this.resourceId = resourceId;
        this.type = type;
        this.homeLocationId = homeLocationId;
        this.capacity = capacity;
        this.availabilityStatus = availabilityStatus;
    }

    public String getResourceId() {
        return resourceId;
    }

    public String getType() {
        return type;
    }

    public String getHomeLocationId() {
        return homeLocationId;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public String getAvailabilityStatus() {
        return availabilityStatus;
    }

    @Override
    public String toString() {
        return "Resource{" +
                "resourceId=" + resourceId +
                ", type='" + type + '\'' +
                ", homeLocationId=" + homeLocationId +
                ", capacity=" + capacity +
                ", availabilityStatus='" + availabilityStatus + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Resource resource))
            return false;
        return Objects.equals(resourceId, resource.resourceId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(resourceId);
    }
}

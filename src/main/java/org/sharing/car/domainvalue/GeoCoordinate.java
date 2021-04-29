package org.sharing.car.domainvalue;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import org.springframework.data.geo.Point;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Embeddable
public class GeoCoordinate {

    @Getter
    @Min(value = -90, message = "latitude is lower than -90")
    @Max(value = 90, message = "latitude is higher than 90")
    @Transient
    private double latitude;

    @Getter
    @Min(value = -180, message = "longitude is lower than -180")
    @Max(value = 180, message = "longitude is lower than 180")
    @Transient
    private double longitude;

    @JsonIgnore
    @Column(name = "coordinate")
    private Point point = new Point(this.latitude, this.longitude);

    private GeoCoordinate() {
    }

    public GeoCoordinate(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.point = new Point(latitude, longitude);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.point == null) ? 0 : this.point.hashCode());
        return result;
    }


    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        final GeoCoordinate other = (GeoCoordinate) obj;
        if (this.point == null) {
            if (other.point != null) {
                return false;
            }
        } else if (!this.point.equals(other.point)) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return this.point.toString();
    }

}

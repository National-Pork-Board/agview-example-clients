package org.npb.agview.api.example;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class MovementAddresses {
    private int movementId;
    private String source;
    private String sourceLatitude;
    private String sourceLongitude;
    private String sourceStreetAddress;
    private String sourceCity;
    private String sourceState;
    private String sourceZip;

    private String target;
    private String targetLatitude;
    private String targetLongitude;
    private String targetStreetAddress;
    private String targetCity;
    private String targetState;
    private String targetZip;
}

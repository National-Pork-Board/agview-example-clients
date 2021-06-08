package com.agview.api.example;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class MovementAddresses {
    private String referenceId;
    private String sourceStreetAddress;
    private String sourceCity;
    private String sourceState;
    private String sourceZip;

    private String targetStreetAddress;
    private String targetCity;
    private String targetState;
    private String targetZip;
}

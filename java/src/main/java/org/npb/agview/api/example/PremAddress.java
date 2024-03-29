package org.npb.agview.api.example;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class PremAddress {
    private String usdaPin;
    private String streetAddress;
    private String city;
    private String state;
    private String zip;
}

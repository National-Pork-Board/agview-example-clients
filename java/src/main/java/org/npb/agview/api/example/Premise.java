package org.npb.agview.api.example;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Premise {
    private String usdaPin;
    private String premName;
    private String species;
    private String iceContactPhone;
    private String iceContactEmail;
    private String locationType;
    private int siteCapacityNumberBarns;
    private int siteCapacityNumberAnimals;
    private int numberOfAnimalsOnSite;
}

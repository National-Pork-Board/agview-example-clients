package org.npb.agview.api.example;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Movement {
    private int movementId;
    private String species;
    private int numberInShipment;
    private String movementDatetime;
    private String movementType;
}

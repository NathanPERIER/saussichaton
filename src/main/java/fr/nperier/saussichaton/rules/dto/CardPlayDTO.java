package fr.nperier.saussichaton.rules.dto;

import fr.nperier.saussichaton.GlobalConstants;
import fr.nperier.saussichaton.engine.GameState;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

/**
 * Object used as an intermediary to read a card play from a JSON file.
 */
@Getter
public class CardPlayDTO {

    private String extension = GlobalConstants.BASE_EXTENSION;
    @Setter
    private Map<String, Integer> cards;
    @Setter
    private List<GameState> states;
    @Setter
    private String action;

    public void setExtension(String extension) {
        if(extension != null) {
            this.extension = extension;
        }
    }

}

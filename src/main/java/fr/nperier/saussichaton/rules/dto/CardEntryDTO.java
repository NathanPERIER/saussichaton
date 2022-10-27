package fr.nperier.saussichaton.rules.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import fr.nperier.saussichaton.GlobalConstants;
import fr.nperier.saussichaton.engine.GameState;
import fr.nperier.saussichaton.utils.MappingFunction;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * Object used as an intermediary to read a card from a JSON file.
 *
 * @see fr.nperier.saussichaton.rules.data.Card
 */
public class CardEntryDTO {

    @Getter @Setter
    private String name;
    @Getter
    private String extension = GlobalConstants.BASE_EXTENSION;
    @JsonIgnore
    private MappingFunction<Integer> numberFunction;
    @JsonProperty("given_at_start")
    @Getter @Setter
    private int givenAtStart = 0;
    @Getter @Setter
    @JsonProperty("insert_state")
    private GameState insertState;
    @Getter @Setter
    @JsonProperty("draw_action")
    private String drawAction = null;

    @JsonProperty("count_in_pile")
    public void setNumberMapping(final Map<String, Integer> mapping) {
        numberFunction = new MappingFunction<>(mapping);
    }

    public void setExtension(String extension) {
        if(extension != null) {
            this.extension = extension;
        }
    }

    public int getInitialNumber(final int nPlayers) {
        return numberFunction.apply(nPlayers);
    }
}

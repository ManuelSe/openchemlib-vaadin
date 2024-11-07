package ch.artaios.openchemlib.vaadin;

import com.actelion.research.chem.reaction.Reaction;

/**
 * Project: openchemlib-vaadin
 * Date:    22.10.24
 * <p>
 * Copyright (c) 2017 - 2024
 * Idorsia Pharmaceuticals Ltd.
 * Hegenheimermattweg 91
 * CH-4123 Allschwil, Switzerland
 * <p>
 * All Rights Reserved.
 * <p>
 * This software is the proprietary information of Idorsia Pharmaceuticals, Ltd.
 * Use is subject to license terms.
 * <p>
 * Author: Roman Bär
 */

/**
 * View for reactions
 * @see OpenChemLibEditor
 */
public class ReactionView extends OpenChemLibEditor<Reaction> {
    /**
     * Constructor
     * @see OpenChemLibEditor
     *
     * Initializes the view with an empty reaction
     *
     * @param fragment if true, fragment mode is enabled
     * @param editable if true, the editor is editable (e.g. by setting the idcode by pasting/dropping)
     */
    public ReactionView(boolean fragment, boolean editable) {
        super(Mode.REACTION, fragment, true, editable, new Reaction(), ChemUtils.PRESENTATION_TO_MODEL_REACTION, ChemUtils.MODEL_TO_PRESENTATION_REACTION);
    }
}

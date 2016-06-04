package qcox.tacoma.uw.edu.farmgame;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

/**
 * This is used to communicate between FieldPlantSeedListDialogFragment and FarmFragment
 * by letting FarmActivity implement this interface.
 * @author James, Quinn
 * @version 1.0
 * @since 2016-5-4
 */
public interface Communicater {
    void plantSeed (String seed);
}

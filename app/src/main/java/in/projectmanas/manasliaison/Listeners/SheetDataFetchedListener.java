package in.projectmanas.manasliaison.Listeners;

import java.util.ArrayList;

/**
 * Created by knnat on 9/14/2017.
 */

public interface SheetDataFetchedListener {
    void processFinish(ArrayList<ArrayList<ArrayList<String>>> output);
}

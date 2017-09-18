package in.projectmanas.manasliaison.listeners;

import java.util.ArrayList;

/**
 * Created by knnat on 9/14/2017.
 */

public interface SheetDataFetchedListener {
    void onProcessFinish(ArrayList<ArrayList<ArrayList<String>>> outputList);
}

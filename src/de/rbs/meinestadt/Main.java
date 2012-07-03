package de.rbs.meinestadt;

import de.rbs.meinestadt.datatypes.SpecialBuilding;
import de.rbs.meinestadt.gui.ProgressWindow;
import de.rbs.meinestadt.statusChangeEvent.StatusChange;
import de.rbs.meinestadt.statusChangeEvent.StatusEventListener;
import org.htmlparser.util.ParserException;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * User: rbs
 * Date: 30.05.12
 */
public class Main {

    public static void main(String[] args) throws IOException, ParserException, InterruptedException, ClassNotFoundException, SQLException {


        System.setProperty("proxyPort","8123");
        System.setProperty("proxyHost","192.168.1.100");

        final ProgressWindow progressWindow = new ProgressWindow();

        MeineStadtAnalyser meineStadtAnalyser = new MeineStadtAnalyser();
        meineStadtAnalyser.writeBuildings2DB(new ArrayList<SpecialBuilding>());
        meineStadtAnalyser.addStatusListener(new StatusEventListener() {
            @Override
            public void statusChanged(StatusChange change) {
                progressWindow.setMsg(change.getActual_message());
                progressWindow.setPercentage(change.getPercentForActualTask());
                progressWindow.setTaskProcess(change.getType(), change.getActual_elem(), change.getMax_elems());
            }
        });
        meineStadtAnalyser.start();
    }
}

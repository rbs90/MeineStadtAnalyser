package de.rbs.meinestadt.gui;

import de.rbs.meinestadt.statusChangeEvent.StatusType;

import javax.swing.*;
import javax.swing.text.html.HTMLDocument;
import java.awt.*;
import java.text.DecimalFormat;

/**
 * User: rbs
 * Date: 05.06.12
 */
public class ProgressWindow extends JFrame{

    private final JProgressBar progress;
    private final JLabel status;
    private final JProgressBar alphabet;
    private final JProgressBar branchen;
    private final JProgressBar buildings;
    private final JProgressBar database;
    private final JProgressBar memConsumption;

    public ProgressWindow() {
        setMinimumSize(new Dimension(400, 400));

        alphabet = new JProgressBar();
        alphabet.setStringPainted(true);
        branchen = new JProgressBar();
        branchen.setStringPainted(true);
        buildings = new JProgressBar();
        buildings.setStringPainted(true);
        database = new JProgressBar();
        database.setStringPainted(true);
        memConsumption = new JProgressBar();
        memConsumption.setStringPainted(true);

        JPanel subProcesses = new JPanel();
        subProcesses.setLayout(new GridLayout(4,2));
        subProcesses.add(new JLabel("Alphabetische Analyse"));
        subProcesses.add(alphabet);
        subProcesses.add(new JLabel("Branchensuche"));
        subProcesses.add(branchen);
        subProcesses.add(new JLabel("Gebäudesuche"));
        subProcesses.add(buildings);
        subProcesses.add(new JLabel("Gebäude Speichern"));
        subProcesses.add(database);

        this.setLayout(new BorderLayout());
        progress = new JProgressBar();
        this.add(progress, BorderLayout.SOUTH);
        progress.setStringPainted(true);
        status = new JLabel();
        this.add(status, BorderLayout.CENTER);        
        setVisible(true);

        this.add(subProcesses, BorderLayout.NORTH);
        this.add(memConsumption, BorderLayout.EAST);
    }
    
    public void setPercentage(float perc){
        this.progress.setValue((int) perc);
        DecimalFormat df = new DecimalFormat("#.##");
        this.progress.setString(df.format(perc) + "%");
    }
    
    public void setMsg(String msg){
        this.status.setText(msg);
    }

    public void setTaskProcess(StatusType type, int act, int max){
        switch (type){
            case ALPHABETIC_BRANCH_SEARCH:
                alphabet.setMinimum(0);
                alphabet.setMaximum(max);
                alphabet.setValue(act);
                alphabet.setString(act + "/" +  max);
                break;
            case BRANCH_SEARCH:
                branchen.setMinimum(0);
                branchen.setMaximum(max);
                branchen.setValue(act);
                branchen.setString(act + "/" +  max);
                break;
            case BUILDING_SEARCH:
                buildings.setMinimum(0);
                buildings.setMaximum(max);
                buildings.setValue(act);
                buildings.setString(act + "/" +  max);
                break;
            case DB_WRITE:
                database.setMinimum(0);
                database.setMaximum(max);
                database.setValue(act);
                database.setString(act + "/" +  max);
                break;
        }

        //long freeMem = Runtime.getRuntime().freeMemory();
        long totalMem = Runtime.getRuntime().totalMemory() / (1024 * 1024);
        long maxMem = Runtime.getRuntime().maxMemory() / (1024 * 1024);

        this.memConsumption.setString(totalMem + " MB / " + maxMem + " MB");
        this.memConsumption.setMinimum(0);
        this.memConsumption.setMaximum((int) maxMem);
        this.memConsumption.setValue((int) totalMem);
    }
}

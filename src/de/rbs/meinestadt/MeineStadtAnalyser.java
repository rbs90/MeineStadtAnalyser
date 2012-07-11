package de.rbs.meinestadt;

import de.rbs.meinestadt.datatypes.Branch;
import de.rbs.meinestadt.datatypes.SpecialBuilding;
import de.rbs.meinestadt.datatypes.SpecialBuildingCollection;
import de.rbs.meinestadt.statusChangeEvent.StatusChange;
import de.rbs.meinestadt.statusChangeEvent.StatusEventListener;
import de.rbs.meinestadt.statusChangeEvent.StatusType;
import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.Tag;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.lexer.Lexer;
import org.htmlparser.lexer.Page;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.htmlparser.util.SimpleNodeIterator;
import org.htmlparser.visitors.NodeVisitor;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.*;
import java.util.ArrayList;

public class MeineStadtAnalyser {

    ArrayList<StatusEventListener> listeners = new ArrayList<StatusEventListener>();
    
    public static final int MAX_TASKS = 3;
    
    private int complete_size;

    private final SpecialBuildingCollection buildings = new SpecialBuildingCollection();
    private int running = 0;
    private int done = 0;
    private final ArrayList<BuildingDataExtractor> runningThreads;

    public MeineStadtAnalyser() {
        runningThreads = new ArrayList<BuildingDataExtractor>();
    }

    public void start() throws IOException, ParserException, InterruptedException, ClassNotFoundException, SQLException {
        String baseURL = "http://home.meinestadt.de";
        String base = "hohenstein-ernstthal";

        //get streets
        /*
        ArrayList<String> nearCitys = getNearCitys(base);
        System.out.println("nearcitys: " + nearCitys.size());
        nearCitys.add(base);



        PreparedStatement statement = DatabaseManager.getConnection().prepareStatement("INSERT INTO streets(name, region) VALUES (?, ?)");
        PreparedStatement cityAdd = DatabaseManager.getConnection().prepareStatement("INSERT INTO region(name) VALUES (?)");

        for(int i = 0; i < nearCitys.size(); i++){
            String city = nearCitys.get(i);
            cityAdd.setString(1, city);
            cityAdd.addBatch();

            ArrayList<String> streets = getStreets(city);
            for(String street : streets){
                statement.setString(1, street);
                statement.setInt(2, i+1);
                statement.addBatch();
            }
        }

        System.out.println("Starte citys");
        cityAdd.executeBatch();
        System.out.println("Ende citys, start streets");
        statement.executeBatch();
        System.out.println("Ende streets");



        System.out.println("ENDE Straﬂen, BEGIN SpecialBuildings");


         */

        ArrayList<String> alphabeticHrefs = getAlphabeticHrefs(baseURL + "/" + base);
        ArrayList<Branch> branches = getBranches(baseURL, alphabeticHrefs);
        ArrayList<SpecialBuilding> buildings = getSpecialBuildings(branches);
        writeBuildings2DB(buildings);
    }

    private ArrayList<String> getNearCitys(String city) {
        final ArrayList<String> citys = new ArrayList<String>();
        String href = "http://home.meinestadt.de/" + city;
        try {
            URL url = new URL(href);
            URLConnection urlConnection = null;
            urlConnection = url.openConnection();
            Parser parser = new Parser(new Lexer(new Page(urlConnection)));
            parser.visitAllNodesWith(new NodeVisitor() {

                @Override
                public void visitTag(Tag tag) {

                    //analysis area not reached:
                    if(tag.getTagName().equalsIgnoreCase("div")){
                        if(tag.getChildren() != null){
                            if(tag.getFirstChild().toHtml().contains("Umgebung von ")){
                                NodeList children = tag.getParent().getChildren().elementAt(3).getChildren();
                                SimpleNodeIterator it = children.elements();
                                while(it.hasMoreNodes()){
                                    Node node = it.nextNode();
                                    if(node.getFirstChild() != null){
                                        String loc = ((Tag) node.getChildren().elementAt(1)).getAttribute("href").replaceFirst("/", "");
                                        //node.getChildren().elementAt(1).getChildren().
                                        //getChildren().toHtml();
                                        citys.add(loc);
                                    }
                                }
                            }
                        }
                    }
                }
            });
        } catch (MalformedURLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (ParserException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return citys;
    }

    private ArrayList<String> getStreets(String city) {

        final ArrayList<String> result = new ArrayList<String>();

        for(char i = 'a'; i <= 'z'; i++){
            String href = "http://www.meinestadt.de/" + city + "/stadtplan/strassenverzeichnis/" + i;
            //System.out.println("analying: " + href);
            new StreetExtractor(href) {
                @Override
                public void finished(ArrayList<String> streets) {
                    result.addAll(streets);
                    running--;
                }
            }.start();
            running++;
        }

        //wait for threads finishing:
        while(running > 0){

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
        return result;
    }

    abstract class StreetExtractor extends Thread{

        ArrayList<String> streets = new ArrayList<String>();
        private String href;

        protected StreetExtractor(String href) {
            this.href = href;
        }

        public void run() {
            URL url = null;
            try {
                url = new URL(href);
                URLConnection urlConnection = null;
                urlConnection = url.openConnection();
                Parser parser = new Parser(new Lexer(new Page(urlConnection)));
                parser.visitAllNodesWith(new NodeVisitor() {
                    @Override
                    public void visitTag(Tag tag) {
                        if(tag.getTagName().equalsIgnoreCase("a")){
                            if(tag.getAttribute("href") != null)
                                if(tag.getAttribute("href").contains("stadtplan/strasse/")){
                                    String street = tag.getFirstChild().toHtml().replace("str.", "straﬂe").replace("Str.", "Straﬂe").trim();
                                    streets.add(street);
                                }
                        }
                    }

                    @Override
                    public void finishedParsing() {
                        finished(streets);
                    }
                });
            } catch (MalformedURLException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (ParserException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }

        public abstract void finished(ArrayList<String> streets);
    }

    private ArrayList<String> getAlphabeticHrefs(String siteUrl) throws IOException, ParserException {

        //System.setProperty("http.proxyHost", "192.168.1.100");
        //System.setProperty("http.proxyPort", "8123");

        final ArrayList<String> hrefs = new ArrayList<String>();

        fireStatusChangeEvent(StatusType.ALPHABETIC_BRANCH_SEARCH, "Suche alphabetische Unterseiten...", 0, 0);
        URL url = new URL(siteUrl);
        URLConnection urlConnection = url.openConnection();
        Parser parser = new Parser(new Lexer(new Page(urlConnection)));
        parser.visitAllNodesWith(new NodeVisitor() {
            @Override
            public void visitTag(Tag tag) {
                if (tag.getTagName().equalsIgnoreCase("a")) {
                    if (tag.getChildren() != null) {
                        String content = tag.getChildren().toHtml();
                        if (content.length() == 1) {
                            String href = tag.getAttribute("href");
                            if (href != null)
                                hrefs.add(href);
                        }
                    }
                }
            }
        });
        fireStatusChangeEvent(StatusType.ALPHABETIC_BRANCH_SEARCH, "Suche alphabetische Unterseiten...", hrefs.size(), hrefs.size());

        return hrefs;
    }

    private ArrayList<Branch> getBranches(final String base_url, ArrayList<String> alphabetHrefs) throws IOException, ParserException {

        final ArrayList<String> branchHrefs = new ArrayList<String>();
        final ArrayList<String> categoryHrefs = alphabetHrefs;
        
        final ArrayList<Branch> branchList = new ArrayList<Branch>();
        ArrayList<String> scanHrefs = new ArrayList<String>(categoryHrefs);
        int i = 0;

        complete_size = scanHrefs.size();
        int done_size = 0;

        fireStatusChangeEvent(StatusType.BRANCH_SEARCH, "Suche Branchen...", done_size, complete_size);

        do {
            scanHrefs.clear();
            scanHrefs.addAll(categoryHrefs);
            categoryHrefs.clear();

            for (String letter : scanHrefs) {
                URL url = new URL(letter);
                URLConnection urlConnection = url.openConnection();
                Parser parser = new Parser(new Lexer(new Page(urlConnection)));
                parser.visitAllNodesWith(new LetterParser() {
                    @Override
                    public void branchFound(Branch branch) {
                        if(branch.getHref().contains("brazl"))
                                branchList.add(branch);
                        else{
                            categoryHrefs.add("http://branchenbuch.meinestadt.de" + branch.getHref());
                            complete_size++;
                            //System.out.println("added category");
                        }

                    }
                });
                done_size++;
                fireStatusChangeEvent(StatusType.BRANCH_SEARCH, "Suche Branchen...", done_size, complete_size);
            }
            System.out.println("ROUND: " + i + "  --- " + branchHrefs.size() + " branches, " + categoryHrefs.size() + " cats");

            i++;



        } while (!categoryHrefs.isEmpty());

        return branchList;
    }

    public void writeBranches2DB(ArrayList<Branch> branches){

        try {
            PreparedStatement preparedStatement = DatabaseManager.getConnection().prepareStatement("INSERT INTO branches(name, href) VALUES (?, ?)");

            for(Branch branch : branches){
                preparedStatement.setString(1, branch.getName());
                preparedStatement.setString(2, branch.getHref());
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();

        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public abstract class LetterParser extends NodeVisitor {
        
        Boolean analyserArea = false;

        @Override
        public void visitTag(Tag tag) {
            if(analyserArea){
                if(tag.getTagName().equalsIgnoreCase("a")){
                    String href = tag.getAttribute("href");
                    if (href != null)
                        branchFound(new Branch(tag.getFirstChild().toHtml(), href));
                }
            }
            else{
                if(tag.getTagName().equalsIgnoreCase("div")) {
                    String classAttribute = tag.getAttribute("class");
                    if (classAttribute != null && classAttribute.contains("branchen-kat"))
                        analyserArea = true;
                }
            }
        }

        @Override
        public void visitEndTag(Tag tag) {
            if(analyserArea && tag.getTagName().equalsIgnoreCase("table"))
                analyserArea = false;
        }

        public abstract void branchFound(Branch branch);
    }

    private ArrayList<SpecialBuilding> getSpecialBuildings(final ArrayList<Branch> branches) throws IOException, ParserException, InterruptedException {

        int started = 0;
        done = 0;
        for(int i = 0; i < branches.size(); i++){

            try {
                while(running > 10){ Thread.sleep(100); }
            } catch (InterruptedException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            };

            started ++;
            running ++;

            BuildingDataExtractor buildingDataExtractor = new BuildingDataExtractor(branches.get(i).getHref()) {
                @Override
                public void finished() {
                    running--;
                    done++;
                    runningThreads.remove(this);
                    fireStatusChangeEvent(StatusType.BUILDING_SEARCH, "Suche Geb‰ude...", done, branches.size());
                }
            };
            runningThreads.add(buildingDataExtractor);
            buildingDataExtractor.start();

        }

        while(done < branches.size()) { Thread.sleep(100); };

        return buildings;
        
    }

    abstract class BuildingDataExtractor extends Thread {

        private URL url;

        BuildingDataExtractor(String scanUrl) throws MalformedURLException {
            url = new URL("http://branchenbuch.meinestadt.de" + scanUrl);

        }

        @Override
        public void run() {
            //System.out.println("Thread started");
            try{
                URLConnection urlConnection = url.openConnection();
                Parser parser = new Parser(new Lexer(new Page(urlConnection)));
                parser.visitAllNodesWith(new BranchParser() {
                    @Override
                    public void buildingFound(SpecialBuilding building) {
                        synchronized (building){
                            if(!buildings.containsBuilding(building))
                                buildings.add(building);
                        }
                    }
                });
            } catch (Exception e){
                System.out.println("ERROR @ " + url);
                e.printStackTrace();
            }
            finished();
        }
        
        public abstract void finished();
    }
    
    public void writeBuildings2DB(ArrayList<SpecialBuilding> specialBuildings) throws ClassNotFoundException, SQLException {
        DatabaseManager.getStatement().executeUpdate("drop table if exists specialBuildings;");
        DatabaseManager.getStatement().executeUpdate("create table specialBuildings (id INTEGER PRIMARY KEY, name VARCHAR, street_id INTEGER, hnr VARCHAR, branch_id INTEGER );");
        String statement = "INSERT INTO specialBuildings(name, branch_id, street_id, hnr) VALUES(?, ?, ?, ?)";
        PreparedStatement preparedStatement = DatabaseManager.getConnection().prepareStatement(statement);

        for(int k = 0; k < specialBuildings.size(); k+=10){
            for(int i = k; i < (k + 10); i++){
                if(i + 1 > specialBuildings.size()) continue;

                SpecialBuilding specialBuilding = specialBuildings.get(i);
                preparedStatement.setString(1, specialBuilding.getName());
                preparedStatement.setString(2, String.valueOf(specialBuilding.getBranch_id()));
                preparedStatement.setString(3, String.valueOf(specialBuilding.getAddress().getStreet().getId()));
                preparedStatement.setString(4, String.valueOf(specialBuilding.getAddress().getHnr()));
                preparedStatement.addBatch();
                fireStatusChangeEvent(StatusType.DB_WRITE, "Geb‰ude speichern", i + 1, specialBuildings.size());
            }
            preparedStatement.executeBatch();
        }
    }

    public abstract class BranchParser extends NodeVisitor {
        //TODO: support multiple sites
        
        //TODO: insert branch saving
        //TODO: insert Branch_id
        int branch_id = 0;


        @Override
        public void visitTag(Tag tag) {
            if(tag.getAttribute("class") != null && tag.getAttribute("class").equalsIgnoreCase("mt_ms_left_print_strait")){
                NodeList children = tag.getChildren();
                Tag addressTag = (Tag) children.extractAllNodesThatMatch(new HasAttributeFilter("class", "mt-ms_address")).elementAt(0);
                Tag nameTag = (Tag) children.elementAt(1).getChildren().extractAllNodesThatMatch(new HasAttributeFilter("class", "katalogtitel-gratis")).elementAt(0);
                String name = nameTag.getChildren().toHtml();
                String address = addressTag.getChildren().toHtml();
                buildingFound(new SpecialBuilding(name , address, branch_id));
            }
        }

        public abstract void buildingFound(SpecialBuilding building);
    }

    public void addStatusListener(StatusEventListener listener){
        this.listeners.add(listener);        
    }
    
    public void fireStatusChangeEvent(StatusType type, String msg, int act, int max){
        for (StatusEventListener listener : listeners){
            listener.statusChanged(new StatusChange(type, msg, act, max));
        }
    }

}
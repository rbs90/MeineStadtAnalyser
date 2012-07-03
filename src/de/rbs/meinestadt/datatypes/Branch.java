package de.rbs.meinestadt.datatypes;

/**
 * Created by IntelliJ IDEA.
 * User: rbs
 * Date: 21.06.12
 * Time: 09:04
 * To change this template use File | Settings | File Templates.
 */
public class Branch {
    String name;
    String href;

    public Branch(String name, String href) {
        this.name = name;
        this.href = href;
    }

    public String getName() {
        return name;
    }

    public String getHref() {
        return href;
    }
}

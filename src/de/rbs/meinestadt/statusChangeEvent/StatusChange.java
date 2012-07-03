package de.rbs.meinestadt.statusChangeEvent;

/**
 * User: rbs
 * Date: 05.06.12
 */
public class StatusChange {
    private StatusType type;
    String actual_message;
    private int actual_elem;
    private int max_elems;

    public StatusChange(StatusType type, String actual_message, int actual_elem, int max_elems) {
        this.type = type;
        this.actual_message = actual_message;
        this.actual_elem = actual_elem;
        this.max_elems = max_elems;
    }

    public String getActual_message() {
        return actual_message;
    }

    public StatusType getType() {
        return type;
    }

    public int getActual_elem() {
        return actual_elem;
    }

    public int getMax_elems() {
        return max_elems;
    }

    public float getPercentForActualTask(){
        int max_task = StatusType.values().length;
        int task_number = type.ordinal();
        float task_percentage = 100 / (max_task);
        //System.out.println("DEBUG::: " + actual_elem + "/" + max_elems);

        float v = task_percentage * task_number + (task_percentage / max_elems) * actual_elem;
        if(v > 100){
            System.out.println("FEHLER!!!");
        }

        return v;

    }
}

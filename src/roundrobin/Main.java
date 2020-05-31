package roundrobin;

import java.util.ArrayList;
import java.util.List;

public class Main {
    
    public static List<Process> processList = new ArrayList<Process>();
    
    public static void main(String[] args) {
//        new MainFrame().setVisible(true);
    new ProcessingOverView(new ArrayList<Process>(), 2).setVisible(true);
    }
    
}

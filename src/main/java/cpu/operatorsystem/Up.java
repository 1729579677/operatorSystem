package cpu.operatorsystem;

import java.util.Comparator;

public class Up implements Comparator<Space> {
    @Override
    public int compare(Space a,Space b){
        if (a.getLength()>b.getLength())
            return 1;
        else
            return -1;
    }
}

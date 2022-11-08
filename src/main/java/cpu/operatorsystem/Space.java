package cpu.operatorsystem;

import java.util.Comparator;

public class Space implements Comparable<Space> {
    int length;
    int start;
    int end;
    public Space(){}

    public Space(int length, int start, int end) {
        this.length = length;
        this.start = start;
        this.end = end;
    }
    @Override
    public int compareTo(Space o) {
        if (o.getLength()>this.getLength())
            return 1;
        else
            return -1;

    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }
    public static void up(){

    }
}

package cpu.operatorsystem;

public class PCB {
    String name;
    String PID;
    String time;
    String priority;
    int state;
    PCB pcb;


    public PCB(){}

    public String getTime() {
        return time;
    }

    public PCB(String name, String time, String priority) {
        this.name = name;
        this.time = time;
        this.priority = priority;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPID() {
        return PID;
    }

    public void setPID(String PID) {
        this.PID = PID;
    }

    public String getState() {
        if(state==1)        return "后备";
        else if(state==2)   return "就绪";
        else if(state==3)   return "运行";
        else if(state==4)   return "挂起";
        else                return "终止";
    }

    public void setState(int state) {
        this.state = state;
    }
}

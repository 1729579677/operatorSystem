package cpu.operatorsystem;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;


import java.lang.reflect.Array;
import java.net.URL;
import java.util.*;

public class CPUscheduleController implements Initializable {
    int running=0;
    int pid=0;

    PCB[] memories=new PCB[50];
    boolean[] flags=new boolean[50];
    boolean[] head=new boolean[50];
    Vector<PCB> reserve=new Vector<PCB>();
    Vector<PCB> ready=new Vector<PCB>();
    Vector<PCB> end=new Vector<PCB>();
    Vector<PCB> tie=new Vector<PCB>();
    PCB rpcb=new PCB();
    int readyNum=0;
    boolean auto=true;
    int timeslice=0;
    int runtime=0;
    boolean istie=false;
    Space[] spaces=new Space[50];
    class autoRunning extends Thread{
        ActionEvent event;

        public autoRunning(ActionEvent event) {
            this.event = event;
        }

        @Override
        public void run() {
            auto=true;
            while(auto){
                runningChecked(event);
                try{
                    this.sleep(700);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    void sort(Vector<PCB> v){
        for(int i=0;i<v.size();i++){
            for(int j=0;j<v.size()-1;j++)
                if(Integer.parseInt(v.get(j).getPriority())>Integer.parseInt(v.get(j+1).getPriority())){
                    PCB p=v.get(j);
                    v.set(j,v.get(j+1));
                    v.set(j+1,p);
                }
        }
    }
    @FXML
    private TextArea memory;
    @FXML
    private TextField newLengthTxt;
    @FXML
    private ComboBox<String> CPUNumCmbbox;

    @FXML
    private TextField newTimeTxt;

    @FXML
    private TextArea endTxt;

    @FXML
    private Label CPUNumTxt;

    @FXML
    private TextArea reversePriorityTxt;

    @FXML
    private Button findBtn;

    @FXML
    private Button runningBtn;

    @FXML
    private TextField tieNumTxt;

    @FXML
    private Label newTxt;

    @FXML
    private Button addBtn;

    @FXML
    private ComboBox<String> newPriorityCmbbox;

    @FXML
    private Button resetBtn;

    @FXML
    private Button dispathBtn;

    @FXML
    private TextField runningPriorityTxt;

    @FXML
    private TextField findNameTxt;

    @FXML
    private TextField runningNameTxt;

    @FXML
    private TextField runningTimeTxt;

    @FXML
    private TextArea tieTimeTxt;

    @FXML
    private TextArea reserveNameTxt;

    @FXML
    private TextArea readyNameTxt;

    @FXML
    private TextField newNameTxt;

    @FXML
    private TextArea readyPrioirtyTxt;

    @FXML
    private TextField findStateTxt;

    @FXML
    private Button untieBtn;

    @FXML
    private ComboBox<String> modeCmbbox;
    @FXML
    private TextArea readyTimeTxt;

    @FXML
    private TextArea reserveTimeTxt;

    @FXML
    private TextArea tiePriorityTxt;

    @FXML
    private TextArea tieNameTxt;

    @FXML
    private TextField untieNumTxt;

    @FXML
    private Button tieBtn;

    @FXML
    private Button randomBtn;

    @FXML
    private Button pauseBtn;

    @FXML
    private Button autorunningBtn;
    @FXML
    private ComboBox<String> setTimeCmbbox;
    @FXML
    private ComboBox<String> devideCmbbox;

    public void priorityDispatch(){
        if(running==0){
            int pos=0,minn=1000;
            for(int i=0;i<ready.size();i++){
                if(Integer.parseInt(ready.get(i).getPriority())<minn) {
                    pos = i;
                    minn=Integer.parseInt(ready.get(i).getPriority());
                }
            }
            rpcb=ready.get(pos);
            rpcb.setState(3);
            ready.removeElementAt(pos);
            this.runningNameTxt.setText(rpcb.name);
            this.runningTimeTxt.setText(rpcb.time);
            this.runningPriorityTxt.setText(rpcb.priority);
            running++;
        }
    }
    public void timeDispatch(){
        if(running==0) {
            rpcb = ready.get(0);
            rpcb.setState(3);
            ready.removeElementAt(0);
            this.runningNameTxt.setText(rpcb.name);
            this.runningTimeTxt.setText(rpcb.time);
            this.runningPriorityTxt.setText(rpcb.priority);
            running++;
        }
    }
    public void firstMate(){
        for(int i=0;i<ready.size();i++){
            if(!ready.get(i).getMate()) {
                for(int j=0;j<50-Integer.parseInt(ready.get(i).getLength());j++){//可更改内存的大小
                    boolean flag1=true;
                    if(flags[j])
                        for(int k=1;k<Integer.parseInt(ready.get(i).getLength());k++){
                            if(!flags[j+k]) {flag1=false;break;}
                        }
                    else          continue;
                    if(flag1){
                        memories[j]=ready.get(i);
                        head[j]=true;
                        for(int k=j;k<j+Integer.parseInt(ready.get(i).getLength());k++)
                            flags[k]=false;
                        break;
                    }
                }
            }
            ready.get(i).isMate(true);
        }

    }
    public int findSpace(){
        int start=-1;
        int length=0;
        int end=0;
        int si=0;
        for(int i=0;i<50;i++){
            if(flags[i] && start==-1){
                start=i;
                length++;
            }else if(flags[i]){
                if(i==49)  {//这里有个小错误
                    end=i-1;
                    Space space=new Space(length,start,end);
                    si++;
                    spaces[si]=space;
                    start=-1;
                    length=0;
                }else
                length++;
            }else if(!flags[i] && start!=-1){
                end=i-1;
                Space space=new Space(length,start,end);
                si++;
                spaces[si]=space;
                start=-1;
                length=0;
            }
        }
        Arrays.sort(spaces,1,si+1);
        for(int i=1;i<=si;i++)
            System.out.println(spaces[i].getLength());
        return si;
    }

    public int findSpaceDowm(){
        int start=-1;
        int length=0;
        int end=0;
        int si=0;
        for(int i=0;i<50;i++){
            if(flags[i] && start==-1){
                start=i;
                length++;
            }else if(flags[i]){
                if(i==49)  {//这里有个小错误
                    end=i-1;
                    Space space=new Space(length,start,end);
                    si++;
                    spaces[si]=space;
                    start=-1;
                    length=0;
                }else
                    length++;
            }else if(!flags[i] && start!=-1){
                end=i-1;
                Space space=new Space(length,start,end);
                si++;
                spaces[si]=space;
                start=-1;
                length=0;
            }
        }
        Comparator cmp=new Up();
        Arrays.sort(spaces,1,si+1,cmp);
        for(int i=1;i<=si;i++)
            System.out.println(spaces[i].getLength());
        return si;
    }
    public void bestMate(){
        for(int i=0;i<ready.size();i++){
            if(!ready.get(i).getMate()){
                int num=findSpaceDowm();
                for(int j=1;j<=num;j++){
                    if(spaces[j].getLength()>=Integer.parseInt(ready.get(i).getLength())){
                        memories[spaces[j].getStart()]=ready.get(i);
                        head[spaces[j].getStart()]=true;
                        for(int k=spaces[j].getStart();k<spaces[j].getStart()+Integer.parseInt(ready.get(i).getLength());k++)
                            flags[k]=false;
                        break;
                    }

                }
            }
            ready.get(i).isMate(true);
        }
    }
    public void worstMate(){
        for(int i=0;i<ready.size();i++){
            if(!ready.get(i).getMate()){
                int num=findSpace();
                for(int j=1;j<=num;j++){
                    if(spaces[j].getLength()>=Integer.parseInt(ready.get(i).getLength())){
                        memories[spaces[j].getStart()]=ready.get(i);
                        head[spaces[j].getStart()]=true;
                        for(int k=spaces[j].getStart();k<spaces[j].getStart()+Integer.parseInt(ready.get(i).getLength());k++)
                            flags[k]=false;
                        break;
                    }

                }
            }
            ready.get(i).isMate(true);
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        CPUNumCmbbox.getItems().addAll(
                "1",
                "2",
                "3",
                "4",
                "5",
                "6"
        );
        //数据源为空时显示
        CPUNumCmbbox.setPlaceholder(new Label("Placeholder"));
        //设置可编辑
        CPUNumCmbbox.setEditable(true);

        newPriorityCmbbox.getItems().addAll(
                "1",
                "2",
                "3",
                "4",
                "5"
        );
        //数据源为空时显示
        newPriorityCmbbox.setPlaceholder(new Label("Placeholder"));
        //设置可编辑
        newPriorityCmbbox.setEditable(true);
        modeCmbbox.getItems().addAll(
                "优先级",
                "时间片流转"
        );
        //数据源为空时显示
        newPriorityCmbbox.setPlaceholder(new Label("Placeholder"));
        //设置可编辑
        newPriorityCmbbox.setEditable(false);

        setTimeCmbbox.getItems().addAll(
                "1", "2", "3"
        );
        setTimeCmbbox.setPlaceholder(new Label("Placeholder"));
        setTimeCmbbox.setEditable(false);
        for (int i = 1; i <= 50; i++)
            this.memory.setText(this.memory.getText() + i + "\n");
        for (int i = 0; i < 50; i++){
            flags[i] = true;
            head[i]=false;
        }


        devideCmbbox.getItems().addAll(
                "首次适应算法",
                "最佳适应算法",
                "最差适应算法"
        );
//        devideCmbbox.setPromptText("首次适应算法");
//        modeCmbbox.setPromptText("优先级");
//        CPUNumCmbbox.setPromptText("5");
        this.memory.setEditable(false);

    }
    public void view(){
        memory.setText("");
        int length=0;
        int number=1;
        for(int i=0;i<50;i++){
            this.memory.setText(this.memory.getText() +(i+1));
            if(head[i]){
                this.memory.setText(this.memory.getText()+"\t\t\t"+
                        this.memories[i].getName()+"\t   "+
                        this.memories[i].getState()+"\t\t   "+
                        (i+1)+"\t\t        "+this.memories[i].getLength()+
                        "\n");
                length=Integer.parseInt(memories[i].getLength())-1;
                number=1;
            }else if(length!=0){
                this.memory.setText(this.memory.getText()+"\t\t\t\t\t\t\t\t\t\t\t"+(++number)+"\n");
                --length;
            }else{
                this.memory.setText(this.memory.getText()+"\n");
            }

        }//可改变大小


    }

    @FXML
    void addChecked(ActionEvent event) {
        String name=this.newNameTxt.getText();
        String time=this.newTimeTxt.getText();
        String priority=this.newPriorityCmbbox.getValue().toString();
        String length=this.newLengthTxt.getText();
        PCB pcb=new PCB(name,time,priority,length);
        pcb.setPID("A"+(++pid));
        pcb.setState(1);
        reserve.add(pcb);//reserve[i]=pcb;i++;
        this.newNameTxt.setText("");
        this.newTimeTxt.setText("");
        this.newPriorityCmbbox.setValue(null);


        this.reserveNameTxt.setText("");
        this.reserveTimeTxt.setText("");
        this.reversePriorityTxt.setText("");
        for (int i = 0; i < reserve.size(); i++) {
           this.reserveNameTxt.setText(this.reserveNameTxt.getText()+reserve.get(i).name+'\n');
           this.reserveTimeTxt.setText(this.reserveTimeTxt.getText()+reserve.get(i).time+'\n');
           this.reversePriorityTxt.setText(this.reversePriorityTxt.getText()+reserve.get(i).priority+'\n');
        }
    }
    @FXML
    void randomChecked(ActionEvent event){
        for(int i=1;i<=5;i++){
            int n=(int)(Math.random()*1001+1);
            String num;
            if(n<10)                 num="00"+n;
            else if(n>=10 && n<100)  num="0"+n;
            else                     num=""+n;
            String name="p"+num;
            int time=(int)(Math.random()*6+1);
            int priority=(int)(Math.random()*5+1);
            int length=(int)(Math.random()*7+1);
            PCB pcb=new PCB(name,""+time,""+priority,""+length);
            pcb.setState(1);
            reserve.add(pcb);

        }
        this.reserveNameTxt.setText("");
        this.reserveTimeTxt.setText("");
        this.reversePriorityTxt.setText("");
        for (int i = 0; i < reserve.size(); i++) {
            this.reserveNameTxt.setText(this.reserveNameTxt.getText()+reserve.get(i).name+'\n');
            this.reserveTimeTxt.setText(this.reserveTimeTxt.getText()+reserve.get(i).time+'\n');
            this.reversePriorityTxt.setText(this.reversePriorityTxt.getText()+reserve.get(i).priority+'\n');
        }
    }

    @FXML
    void resetChecked(ActionEvent event) {
        this.newNameTxt.setText("");
        this.newTimeTxt.setText("");
        this.newPriorityCmbbox.setValue(null);
    }

    @FXML
    void dispathChecked(ActionEvent event) {

        int road=Integer.parseInt(CPUNumCmbbox.getValue());
        if(reserve.size()<road-readyNum-running){
            if(reserve.size()==0)  return;
            else {
                road += reserve.size();
                for (int i = 0; i < reserve.size(); i++) {
                    ready.add(reserve.get(i));
                    ready.get(ready.size() - 1).setState(2);
                }
                for (int i = 0; i < road - readyNum; i++) {
                    reserve.removeElementAt(0);
                }
            }
        }else{
            for(int i=0;i<road-readyNum-running;i++){
                ready.add(reserve.get(i));
                ready.get(ready.size()-1).setState(2);
            }
            for(int i=0;i<road-readyNum;i++) {
                reserve.removeElementAt(0);
            }
            readyNum=road;
        }


        if(devideCmbbox.getValue().equals("首次适应算法"))
            firstMate();
        else if(devideCmbbox.getValue().equals("最佳适应算法"))
            bestMate();
        else
            worstMate();



        //调度转换
        if(running==0){
            if(modeCmbbox.getValue().equals("优先级"))     priorityDispatch();
            else                                          timeDispatch();
        }
        if(modeCmbbox.getValue().equals("优先级"))    sort(ready);

        this.reserveNameTxt.setText("");
        this.reserveTimeTxt.setText("");
        this.reversePriorityTxt.setText("");
        for (int i = 0; i < reserve.size(); i++) {
            this.reserveNameTxt.setText(this.reserveNameTxt.getText()+reserve.get(i).name+'\n');
            this.reserveTimeTxt.setText(this.reserveTimeTxt.getText()+reserve.get(i).time+'\n');
            this.reversePriorityTxt.setText(this.reversePriorityTxt.getText()+reserve.get(i).priority+'\n');
        }
        this.readyNameTxt.setText("");
        this.readyTimeTxt.setText("");
        this.readyPrioirtyTxt.setText("");
        for(int i=0;i<ready.size();i++){
            this.readyNameTxt.setText(this.readyNameTxt.getText()+ready.get(i).name+'\n');
            this.readyTimeTxt.setText(this.readyTimeTxt.getText()+ready.get(i).time+'\n');
            this.readyPrioirtyTxt.setText(this.readyPrioirtyTxt.getText()+ready.get(i).priority+'\n');
        }
        view();

    }

    @FXML
    void runningChecked(ActionEvent event) {
        if(modeCmbbox.getValue().equals("优先级")){
            int time=Integer.parseInt(rpcb.getTime())-1;
            rpcb.setTime(""+time);
            if(time==0) {
                for(int i=0;i<ready.size();i++){
                    if(!ready.get(i).getPriority().equals("1")){
                        int priority=Integer.parseInt(ready.get(i).getPriority())-1;
                        ready.get(i).setPriority(""+priority);
                    }
                }//优先级减一防止饥饿

                this.runningNameTxt.setText("");
                this.runningTimeTxt.setText("");
                this.runningPriorityTxt.setText("");
                rpcb.setState(0);
                end.add(rpcb);
                this.endTxt.setText("");
                for (int i = 0; i < end.size(); i++) {
                    this.endTxt.setText(this.endTxt.getText() + end.get(i).getName() + "\n");
                }
                running=0;
                readyNum--;
                for(int i=0;i<50;i++){
                    if(head[i] && memories[i].getName().equals(rpcb.getName())){
                        head[i]=false;
                        for(int j=i;j<i+Integer.parseInt(memories[i].getLength());j++)
                            flags[j]=true;
                        memories[i]=null;
                        break;
                    }
                }
                if(istie){
                    priorityDispatch();
                    int n=Integer.parseInt(untieNumTxt.getText());
                    PCB pcb=tie.get(n-1);
                    pcb.setState(2);
                    ready.add(pcb);
                    readyNum++;
                    tie.removeElementAt(n-1);
                    this.untieNumTxt.setText("");
                    this.tieNameTxt.setText("");
                    this.tieTimeTxt.setText("");
                    this.tiePriorityTxt.setText("");
                    for(int i=0;i<tie.size();i++){
                        this.tieNameTxt.setText(this.tieNameTxt.getText()+tie.get(i).name+'\n');
                        this.tieTimeTxt.setText(this.tieTimeTxt.getText()+tie.get(i).time+'\n');
                        this.tiePriorityTxt.setText(this.tiePriorityTxt.getText()+tie.get(i).priority+'\n');
                    }
                    this.readyNameTxt.setText("");
                    this.readyTimeTxt.setText("");
                    this.readyPrioirtyTxt.setText("");
                    for(int i=0;i<ready.size();i++){
                        this.readyNameTxt.setText(this.readyNameTxt.getText()+ready.get(i).name+'\n');
                        this.readyTimeTxt.setText(this.readyTimeTxt.getText()+ready.get(i).time+'\n');
                        this.readyPrioirtyTxt.setText(this.readyPrioirtyTxt.getText()+ready.get(i).priority+'\n');
                    }
                    istie=false;
                    view();
                } else if(reserve.size()!=0){
                    dispathChecked(event);
                } else if(readyNum!=0){
                    priorityDispatch();
                    this.readyNameTxt.setText("");
                    this.readyTimeTxt.setText("");
                    this.readyPrioirtyTxt.setText("");
                    for(int i=0;i<ready.size();i++){
                        this.readyNameTxt.setText(this.readyNameTxt.getText()+ready.get(i).name+'\n');
                        this.readyTimeTxt.setText(this.readyTimeTxt.getText()+ready.get(i).time+'\n');
                        this.readyPrioirtyTxt.setText(this.readyPrioirtyTxt.getText()+ready.get(i).priority+'\n');
                    }
                    view();
                }else{
                    rpcb=null;
                    auto=false;
                    this.runningNameTxt.setText("");
                    this.runningTimeTxt.setText("");
                    this.runningPriorityTxt.setText("");
                    view();
                }
            }else{
                this.runningNameTxt.setText(rpcb.getName());
                this.runningTimeTxt.setText(rpcb.getTime());
                this.runningPriorityTxt.setText(rpcb.getPriority());
            }
        }else{
            runtime++;
            timeslice=Integer.parseInt(setTimeCmbbox.getValue());
            int time=Integer.parseInt(rpcb.getTime())-1;
            rpcb.setTime(""+time);
            if(time==0){
                this.runningNameTxt.setText("");
                this.runningTimeTxt.setText("");
                this.runningPriorityTxt.setText("");
                rpcb.setState(0);
                end.add(rpcb);
                this.endTxt.setText("");
                for (int i = 0; i < end.size(); i++) {
                    this.endTxt.setText(this.endTxt.getText() + end.get(i).getName() + "\n");
                }
                running=0;
                readyNum--;
                for(int i=0;i<50;i++){
                    if(head[i] && memories[i].getName().equals(rpcb.getName())){
                        head[i]=false;
                        for(int j=i;j<i+Integer.parseInt(memories[i].getLength());j++)
                            flags[i]=true;
                        memories[i]=null;
                        break;
                    }
                }
                if(istie){
                    priorityDispatch();
                    int n=Integer.parseInt(untieNumTxt.getText());
                    PCB pcb=tie.get(n-1);
                    pcb.setState(2);
                    ready.add(pcb);
                    readyNum++;
                    tie.removeElementAt(n-1);
                    this.untieNumTxt.setText("");
                    this.tieNameTxt.setText("");
                    this.tieTimeTxt.setText("");
                    this.tiePriorityTxt.setText("");
                    for(int i=0;i<tie.size();i++){
                        this.tieNameTxt.setText(this.tieNameTxt.getText()+tie.get(i).name+'\n');
                        this.tieTimeTxt.setText(this.tieTimeTxt.getText()+tie.get(i).time+'\n');
                        this.tiePriorityTxt.setText(this.tiePriorityTxt.getText()+tie.get(i).priority+'\n');
                    }
                    this.readyNameTxt.setText("");
                    this.readyTimeTxt.setText("");
                    this.readyPrioirtyTxt.setText("");
                    for(int i=0;i<ready.size();i++){
                        this.readyNameTxt.setText(this.readyNameTxt.getText()+ready.get(i).name+'\n');
                        this.readyTimeTxt.setText(this.readyTimeTxt.getText()+ready.get(i).time+'\n');
                        this.readyPrioirtyTxt.setText(this.readyPrioirtyTxt.getText()+ready.get(i).priority+'\n');
                    }
                    istie=false;
                    view();
                }else if(reserve.size()!=0)
                    dispathChecked(event);
                else if(readyNum!=0) {
                    timeDispatch();
                    this.readyNameTxt.setText("");
                    this.readyTimeTxt.setText("");
                    this.readyPrioirtyTxt.setText("");
                    for (int i = 0; i < ready.size(); i++) {
                        this.readyNameTxt.setText(this.readyNameTxt.getText() + ready.get(i).name + '\n');
                        this.readyTimeTxt.setText(this.readyTimeTxt.getText() + ready.get(i).time + '\n');
                        this.readyPrioirtyTxt.setText(this.readyPrioirtyTxt.getText() + ready.get(i).priority + '\n');
                    }
                    view();
                }else{
                    rpcb=null;
                    auto=false;
                    this.runningNameTxt.setText("");
                    this.runningTimeTxt.setText("");
                    this.runningPriorityTxt.setText("");
                    view();
                }
                runtime=0;
            }else if(runtime>=timeslice){
                ready.add(rpcb);
                running=0;
                timeDispatch();
                this.readyNameTxt.setText("");
                this.readyTimeTxt.setText("");
                this.readyPrioirtyTxt.setText("");
                for (int i = 0; i < ready.size(); i++) {
                    this.readyNameTxt.setText(this.readyNameTxt.getText() + ready.get(i).name + '\n');
                    this.readyTimeTxt.setText(this.readyTimeTxt.getText() + ready.get(i).time + '\n');
                    this.readyPrioirtyTxt.setText(this.readyPrioirtyTxt.getText() + ready.get(i).priority + '\n');
                }
                runtime=0;
            }else{
                    this.runningNameTxt.setText(rpcb.getName());
                    this.runningTimeTxt.setText(rpcb.getTime());
                    this.runningPriorityTxt.setText(rpcb.getPriority());
            }

        }

    }
    @FXML
    void autorunningChecked(ActionEvent event){
        autoRunning autorunning=new autoRunning(event);
        autorunning.start();
    }
    @FXML
    void pauseChecked(ActionEvent event){
        auto=false;
    }
    @FXML
    void untieChecked(ActionEvent event) {
        istie=true;
    }

    @FXML
    void tieChecked(ActionEvent event) {
        int n=Integer.parseInt(tieNumTxt.getText());
        PCB pcb=ready.get(n-1);
        pcb.setState(4);
        tie.add(pcb);
        ready.removeElementAt(n-1);
        readyNum--;
        this.tieNumTxt.setText("");
        this.readyNameTxt.setText("");
        this.readyTimeTxt.setText("");
        this.readyPrioirtyTxt.setText("");
        for(int i=0;i<ready.size();i++){
            this.readyNameTxt.setText(this.readyNameTxt.getText()+ready.get(i).name+'\n');
            this.readyTimeTxt.setText(this.readyTimeTxt.getText()+ready.get(i).time+'\n');
            this.readyPrioirtyTxt.setText(this.readyPrioirtyTxt.getText()+ready.get(i).priority+'\n');
        }
        this.tieNameTxt.setText("");
        this.tieTimeTxt.setText("");
        this.tiePriorityTxt.setText("");
        for(int i=0;i<tie.size();i++){
            this.tieNameTxt.setText(this.tieNameTxt.getText()+tie.get(i).name+'\n');
            this.tieTimeTxt.setText(this.tieTimeTxt.getText()+tie.get(i).time+'\n');
            this.tiePriorityTxt.setText(this.tiePriorityTxt.getText()+tie.get(i).priority+'\n');
        }

    }
    @FXML
    void findChecked(ActionEvent event) {
        this.findStateTxt.setText("");
        String s=findNameTxt.getText();
        for(int i=0;i<reserve.size();i++) {
            if (reserve.get(i).getName().equals(s)){
                findStateTxt.setText(reserve.get(i).getState());
                return;
            }
        }
        for(int i=0;i<ready.size();i++){
            if(ready.get(i).getName().equals(s)){
                findStateTxt.setText(ready.get(i).getState());
                return;
            }

        }
        for(int i=0;i<end.size();i++){
            if(end.get(i).getName().equals(s)){
                findStateTxt.setText(end.get(i).getState());
                return;
            }
        }
        for(int i=0;i<tie.size();i++){
            if(tie.get(i).getName().equals(s)){
                findStateTxt.setText(tie.get(i).getState());
                return;
            }
        }
        if(rpcb.getName().equals(s))   findStateTxt.setText(rpcb.getState());
    }

}
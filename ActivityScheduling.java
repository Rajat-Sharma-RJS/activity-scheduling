
package activityscheduling;
import java.util.*;
import java.io.*;
/**
 *
 * @author rajat
 */
class Node{
    String label;
    int early_start=-1, early_finish;
    int duration;
    int late_start, late_finish=100000;
    int flot;
    int critical=0;
    
    ArrayList<String> forward=new ArrayList<>();
    ArrayList<String> backward=new ArrayList<>();
}

public class ActivityScheduling {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        try{
            File file= new File("/home/rajat/Desktop/input.txt");
            Scanner scan=new Scanner(file);
            scan.nextLine();
            
            ArrayList<Node> nodes=new ArrayList<Node>();
            while(scan.hasNext()){
                Node node=new Node();
                node.label=scan.next();
                String str=scan.next();
                while(str.indexOf(',')!=-1){
                    String strNew=str.replace(",","");
                    str=scan.next();
                    node.backward.add(strNew);
                }
                if(str.indexOf('-')==-1){
                    node.backward.add(str);
                }
                node.duration= Integer.parseInt(scan.next());
                
                nodes.add(node);
            }
            
            for(Node node : nodes){
                if(node.backward.isEmpty())
                    continue;
                for(String str : node.backward){
                    for(Node nod : nodes){
                        if(nod.label.equals(str)){
                            nod.forward.add(node.label);
                        }
                    }
                }
            }
            
            //forward pass
            int max=-1;
            for(Node node : nodes){
                if(node.backward.isEmpty())
                    node.early_start=0;
                
                else{
                    for(String str : node.backward){
                        for(Node nod : nodes){
                            if(nod.label.equals(str)){
                                if(nod.early_finish>node.early_start){
                                    node.early_start=nod.early_finish;
                                }
                            }
                        }
                    }
                }
                
                node.early_finish=node.duration+node.early_start;
                if(node.early_finish>max)
                    max=node.early_finish;
            }
            
            /*for(Node node : nodes){
                System.out.println(node.label+" early time:"+node.early_start+" early finish:"+node.early_finish);
            }*/
            
            //backward pass
            Collections.reverse(nodes);
            for(Node node : nodes){
                if(node.forward.isEmpty())
                    node.late_finish=max;
                
                else{
                    for(String str : node.forward){
                        for(Node nod : nodes){
                            if(nod.label.equals(str)){
                                if(node.late_finish>nod.late_start){
                                    node.late_finish=nod.late_start;
                                }
                            }
                        }
                    }
                }
                
                node.late_start=node.late_finish-node.duration;
            }
            
            Collections.reverse(nodes);
            
            for(Node node : nodes){
                node.flot=node.late_start-node.early_start;
                if(node.flot==0){
                    node.critical=1;
                    //System.out.println(node.label);
                }
            }
            
            FileWriter fw=new FileWriter("/home/rajat/Desktop/output.txt");
            
            fw.write("Activity     Start Time     Completion Time     Critical Path\n");
            for(Node node : nodes){
                fw.write(node.label+"             "+node.early_start+"               "+node.early_finish);
                if(node.critical==1)
                    fw.write("                  *\n");
                else
                    fw.write("\n");
            }
            fw.write("\n\n");
            fw.write("The critical path is ");
            String str=" ";
            for(Node node :nodes){
                if(node.critical==1)
                    str=str+node.label+"->";
            }
            String str1 = str.substring(0, str.length() - 2);
            //System.out.println(sb);
            fw.write(str1);
            fw.close(); 
            System.out.println("Successfully wrote to output.txt");
        }
        catch(Exception e){
            System.out.println("error");
        }
    }
    
}

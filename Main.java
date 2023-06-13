import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;


class Second_chance{
    int value;
    int second_value;
    Second_chance(int value , int second_value){
        this.value= value;
        this.second_value = second_value;
    }
}

public class Main {
   static Queue<Integer> FIFO = new LinkedList<>() ;
   static Queue<Integer> LRU = new LinkedList<>();
   static Queue<Second_chance> S_chance = new LinkedList<>();
   static int F_fault = 0;
   static int L_fault = 0;
   static int S_fault = 0;
    public static void FIFO_function(int voroodi, int n){
        if(FIFO.contains(voroodi)){
            Iterator ari = FIFO.iterator();
            while(ari.hasNext()){
                System.out.print(String.valueOf(ari.next())+"  ");
            }
            System.out.println();
            return;
        }
        else{
            if(FIFO.size()<n){
                FIFO.add(voroodi);
            }
            else{
                FIFO.poll();
                FIFO.add(voroodi);
                F_fault++;

            }
        }

        Iterator ari = FIFO.iterator();
        while(ari.hasNext()){
            System.out.print(String.valueOf(ari.next())+"  ");
        }
        System.out.println();
    }
    public static void LRU_function(int voroodi , int n){
        if(LRU.contains(voroodi)){
            LRU.remove(voroodi);
            LRU.add(voroodi);
        }
        else{
            if(LRU.size()<n){
                LRU.add(voroodi);

            }
            else{
                LRU.poll();
                LRU.add(voroodi);
                L_fault++;
            }

        }
        Iterator ari = LRU.iterator();
        while(ari.hasNext()){
            System.out.print(String.valueOf(ari.next())+"  ");
        }
        System.out.println();
    }
    public static void S_chance_function(int voroodi , int n){
        Second_chance x = null ;
        boolean f = false;
        Iterator ari = S_chance.iterator();
        while(ari.hasNext()){
            x = (Second_chance)ari.next();
           if(x.value==voroodi){
               f = true;
               break;
           }
       }

        if(f){
            x.second_value = 1 ;
        }
        else{
            x = new Second_chance(voroodi,1);

              if(S_chance.size()<n){
                  S_chance.add(x);
              }
              else{
                  S_fault++;
              Second_chance y =  S_chance.poll();
              while(y.second_value==1){
                  y .second_value = 0 ;
                  S_chance.add(y);
                  y = S_chance.poll();
              }
              S_chance.add(x);
              }
        }

        Iterator ari2 = S_chance.iterator();
        while(ari2.hasNext()){
            int c = ((Second_chance)ari2.next()).value;
            System.out.print(c+"  ");
        }
        System.out.println();
    }

    public static void main(String[] args) {

        try {
            Socket socket = new Socket("localhost", 8080);
          DataInputStream  dis = new DataInputStream(socket.getInputStream());
             int n = dis.readInt();
            System.out.println(n);
            while(true){
                int voroodi = dis.readInt();
                if(voroodi==0){
                    break;
                }
                System.out.println("adding : "+voroodi);
                System.out.println();
              System.out.print("FIFO : ");
              FIFO_function(voroodi,n);
                System.out.print("LRU : ");
              LRU_function(voroodi,n);
                System.out.print("S_chance : ");
              S_chance_function(voroodi,n);
                System.out.println("========================================================");
            }
            System.out.println("LRU:<"+L_fault+">"+"FIFO:<"+F_fault+">"+"Second-chance:<"+S_fault+">");
            System.out.println("========================================================");
            System.out.println("+Cool miss");
            System.out.println("LRU:<"+(L_fault+n)+">"+"FIFO:<"+(F_fault+n)+">"+"Second-chance:<"+(S_fault+n)+">");
        } catch (IOException e) {
            e.printStackTrace();
        }



    }
}

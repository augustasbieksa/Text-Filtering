package textui;

import java.util.ArrayList;
import java.util.Date;

public class Irasas implements Comparable<Irasas> {

    String id;
    int uzklausuSkaicius;
    String minLaikas="";
    String maxLaikas="";
    String uzklausa="";
    String uzklausosTipas="";
    String programa="";
    String tipas="";
    String pool="";
    String sourceIP="";
    String destinationIP="";
    String diena="";
    boolean blokuotas;
    boolean tinkantisAnalizei = false;
    int seka = 0;
    
    String blokuotas_1 = "", blokuotas_2 = "", blokuotas_3 = "", blokuotas_4 = "", blokuotas_5 = "";

    public Irasas(String id, ArrayList<String> eilutes) {
        this.id = id;
        this.uzklausuSkaicius = eilutes.size();
        this.diena = "2017.04.07";
        this.minLaikas = eilutes.get(0).substring(0, 12);
        this.maxLaikas = eilutes.get(eilutes.size() - 1).substring(0, 12);
        for (String eilute : eilutes) {
            String[] dalys = eilute.split(" "); 
            if (eilute.contains("Processing")) {
                this.uzklausosTipas = dalys[8]; 
                this.uzklausa = dalys[11]; 
                tinkantisAnalizei = true;
            } else if (eilute.contains("Found host name")) {
                this.uzklausosTipas = "CONNECT";
                this.uzklausa = dalys[10]; 
                tinkantisAnalizei = true;
            } else if (eilute.contains("Extracted host name")) {
                this.uzklausosTipas = "CONNECT";
                this.uzklausa = dalys[10]; 
                tinkantisAnalizei = true;
            } else if (eilute.contains("Black list rule found")) {
                blokuotas = true;
            } else if (eilute.contains("Server has accepted new tcp connection.")) {
                this.programa = dalys[14].substring(5);
                this.pool = dalys[1];
                this.sourceIP = dalys[15].substring(1);
                this.destinationIP = dalys[16].substring(1);

            }       
            else if (eilute.contains("Accepting incoming")) {
                this.tipas = dalys[9];

            }

            
        }
    }

    @Override
    public String toString() {
        
        return "" + id + ";" + uzklausuSkaicius + ";" + diena + ";" + minLaikas + ";" + diena + ";" + maxLaikas + ";" + uzklausa + ";" + tipasKaipString() + ";" + (blokuotas ? "TRUE" : "FALSE") + ";" + seka + ";" + programa + ";" + pool
                + ";" + sourceIP + ";" + destinationIP + ";" + rysysKaipString() + ";"
                + blokuotas_1 + ";" + blokuotas_2 + ";" + blokuotas_3 + ";" + blokuotas_4 + ";" + blokuotas_5;

    }

    public String tipasKaipString() {
        switch (uzklausosTipas) {
            case "GET":
                return "GET";
            case "POST":
                return "POST";
            case "CONNECT":
                return "CONNECT";
            case "HEAD":
                return "HEAD";
        }
        return "";
    }
    
    public String rysysKaipString() {
        switch (tipas) {
            case "HTTP":
                return "HTTP";
            case "SSL":
                return "SSL";
        }
        return "";
    }    

    @Override
    public int compareTo(Irasas t) {
        try {
            return minLaikas.compareTo(t.minLaikas);
        } catch (Exception e) {

        }
        return id.compareTo(id);
    }

}

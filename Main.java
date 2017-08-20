package textui;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) {
        

        
        HashMap<String, ArrayList<String>> sarasas = new HashMap(); 
        ArrayList<Irasas> rezultatas = new ArrayList(); 
        try {
            SimpleDateFormat formatas = new SimpleDateFormat("HH:mm:ss.SSS");
            Scanner skaitymui = new Scanner(new File("adguard.2017-04-07.log"));
            int i = 0;
            while (skaitymui.hasNext()) {
                
                String eilute = skaitymui.nextLine();
                if (eilute.contains(" id=")) {
                    String[] dalys = eilute.split(" "); 
                    if (dalys.length > 7) {
                        String id = dalys[6].substring(3); 
                        if (!sarasas.containsKey(id)) {
                            sarasas.put(id, new ArrayList());
                        }
                        
                        sarasas.get(id).add(eilute);
                    }
                    i++;
                }
            }
            skaitymui.close();
            

            PrintWriter writer = new PrintWriter("rezultatai-2017-04-07.csv", "UTF-8");
            HashMap<String, ArrayList<Irasas>> grupei = new HashMap();

            Iterator it = sarasas.entrySet().iterator(); 
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next(); 
                Irasas irasas = new Irasas((String) pair.getKey(), (ArrayList) pair.getValue());
                if (irasas.tinkantisAnalizei) {
                    rezultatas.add(irasas);
                }
                it.remove();
            }
            Collections.sort(rezultatas);
            for (Irasas ir : rezultatas) {
                if (!grupei.containsKey(ir.programa)) {
                    grupei.put(ir.programa, new ArrayList());
                    ir.seka = 0;
                } else {
                    Irasas paskutinis = grupei.get(ir.programa).get(grupei.get(ir.programa).size() - 1);
                    ir.seka = paskutinis.seka + 1;
                    Date ankstesnio_pabaiga = formatas.parse(paskutinis.maxLaikas);
                    Date dabartinio_pradzia = formatas.parse(ir.minLaikas);
                    long skirtumas = dabartinio_pradzia.getTime() - ankstesnio_pabaiga.getTime();
                    long minutemis = (TimeUnit.MINUTES).convert(skirtumas, TimeUnit.MILLISECONDS);
                    if (minutemis > 1) {
                        ir.seka = 0;
                    }
                    if (ir.seka >= 1) {
                        ir.blokuotas_1 = (paskutinis.blokuotas ? "TRUE" : "FALSE");
                        if (ir.seka >= 2) {
                            Irasas du_atgal = grupei.get(ir.programa).get(grupei.get(ir.programa).size() - 2);
                            ir.blokuotas_2 = (du_atgal.blokuotas ? "TRUE" : "FALSE");
                            if (ir.seka >= 3) {
                                Irasas trys_atgal = grupei.get(ir.programa).get(grupei.get(ir.programa).size() - 3);
                                ir.blokuotas_3 = (trys_atgal.blokuotas ? "TRUE" : "FALSE");
                                if (ir.seka >= 4) {
                                    Irasas keturi_atgal = grupei.get(ir.programa).get(grupei.get(ir.programa).size() - 4);
                                    ir.blokuotas_4 = (keturi_atgal.blokuotas ? "TRUE" : "FALSE");
                                    if (ir.seka >= 5) {
                                        Irasas penki_atgal = grupei.get(ir.programa).get(grupei.get(ir.programa).size() - 5);
                                        ir.blokuotas_5 = (penki_atgal.blokuotas ? "TRUE" : "FALSE");
                                    }
                                }
                            }
                        }
                    }
                }
                grupei.get(ir.programa).add(ir);

                writer.println(ir.toString());
            }

            writer.close();

        } catch (Exception ex) {
            System.out.println("KLAIDA:");
            ex.printStackTrace();
        }
    }
}

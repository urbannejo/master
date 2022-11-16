/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tp12_urban;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.swing.JLabel;

/**
 *
 * @author nejo
 */
public class Hora extends Thread{
    private final JLabel horaLbl;
    Calendar cal = GregorianCalendar.getInstance();
    String AmPm = cal.get(Calendar.AM_PM)==Calendar.AM?"AM":"PM";
    
        
    public Hora(JLabel horaLbl) {
    this.horaLbl = horaLbl;
    }
    
    @Override
    public void run(){
        cal.roll(Calendar.HOUR, 0);
        while(true) {
            Date hoy = new Date();
            SimpleDateFormat sdt1 = new SimpleDateFormat("hh:");
            SimpleDateFormat sdt2 = new SimpleDateFormat("mm:ss");
            SimpleDateFormat sdt3 = new SimpleDateFormat("dd-MM-yyyy");
            horaLbl.setText("  Hora: "+sdt1.format(cal.getTime())+sdt2.format(hoy)+" "+AmPm+" | Fecha: "+sdt3.format(hoy));
            
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                java.util.logging.Logger.getLogger(Hora.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            }
        }
    }
}

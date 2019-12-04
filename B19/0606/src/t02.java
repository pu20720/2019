import lejos.hardware.Sound;
 
import lejos.utility.Delay;
 
public class t02 {
    public static void main(String[] args) {
        // 叫兩聲
        Sound.twoBeeps();
        // 休息三秒
        Delay.msDelay(3000);
        // 叫一聲
        Sound.beep();
    }
} 
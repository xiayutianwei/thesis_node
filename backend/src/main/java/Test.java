/**
 * Created by liuziwei on 2017/12/29.
 */
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Test {
    public static void main(String[] args){
        try{
            System.out.println("start");
            Process pr = Runtime.getRuntime().exec("python E:\\workspace\\test\\main.py");

            BufferedReader in = new BufferedReader(new
                    InputStreamReader(pr.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }
            in.close();
            pr.waitFor();
            System.out.println("end");
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}

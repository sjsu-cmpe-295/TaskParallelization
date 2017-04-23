package sjsu.cmpe.B295.clusterMonitoring;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.lang.Process;
import java.lang.Runtime;
import java.net.NetworkInterface;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class SystemStatusReader
{
    public static final int CONSERVATIVE    = 0;
    public static final int AVERAGE     = 1;
    public static final int OPTIMISTIC  = 2;

    
    public static Double cpuUsage (int measureMode) throws Exception {

        BufferedReader mpstatReader = null;

        String      mpstatLine;
        String[]    mpstatChunkedLine;
        double total_used = 0.0;
        Double      selected_idle;

        try {
            Runtime runtime = Runtime.getRuntime();
            Process mpstatProcess = runtime.exec("mpstat -P ALL");
            
            mpstatReader = new BufferedReader(new InputStreamReader(mpstatProcess.getInputStream()));
            mpstatReader.readLine();
            mpstatReader.readLine();
            mpstatReader.readLine();

            mpstatLine = mpstatReader.readLine();
            
            if (mpstatLine == null) {
                throw new Exception("mpstat didn't work well");
            } else if (measureMode == SystemStatusReader.AVERAGE) {
            	
                mpstatChunkedLine = mpstatLine.replaceAll(",", ".").split("\\s+");
                selected_idle = Double.parseDouble(mpstatChunkedLine[mpstatChunkedLine.length-1]);
            } else {
                selected_idle   = (measureMode == SystemStatusReader.CONSERVATIVE)?200.:0.;
                Double candidate_idle;

                int i = 0;
                while((mpstatLine = mpstatReader.readLine()) != null) {
                    mpstatChunkedLine = mpstatLine.replaceAll(",", ".").split("\\s+");
                    candidate_idle = Double.parseDouble(mpstatChunkedLine[10]);

                    if (measureMode == SystemStatusReader.CONSERVATIVE) {
                        selected_idle = (selected_idle < candidate_idle)?selected_idle:candidate_idle;
                    } else if (measureMode == SystemStatusReader.OPTIMISTIC) {
                        selected_idle = (selected_idle > candidate_idle)?selected_idle:candidate_idle;
                    }
                    ++i;
                }
                if (i == 0) {
                    throw new Exception("mpstat didn't work well");
                }
            }
        } catch (Exception e) {
            throw e; // It's not desirable to handle the exception here
        } finally {
            if (mpstatReader != null) try {
                mpstatReader.close();
            } catch (IOException e) {
                // Do nothing
            }
        }
        return  100-selected_idle;
    }
    public static float memUsage() throws Exception{
		
    	 BufferedReader statReader = null;
    	 int total_Memory,free_Memory;
    	 float mem_utilization;
    	 String [] total_Memory_Line , free_Memory_Line;
    	 Runtime runtime = Runtime.getRuntime();
         Process memoryUtilizationOutput = runtime.exec("cat /proc/meminfo");
         statReader = new BufferedReader(new InputStreamReader(memoryUtilizationOutput.getInputStream()));
         total_Memory_Line = statReader.readLine().split("\\s+");
         free_Memory_Line = statReader.readLine().split("\\s+");  
         total_Memory = Integer.valueOf(total_Memory_Line[1]);
         free_Memory = Integer.valueOf(free_Memory_Line[1]);
         mem_utilization = ((total_Memory - free_Memory)*100) / total_Memory;
    	 return mem_utilization;
   }
    public static HashMap<String, Float> networkInOut() throws Exception{
    	BufferedReader netStatReader = null;
    	String [] networkStats;
    	HashMap <String,Float>netStats =  new HashMap<String,Float> ();
    	try{
    		Runtime runtime = Runtime.getRuntime();
    		Process networkStat = runtime.exec("ifstat -S");
    		TimeUnit.SECONDS.sleep(3);
    		netStatReader = new BufferedReader(new InputStreamReader(networkStat.getInputStream()));
    		netStatReader.readLine();
    		netStatReader.readLine();
  		System.out.println(netStatReader.readLine());
    		
    		networkStats = netStatReader.readLine().split("\\s+");
    		netStats.put("Network-In", Float.valueOf(networkStats[networkStats.length-1]));
    		netStats.put("Network-Out", Float.valueOf(networkStats[networkStats.length-2]));
//   		System.out.println("Network IN "+ Float.valueOf(networkStats[networkStats.length-1]));
//   		System.out.println("Network OUT "+Float.valueOf(networkStats[networkStats.length-2]));
    	}catch(Exception e){
    		throw e;
    	}
    	return netStats;
    	
    }
 }
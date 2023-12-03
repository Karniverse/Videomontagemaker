import java.io.*;
import java.util.Scanner;

import javax.sound.midi.Soundbank;

//import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;


public class VideoMontageMaker {
	  static ArrayList<String> commandOutputs = new ArrayList<String>();
	    private static String formatDouble(double value) {
	        return String.format("%.2f", value);
	    }
	  static void tempstringforffmpegduration(String nameofthefile) {
	        try {
	            // Specify the command you want to execute
	            String command = "ffprobe -i "+nameofthefile+" -show_entries format=duration -v quiet -of csv=\"p=0\"";
	            //System.out.println(command);

	            // Execute the command using ProcessBuilder
	            Process process = new ProcessBuilder("cmd.exe", "/c", command).start();

	            // Capture the output of the process
	            String output = captureOutput(process);

	            // Wait for the process to complete
	            int exitCode = process.waitFor();

	            // Check the exit code to determine if the command was successful
	            if (exitCode == 0) {
	                //System.out.println("Command executed successfully");
	                //System.out.print(output);
	            	
	            	commandOutputs.add(output);
	            } else {
	                //System.err.println("Command failed with exit code: " + exitCode);
	            }
	        } 
	        catch (IOException | InterruptedException e) {
	            e.printStackTrace();
	        }
	        //System.out.println(cars);
		  }
	    private static String captureOutput(Process process) throws IOException {
	        // Capture the output of the process
	        try (InputStream inputStream = process.getInputStream();
	             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
	            StringBuilder output = new StringBuilder();
	            String line;
	            while ((line = reader.readLine()) != null) {
	                output.append(line).append("");
	            }
	            return output.toString();
	        }
	    }

    public static void main(String[] args) {
    	
    	Scanner scanner = new Scanner(System.in);    	
        //Scanner folderlocation = new Scanner(System.in);  // Create a Scanner object
        System.out.print("Enter folder location : ");
        StringBuilder finalffmpegCommand = new StringBuilder();  
        finalffmpegCommand.append("ffmpeg -y");
        //Process ffmpegduration = new ProcessBuilder("cmd.exe", "/c", ffmpegCommand).inheritIO().start();
        //String templocationString = "G:\\TEST";
    	//File folder = new File(templocationString);
        String folderLocation = scanner.nextLine();
        //System.out.println(folderLocation);
    	File folder = new File(folderLocation);
    	File[] listOfFiles = folder.listFiles();
    	//folderlocation.close();


    	for (int i = 0; i < listOfFiles.length; i++) {
    	  if (listOfFiles[i].isFile() && listOfFiles[i].toString().endsWith(".mp4") ) {
    		String filesforffmpeg = (" -i " + "\""+folderLocation+"\\"+listOfFiles[i].getName()+"\"");
    		tempstringforffmpegduration("\""+folderLocation+"\\"+listOfFiles[i].getName()+"\"");
    		finalffmpegCommand.append(filesforffmpeg);
    		//System.out.println(folderLocation+"\\"+listOfFiles[i].getName());
    	    //System.out.print(" -i " + "\""+listOfFiles[i].getName()+"\"");
    	  }
    	  
    	}
        Double[] clipdurations= new Double[commandOutputs.size()];
        for(int i=0; i<commandOutputs.size(); i++) {
        	clipdurations[i] = Double.parseDouble(commandOutputs.get(i));
        }
        double nextsum = 0;
        Double[] clipduration=new Double[clipdurations.length];
        for(int i=0; i<clipdurations.length; i++) {
        	clipduration[i] =nextsum+(clipdurations[i] - 1);
        	nextsum=clipduration[i];
        }
        //System.out.println(Arrays.toString(clipduration));
        StringBuilder outputBuilder = new StringBuilder();
    for (int k = 0; k < clipduration.length - 1; k++) {        
        String videoString =("[vclip"+k+"]"+"["+(k+1)+":v]"+"xfade=transition=fade:duration=1:offset="+clipduration[k]+",format=yuv420p"+"[vclip"+(k+1)+"]"+";").replace("[vclip0]", "[0:v]");
        outputBuilder.append(videoString);      
    }
    for (int k = 0; k < clipduration.length - 1; k++) {
        //System.out.print((("[clip"+k+"]"+"["+(k+1)+":a]"+"acrossfade=d=1"+"[clip"+(k+1)+"]"+";").replace("[clip0]", "[0:a]")));
        String audioString =("[aclip"+k+"]"+"["+(k+1)+":a]"+"acrossfade=d=1"+"[aclip"+(k+1)+"]"+";").replace("[aclip0]", "[0:a]");
        outputBuilder.append(audioString);
    
    } 
    //System.out.println(outputBuilder.toString());
    	System.out.println(finalffmpegCommand+" -filter_complex \""+outputBuilder.toString().substring(0,(outputBuilder.length()-1))+"\" -c:v h264_nvenc -b:v 60M -bf 2 -preset slow -c:a aac -map \"["+"vclip"+(clipduration.length-1)+"]\" -map \"["+"aclip"+(clipduration.length-1)+"]\" -movflags +faststart "+"\""+folderLocation+"\\out.mp4"+"\"");    
 }
}


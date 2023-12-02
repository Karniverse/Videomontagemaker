import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class DurationCalculator {

    public static void main(String[] args) {
        String filePath = "G:\\TEST\\forprocessing.txt";


        try {
            List<String> lines = Files.readAllLines(Paths.get(filePath));

            // Using dynamic ArrayList to store modified lines
            List<String> modifiedLines = new ArrayList<>();

            double previousSum = 0;
            

            // Process each line
            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i);
                // Assuming values are separated by some delimiter, e.g., comma (",")
                String[] values = line.split(",");

                if (i == 0 && values.length > 0) {
                    // Subtract 1 from the first value in the first line
                    double firstValue = Double.parseDouble(values[0]) - 1;
                    values[0] = formatDouble(firstValue);
                    previousSum = firstValue;
                } else {
                    // Add the previous sum to each subsequent value
                    for (int j = 0; j < values.length; j++) {
                        if (!values[j].isEmpty()) {
                            double currentValue = Double.parseDouble(values[j]);
                            values[j] = formatDouble(previousSum + currentValue);
                            previousSum = previousSum + currentValue;
                        }
                    }
                }

                // Join the values back into a line
                String modifiedLine = String.join(",", values);
                modifiedLines.add(modifiedLine);
            }
            StringBuilder outputBuilder = new StringBuilder();
            outputBuilder.append("-filter_complex \"");
            for (int k = 0; k < modifiedLines.size() - 1; k++) {
                //System.out.print(("[clip"+k+"]"+"["+(k+1)+":v]"+"xfade=transition=fade:duration=1:offset="+modifiedLines.get(k)+",format=yuv420p"+"[clip"+(k+1)+"]"+";").replace("[clip0]", "[0:v]"));
                String videoString =("[vclip"+k+"]"+"["+(k+1)+":v]"+"xfade=transition=fade:duration=1:offset="+modifiedLines.get(k)+",format=yuv420p"+"[vclip"+(k+1)+"]"+";").replace("[vclip0]", "[0:v]");
                outputBuilder.append(videoString);
                //outputArray[index] = outputString;
                //index++;
            }
            for (int k = 0; k < modifiedLines.size() - 1; k++) {
                //System.out.print((("[clip"+k+"]"+"["+(k+1)+":a]"+"acrossfade=d=1"+"[clip"+(k+1)+"]"+";").replace("[clip0]", "[0:a]")));
                String audioString =("[aclip"+k+"]"+"["+(k+1)+":a]"+"acrossfade=d=1"+"[aclip"+(k+1)+"]"+";").replace("[aclip0]", "[0:a]");
                outputBuilder.append(audioString.toString().substring(0, audioString.toString().length() - 1));
            
            }
            outputBuilder.append("\" -c:v h264_nvenc -b:v 60M -bf 2 -preset slow -c:a aac "+"-map \"[vclip"+(modifiedLines.size()-1)+"]\""+" -map \"[aclip"+(modifiedLines.size()-1)+"]\""+" -movflags +faststart ");
            System.out.println(outputBuilder);
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
    }

    private static String formatDouble(double value) {
        return String.format("%.2f", value);
    }
}

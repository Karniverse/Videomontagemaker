// import statement  
import java.io.File;
import java.util.Scanner;  
public class DisplayFileExample  
{  
public void printFileNames(File[] a, int i, int lvl)  
{  
// base case of the recursion  
// i == a.length means the directory has   
// no more files. Hence, the recursion has to stop  
if(i == a.length)  
{  
return;  
}  
// checking if the encountered object is a file or not  
if(a[i].isFile() && a[i].getName().toLowerCase().endsWith(".mp4"))  
{  
System.out.println(a[i].getName());  
}  
// recursively printing files from the directory  
// i + 1 means look for the next file  
printFileNames(a, i + 1, lvl);  
}  
// Main Method  
public static void main(String[] argvs)  
{  
// Providing the full path for the directory
Scanner inputdirectorypath= new Scanner(System.in);
System.out.println("Enter the Folder to scan");
// Check if command-line argument is provided
//String path = (args.length > 0) ? args[0] : "."; // Default to current directory if not provided
String path = inputdirectorypath.nextLine();
//String path = "G:\\TEST";  
// creating a file object  
File fObj = new File(path);  
// creating on object of the class DisplayFileExample  
DisplayFileExample obj = new DisplayFileExample();  
if(fObj.exists() && fObj.isDirectory())  
{  
// array for the files of the directory pointed by fObj  
File a[] = fObj.listFiles();
// display statements  
System.out.println("= = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = =");  
System.out.println("Displaying Files from the directory : " + fObj);  
System.out.println("= = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = =");  
// Calling the method  
obj.printFileNames(a, 0, 0);  
}  
}  
}  


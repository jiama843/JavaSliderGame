import java.io.*;

public class IO {

		//WRITING AN IO FILE
		private static PrintWriter fileOut;
		
		//Create a new File, stored in the current folder
		public static void createOutputFile(String fileName)
		{
			createOutputFile(fileName, false);
			try{
				fileOut = new PrintWriter(new BufferedWriter(new FileWriter(fileName)));
			}
			catch(IOException e){
				System.out.println("***Cannot create file: "+ fileName + " ***");
			}
		}
		
		//Adds to the Existing File IF our append boolean is set to TRUE
		//and rewrites the file IF set to FALSE
		public static void createOutputFile(String fileName, boolean append)
		{
			try{
				fileOut = new PrintWriter(new BufferedWriter(new FileWriter(fileName, append)));
			}
			catch(IOException e){
				System.out.println("***Cannot create file: "+ fileName + " ***");
			}
		}
		
		public static void print(String text)
		{
			fileOut.print(text);
		}
		
		public static void println(String text)
		{
			fileOut.println(text);
		}
		
		//MUST call this method when we are done writing to a file
		//IN ORDER TO SAVE IT
		public static void closeOutputFile()
		{
			fileOut.close();
		}
		
		//READING FROM A FILE
		private static BufferedReader fileIn;
		
		public static void openInputFile(String fileName)
		{
			try{
				fileIn = new BufferedReader(new FileReader(fileName));
			}
			catch(FileNotFoundException e)
			{
				System.out.println("*** Cannot open: " + fileName + " ***");
			}
		}
		
		public static String readLine()
		{
			try{
				return fileIn.readLine();
			}
			catch(IOException e){}
			return null;
		}
		
		public static void closeInputFile()
		{
			try{
				fileIn.close();
			}
			catch(IOException e){}
		}
		
}








/**There are some bugs that are out of my control:
 * 
 * Sometimes a few blocks appear white or black when the program starts
 * (If this happens, press scramble or restart the program until every button appears)
 * 
 * Clicking a block will cause the puzzle to lag a bit before moving
 * 
 */

/*This program is a Sliding Game program
 * 
 * It uses JFrame, ActionListener, IO files, Sounds, and ImageIcon
 * It is modelled after Touhou, a 2D shooter game franchise
 * 
 * To start the game, press Play game, afterwards you can press scramble
 * All the tiles will shuffle
 * 
 * When a tile is clicked, if it is next to an empty space, it can move to it
 * The goal of the game is to complete the picture in the least number of moves
 * 
 * After that is done, a YOU WIN! screen will appear and you can enter your name
 * 
 * Then, if your score(lowest number of moves) was spectacular, you will be showcased
 * on the leaderboard which holds the top 7 ranks
 */

//import swing, awt, awt.event and random
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;


//Source: http://alvinalexander.com/blog/post/java/open-read-image-file-java-imageio-class
//Import required for images
import javax.swing.ImageIcon;

import java.io.File;
import java.net.URL;
import javax.sound.sampled.*;//Import required for sounds

public class SlidingGameGUI extends JFrame implements ActionListener{

	private JButton [] buttons = new JButton [16]; // Declare/initialize new Button array with 16 elements [0-15]
	private JButton scramble, playgame, score;// Declare new buttons, score is only for show.
	private JTextArea Win;//Declare a new text area that will pop up when the tiles are in the correct order

	private int moves = 0;//set moves made = 0

	private int [] highScore = new int [7];//New array that stores moves
	private String [] names = new String [7];//New array that stores names
	
	//Creating new ImageIcon Arrays
	private ImageIcon [] block = new ImageIcon [16];
	private ImageIcon [] blocks = new ImageIcon [16];
	private ImageIcon [] blocki = new ImageIcon [16];
	
	Random ran = new Random();//New random is initialized (This is used in the scramble if statement)
	
	
	/*Constructor method SlidingGameGUI
	 * 
	 * Creates the GUI
	 * initializes all the buttons, textareas etc
	 * 
	 */
	
	public SlidingGameGUI(){
	
		/*The IO file that holds the highscores is accessed and extracted from upon start
		 * 
		 * Due to the layout of the IO file, names are to be extracted first and highscores second
		 * Two for loops fill the arrays for name and highscore
		 */
		
		IO.openInputFile("Highscores.txt");
		
		for(int i = 0; i < names.length; i++){

			names [i] = IO.readLine();				
			
		}
		for(int i = highScore.length; i < highScore.length*2; i++){

			highScore [i-7] = Integer.parseInt(IO.readLine());				
			
		}
		
		IO.closeInputFile();
		
		//Set the size, the title and the layout of the GUI
		this.setSize(750,563);
		this.setResizable(false);
		this.setLayout(null);
		this.setTitle("Touhou Sliding Game");
		
		//set a Background for the game
		//Source: http://stackoverflow.com/questions/23665784/java-gui-background-image
		//JPG is in images folder
		setContentPane(new JLabel(new ImageIcon("images/background.jpg")));
		
		Container c = getContentPane(); //initialize container
		
		this.setVisible(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		//Set the visibility to true and for it to exit on close
		
		/*Create buttons scramble, playgame and score
		 * 
		 * playgame is set on top of scramble and will only show up at the very beginning
		 * scramble starts off invisible
		 * score is set directly below scramble
		 * Text area win is set to take up the left side of the GUI and is invisible at first
		 */
		
		scramble = new JButton("Scramble");
		scramble.setBounds(500,350,200,30);
		scramble.setVisible(false);
		scramble.setFont(new Font("Georgia", Font.BOLD,18));
		scramble.addActionListener(this);
		c.add(scramble);

		playgame = new JButton("Play Game");
		playgame.setBounds(500,350,200,30);
		playgame.setVisible(true);
		playgame.setFont(new Font("Georgia", Font.BOLD,18));
		playgame.addActionListener(this);
		c.add(playgame);
		

		score = new JButton("Moves: "+moves);
		score.setBounds(500,400,200,30);
		score.setVisible(true);
		score.setFont(new Font("Georgia", Font.BOLD,18));
		score.addActionListener(this);
		c.add(score);
		
		Win = new JTextArea();
		Win.setBounds(50, 50, 430, 400);
		Win.setVisible(false);
		Win.setLineWrap(true);
		Win.setFont(new Font("GEORGIA", Font.BOLD, 17));
		Win.setBackground(Color.BLACK);
		Win.setForeground(Color.WHITE);
		c.add(Win);
		
		//Initializing 3 new ImageIcons in the array by means of for loop
		//Source: http://stackoverflow.com/questions/4801386/how-do-i-add-an-image-to-a-jbutton
		//Source: https://www.daniweb.com/software-development/java/threads/256972/how-to-put-my-picture-in-jbutton
		
		//These will serve as the block images and can be found in the images folder
		//By taking advantage of the names given to the images, this for loop is possible
		for(int i = 1; i < blocks.length; i++){
			
			block [i] = new ImageIcon("images/block"+i+".jpg");
			
		}
		for(int i = 1; i < blocks.length; i++){
			
			blocks [i] = new ImageIcon("images/blocks"+i+".jpg");
			
		}
		for(int i = 1; i < blocks.length; i++){
		
			blocki [i] = new ImageIcon("images/blocki"+i+".jpg");
		
		}
		
		/*Initializing the buttons array
		 * 
		 * for loop -> goes through buttons 0 to 15
		 * A string is created that will store the number i
		 * n is set to equal i * 100 to space buttons equally on the same line
		 * 
		 * if statements check which line the buttons are to be declared on
		 * This is done by checking the range of i (if i is between 0 and 4, it is the first line)
		 * 
		 * A value is subtracted from/added to n to make sure every first button starts on the same vertical line 
		 * All buttons have the appropriate number written on them
		 * They are arranged in a 4x4 grid
		 * They are all invisible
		 * They have FONT SIZE 0 so no text collides with the images
		 * They all have ActionListener
		 */
		
		for(int i = 0; i < buttons.length; i++){
			
			String s = ""+i;
			int n = i*100;
			
			if(i <= 3){
			
				if(i == 0){
				
					buttons [i] = new JButton(s);
					buttons [i].setBounds(n+50,70,95,95);
					buttons [i].setVisible(false);
					buttons [i].setFont(new Font("Georgia", Font.BOLD,0));
					buttons [i].addActionListener(this);
					c.add(buttons[i]);
				
				}else{
					
					buttons [i] = new JButton(s);
					buttons [i].setBounds(n+50,70,95,95);
					buttons [i].setVisible(false);
					buttons [i].setFont(new Font("Georgia", Font.BOLD,0));
					buttons [i].addActionListener(this);
					c.add(buttons[i]);
					
				}
			
			}else if(i <= 7 && i > 3){

				buttons [i] = new JButton(s);
				buttons [i].setBounds(n-350,170,95,95);
				buttons [i].setVisible(false);
				buttons [i].setFont(new Font("Georgia", Font.BOLD,0));
				buttons [i].addActionListener(this);
				c.add(buttons[i]);
				
			}else if(i <= 11 && i > 7){
				
				buttons [i] = new JButton(s);
				buttons [i].setBounds(n-750,270,95,95);
				buttons [i].setVisible(false);
				buttons [i].setFont(new Font("Georgia", Font.BOLD,0));
				buttons [i].addActionListener(this);
				c.add(buttons[i]);
				
			}else if(i <= 15 && i > 11){
				
				
				buttons [i] = new JButton(s);
				buttons [i].setBounds(n-1150,370,95,95);
				buttons [i].setVisible(false);
				buttons [i].setFont(new Font("Georgia", Font.BOLD,0));
				buttons [i].addActionListener(this);
				c.add(buttons[i]);
				
			}
			
		}

		StartScreen();//Plays the background music

	}
	
	//Sources: https://www.youtube.com/watch?v=VMSTTg5EEnY
	//http://www.javaworld.com/article/2077521/learn-java/java-tip-24--how-to-play-audio-in-applications.html
	
	/* Call upon these methods to play audio
	 * 
	 * There is one for every move
	 * One for the scramble button
	 * One for when you win
	 * One for the background music
	 * 	
	 * Audio files can be found in the Sounds folder
	 */
	
	public void playScramble() {
		
		//This is how each one works
		
		  try {//try catch is required to catch Exception e or else there will be a Syntax error
			  
		   File file = new File("Sounds/ATTACK3.wav");//A file is initialized from the Sounds folder
		   Clip clip = AudioSystem.getClip();// an audio clip is declared
		   clip.open(AudioSystem.getAudioInputStream(file));//The audio clip is set to store the file
		   clip.start();//The audio file is played only once
		   
		  } catch (Exception e){
			  
		  }
		  
		}
	
	public void StartScreen() {
		
		  try {

			  File file = new File("Sounds/STARTSCREEN1.wav");
			  Clip clip = AudioSystem.getClip();
			  clip.open(AudioSystem.getAudioInputStream(file));
			  clip.loop(Clip.LOOP_CONTINUOUSLY);//The audio file loops infinitely
			  
		  } catch (Exception e){
			  
		  }
		  
		}
	
	public void playWin() {
		
		  try {
			  
		   File file = new File("Sounds/TADA.wav");
		   Clip clip = AudioSystem.getClip();
		   clip.open(AudioSystem.getAudioInputStream(file));
		   clip.start();
		   
		  	}catch (Exception e){
			  
		  }
		  
		}
	
	public void playMove() {
		
		  try {
			  
		   File file = new File("Sounds/ATTACKG.wav");
		   Clip clip = AudioSystem.getClip();
		   clip.open(AudioSystem.getAudioInputStream(file));
		   clip.start();
		   
		   
		   
		  } catch (Exception e){
			  
		  }
		  
		}

	//The main method
	public static void main(String [] args){
	        
		SlidingGameGUI one = new SlidingGameGUI();
		one.show();//Shows the GUI
		
	}
	
	/*A selection sort is created
	 * 
	 * This sort will scan for the minimum values
	 * and it will organize the numbers in a int array from lowest to highest
	 * 
	 * This is mainly for show
	 */
	
	 public void SelectionSort(int [] intArray){
	 
	 int n = 0;

	for(int i = 0; i < intArray.length; i++){

		int min = i;
		n++;
		
		for(int a = n-1; a < intArray.length; a++){

			if(intArray [min] > intArray [a]){
				
				min = a;
				
			}
		}
		
		if(min != i){
			
			int n1 = intArray [i];
			int n2 = intArray [min];
			
			intArray [min] = n1;
			intArray [i] = n2;
			
			}
		
		}
	}
	
	//ActionPerformed class
	//Satisfies the implement ActionListener 
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	//For loop scans for all buttons from 0 to 15
	 for(int i = 0; i < buttons.length; i++){
		
		 //If a button from 0 to 15 is pressed, it will run this statement
		 if(e.getSource() == buttons[i]){
			 
			/* This section is dedicated for movement of the blocks
			 * There are 4 if statements, one for every direction
			 * 
			 * These directions are organized in relation to the array positions(Check the game for reference):
			 * i+1 means right
			 * i-1 means left
			 * i+4 means down
			 * i-4 means up
			 * 
			 * 
			 * (Order is important)
			 * These if statements first check if the move made will be out of bounds or not (ex. i-1 can't be less than 0) so it checks for that to avoid syntax errors
			 * They then check for an invisible button(which is always button 0) around the button clicked
			 * Last, they check(only for moves left and right (i-1,i+1)) if the array position is:
			 * 
			 * 3,7,11 for left
			 * 4,8,12 for right
			 * 
			 * The reason for this is because they are illegal moves(Check the game for reference)
			 * They will skip the first if statements and check for a possible move down or up
			 */
			 
			if(i-1 < buttons.length && i-1 >= 0 && buttons[i-1].isVisible() == false
			&& i-1 != 3 && i-1 != 7 && i-1 != 11){

				//In an if statement
				buttons[i].setVisible(false);//the button clicked is made invisible
				buttons[i-1].setText(buttons[i].getText());//The invisible button inherits the text
				buttons[i-1].setIcon(buttons[i].getIcon());//The invisible button inherits the icon
				buttons[i-1].setVisible(true);//The invisible button is made visible
				moves++;//The move counter increases
			
			}else if(i+1 < buttons.length && i+1 >= 0 && buttons[i+1].isVisible() == false 
				 && i+1 != 4 && i+1 != 8 && i+1 != 12){
				
				buttons[i].setVisible(false);
				buttons[i+1].setText(buttons[i].getText());
				buttons[i+1].setIcon(buttons[i].getIcon());
				buttons[i+1].setVisible(true);
				moves++;
			
			}else if(i+4 < buttons.length && i+4 >= 0 && buttons[i+4].isVisible() == false ) {
				
				buttons[i].setVisible(false);
				buttons[i+4].setText(buttons[i].getText());
				buttons[i+4].setIcon(buttons[i].getIcon());
				buttons[i+4].setVisible(true);
				moves++;

			}else if(i-4 < buttons.length && i-4 >= 0 && buttons[i-4].isVisible() == false){
				
				buttons[i].setVisible(false);
				buttons[i-4].setText(buttons[i].getText());
				buttons[i-4].setIcon(buttons[i].getIcon());
				buttons[i-4].setVisible(true);
				moves++;
				
			}
			
			playMove();//The sound for a move is played (void method)
			
			/*This for loop then checks for the invisible button and sets its text to 0
			 */
			for(i = 0; i < buttons.length; i++){
				
				if(buttons[i].isVisible() == false){
					
					buttons[i].setText("0");
					
				}
				
			}
			
			//The moves integer(which was incremented above) is printed out as new text on the score button
			score.setText("Moves: "+moves);
			
			/*This section is dedicated to checking if the game is won after every move
			 * (This means that every button is in place starting with the invisible button at the top left)
			 * 
			 * An integer (check) is initialized as 0
			 * as each button is checked, if the button number is equal to its position in the array,
			 * check increments by 1
			 * 
			 * If check is equal to the number of elements in the array, all the buttons are in place and the game is won
			 * When this happens, the win sound is played, the buttons disappear and the win screen pops up
			 * 
			 */
			
			int check = 0;
			
			for(i = 0; i < buttons.length; i++){
				
				if(buttons[i].getText().equalsIgnoreCase(""+i)){
					
					check++;
					
				}
				
				if(check == buttons.length){
					
					check -= check;
					playWin();//Win sound played
					
				//After the puzzle is solved, A YOU WIN! textbox appears asking for a
				//name input an the input is stored as a String (tempName)
				String tempName = JOptionPane.showInputDialog("YOU WIN! Enter Your Name (MAX 7 characters):");
				
				//This while loop makes sure that the name inputed has a maximum of 7 letters
				while(tempName.length() > 7){
					
					tempName = JOptionPane.showInputDialog("YOU WIN! Enter Your Name (MAX 7 characters):");
					
				}
				
				//Every button is set invisible during this for loop
				for(i = 0; i < buttons.length; i++){
					
					buttons [i].setVisible(false);					
				}
					
				/*This section after the winning if statement deals with the player rankings
				 * 
				 * First every integer int the highscore array is checked(smallest number to the largest number)
				 */
				
				for(i = 0; i < highScore.length; i++){	
					
					/* If the moves for this run are less than the moves for point i in the array,
					 * 
					 * A for loop starts from the second last element of the highscore array
					 * and shifts every score after point i down by one. The name array parallels
					 * the highscore array and each name is shifted down. This makes room for
					 * the newest highscore and name
					 * 
					 */
					if(moves < highScore[i]){
						
						for(int a = highScore.length-2; a >= i; a--){
							
							highScore[a+1] = highScore [a];
							names [a+1] = names [a];
							
						}
						
						//Afterwards, the highscore at position i in the array is set
						//equal to the moves of the current run and the name at position i to
						//tempName
						highScore[i] = moves;		
						names[i] = tempName;
						
						//This stops the for loop from continuing
						i = highScore.length;
						
					}
					
				}
				
				/* The highscore array is placed into the selection sort to organize the highscore
				 * values from min to max
				 * The selection sort isn't actually necessary
				 */
				SelectionSort(highScore);
					
				/* We access the highscores file and OVERWRITE it with new names, then highscores
				 * Layout is IMPORTANT because it reads line by line
				 * Names are filled first and then highscore
				 * 
				 * The IO text file should save the player rankings for every single game so the highscores
				 * are PERMANENT regardless if the application is closed
				 */
				IO.createOutputFile("Highscores.txt", false);
				
				for(i = 0; i < names.length; i++){

					IO.println(names[i]);
					
				}
				for(i = highScore.length; i < highScore.length*2; i++){

					IO.println(""+highScore[i-7]);
					
				}
				
				IO.closeOutputFile();
				
					//This line sets the heading of the win textarea to HighScores
					//Everytime the game is won, setText will delete all the text already present
					Win.setText("HighScores\n");
				
				/* Finally, this for loop increments an integer i until it reaches
				 * the length of the highscore array
				 * 
				 * Everytime the for loop completes a cycle, the rank number,
				 * name and highScore are added on a new line creating an organized
				 * list of player rankings
				 */
				for(i = 0; i < highScore.length; i++){	
				
					Win.append("\n"+(i+1)+"       "+names[i]+"       "+highScore[i]);
					Win.setVisible(true);//The textarea is set to be visible
			
				}
			}	
		}
	}
		
	/*This if statement applies only if the Play Game button is pressed
	 *
	 * If the playgame button is pressed, it simply sets scramble visible and
	 * playgame invisible
	 */
	}if(e.getSource() == playgame){
			
				scramble.setVisible(true);
				playgame.setVisible(false);
				
			
		/* This if statement applies when the scramble or the play game button is pressed
		 * 
		 * It resets the move counter to 0 (because it begins a new game)
		 * It reprints the text on the score button so it displays 0
		 *
		 * The sound effect for the scramble button is played
		 * 
		 * The buttons are scrambled
		 */
				
		}if(e.getSource() == scramble || e.getSource() == playgame){
		
			moves -= moves;
			score.setText("Moves: "+moves);
			playScramble();
			
			/*Everytime the scramble button is pressed, 
			 * a random set of icons is placed on each block
			 * 
			 * There are three possible image packs and they are decided by the int randomIcon
			 * 
			 * If randomIcon = 1, the block image array is used
			 * If randomIcon = 2, the blocks image array is used
			 * If randomIcon = 3, the blocki image array is used
			 */
			int randomIcon = ran.nextInt(3)+1;
			
			if(randomIcon == 1){
				
				/* In the outer for loop, i represents the number on each button
				 * In the inner loop, a represents the position in the array
				 * 
				 * The outer loop sets an integer to check for
				 * The inner loop checks every button in the array for the integer
				 * 
				 * When a button that holds the integer is found, the icon is synced with
				 * its position in the array
				 */
				
				for(int i = 1; i < buttons.length; i++){
					
					for(int a = 0; a < buttons.length; a++){
						
						if(buttons[a].getText().equalsIgnoreCase(""+i)){
							
							buttons [a].setIcon(block[i]);
						
						}
					}
				}
				
			}else if(randomIcon == 2){
				
				for(int i = 1; i < buttons.length; i++){
					
					for(int a = 0; a < buttons.length; a++){
						
						if(buttons[a].getText().equalsIgnoreCase(""+i)){
							
							buttons [a].setIcon(blocks[i]);
						
						}
					}
				}
			}else if(randomIcon == 3){
				
				for(int i = 1; i < buttons.length; i++){
					
					for(int a = 0; a < buttons.length; a++){
						
						if(buttons[a].getText().equalsIgnoreCase(""+i)){
							
							buttons [a].setIcon(blocki[i]);
						
						}
					}
				}
			}
			
			/*This is how the scramble system works:
			 * 
			 * The first for loop determines how many times the scramble happens
			 * 
			 * The second for loop and if statement checks for button 0, (the invisible one)
			 * 
			 * Inside the if statement, a random number(int a) between 1 to 4 is set
			 * This number represents a random direction:
			 * 
			 * 1 means a move right
			 * 2 means a move left
			 * 3 means a move up
			 * 4 means a move down
			 * 
			 * From here, the moves are made to be completely legal
			 * This GURRANTEES A SOLVABLE SOLUTION EACH TIME
			 * 
			 * For every direction it checks if the move is out of bounds or not
			 * If int a = 1 or 2, it checks for the side buttons:
			 * 
			 * 4, 8, 12 for a move right
			 * 3, 7, 11 for a move left
			 * 
			 * If a move can't be made, the next scramble begins
			 * If a move can be made, the button icons switch,
			 * the button texts switch and the visibilities switch
			 * 
			 * (This is very similar to how the blocks are moved when pressed)
			 */
		
			for(int numScrambles = 0; numScrambles < 100; numScrambles++){	
			
			for(int i = 0; i < buttons.length; i++){
				
				if(buttons[i].getText().equalsIgnoreCase("0")){
					
					int scramble = ran.nextInt(4)+1;
					
						if(scramble == 1 && i+1 < buttons.length && i+1 >= 0 && i+1 != 4 && i+1 != 8 && i+1 != 12){
										
							//Temporary variables are created to hold the text and images of each button
							String word = buttons[i].getText(), word2 = buttons[i+1].getText();
							Icon image1 = buttons[i].getIcon(), image2 = buttons[i+1].getIcon();

							//Button texts are swapped by use of variables
							buttons[i].setText(word2);
							buttons[i+1].setText(word);

							//Button icons are swapped by use of variables
							buttons[i].setIcon(image2);
							buttons[i+1].setIcon(image1);
							
							//The invisible button is set visible
							//And the visible button is set invisible
							buttons[i].setVisible(true);
							buttons[i+1].setVisible(false);
							
						}else if(scramble == 2 && i-1 < buttons.length && i-1 >= 0 && i-1 != 3 && i-1 != 7 && i-1 != 11){
							
							String word = buttons[i].getText(), word2 = buttons[i-1].getText();
							Icon image1 = buttons[i].getIcon(), image2 = buttons[i-1].getIcon();

							buttons[i].setText(word2);
							buttons[i-1].setText(word);

							buttons[i].setIcon(image2);
							buttons[i-1].setIcon(image1);
							
							buttons[i].setVisible(true);
							buttons[i-1].setVisible(false);	
							
						}else if(scramble == 3 && i+4 < buttons.length && i+4 >= 0){
						
						
							String word = buttons[i].getText(), word2 = buttons[i+4].getText();
							Icon image1 = buttons[i].getIcon(), image2 = buttons[i+4].getIcon();
							
							buttons[i].setText(word2);
							buttons[i+4].setText(word);
			
							buttons[i].setIcon(image2);
							buttons[i+4].setIcon(image1);
							
							buttons[i].setVisible(true);
							buttons[i+4].setVisible(false);
							
						}else if(scramble == 4 && i-4 < buttons.length && i-4 >= 0){
							
							String word = buttons[i].getText(), word2 = buttons[i-4].getText();
							Icon image1 = buttons[i].getIcon(), image2 = buttons[i-4].getIcon();
							
							buttons[i].setText(word2);
							buttons[i-4].setText(word);
				
							buttons[i].setIcon(image2);
							buttons[i-4].setIcon(image1);
							
							buttons[i].setVisible(true);
							buttons[i-4].setVisible(false);
							
				
						}
						
							Win.setVisible(false);	
					}
				}
			}
			
			/**This cancelled out code is far more efficient, however it doesn't guarantee a
			 * solvable solution each time. This was the beta code used before the one above.
			 */
		
			/*for(int i = 0; i < buttons.length ; i++){
				
				buttons[i].setVisible(true);
				
			}
			for(int i = 0; i < buttons.length ; i++){
				
				int a = ran.nextInt(15);
				int j = ran.nextInt(15);
			
				if(a != j){
					
				String word = buttons[a].getText(), word2 = buttons[j].getText();
				buttons[a].setText(word2);	
				buttons[j].setText(word);	
				
				}	
			}
				for(int i = 0; i < buttons.length; i++){				
					
					if(buttons[i].getText().equalsIgnoreCase("0")){
			
						buttons[i].setVisible(false);
					
					}
				}*/
			}
				
		}
		
	}
	
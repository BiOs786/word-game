package wordGame;
import dictionary.*;
import java.util.List;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
public class MainApp {
	
	static DictionaryBST db;
	static AutoCompleteDictionaryTrie autoDict;
	static String fileName="data/dict.txt";
	static public boolean startBool = false;
	static boolean isPresent=false;
	long startTime=0;
	long endTime= 0;
	JFrame frame;
	
	int hintCount=0;
	int i=0;
	public  String mainAppString = "";
	LinkedList<String> listofstring = new LinkedList<>();
	int currScore=0;
	
	public int row,col;
	
	JTextArea textarea = new JTextArea(1,10);
	public JTextArea textField = new JTextArea();
	
	JLabel enteredWord = new JLabel("Entered word:");
	JLabel score;
	JLabel timeTaken;
	JLabel currString = new JLabel("Typed words:");

	JPanel panel = new JPanel(new GridLayout(10,10,2,2));
	JPanel panelEast = new JPanel();
	JPanel panelSouth = new JPanel();
	JPanel panelWest = new JPanel();
	JPanel panelEast1 = new JPanel(new GridLayout(0,1,10,10));
	JLabel startGame = new JLabel("CLICK ON START");
	
	JButton start = new JButton("START");
	JButton stop = new JButton("STOP");
	JButton hint = new JButton("HINT?");
	JButton enter;
	
	//Character array to store alphabets
	public static char[] charArray = new char[26];
	
	//CREATING OBJECT OF TILES
	Tile[][] t = new Tile[10][10];
	
	static MainApp wordGame;
	public static void main(String[] args)
	{

		/*
		 * Creating Binary Search Tree to hold dictionary.
		 * Loading all words in fileName in dictionary db.
		 * Making another autoDictionary that will show hints in real time.
		 * Loading all words in fileName to autoDict.
		 */
		db = new DictionaryBST();
		DictionaryLoader.loadDictionary(db, fileName);
		autoDict = new AutoCompleteDictionaryTrie();
		loadAuto(autoDict, fileName);
		wordGame = new MainApp();
		wordGame.setup();
	}
	
	/*
	 * Function to load the dictionary in Tries.
	 */
	public static void loadAuto(AutoCompleteDictionaryTrie d, String filename)
	{
	        // Dictionary files have 1 word per line
	        BufferedReader reader = null;
	        try {
	            String nextWord;
	            reader = new BufferedReader(new FileReader(filename));
	            while ((nextWord = reader.readLine()) != null) {
	                d.addWord(nextWord);
	                
	            }
	            //d.printTree();
	        } catch (IOException e) {
	            System.err.println("Problem loading dictionary file: " + filename);
	            e.printStackTrace();
	        }
	        
	 }

	public void setup()
	{
		frame = new JFrame();
		frame.setTitle("Word Game!");
		
		startGame.setSize(new Dimension(300,300));
		startGame.setForeground(Color.CYAN);
		startGame.setFont(new Font("MONOSPACE",Font.BOLD,22));
		//Adding startGame label to panelEast
		panelEast.add(startGame);
		
		timeTaken= new JLabel();
		score = new JLabel();
		
		//JButton to check the word is real word or not.
		enter = new JButton("CHECK");
		enter.addMouseListener(new ButtonListener());
		enter.setForeground(Color.ORANGE);
		
		//JButton to provide hints.
		hint = new JButton("HINT?");
		hint.addMouseListener(new HintListener());
		hint.setForeground(Color.ORANGE);
		hint.setBackground(Color.BLACK);

		
		enter.setBackground(Color.BLACK);
		enter.setFont(new Font("MONOSPACE",Font.BOLD,16));
		
		
		stop.setBorder(BorderFactory.createLineBorder(Color.ORANGE,1));
		textarea.setSize(40,40);
		textField.setSize(40,40);
		
		//Adding components to panelWest, panelEast, panelSouth.
		panelWest.add(enteredWord);
		panelWest.setLayout(new BoxLayout(panelWest,BoxLayout.Y_AXIS));
		panelWest.add(textarea);
					 
		panelEast1.add(start);
		panelEast1.add(stop);
		panelEast1.add(enter);
		panelEast1.add(hint);
		panelEast.add(score);
		panelEast1.setMaximumSize(new Dimension(100,100));
		panelEast1.setBackground(Color.BLACK);
		panelEast.setLayout(new BoxLayout(panelEast, BoxLayout.Y_AXIS));
		panelEast.add(panelEast1);		
		panelEast.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
		
		panelSouth.add(currString);
		panelSouth.add(textField);
		
		//Configuring main frame.
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(BorderLayout.CENTER,panel);
		frame.getContentPane().add(BorderLayout.EAST,panelEast);
		frame.getContentPane().add(BorderLayout.WEST,panelWest);
		frame.getContentPane().add(BorderLayout.SOUTH,panelSouth);
		frame.setVisible(true);
		frame.setBounds(new Rectangle(800,400));
		frame.setResizable(false);		
		frame.setBackground(Color.RED);//for frame color
				
		//JButton to start the game.
		start.setForeground(Color.GREEN);
		start.setBackground(Color.BLACK);
		start.addMouseListener(new StartListener());
		start.setFont(new Font("MONOSPACE",Font.BOLD,16));
		start.setBorder(BorderFactory.createLineBorder(Color.ORANGE,1));
		
		//JButton to stop the game.
		stop.setForeground(Color.RED);
		stop.addMouseListener(new StopListener());
		stop.setBackground(Color.BLACK);
		stop.setFont(new Font("MONOSPACE",Font.BOLD,16));
		stop.setBorder(BorderFactory.createLineBorder(Color.ORANGE,1));

		// NOD panelEast.add(score);
		currString.setFont(new Font("MONOSPACE",Font.BOLD,16));
		currString.setForeground(Color.ORANGE);
		
		enteredWord.setFont(new Font("MONOSPACE",Font.BOLD,16));
		enteredWord.setForeground(Color.ORANGE);
		
		//Configuring panels.
		panelWest.setBackground(Color.BLACK);
		panelEast.setBackground(Color.BLACK);
		panelSouth.setBackground(Color.BLACK);
		
		//Text area to maintain record of real words typed.
		textarea.setBackground(Color.BLACK);
		textarea.setFont(new Font("MONOSPACE",Font.BOLD,16));
		textarea.setForeground(Color.ORANGE);
		textField.setBackground(Color.ORANGE);
		
		//Creating the tiles
		startingTiles();
	}
	
	public void changeTextField(String s)
	{
		textField.setText(s);
	}
	
	public void startingTiles()
	{
		char[] wordChar = new char[10];
		wordChar[0] = ' ';
		wordChar[1] = 'W';
		wordChar[2] = 'O';
		wordChar[3] = 'R';
		wordChar[4] = 'D';
		wordChar[5] = 'G';
		wordChar[6] = 'A';
		wordChar[7] = 'M';
		wordChar[8] = 'E';
		wordChar[9] = ' ';
		for(int i=0;i<26;i++)
		{
			charArray[i] = (char)(i+65);
		}
		for(int i=0;i<10;i++)
		{
			for(int j=0;j<10;j++)
			{
					if(i==5)
					{
						t[i][j] = new Tile(new Character(wordChar[j]),this);
						t[i][j].setSize(40,40);
						panel.add(t[i][j]);
						t[i][j].setBackground(Color.GREEN);
						t[i][j].setOpaque(true);
						panel.setBackground(Color.GREEN);//panel color
						if(j==0 || j==9)
							t[i][j].setBackground(Color.BLACK);
						t[i][j].setForeground(Color.BLACK);
						t[i][j].setBorder(BorderFactory.createLineBorder(Color.ORANGE,1));
					}
					else
					{
						Random r = new Random();
						char randomChar = charArray[r.nextInt(26)];
						t[i][j] = new Tile(new Character(randomChar),this);
						t[i][j].setSize(40,40);
						panel.add(t[i][j]);
						t[i][j].setBackground(Color.black);
						t[i][j].setOpaque(true);
						panel.setBackground(Color.black);//panel color
						t[i][j].setForeground(Color.orange);
						t[i][j].setBorder(BorderFactory.createLineBorder(Color.ORANGE,1));
					}
			}
			
		}
	}
	
	/*
	 * Method to arrange tiles in the word game when StartButton is clicked
	 */
	public void arrangeTiles()
	{
		for(int i=0;i<26;i++)
		{
			charArray[i] = (char)(i+65);
		}
		for(int i=0;i<10;i++)
		{
			for(int j=0;j<10;j++)
			{
					Random r = new Random();
					char randomChar = charArray[r.nextInt(26)];
					t[i][j] = new Tile(new Character(randomChar),this);
					t[i][j].setSize(40,40);
					panel.add(t[i][j]);
					t[i][j].setBackground(Color.black);
					t[i][j].setOpaque(true);
					panel.setBackground(Color.black);//panel color
					t[i][j].setForeground(Color.orange);
					t[i][j].setBorder(BorderFactory.createLineBorder(Color.ORANGE,1));
			}
			
		}
	
		
	}
	
	/*
	 * Method to check whether the clicked word is dictionary word
	 */
	public void checkDictionary()
	{
		
		if(mainAppString.length() > 2 && db.isWord(mainAppString) && checkForRepetition(mainAppString) )
		{
			//Updating text area when word is valid.
			updateTextArea();
			currScore= currScore+mainAppString.length();
			isPresent=true;
			score.setText("Score: "+currScore);
			score.setForeground(Color.ORANGE);;
			panelEast.add(score);
			score.setForeground(Color.GREEN);
			score.setFont(new Font("MONOSPACE",Font.BOLD,16));
			textField.setText("");
			mainAppString = "";
			Tile.removeMainString();
		}
		if(!db.isWord(mainAppString))
		{
			Tile.removeMainString();
		}
		
	}
	
	/*
	 * Method to update the text area if the present word is in dictionary
	 */
	private void updateTextArea() {
		// TODO Auto-generated method stub
		textarea.append((++i)+". "+mainAppString+"\n");
	}

	/*
	 * Method to check whether the word is already clicked.
	 */
	private boolean checkForRepetition(String str) {
		// TODO Auto-generated method stub
		if(listofstring.contains(str))
		{
			return false;
		}
		listofstring.add(str);
		return true;
	}

	/*
	 * Method to set main string to the given string
	 */
	public void setmainAppString(String str)
	{
		mainAppString = str;
	}
	
	/*
	 * Method to shuffle tiles.
	 */
	public static void shuffleTiles(Tile[][] s)
	{
		for(int i=0;i<10;i++)
		{
			for(int j=0;j<10;j++)
			{
				Random r = new Random();
				char randomChar = charArray[r.nextInt(26)];
				s[i][j].setCharacter(randomChar);
				s[i][j].setSize(40,40);
				s[i][j].setBackground(Color.black);
				s[i][j].setOpaque(true);
				s[i][j].setForeground(Color.orange);
				s[i][j].setBorder(BorderFactory.createLineBorder(Color.ORANGE,1));
				s[i][j].setText(Character.valueOf(randomChar).toString());
			}
			
		}
	}
	
	/*
	 * ActionListener for START button
	 * when the button is clicked,
	 * start the timer and shuffle tiles.
	 */
	public class StartListener implements MouseListener
	{

		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub
			// Start Listening Grid events
			startGame.setForeground(Color.BLACK);
			//panelEast.remove(startGame);
			startBool = true;
			//panelSouth.remove(startGame);
			shuffleTiles(t);
			startTime = System.nanoTime();
			textarea.setText("");
			textField.setText("");
			i=0;
			hintCount=0;
			
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub		
		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub			
		}

		
	}
	/*
	 * ActionListener for STOP button
	 * when the button is clicked,
	 * stop the timer, display the score and time taken.
	 */
	public class StopListener implements MouseListener
	{

		@Override
		public void mouseClicked(MouseEvent e) {
			startBool = false;
			textField.setText("");
			endTime = System.nanoTime();
			panelEast.remove(score);
			long time = (endTime-startTime)/1000000000;
			score.setText("Score: "+currScore);
			score.setForeground(Color.ORANGE);;
			panelEast.add(score);
			score.setForeground(Color.GREEN);
			score.setFont(new Font("MONOSPACE",Font.BOLD,16));
			timeTaken.setText("Time Taken:"+time+" sec.");
			timeTaken.setFont(new Font("MONOSPACE",Font.BOLD,16));
			timeTaken.setForeground(Color.ORANGE);
			panelEast.add(timeTaken);
			textField.setText("");
			Tile.removeMainString();
			listofstring.clear();
			currScore = 0;
			startTime = System.nanoTime();
			
			// Refreshing the tiles.
			for(int i=0;i<10;i++)
			{
				for(int j=0;j<10;j++)
				{
					t[i][j].setBackground(Color.black);
					t[i][j].setOpaque(true);
					t[i][j].setVisited(false);
					panel.setBackground(Color.black);//panel color
					t[i][j].setForeground(Color.orange);
					t[i][j].setBorder(BorderFactory.createLineBorder(Color.ORANGE,1));
				}
				
			}
			shuffleTiles(t);
			Tile.refreshListeners(t);
			i=0;
			textarea.setText("");
			startGame.setForeground(Color.CYAN);

		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}


	}
	
	/*
	 * Listener for the CHECK Button
	 * calling the checkDictionary() method on mainString.
	 */
	public class ButtonListener implements MouseListener
	{

		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub
			isPresent = false;
			mainAppString = Tile.getmainString();
			checkDictionary();	
			if(!isPresent)
				JOptionPane.showMessageDialog(null, "Not a valid word");
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	/*
	 *	ActionListener for HINT Button.
	 *	List all the possible words that can be made using the clicked word.
	 */
	public class HintListener implements MouseListener
	{

		@Override
		public void mouseClicked(MouseEvent arg0) {
			// TODO Auto-generated method stub
			if(hintCount<3 && !textField.getText().equals("") && textField.getText().length()!=1)
			{
				List<String> predict = new LinkedList<String>();
				predict = autoDict.predictCompletions(textField.getText(), 3);	
				String str = "";
				for(String s: predict)
				{
					str = str.concat(s.toUpperCase()+"\n");
				}
				hintCount++;
				// Dialog Box to show hints.
				JOptionPane.showMessageDialog(null, str);
			}
			if(hintCount>=3 && hintCount<6)
			{
				currScore = currScore-3;
				hintCount++;
			}
			if(hintCount==6)
			{
				JOptionPane.showMessageDialog(null, "Can't use hint now");
			}
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mousePressed(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		
	}
}

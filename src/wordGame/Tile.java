package wordGame;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.*;
public class Tile extends JButton implements ActionListener{
	Character c;
	static String mainString="";
	Tile button;
	//Tile button;
	private boolean isVisited = false;
	static public boolean start = false;
	MainApp objectofMain;
	TileListener t = new TileListener();
	public Tile(Character s, MainApp e)
	{
		super(s.toString());
		this.objectofMain = e;
		button = this;
		this.addMouseListener(t);
		this.c = s;
		//makeButton();
	}
	
	// Return mainString
	public static String getmainString()
	{
		return mainString;
	}
	
	// Check if the tile is visited or not.
	public boolean getVisited()
	{
		return isVisited;
	}
	
	// Set the tile isVisited to true or false.
	public void setVisited(boolean b)
	{
		isVisited = b;
	}
	
	// Get the character of the button
	public Character getElement()
	{
		return c;
	}
	
	// Set the character of the button
	public void setCharacter(char s)
	{
		c = Character.valueOf(s);
	}
	
	// Remove the mainString to empty string.
	public static void removeMainString(){
		mainString="";
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	/*
	 * Refresh all the listener so that the button cant be clicked if already clicked.
	 */
	public static void refreshListeners(Tile[][] s)
	{
		for(int i=0;i<10;i++)
		{
			for(int j=0;j<10;j++)
			{
				if(s[i][j].getVisited())
				{
					s[i][j].isVisited = false;
				}
			}
		}
	}
	
	/*
	 * ActionListener for Tiles.
	 * if the tile is clicked set isVisited to true.
	 * check if the game is started, then only the button can be clicked.
	 */
	public class TileListener implements MouseListener
	{

		@Override
		public void mouseClicked(MouseEvent e) {
			
			// TODO Auto-generated method stub
			Tile buton = (Tile) e.getSource();
			if(!button.isVisited && MainApp.startBool)
			{
				setVisited(true);
				mainString = mainString.concat(getElement().toString());
				objectofMain.changeTextField(mainString);
				button.setBackground(Color.BLACK);
				button.setForeground(Color.BLACK);
				button.setBorder(BorderFactory.createLineBorder(Color.BLACK,1));
			}
			
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
	
}

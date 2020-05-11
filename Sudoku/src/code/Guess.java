package code;

public class Guess {
	
	private int[][] copyOfBoard = new int[9][9];
	private int thisGuess;
	
	public Guess(Cell[][] board)
	{
		boolean guessMade = false;
		
		for(int x = 0; x<9 ; x++)
			for(int y = 0; y < 9 ; y++)
			{
				copyOfBoard[x][y] = board[x][y].getNumber();
				
				if(board[x][y].getNumber() == 0 && !guessMade)
				{
					guessMade = true;
					thisGuess = board[x][y].getFirstPotential();
				}
			}
	}
	
	public int getThisGuess()
	{
		return thisGuess;
	}
	
	public int getBoardValueAt(int x, int y)
	{
		return copyOfBoard[x][y];
	}
	
}
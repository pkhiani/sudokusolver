package code;

import java.io.File;
import java.util.Scanner;

public class SudokuSolver {
	
	private static Cell[][] board = new Cell [9][9];
	
	public static void solve(int x, int y, int number)
	{
		board[x][y].setNumber(number);
		
		//turn off potential number for every other cell in that column
		for(int q = 0; q < 9; q++)
		{
			if(q != x)
				board[q][y].turnOffPotential(number);
		}
		
		//turn off potential number for every other cell in that row
		for(int q = 0; q < 9; q++)
		{
			if(q != y)
				board[x][q].turnOffPotential(number);
		}
		
		//turn off potential number for the box
		for(int i = 0; i < 9; i++)
		{
			for(int j = 0; j < 9; j++)
			{
				if(board[i][j].getBoxID() == board[x][y].getBoxID() && i != x && j != y)
						board[i][j].turnOffPotential(number);
			}
		}
	}
	
	public static void display()
	{
		for(int x = 0; x < 9; x++)
		{
			for(int y = 0; y < 9; y++)
			{
				if(y == 3 || y == 6)
					System.out.print("| ");
				System.out.print(board[x][y].getNumber() + " ");
			}
			System.out.println();
			if(x == 2 || x == 5)
				System.out.println("---------------------");
		}
		
		System.out.println();
	}
	
	public static void setBoxIDs()
	{
		for(int x = 0; x < 9; x++)	
		{
			for(int y = 0; y < 9; y++)
			{
				board[x][y].setBoxID((x/3)*3 + ((y/3)+1));
			}
		}
	}
	
	public static void loadPuzzle(String filename) throws Exception
	{
		File infile = new File(filename);
		Scanner input = new Scanner(infile);
		
		for(int x = 0; x < 9; x++)
			for(int y = 0; y < 9; y++)
			{
				int number = input.nextInt();
				if(number != 0)
				{
					solve(x, y, number);
				}
			}
	}
	
	public static boolean contradictions()
	{
		for(int x = 0; x < 9; x++)
			for(int y = 0; y < 9; y++)
				if(board[x][y].getNumber() == 0 && board[x][y].numberOfPotentials() == 0)
					return true;
		
		return false;
	}
	
	
	public static int solvedCells()
	{
		int counter = 0;
		
		for(int x = 0; x < 9; x++)
			for(int y = 0; y < 9; y++)
				if(board[x][y].getNumber() != 0)
					counter++;
		
		return counter;
	}
	
	//checks a row to see if there are two cells that share the same two potentials (2P)
	//if this condition exists, then the rest of the cells in the row cannot have these two numbers as potentials
	public static int logic4r() 
	{							
		int changes = 0;
		for(int x = 0; x < 9; x++)
		for(int y = 0; y < 9; y++) //cycling throughout the columns in row x
		{
			if(board[x][y].numberOfPotentials() == 2)//found a 2P cell
			{
				for(int j = y+1; j < 9; j++)
				{
					if(board[x][j].numberOfPotentials() == 2)//found another 2P cell
						{
							//check if they have the same two potentials
							if(board[x][y].getFirstPotential() == board[x][j].getFirstPotential() && board[x][y].getSecondPotential() == board[x][j].getSecondPotential())
							{
								//turn off potentials for the rest of the row
								//but not these two cells [x][y] & [x][j]
								for(int q = 0; q < 9; q++)
								{
							 		if(q == j || q==y)//if we are at one of the 2P cells
									{
							 			//do nothing
									}
									else//turn off potentials
									{
										if(board[x][q].canBe(board[x][y].getFirstPotential()))
										{
											board[x][q].turnOffPotential(board[x][y].getFirstPotential());
											changes++;
										}
										if(board[x][q].canBe(board[x][y].getSecondPotential()))
										{
											board[x][q].turnOffPotential(board[x][j].getSecondPotential());
											changes++;
										}
										
									}
								}	
							}
						}
				}
			}
		}
		return changes;
	}
	
	public static int logic4c() //checks a column to see if there are two cells that share the same potential
	{							 //if this condition exist then the rest of the cells in the row cannot have these two numbers as potentials
		int changes = 0;
		for(int y = 0; y < 9; y++)
		for(int x = 0; x < 9; x++) //cycling throughout the columns in row x
		{
			if(board[x][y].numberOfPotentials() == 2)//found a cell with 2 potentials
			{
				for(int j = y+1; j < 9; j++)
				{
					if(board[x][j].numberOfPotentials() == 2)//found another cell with 2 potentials 
						{
							//check if they have the same two potentials
							if(board[x][y].getFirstPotential() == board[x][j].getFirstPotential() && board[x][y].getSecondPotential() == board[x][j].getSecondPotential())
							{
								//turn off potentials for the rest of the row
								//but not these two cells [x][y] & [x][j]
								for(int q = 0; q < 9; q++)
								{
							 		if(q == j || q == y)//if we are at one of the two cells
									{
							 			//do nothing
									}
									else//turn off potentials
									{
										if(board[x][q].canBe(board[x][y].getFirstPotential()))
										{
											board[x][q].turnOffPotential(board[x][y].getFirstPotential());
											changes++;
										}
										if(board[x][q].canBe(board[x][y].getSecondPotential()))
										{
											board[x][q].turnOffPotential(board[x][j].getSecondPotential());
											changes++;
										}
										
									}
								}	
							}
						}
				}
			}
		}
		return changes;
	}
	
	public static int logic4b()
	{
		int changes = 0;
		
		//checks each box
		for(int boxID = 1; boxID < 10; boxID++)
		{
			
			//checks for box in use
			for(int x = 0; x < 9; x++)
			{
				for(int y = 0; y < 9; y++)
				{
				//if the cell has 2P
				if(board[x][y].getBoxID() == boxID && board[x][y].numberOfPotentials() == 2)
				{
					for(int i = 0; i < 9; i++) //tries to find a different 2P
					{
						for(int j = 0; j < 9; j++)
						{
							if(board[i][j].getBoxID() == boxID && board[i][j].numberOfPotentials() == 2 && x != i && y != j)
							{//checks to make sure both 2P cells are different
								if(board[i][j].getFirstPotential() == board[x][y].getFirstPotential() && board[i][j].getSecondPotential() == board[x][y].getSecondPotential())
								{//eliminates all other potentials from the box
									for(int q = 0; q < 9; q++)
									{
										for(int w = 0; w < 9; w++)
										{
											if(board[q][w].getBoxID() == board[x][y].getBoxID() && q != x && w != y && q != i && w != j)
											{
												if(board[q][w].canBe(board[x][y].getFirstPotential()))
												{
													changes++;
													board[q][w].turnOffPotential(board[x][y].getFirstPotential());
												}
												
												if(board[q][w].canBe(board[x][y].getSecondPotential()))
												{
													changes++;
													board[q][w].turnOffPotential(board[x][y].getSecondPotential());
												}
											}
										}
									}
								}
							}
						}
					}
				}
					
				}
			}
			
		}
		
		return changes;
	}
	
	
	public static int logic5()
	{
		int changes = 0;
		for(int x = 0; x < 9; x++)
			for(int y = 0; y < 9; y++)
				if(board[x][y].getNumber() == 0 && board[x][y].numberOfPotentials() == 1) //if the cell has only one potential
				{
					changes++;
					solve(x,y,board[x][y].getFirstPotential()); //runs solve method
				}
		return changes;
	}
	
	public static int logic1()
	{
		int changes = 0;
		
		for(int x = 0; x < 9; x++)
			for(int y = 0; y < 9; y++)
			{
				int counter = 0;
				if(board[x][y].getNumber() == 0)//if the cell is blank
				{
					for(int i = 1; i < 9; i++)
					{
						if(board[x][y].canBe(i) == false) //if the potential at i is false
						{			
							counter++;
						}//runs the counter till it equals 8
					}
					for(int i = 1; i < 9; i++)
					{
					 if(board[x][y].canBe(i) == true)
					 {
						 
						 if(counter == 8)
						{
							solve(x, y, i); //runs solve method
						}	
					 }
					 
					}
				}	
			}
		return changes;	
	}
	public static int logic2()
	{
		int changes = 0;
		
		for(int x = 0; x < 9; x++)
			for(int y = 0; y < 9; y++)
			{
				int counter = 0;
				if(board[y][x].getNumber() == 0) //if the cell is blank
				{
					for(int i = 1; i < 9; i++)
					{
						if(board[y][x].canBe(i) == false) //if the potential at i is false
						{			
							counter++;
						}//runs the counter till it equals 8
					}
					
					for(int i = 1; i < 9; i++)
					{
					 if(board[y][x].canBe(i) == true)
					 {
						 if(counter == 8)
						{
							solve(y, x, i); //runs solve method
						}
						 
					 }
					 
					}
				}	
			}
		return changes;
	}
	
	public static int logic3() {
		
		int changes = 0;

		for (int i = 0; i < 10; i++) //numbers
			for (int j = 1; j < 10; j++) //boxes
			{
				
				int counter = 0;
				int rowNumber = 0;
				int columnNumber = 0;
				
				for (int x = 0; x < 9; x++) //rows
					for (int y = 0; y < 9; y++) //columns
					{
						if (board[x][y].getNumber() == 0 && board[x][y].getBoxID() == (j)) // if the cell is empty
						{//and the box ID is equal to the for loop traversing the boxes
							if (board[x][y].canBe(i) == true) //once the potential at i is true
							{
								counter++; //sets counter to 1, and runs solve method below
								rowNumber = x; //sets x and y to new variables
								columnNumber = y;//as they are not defined by for loops above
							}
						}
					}
				if (counter == 1) {
					solve(rowNumber, columnNumber, i);
					changes++;
				}
			}

		return changes;
	}
	
	public static boolean revert(Guess guess)
	{
		
		for(int x = 0; x<9; x++)
			for(int y = 0; y < 9; y++)
			{
				board[x][y].reset();//resets grid
			}

		for(int x = 0; x<9; x++)
			for(int y = 0; y < 9; y++)
			{
				//reloads the sudoku
				if(guess.getBoardValueAt(x,y) != 0)
				{
					solve(x, y, guess.getBoardValueAt(x, y));
				}
			}

		//runs logic cycle again until it gets stuck
		int changes = 0;
		do
		{
			changes = 0;
			changes += logicCycle();

		}while(changes > 0);

	boolean potentialTurnedOff = false;
	
	for(int x = 0; x<9; x++)
		for(int y = 0; y < 9; y++)
		{
			if(board[x][y].getNumber() == 0 && !potentialTurnedOff)
			{
				//turns off potential of previously found cell
				potentialTurnedOff = true;
				board[x][y].turnOffPotentialsBefore(guess.getThisGuess());
		
		
				if(board[x][y].getFirstPotential() == -1)//if an error occurs
				{
					return false;
				}
				
				else
				{
					return true;
				}
			
			}
	}
	return false;

	}
		
	public static int logicCycle()
	{
		int changes = 0;
		
		do{
			
			changes = 0;
			changes += logic1();
			changes += logic2();
			changes += logic3();
			changes += logic4r();
			changes += logic4c();
			changes += logic4b();
			changes += logic5();

			display();
			
		}while(changes > 0);
		
		return changes;
	}

	public static void main(String[] args) throws Exception {

		Guess[] listOfGuesses = new Guess[81];
		int guessesCounter = 0;

		//makes 81 cells
		for(int x = 0; x < 9; x++)
			for(int y = 0; y < 9; y++)
				board[x][y] = new Cell();
		
		setBoxIDs();

		//loadPuzzle("easy.txt");
		//loadPuzzle("medium.txt");
		//loadPuzzle("hard.txt");
		loadPuzzle("oni.txt");
		
		display();

		boolean revertedSuccessfully = true;
		int changes = 0;
		do
		{
			//runs the logic cycle as long as there are changes being made
			do
			{
				changes = 0;
				changes += logicCycle();
	
			}while(changes > 0);
	
			//if no changes occurred, then run guessing algorithm
			if(solvedCells() < 81 && contradictions() == false)
			{
				boolean guessMade = false;
				//finds first unsolved cell
				for(int x = 0; x<9; x++)
					for(int y = 0; y < 9; y++)
					{
						if(!guessMade && board[x][y].getNumber() == 0 && board[x][y].getFirstPotential() != -1)
						{
							display();
							System.out.println("Made a guess: " + board[x][y].getFirstPotential() + " at " + x + "," + y);
							
							listOfGuesses[guessesCounter] = new Guess(board);//copies board
							
							guessesCounter++;
							
							solve(x,y,board[x][y].getFirstPotential());
							
							guessMade = true;
	
						}
					}
	
			}
			
			if(solvedCells() < 81 && contradictions() == true)
			{
				do 
				{
					revertedSuccessfully = revert(listOfGuesses[guessesCounter-1]);
					guessesCounter--;
				}while(revertedSuccessfully == false);
			}


		}while(solvedCells() < 81);


		display();
		System.out.println();
		
	}

}

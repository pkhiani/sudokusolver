package code;

public class Cell 
{
	private int number;
	private int boxID;
	private boolean[] potential = {false,true,true,true,true,true,true,true,true,true};
	
	public Cell()
	{
		number = 0;
		boxID = 0;
	}
	
	public void turnOffPotentialsBefore(int number)
	{
		for(int x = number; x >0; x--)
			potential[x] = false;	
	}
	
	public void reset()
	{
		for(int x = 1; x < 10; x++)
			potential[x] = true;
		number = 0;
	}
	
	
	public int numberOfPotentials()
	{
		int count = 0;
		for(int x = 0; x < 10; x++) 
		{
			if(potential[x] == true)
				count++;
		}
		return count;
	}
	
	public int getFirstPotential()
	{
		for(int x = 0; x < 10; x++)
		{
			if(potential[x] == true)
				return x;
		}
		return -1;
	}
	
	public int getSecondPotential()
	{
		boolean firstFound = false;
		
		for(int x = 0; x < 10; x++)
		{
			if(potential[x] == true && !firstFound)
			{
				firstFound = true;
			}
			else if(potential[x] == true)
			{
				return x;
			}
		}
		return -1;
	}
	
	public boolean canBe(int number)
	{
		if(number < 10 && number > 0)
			return potential[number];
		
		else return false;
	}
		
	public int getNumber() 
	{
	return number;
	}
	
	public void setNumber(int number) 
	{
		this.number = number;
		
		for(int x = 0; x<10; x++)
		{
			if(x != number)
			potential[x] = false;
		}
	}
	
	public int getBoxID() {
		return boxID;
	}
	
	public void setBoxID(int boxID) {
		this.boxID = boxID;
	}
	
	public boolean[] getPotential() {
		return potential;
	}
	
	public void setPotential(boolean[] potential) {
		this.potential = potential;
	}
	
	public void turnOffPotential(int number)
	{
		potential[number] = false;
	}
	
	public void showPotential()
	{
		for(int x = 1; x < 10; x++)
			System.out.print(x + ":" + potential[x] + " ");
	}
}
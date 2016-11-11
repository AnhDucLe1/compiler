import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class compiler {
	static List<String> terminal = new ArrayList<String>();
	static List<String> nonTerminal = new ArrayList<String>();
	static List<String> grammarList = new ArrayList<String>();
	static Map<String,Set<String>> firstSetTerminal = new LinkedHashMap<>();
	static Map<String,Set<String>> firstSet = new LinkedHashMap<>();
	static Map<String,Set<String>> followSet = new LinkedHashMap<>();
	static Map<String,Set<String>> followList = new LinkedHashMap<>();
	static String startSymbol;
	static List<ProductionGrammar> production = new ArrayList<ProductionGrammar>();


	public static void main(String[] args) {
		// TODO Auto-generated method stub		
		
		readFromBuffer("GrammarList.txt", grammarList);
		readFromBuffer("nonTerminalList.txt", nonTerminal);
		readFromBuffer("terminalList.txt", terminal);

	
		setProduction(grammarList);
		/*
		for (int j = 0; j <production.size(); j ++)
		{
			System.out.print("Number: " +j);
			System.out.print("LeftSide: "+ production.get(j).getLeftSide());
			for (int k = 0; k <production.get(j).getRightSide().size(); k++)
			System.out.print("RightSide: "+ production.get(j).getRightSide().get(k));
			System.out.print("\n");
		} */
		for (int i = 0 ; i < production.size(); i ++)
		{			
			List<String> test;
			test = production.get(i).getRightSide();
            //System.out.println("Left side:" + production.get(i).getLeftSide() + " :: ");  
			for(int j =0;j < test.size();j++){
			    //System.out.println(test.get(j));
			} 
		}
		buildTheFirst();
		printToTextFile(firstSet, "First");
		/*
		for (String name: firstSet.keySet())
		{
            String key = name.toString();
            String value = firstSet.get(name).toString();  
            System.out.println(key + " :: " + value);  
		} */
		// Start Symbol for the Program
		startSymbol = production.get(0).getLeftSide();
		buildTheFollow();
		printToTextFile(followSet,"Follow");

		/*for (String name: followSet.keySet())
		{
            String key = name.toString();
            String value = followSet.get(name).toString();  
            System.out.println("Key: " +key + " Terminal:" + value);  
		} 
		/*
		for (int j = 0; j <production.size(); j ++)
		{
			System.out.print("Number: " +j);
			System.out.print("LeftSide: "+ production.get(j).getLeftSide());
			for (int k = 0; k <production.get(j).getRightSide().size(); k++)
			System.out.print("RightSide: "+ production.get(j).getRightSide().get(k));
			System.out.print("\n");
		}
		*/
		
	}
	
	
	public static void buildTheFollow()
	{
		//create empty set
		for(int i = 0; i < nonTerminal.size(); i ++)
		{
			Set<String> emptySet = new LinkedHashSet<String>();
			followSet.put(nonTerminal.get(i),emptySet);
			Set<String> emptySet1 = new LinkedHashSet<String>();
			followList.put(nonTerminal.get(i),emptySet1);
		}
		
		buildSetFollow(production);
		buildFollow();
		for(int j = 0; j < nonTerminal.size(); j++)
		{
			String currentNonTerminal = nonTerminal.get(j);
			Set<String> duplicate = new LinkedHashSet<String>();
			buildfollowRecursive(currentNonTerminal, currentNonTerminal,duplicate);
		}
		System.out.println("----------Final Product------------------");

		for (String name: followSet.keySet())
		{
         String key = name.toString();
         String value = followSet.get(name).toString();  
         System.out.println(key + " :: " + value);  
		} 
		
		
		/*for(int i = 0; i <nonTerminal.size(); i ++)
		{
			String nonTm = nonTerminal.get(i);
			Set<String> originalSetFollow = new LinkedHashSet<String>();
			System.out.println("This is for: " +nonTerminal.get(i));

			buildSetFollow(nonTerminal.get(i),startSymbol, originalSetFollow, production);
			System.out.println(originalSetFollow.size());
			followSet.put(nonTm,originalSetFollow);
		}*/
	}
	//merge The FollowSet and the FollowList together
	
	public static void buildfollowRecursive(String currentPointedNTM, String originalNTM, Set<String>duplicate )
	{
		Set<String> tempSet = new LinkedHashSet<String>();
		tempSet = followList.get(currentPointedNTM);
		if (!tempSet.isEmpty())
		{
			for(String value : tempSet)
			{
				if(duplicate.contains(value))
				{
					continue;
				}
				else
				{
					Set<String> inputSet = new LinkedHashSet<String>();
					inputSet = followSet.get(originalNTM);
					inputSet.addAll(followSet.get(value));
					followSet.put(originalNTM,inputSet);
					duplicate.add(value);
					buildfollowRecursive(value,originalNTM,duplicate);
				}
			}
		}
		else
		{

			Set<String> inputSet = new LinkedHashSet<String>();
			inputSet = followSet.get(originalNTM);
			inputSet.addAll(followSet.get(currentPointedNTM));
			followSet.put(originalNTM,inputSet);
		}
	}
	
	public static void buildFollow()
	{
		System.out.println("-------------------------------");

		for (String name: followSet.keySet())
		{
         String key = name.toString();
         String value = followSet.get(name).toString();  
         System.out.println(key + " :: " + value);  
		} 
		System.out.println("-------------------------------");
		for (String name: followList.keySet())
		{
         String key = name.toString();
         String value = followList.get(name).toString();  
         System.out.println(key + " :: " + value);  
		} 
		
		
		
	}
	public static void buildSetFollow (List <ProductionGrammar> tempProduction)
	{
		//add start symbol for the P in our grammar
		Set<String> tempSet = new LinkedHashSet<String>();
		tempSet = followSet.get(startSymbol);
		tempSet.add("$");
		followSet.put(startSymbol,tempSet);
		//Go through the production right side for each element on the right side
		for(int i = 0 ; i < tempProduction.size(); i ++)
		{

			for(int j = 0; j< tempProduction.get(i).getRightSide().size(); j ++)
			{
				//check if it was nonterminal and last character. if it does than add the follow of the left side production
				if(nonTerminal.contains(tempProduction.get(i).getRightSide().get(j)) && j== tempProduction.get(i).getRightSide().size()-1)
				{
					System.out.println("This is for Nontermial:"+ tempProduction.get(i).getRightSide().get(j));
					Set<String> newSet = new LinkedHashSet<String>();					
					newSet.addAll(followList.get(tempProduction.get(i).getRightSide().get(j)));
					newSet.add(tempProduction.get(i).getLeftSide());
					//System.out.println("Follow "+tempProduction.get(i).getLeftSide());
					followList.put(tempProduction.get(i).getRightSide().get(j), newSet);
					
				}
				else
				{
					if(nonTerminal.contains(tempProduction.get(i).getRightSide().get(j)))
					{
						System.out.println("This is for Nontermial 2:"+ tempProduction.get(i).getRightSide().get(j));
						boolean allNTMempty = true;
						for(int k = j+1; k < tempProduction.get(i).getRightSide().size(); k ++ )
						{
							//if the next one is terminal
							System.out.println(">>>>>>>>>>>>>>>>>:"+ tempProduction.get(i).getRightSide().get(k));

							if(terminal.contains(tempProduction.get(i).getRightSide().get(k)))
							{
								System.out.println("This is for Terminal 2:"+ tempProduction.get(i).getRightSide().get(k));
								Set<String> newSet = new LinkedHashSet<String>();
								newSet = followSet.get(tempProduction.get(i).getRightSide().get(j));
								newSet.add(tempProduction.get(i).getRightSide().get(k));							
								followSet.put(tempProduction.get(i).getRightSide().get(j), newSet);
								allNTMempty = false;
								break;
							}
							//
							else
							{
								System.out.println("This is for Nontermial Inside:"+ tempProduction.get(i).getRightSide().get(k));

								Set<String> newSet = new LinkedHashSet<String>();
								newSet = followSet.get(tempProduction.get(i).getRightSide().get(j));
								Set<String> tempFirst = new LinkedHashSet<String>();
								tempFirst= firstSet.get(tempProduction.get(i).getRightSide().get(k));
								if(tempFirst.contains("empty"))
								{
									tempFirst.remove("empty");
									newSet.addAll(tempFirst);
									followSet.put(tempProduction.get(i).getRightSide().get(j), newSet);
									continue;
								}
								else
								{
									newSet.addAll(tempFirst);
									followSet.put(tempProduction.get(i).getRightSide().get(j), newSet);
									allNTMempty = false;
									break;
								}
							
							}			
						}	
						//when all is empty
						if (allNTMempty)
						{
							Set<String> newSet = new LinkedHashSet<String>();					
							newSet.addAll(followList.get(tempProduction.get(i).getRightSide().get(j)));
							newSet.add(tempProduction.get(i).getLeftSide());
							//System.out.println("Follow "+tempProduction.get(i).getLeftSide());
							followList.put(tempProduction.get(i).getRightSide().get(j), newSet);
						}
					}	
				}
			}
		}
	}

	//Go to each of the nonTerminal List
	public static void buildTheFirst ()
	{
		for (int i = 0; i < nonTerminal.size(); i ++)
		{
			String nonTM = nonTerminal.get(i);
			Set<String> orginalSet = new LinkedHashSet<String>();
			buildSetFirst(nonTerminal.get(i),orginalSet,production);
			firstSet.put(nonTM,orginalSet);
			//orginalSet.clear();
			
		}
	}
	// The actual code to build the first
	public static Set<String> buildSetFirst (String currentNTM, Set<String> originalSet,List<ProductionGrammar> tempProduction )
	{
		for(int i = 0; i < tempProduction.size();i ++)
		{
			//check if the currentNTM = leftside to point to the right production
			if(tempProduction.get(i).getLeftSide().equals(currentNTM))
			{
				//First rule if it is a terminal or empty add it to the set;
				//Check if first character on the production is equal to terminal or Empty
				if(tempProduction.get(i).getRightSide().get(0).equals("empty") || terminal.contains(tempProduction.get(i).getRightSide().get(0)))
					{
					 originalSet.add(tempProduction.get(i).getRightSide().get(0));
					}
				//If it is nonterminal
				else
				{
					// if it pointed to the same such as L :: L D
					if (tempProduction.get(i).getRightSide().get(0).equals(currentNTM))
					{
						//if it pointed to the same make a new production and remove the same
						List<ProductionGrammar> product = new ArrayList<ProductionGrammar>();
						for(int k = 0 ; k < tempProduction.size();k ++)
						{
				            ProductionGrammar temppg = new ProductionGrammar(tempProduction.get(k).getLeftSide(), tempProduction.get(k).getRightSide());
							product.add(temppg);
						}
						product.remove(i);
						
						//make a new empty set to check for empty
						Set<String> tempSet = new HashSet<String>();
						// check if it return empty and size is 1 if it does move the CurrentTM to the next value
						if (buildSetFirst(currentNTM,tempSet, product).size() == 1 && buildSetFirst(currentNTM,tempSet, product).contains("empty"))
						{
							//System.out.print((buildSet(tempProduction.get(i).getRightSide().get(1),originalSet, tempProduction)[0]);
							originalSet.addAll(buildSetFirst(tempProduction.get(i).getRightSide().get(1),originalSet, tempProduction));						
						}
					}	
			//move on to the next value
			else
					{
						originalSet.addAll(buildSetFirst(tempProduction.get(i).getRightSide().get(0),originalSet, tempProduction));						
					}
				}	
			}
		}
		return originalSet;
	}
	
	
	// parse the production put in the list object....... 
	public static void setProduction ( List<String> grammar)
	{
		
		for(int i = 0 ; i < grammar.size(); i ++)
		{
			String[] productionSides = grammar.get(i).split("::");
			String tempString = productionSides[1].trim();
			String leftSide = productionSides[0].trim();
			String[] tempArray = tempString.split(" ");
			List<String> tempList =  new ArrayList<String>(Arrays.asList(tempArray));
            ProductionGrammar pg = new ProductionGrammar(leftSide, tempList);
			production.add(pg);	
		}
	}
	
	// set up the map for termimal
	public static void setValueInMap (List <String> TempList, Map<String, Set<String>> tempMap)
	{
		for (String temp : TempList) {
			   Set<String> tempset = new HashSet<String>();
			   tempset.add(temp);
			   tempMap.put(temp,tempset);
			}
	}
	
	//store the files.txt to a list
	public static void readFromBuffer( String fileName ,  List<String> theList) 
	{
		BufferedReader reader = null;
		try {
		    File file = new File(fileName);
		    reader = new BufferedReader(new FileReader(file));
		    String line;
		    while ((line = reader.readLine()) != null) {
		    	theList.add(line);	
		    	}
		} catch (IOException e) {
		    e.printStackTrace();
		} finally {
		    try {
		        reader.close();
		    } catch (IOException e) {
		        e.printStackTrace();
		    }
		}
    }
//print to the Text file
 public static void printToTextFile(Map<String,Set<String>> mapPrintout, String nameoftheText)
 {
	 try {
		 String path = "C:\\Users\\Duc Le\\Desktop\\Compiler\\" ;
		 path = path.concat(nameoftheText);
		 path = path.concat(".txt");
		 File file = new File(path);

			// if file doesnt exists, then create it
		 if (!file.exists()) {
		    file.createNewFile();
			}

			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			for (String name: mapPrintout.keySet())
			{
	         String key = name.toString();
	         String value = mapPrintout.get(name).toString();  
	         bw.write(key + " :: " + value);  
	         bw.newLine();
			} 
			bw.close();
	} catch (IOException  e) {
		e.printStackTrace();
	}

 }
}

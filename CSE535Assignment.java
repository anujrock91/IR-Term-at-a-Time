import java.util.*;
import java.io.*;

public class CSE535Assignment {
	
	public static int TAATAndCount = 0;
	public static int TAATORCount = 0;
	
	public static void main( String [] args) throws IOException
	{
		String fileName = args[0].toString();
		BufferedReader bufferObject;
		bufferObject = new BufferedReader( new FileReader(fileName));
		String fileLines;
		int count;
		String result;
		DocPost DocPostObj;
		 
		ArrayList<LinkedList> DocFrArrayList = new ArrayList<LinkedList>();
		ArrayList<TopKTerms> TopKTermsArrayList = new ArrayList<TopKTerms>();
		ArrayList<String> Terms = new ArrayList<String>(); 
		HashMap<String,LinkedList<DocPost>> TermHashmap = new HashMap<String,LinkedList<DocPost>>();
		

				
//Opening a file for writing
		String LogFile = args[1].toString();
		PrintWriter writer = new PrintWriter(LogFile, "UTF-8");
		
		
// Reading the input idx file				

		
		while(( fileLines = bufferObject.readLine()) != null)
		{
			LinkedList<DocPost> DocFrLinkList= new LinkedList<DocPost>();
			String StrSpace [] = fileLines.split("\\\\");			
			TopKTermsArrayList.add(new TopKTerms(StrSpace[0],StrSpace[1]));				
			StrSpace[2]=StrSpace[2].substring(2, StrSpace[2].length()-1);			

			String spaceSub []= StrSpace[2].split(" ");	
			Terms.add(StrSpace[0]);			
			if(spaceSub.length > 1)
			{
				for(count =0; count < spaceSub.length-1; count++)
				{					
					StringBuilder sb = new StringBuilder(spaceSub[count]);
			
					sb.deleteCharAt(spaceSub[count].length()-1);
					result = sb.toString();					
 					
					String SplitDocIDFr[] = result.split("/");
					DocFrLinkList.add(new DocPost(Integer.parseInt(SplitDocIDFr[0]),Integer.parseInt(SplitDocIDFr[1])));	
				}
			   String FinalDocIDf[] = spaceSub[spaceSub.length-1].split("/");
			   DocFrLinkList.add(new DocPost(Integer.parseInt(FinalDocIDf[0]),Integer.parseInt(FinalDocIDf[1])));			   
			}						
			else
			{				
				String SplitDocIDFr[] = spaceSub[0].split("/");
				DocFrLinkList.add(new DocPost(Integer.parseInt(SplitDocIDFr[0]),Integer.parseInt(SplitDocIDFr[1])));	
			}
			TermHashmap.put(StrSpace[0], DocFrLinkList);				
		}	
		
//Sorting the top K terms based on the postings length
		
		Collections.sort(TopKTermsArrayList, new Comparator<TopKTerms>(){
			public int compare(TopKTerms t1, TopKTerms t2){
				return Integer.compare(t2.FinalpostingsCount, t1.FinalpostingsCount);
			}	
		});
		
		

//Printing the top K terms based on the term Postings	
		int KTerms = Integer.parseInt(args[2].toString());
		int i;
		writer.println("Function: getTopK " + Integer.toString(KTerms));
		writer.print("Result");
		writer.print(": ");
		for(i=0;i<KTerms;i++)
		{			
			//System.out.println(TopKTermsArrayList.get(i).Term);
			writer.print(TopKTermsArrayList.get(i).Term.toString());
			if(i != (KTerms-1))
			{
				writer.print(", ");
			}
			
		}
		
 
//Reading Input File				
		
		String InputFile = args[3].toString();
		String InputFileLines;
				BufferedReader bufferObjectInput = new BufferedReader(new FileReader(InputFile));
		while((InputFileLines = bufferObjectInput.readLine()) != null)
		{
			int numInputTerms = 0;
			ArrayList<String> InputTerms = new ArrayList<String>();

			String InputStrings[] = InputFileLines.split(" ");
			if(InputStrings.length > 1)
			{	
			for(int x=0; x<InputStrings.length; x++)
			{

				numInputTerms = ++numInputTerms;
				InputTerms.add(InputStrings[x]);
			}
			}
			
			else if(InputStrings.length == 1)
			{
				numInputTerms = ++numInputTerms;
				InputTerms.add(InputStrings[0]);
			}
			

			
// Checking if there are inputs given in input file			
			if(numInputTerms == 0)
			{
				writer.println(" ");
				writer.print("FUNCTION: term not found");			
			}
			
			
			else{
							
// Getting the postings list of each term
				
				
				for(int InputTermsPostingsDocID=0; InputTermsPostingsDocID<InputTerms.size();InputTermsPostingsDocID++)
				 {
					 writer.println("  ");
					 int foundflag1= 0;
					 for(String Key: TermHashmap.keySet())
						{
							if(Key.equals(InputTerms.get(InputTermsPostingsDocID).toString()))
							{
								foundflag1 =1;
								writer.println("FUNCTION: getPostings " + InputTerms.get(InputTermsPostingsDocID).toString() +" ");
								writer.print("Ordered by doc IDs: ");
								int last1 = 0;
								for(DocPost DocPostObjDID: TermHashmap.get(Key))
								{
									writer.print(Integer.toString(DocPostObjDID.documentId));
									if(last1 != TermHashmap.get(Key).size()-1)
									{
										last1 = ++last1;
										writer.print(", ");
									}

								}
								
								writer.println("  ");
							}			
						}
					 if(foundflag1 == 0)
					 {
								writer.println("FUNCTION: getPostings " + InputTerms.get(InputTermsPostingsDocID).toString() +" ");
								writer.print("Ordered by doc IDs: ");
								writer.println("term not found ");
								
							
					 }
				 			
					int foundflag2=0;	
				    String str2 = InputTerms.get(InputTermsPostingsDocID).toString();
					for( String key: TermHashmap.keySet()){	
							if(str2.equals(key))
							{
								writer.print("Ordered by TF: ");
								Collections.sort(TermHashmap.get(key), new Comparator<DocPost>(){
									public int compare(DocPost d1, DocPost d2){
										return Integer.compare(d2.termFrequency, d1.termFrequency);
									}	
								});
								
								foundflag2 = 1;
								int last2=0;				
								for(DocPost DockPostObjFR: TermHashmap.get(key))
								{
									writer.print(Integer.toString(DockPostObjFR.documentId));
									if(last2 != TermHashmap.get(key).size()-1)
									{
										last2 = ++last2;
									writer.print(", ");
									}
								}
								
								Collections.sort(TermHashmap.get(key), new Comparator<DocPost>(){
									public int compare(DocPost d1, DocPost d2){
										return Integer.compare(d1.documentId, d2.documentId);
									}	
								});				
								
							}
							
							
						}
					
					if(foundflag2 == 0)
					{
						writer.print("Ordered by TF: ");
						writer.print("term not found");
					}
				 }
				 
				 
				 
				 
 // Begin TAAT AND
				 
				 writer.println(" ");
				 writer.print("FUNCTION: termAtATimeQueryAnd ");
				 for(int querryTermsLoop = 0; querryTermsLoop<InputTerms.size(); querryTermsLoop++)
				 {
					 writer.print(InputTerms.get(querryTermsLoop));
					 if(querryTermsLoop < (InputTerms.size()-1))
					 {
					 writer.print(", ");
					 }
				 }
// If there is only one term in input				 
				 	if(numInputTerms == 1)
				 	{
				 		writer.println(" ");
				 		int flagTAATand1 = 0;
				 		Long TAATnumInputTerms1 = System.currentTimeMillis();
				 		for(String Key: TermHashmap.keySet())
				 		{
				 			if(Key.equals(InputTerms.get(0)))
				 			{
				 				writer.println(Integer.toString((TermHashmap.get(InputTerms.get(0)).size())) + " documents are found");
							    writer.println("0 comparisions are made");
							    long StartTimeAnd1 = System.currentTimeMillis();
				 				flagTAATand1 = 1;
				 				LinkedList<DocPost> TAATand1 = TermHashmap.get(InputTerms.get(0));
				 				Collections.sort(TAATand1, new Comparator<DocPost>(){
									public int compare(DocPost d1, DocPost d2){
										return Integer.compare(d2.termFrequency, d1.termFrequency);
									}	
								});
				 				long EndTimeAnd1 = System.currentTimeMillis();
				 				long TotalTimeAnd1 = EndTimeAnd1 - StartTimeAnd1;
				 				writer.println(Long.toString(TotalTimeAnd1) + " Milliseconds are used");
				 				writer.print("Result: ");
				 				int size = 0;
				 				for(DocPost DocPostTAAT1: TermHashmap.get(InputTerms.get(0)))
				 						{
				 					        size = ++size;    					
				 					    	writer.print(Integer.toString(DocPostTAAT1.documentId));
				 					    	if(size < (TermHashmap.get(InputTerms.get(0)).size()-1))
				 					    	{
				 					    		writer.print(", ");
				 					    	}
				 						}
				 				
				 				Collections.sort(TAATand1, new Comparator<DocPost>(){
									public int compare(DocPost d1, DocPost d2){
										return Integer.compare(d1.documentId, d2.documentId);
									}	
								});
				 			}
				 		}
				 		Long EndTAATnumInputTerms1 = System.currentTimeMillis();
				 		Long TotalTAATnumInputTerms1 = EndTAATnumInputTerms1 - TAATnumInputTerms1;
				 		
//If there is no such term in the file				 		
				 		if(flagTAATand1 == 0)
				 		{
				 			writer.println("0 documemts are found");
				 			writer.println("0 comparisions are made");
							writer.println(Long.toString(TotalTAATnumInputTerms1) + " Milliseconds are used");
							writer.println("Result: term not found");
				 		}
				 		}
				 	
				 	
// If the input terms are equal to 2	
				 	else if(numInputTerms == 2)
				 	{
				 		writer.println(" ");
				 		int flagTAAT2 = 0;
				 		long StartFinding2 = System.currentTimeMillis();
				 		for(int checkLinkList = 0; checkLinkList < numInputTerms; checkLinkList++)
				 		{
				 			for(String Key: TermHashmap.keySet())
				 			{
				 				if(Key.equals(InputTerms.get(checkLinkList)))
				 				{
				 					flagTAAT2 = ++flagTAAT2;
				 				}
				 			}
				 		}
				 		long EndFinding2 = System.currentTimeMillis();
				 		long TotalFinding2 = (EndFinding2 - StartFinding2);
				 		
				 		if(flagTAAT2 == numInputTerms)
				 		{
				 			long StartTimeTAATand2 = System.currentTimeMillis();
				 			Collections.sort(TermHashmap.get(InputTerms.get(0)), new Comparator<DocPost>(){
								public int compare(DocPost d1, DocPost d2){
									return Integer.compare(d2.termFrequency, d1.termFrequency);
								}	
							});
				 			
				 			Collections.sort(TermHashmap.get(InputTerms.get(1)), new Comparator<DocPost>(){
								public int compare(DocPost d1, DocPost d2){
									return Integer.compare(d2.termFrequency, d1.termFrequency);
								}	
							});
				 			LinkedList<DocPost> tempList1 = new LinkedList<DocPost>();
				 			tempList1 = IntersectList(TermHashmap.get(InputTerms.get(0)),TermHashmap.get(InputTerms.get(1)));
				 			
				 			Collections.sort(TermHashmap.get(InputTerms.get(0)), new Comparator<DocPost>(){
								public int compare(DocPost d1, DocPost d2){
									return Integer.compare(d1.documentId, d2.documentId);
								}	
							});
				 			
				 			Collections.sort(TermHashmap.get(InputTerms.get(1)), new Comparator<DocPost>(){
								public int compare(DocPost d1, DocPost d2){
									return Integer.compare(d1.documentId, d2.documentId);
								}	
							});
				 			
				 			long EndTimeTAATand2 = System.currentTimeMillis();
				 			long TotalTimeTAATand2 = (EndTimeTAATand2 - StartTimeTAATand2)+TotalFinding2;
				 			if(tempList1.size()==0  || tempList1.isEmpty())
				 			{
				 				writer.println("0 documemts are found");
				 				writer.println(Integer.toString(TAATAndCount) + " comparisions are made");
				 				writer.println(Long.toString(TotalTimeTAATand2) + " Milliseconds are used");
				 				writer.println("Result: term not found");
				 			}
				 			else{
				 				Collections.sort(tempList1, new Comparator<DocPost>(){
				 					public int compare(DocPost d1, DocPost d2){
				 						return Integer.compare(d1.documentId, d2.documentId);
				 					}	
				 				});
				 				
				 			    writer.println(tempList1.size() + " documemts are found");
				 			    writer.println(Integer.toString(TAATAndCount) + " comparisions are made");
				 			    writer.println(Long.toString(TotalTimeTAATand2) + " milliseconds are used");
				 			    writer.print("Result: ");
				  				for(int finalListCount=0; finalListCount<tempList1.size(); finalListCount++)
				 				{
				 					
				  					writer.print(tempList1.get(finalListCount).documentId);
				  					if(finalListCount < (tempList1.size() - 1))
				  					{
				  						writer.print(", ");
				  					}
				 				}
				  				writer.println(" ");
				 			}
				 		}
				 		else if(flagTAAT2 < numInputTerms)
				 		{
				 			//System.out.println("We cannot have an intersection");
				 			writer.println("0 documemts are found");
							writer.println("0 comparisions are made");
							writer.println(Long.toString(TotalFinding2) + " Milliseconds are used");
							writer.println("Result: term not found");
				 		}
				 	}
				 	
// If the input terms are zero or no input				 	
				 	else if(numInputTerms == 0)
				 	{
				 		writer.println(" ");
				 		writer.println("0 documemts are found");
						writer.println("0 comparisions are made");
						writer.println("1 Milliseconds are used");
						writer.println("Result: term not found");
				 	}
				 	
//If the input is more than 2				 	
				 	else if(numInputTerms > 2)
				 	{
				 		long StartTimeTermFindAnd3 = System.currentTimeMillis();
				 		writer.println(" ");
				 		int flagTAATn = 0;
				 		for(int checkLinkList = 0; checkLinkList < numInputTerms; checkLinkList++)
				 		{
				 			for(String Key: TermHashmap.keySet())
				 			{
				 				if(Key.equals(InputTerms.get(checkLinkList)))
				 				{
				 					flagTAATn = ++flagTAATn;
				 				}
				 			}
				 		}
				 		long EndTimeTermFindAnd3 = System.currentTimeMillis();
				 		long TotalTimeFinding3 = (EndTimeTermFindAnd3-StartTimeTermFindAnd3);
				 		
				 		
				 		if(flagTAATn == numInputTerms)
				 		{
				 		
				 			LinkedList<DocPost> LinkedList1st= TermHashmap.get(InputTerms.get(0));
				 			LinkedList<DocPost> LinkedList2nd= TermHashmap.get(InputTerms.get(1));
				 			
				 			long StartTimeAnd3 = System.currentTimeMillis();
				 			Collections.sort(LinkedList1st, new Comparator<DocPost>(){
								public int compare(DocPost d1, DocPost d2){
									return Integer.compare(d2.termFrequency, d1.termFrequency);
								}	
							});
				 			
				 			Collections.sort(LinkedList2nd, new Comparator<DocPost>(){
								public int compare(DocPost d1, DocPost d2){
									return Integer.compare(d2.termFrequency, d1.termFrequency);
								}	
							});
				 			
				 			LinkedList<DocPost> tempList = new LinkedList<DocPost>();
				 			
				 			
				 			tempList = IntersectList(LinkedList1st,LinkedList2nd);
				 			
				 			Collections.sort(tempList, new Comparator<DocPost>(){
									public int compare(DocPost d1, DocPost d2){
										return Integer.compare(d2.termFrequency, d1.termFrequency);
									}	
								});
				 			
				 			Collections.sort(LinkedList1st, new Comparator<DocPost>(){
								public int compare(DocPost d1, DocPost d2){
									return Integer.compare(d1.documentId, d2.documentId);
								}	
							});
				 			
				 			Collections.sort(LinkedList2nd, new Comparator<DocPost>(){
								public int compare(DocPost d1, DocPost d2){
									return Integer.compare(d1.documentId, d2.documentId);
								}	
							});
				 			
				 			for(int restLists=2; restLists<numInputTerms; restLists++)
				 			{
				 				if(tempList.size() == 0 || tempList.isEmpty())
				 				{
				 					break;
				 				}
				 				else
				 				{
				 					
				 				Collections.sort(tempList, new Comparator<DocPost>(){
				 	 				public int compare(DocPost d1, DocPost d2){
				 	 					return Integer.compare(d2.termFrequency, d1.termFrequency);
				 	 				}	
				 				});	
				 				
				 				Collections.sort(TermHashmap.get(InputTerms.get(restLists)), new Comparator<DocPost>(){
				 					public int compare(DocPost d1, DocPost d2){
				 						return Integer.compare(d2.termFrequency, d1.termFrequency);
				 					}	
				 				});
				 				
				 				tempList = IntersectList(tempList,TermHashmap.get(InputTerms.get(restLists))); 				
				 				
				 				Collections.sort(TermHashmap.get(InputTerms.get(restLists)), new Comparator<DocPost>(){
				 					public int compare(DocPost d1, DocPost d2){
				 						return Integer.compare(d1.documentId, d2.documentId);
				 					}	
				 				});
				 				}
				 			}
				 			long EndTimeTAATAnd = System.currentTimeMillis();
				 			long TotalTimeTAATAnd3 = (EndTimeTAATAnd - StartTimeAnd3)+TotalTimeFinding3;
				 			if(tempList.size() == 0 || tempList.isEmpty())
								{
								
									writer.println("0 documents are found");
									writer.println(TAATAndCount + " comparisions are made");
									writer.println(Long.toString(TotalTimeTAATAnd3) + " milliseconds are used");
									writer.println("Result: term not found");
								}
				 			
				 			else
				 			{
				 				Collections.sort(tempList, new Comparator<DocPost>(){
				 					public int compare(DocPost d1, DocPost d2){
				 						return Integer.compare(d1.documentId, d2.documentId);
				 					}	
				 				});
				 				writer.println(Integer.toString(tempList.size())+" documents are found");
								writer.println(TAATAndCount + " comparisions are made");
								writer.println(Long.toString(TotalTimeTAATAnd3) + " milliseconds are used");
								writer.print("Result: ");
				 				
				 				for(int finalListCount=0; finalListCount<tempList.size(); finalListCount++)
				 				{
				 					
				 					writer.print(Integer.toString(tempList.get(finalListCount).documentId));
				 					if(finalListCount < (tempList.size()-1))
				 					{
				 						writer.print(", ");
				 					}
				 				}
				 				writer.println(" ");
				 				
				 			} 			
				 			
				 			}
				 			
				 		
				 		
				 		
				 		else if(flagTAATn < numInputTerms)
				 		{
				 			writer.println("0 documents are found");
							writer.println("0 comparisions are made");
							writer.println(Long.toString(TotalTimeFinding3) + " milliseconds are used");
							writer.println("Result: term not found"); 	
				 		}
				 	}
				 	
				 	
// END of TAAT AND
				 	
				 	
				 	
//Begin TAAT OR
				writer.print("FUNCTION: termAtATimeQueryOr ");
				for(int querryTermsLoop1 = 0; querryTermsLoop1<InputTerms.size(); querryTermsLoop1++)
				{
					 writer.print(InputTerms.get(querryTermsLoop1));
					 if(querryTermsLoop1 < (InputTerms.size()-1))
					 {
					 writer.print(", ");
					 }
				}
				writer.println(" ");

				long startFindingTAATOr = System.currentTimeMillis();
				ArrayList<String> tempTAATORArrayList = new ArrayList<String>();
				for(String Key: TermHashmap.keySet())
				{
					for(int TaatListLoop=0; TaatListLoop< numInputTerms; TaatListLoop++)
					{
						if(Key.equals(InputTerms.get(TaatListLoop)))
						{
							tempTAATORArrayList.add(InputTerms.get(TaatListLoop));
						}
					}
				}
				long endFindingTAATOr = System.currentTimeMillis();
				long totalFindingTAATOr = (endFindingTAATOr - startFindingTAATOr);
				
//If there is no matching term that is entered
				
				if(tempTAATORArrayList.size() == 0)
				{
					
					writer.println("0 documents are found");
					writer.println("0 comparisions are found");
					writer.println(Long.toString(totalFindingTAATOr) +" milliseconds are used");
					writer.print("Result: term not found");
				}

//If the matching term is equal to one				
				else if(tempTAATORArrayList.size() == 1)
				{
					writer.println(Integer.toString(TermHashmap.get((tempTAATORArrayList.get(0))).size()) + " documents are found");
					writer.println("0 comparisions are found");
					writer.println(Long.toString(totalFindingTAATOr) + " milliseconds are used");
					writer.print("Result: ");
					for(int tempTAATORArrayList1=0; tempTAATORArrayList1< (TermHashmap.get((tempTAATORArrayList.get(0)))).size(); tempTAATORArrayList1++)
					{
					
						writer.print(Integer.toString((TermHashmap.get(((tempTAATORArrayList.get(0)))).get(tempTAATORArrayList1)).documentId));
						if(tempTAATORArrayList1 < ((TermHashmap.get((tempTAATORArrayList.get(0)))).size() - 1))
						{
							writer.print(", ");			
						}
					}
				    
				}

//If the matching terms are more than one				
				else if(tempTAATORArrayList.size()>1)
				{
					long startTimeTAATOR2 = System.currentTimeMillis(); 
					LinkedList<DocPost> tempTAATORLinkedList = new LinkedList<DocPost>();
					for(int tempAssignLoop=0; tempAssignLoop< (TermHashmap.get(tempTAATORArrayList.get(0))).size() ; tempAssignLoop++)
					{
						tempTAATORLinkedList.add((TermHashmap.get(tempTAATORArrayList.get(0))).get(tempAssignLoop));
					}
					
					
					for(int ORLoop = 1; ORLoop< tempTAATORArrayList.size(); ORLoop++)
					{
						Collections.sort(tempTAATORLinkedList, new Comparator<DocPost>(){
							public int compare(DocPost d1, DocPost d2){
								return Integer.compare(d2.termFrequency, d1.termFrequency);
							}	
						});
						tempTAATORLinkedList = TaatORCombine(tempTAATORLinkedList, TermHashmap.get(tempTAATORArrayList.get(ORLoop)));			
					}
					
					Collections.sort(tempTAATORLinkedList, new Comparator<DocPost>(){
						public int compare(DocPost d1, DocPost d2){
							return Integer.compare(d1.documentId, d2.documentId);
						}	
					});
					
					long endTimeTAATOR2 = System.currentTimeMillis();
					long totalTimeTAATOR2 = (endTimeTAATOR2 - startTimeTAATOR2)+totalFindingTAATOr;
					
					writer.println(Integer.toString(tempTAATORLinkedList.size()) + " documents are found");
					writer.println(Integer.toString(TAATORCount) + " comparisions are made");
					writer.println(Long.toString(totalTimeTAATOR2) + " milliseconds are used");
					writer.print("Result: ");
					for(int s=0; s<tempTAATORLinkedList.size();s++)
					{
						
						writer.print(Integer.toString(tempTAATORLinkedList.get(s).documentId));
						if(s<(tempTAATORLinkedList.size() - 1))
						{
							writer.print(", ");
						}
					}
				}
			 	
//End TAAT OR 					 	
				 	
		}

			
	}
		
// Closing the log file		
writer.close();
 		
}
	
	
//Method for intersecting two link Lists	
static LinkedList<DocPost> IntersectList(LinkedList<DocPost> l1,LinkedList<DocPost> l2)
	{
		int i=l1.size();
		int j=l2.size();
		LinkedList<DocPost> Final = new LinkedList<DocPost>();

		for(int comp1=0; comp1<i; comp1++)
		{
			for(int comp2=0; comp2<j; comp2++)
			{
				TAATAndCount = ++TAATAndCount;
				if(l1.get(comp1).documentId == l2.get(comp2).documentId)
				{
					Final.add(l1.get(comp1));
					break;
				}
			}
			
		}
		return Final;
		
	}
	

//Method for Combining two link Lists
static LinkedList<DocPost> TaatORCombine(LinkedList<DocPost> l1, LinkedList<DocPost> l2)
	{
		int size1 = l1.size();
		int size2 = l2.size();
		int flag=0;
		
		Collections.sort(l2, new Comparator<DocPost>(){
			public int compare(DocPost d1, DocPost d2){
				return Integer.compare(d2.termFrequency, d1.termFrequency);
			}	
		});
		
		for(int i =0; i<size2; i++)
		{
			for(int j=0;j<size1; j++)
			{
				TAATORCount = ++TAATORCount;
				if(l2.get(i).documentId == l1.get(j).documentId)
				{
					flag = 1;
					break;
				}
			}
			if(flag == 0)
			{
				l1.add(l2.get(i));
				flag = 0;
			}
		}
		
		return l1;
	}
}





//Class for top K terms
class TopKTerms {
	public String Term;
	public String RawpostingsCount;
	public int FinalpostingsCount; 
	
	public TopKTerms(String Term, String postingsCount)
	{
		this.Term = Term;
		this.RawpostingsCount = postingsCount;
		StringBuilder PostingsLength = new StringBuilder(postingsCount);
		PostingsLength.deleteCharAt(0);
		String c = PostingsLength.toString();
		this.FinalpostingsCount = Integer.parseInt(c);
	}
}



// Class For Postings List
class DocPost {
	
	int documentId;
	int termFrequency;
	
	public DocPost(int id, int freq)
	{
		this.documentId = id;
		this.termFrequency = freq;
    }
	
}



package cs131.pa1.filter.sequential;
import cs131.pa1.filter.Message;
import java.util.*;

public class SequentialREPL {

	public static String currentWorkingDirectory;

	public static void main(String[] args){
		//set current working directory
		currentWorkingDirectory = System.getProperty("user.dir");
		Scanner input = new Scanner(System.in);
		System.out.print(Message.WELCOME.toString());
		System.out.print(Message.NEWCOMMAND.toString());
		
		//obtain user commands
		String commands = input.nextLine();
		
		//shell runs until user command equates to "exit" 
		while (!commands.equals("exit")) {
			
			if (!commands.equals("")) {
				//Make a new sequential filter
				List<ModifiedSequentialFilter> filters = SequentialCommandBuilder.createFiltersFromCommand(commands);
				
				//If there is at least one command, process it first, then see whether an error is thrown, 
				//If error was thrown from the last command, cease to continue onto the next filter(s). 
				if (filters != null) {
					filters.get(0).process();
					
					for (int i = 1; i < filters.size();i++) {
						
						if(filters.get(i-1).cont && filters.get(i-1).output.size() != 0) {
							filters.get(i).process();
						} else {
							filters = null;
							break;
						}
					}
					
					//Only print output if no errors were thrown when running filters, there is output to be printed,
					//and CAT didn't throw an error
					if(filters != null && filters.get(filters.size()-1).cont && filters.get(filters.size()-1).contForCat) {
						Queue<String> outputs = filters.get(filters.size()-1).output;	
						
						if (outputs != null) {
							
							for (String output: outputs) {
								System.out.println(output);
							}
						}
					}
				}
			}
			
			//After completing current commands, ask for a new set of commands
			System.out.print(Message.NEWCOMMAND.toString());
			commands = input.nextLine();
		}
		
		//user commands to exit shell 
		input.close();
		System.out.print(Message.GOODBYE.toString());
	}
}

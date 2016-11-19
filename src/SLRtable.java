import java.util.List;

public class SLRtable {
	private final String symbol;
	private final String command;
	private final int state;
	public SLRtable(String symbol, String command, int state) {
		this.symbol = symbol;
		this.command = command;
		this.state = state;
	}
	public SLRtable(String symbol, String command) 
	{
		this.symbol = symbol;
		this.command = command;
		this.state = -1;

	}
	public String getSymbol() {
		return symbol;
	}
	public String getCommand() {
		return command;
	}
	public int getState() {
		return state;
	}
	
	
	
}

import java.util.List;

public class ProductionGrammar {
	private final String leftSide;
	private final List <String> rightSide;
	
	public ProductionGrammar (String leftSide, List<String> rightSide)
	{
		this.leftSide = leftSide;
		this.rightSide = rightSide;
	}

	public String getLeftSide() {
		return leftSide;
	}

	public List<String> getRightSide() {
		return rightSide;
	}
	
}

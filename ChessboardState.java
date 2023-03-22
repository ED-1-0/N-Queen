import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ChessboardState implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private boolean goalReached;
	private float noOfTries;
	private List<Integer> successSteps;
	private List<Integer> failureSteps;
	
	public boolean isGoalReached() {
		return goalReached;
	}
	public void setGoalReached(boolean goalReached) {
		this.goalReached = goalReached;
	}
	public float getNoOfTries() {
		return noOfTries;
	}
	public void setNoOfTries(float noOfTries) {
		this.noOfTries = noOfTries;
	}
	public List<Integer> getSuccessSteps() {
		if(null == successSteps || successSteps.isEmpty()) {
			successSteps = new ArrayList<>();
		}
		return successSteps;
	}
	public void setSuccessSteps(List<Integer> successSteps) {
		this.successSteps = successSteps;
	}
	public List<Integer> getFailureSteps() {
		if(null == failureSteps || failureSteps.isEmpty()) {
			failureSteps = new ArrayList<>();
		}
		return failureSteps;
	}
	public void setFailureSteps(List<Integer> failureSteps) {
		this.failureSteps = failureSteps;
	}
	
}
package net.whiteWolfdoge.relux.task;


/**
 * A RelightSegment is a tool to relight a series of blocks, taking some basic statistics.
 */
public class RelightSegment{
	private long timeStart;
	private long timeFinish;
	/**
	 * Constructor to limit default constructor
	 */
	private RelightSegment(){
		// Do nothing
	}
	
	/**
	 * This constructor allows you to crate a new RelightSegment using a list of blocks.
	 */
	public RelightSegment(Block[] newBlkList){
		// TODO
	}
	
	/**
	 * This method gets the time that the relight segment began.
	 * @return The milliseconds between the time the relight segemnt began and the standard Java epoch (UTC)
	 * @see java.lang.System#currentTimeMillis()
	 */
	public long getTimeStart(){
		return timeStart;
	}
	
	/**
	 * This method gets the time that the relight segment completed.
	 * @return The milliseconds between the time the relight segemnt completed and the standard Java epoch (UTC)
	 * @see java.lang.System#currentTimeMillis()
	 */
	public long getTimeFinish(){
		return timeFinish;
	}
}

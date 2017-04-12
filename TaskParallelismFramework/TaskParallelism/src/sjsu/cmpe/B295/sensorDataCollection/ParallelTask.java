package sjsu.cmpe.B295.sensorDataCollection;

public class ParallelTask implements Parallelizable {
	private Parallelizable p;
	
	public ParallelTask(){
		
	}
	public ParallelTask(Parallelizable parallelizable) {
		// TODO Auto-generated constructor stub
		this.p = parallelizable;
	}

	public void perform() {
		System.out.println("In Base Class perform");
	}
	
	public void start() throws InterruptedException{
		p.perform();
	}

}

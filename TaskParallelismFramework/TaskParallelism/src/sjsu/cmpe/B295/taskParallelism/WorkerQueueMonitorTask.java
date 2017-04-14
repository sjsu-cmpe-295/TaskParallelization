package sjsu.cmpe.B295.taskParallelism;

import java.util.TimerTask;

import sjsu.cmpe.B295.election.Follower;

public class WorkerQueueMonitorTask extends TimerTask {
	private Follower f;

	public WorkerQueueMonitorTask(Follower f) {
		this.f = f;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (true) {
//			try {
//				this.f.getMyTaskQueue().take().perform();
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
		}
		
	}

}

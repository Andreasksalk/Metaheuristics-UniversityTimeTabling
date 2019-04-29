
class StopWatch {
	private double startTime;
	
	public StopWatch() {
		startTime = -1;
	}
	
	public void start() {
		startTime = System.currentTimeMillis()/1000;
	}

	public double stop() {
		double elapsedTime = lap();
		startTime = -1;
		return elapsedTime/1000;
	}
	public double lap() {
		if (startTime ==-1) {
			throw new RuntimeException("The stop watch must be started before it can be stopped");
			}
		return (System.currentTimeMillis()/1000-startTime);
		}
	
	
	}


package fr.inra.sad.bagap.chloe.thread;

public class MyThread implements Runnable {

	private int index;
	
	public MyThread(int index){
		this.index = index;
	}
	
	@Override
	public void run() {
		for(int i=1; i<1000; i++){
			System.out.println(index+" "+i);
		}
	}

}

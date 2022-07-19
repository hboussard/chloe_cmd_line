package fr.inra.sad.bagap.chloe.thread;

import java.lang.Thread.State;

public class MyTestThread {

	public static void main(String[] args) {
		
		ThreadGroup tg = new ThreadGroup("Mon groupe");
		
		Thread t1 = new Thread(tg, new MyThread(1));
		Thread t2 = new Thread(tg, new MyThread(2));
		Thread t3 = new Thread(tg, new MyThread(3));

		
		t1.start();
		t2.start();
		t3.start();
		
		System.out.println("--------------------->>>>>> ici");
		
		while(t1.getState() != State.TERMINATED
				&& t2.getState() != State.TERMINATED
				&& t3.getState() != State.TERMINATED){
			
		}
		
		System.out.println("---------------------->>>>>>>>>>>>>>>> fin");
		
		
	}

}

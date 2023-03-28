package model;

import view.View;

public class RefreshThread extends Thread {

	private View view;

	public RefreshThread(View view) {
		this.view = view;
		this.start();
	}

	public void run() {
		long beforeTime, timeDiff, sleep;

		beforeTime = System.currentTimeMillis();
		while (true) {

			view.repaint();

			timeDiff = System.currentTimeMillis() - beforeTime;
			sleep = 33 - timeDiff;

			if (sleep < 0) {
				sleep = 2;
			}

			try {
				Thread.sleep(sleep);
			} catch (java.lang.InterruptedException a) {
				System.out.println(a.getStackTrace());
			}
			beforeTime = System.currentTimeMillis();
		}
	}

}

package com.xianxian.comm.utils;

import java.util.List;

import com.google.inject.Singleton;
import com.xian.common.Objects;
import com.xian.common.logger.Logger;
import com.xian.common.logger.LoggerFactory;
import com.xian.common.util.CircularQueue;

/**
 * 任务队列
 */
@Singleton
public class JobQuery extends Thread   {
	
	private static final Logger logger = LoggerFactory.getLogger(JobQuery.class);
	
	private boolean isFlag;
	
	private CircularQueue<Runnable> queue;
	
	public JobQuery() {
		isFlag = false;
		queue = new CircularQueue<>();
	}
	
	public void addWork(Runnable task) {
		synchronized (queue) {
			queue.push(task);
			queue.notify();
		}
	}
	
	public void addWork(List<Runnable> taskList) {
		synchronized (queue) {
			taskList.forEach(task->queue.push(task));
			queue.notify();
		}
	}
	
	public void startUP() {
		isFlag = false;
		this.start();
	}
	
	public void shutDown() {
		isFlag = true;
	}
	
	@Override
	public void run() {
		while (!isFlag) {
			doWork(queue);
		}
	}
	
	private void doWork(CircularQueue<Runnable> queue) {
		Runnable runnable = null;
		synchronized (queue) {
			if (queue.isEmpty()) {
				try {
					queue.wait(1000L);
				} catch (InterruptedException e) {
				}
			}
			runnable = queue.pop();
		}
		
		if (Objects.nonNull(runnable)) {
			try {
				logger.debug("==============begin:{}==============", runnable);
				long time1 = System.nanoTime();
				runnable.run();
				long time2 = System.nanoTime();
				logger.debug("==============begin:{}, time:{}==============", runnable, (time2 - time1)/1000000);
			} catch (Throwable e) {
				logger.error("", e);
			}
		}
		
	}
}

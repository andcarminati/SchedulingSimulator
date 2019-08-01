/* 
 * This file is part of the Operating Systems Course.
 * Copyright (c) 2019 Andreu Carminati.
 * 
 * This program is free software: you can redistribute it and/or modify  
 * it under the terms of the GNU General Public License as published by  
 * the Free Software Foundation, version 3.
 *
 * This program is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU 
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License 
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package simulator.engine.scheduler;
import java.util.LinkedList;
import java.util.Queue;

import simulator.engine.AbstractScheduler;
import simulator.engine.Task;

public class FIFOScheduler extends AbstractScheduler{

	private Task currentTask;
	private Queue<Task> readyQueue;
	
	public FIFOScheduler() {
		super();
		currentTask = null;
		readyQueue = new LinkedList<Task>();
	}
	
	@Override
	public void addTask(Task task) {
		readyQueue.add(task);
		
	}

	@Override
	public boolean needReschedule() {
		
		boolean reschedule = false;
		
		if(currentTask == null) {
			reschedule = true;
		} else {
			int currentExecTime = currentTask.getRemainingExecutionTime();
			currentExecTime--;
			currentTask.setRemainingExecutionTime(currentExecTime);
			if(currentExecTime == 0) {
				reschedule = true;
			}
		}
		return reschedule;
	}

	@Override
	public Task nextTask() {
		
		Task next = null;
		
		if(currentTask == null) {
			if(!readyQueue.isEmpty()) {
				currentTask = readyQueue.remove();
				next = currentTask;
			} else {
				next = getIdleTask();
			}
		} else if(!readyQueue.isEmpty()) {
			currentTask = readyQueue.remove();
			next = currentTask;
		} else {
			currentTask = null;
			next = getIdleTask();
		}
		
		return next;
	}

	
}

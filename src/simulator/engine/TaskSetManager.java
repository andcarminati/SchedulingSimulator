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

package simulator.engine;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class TaskSetManager {

	private List<Task> arrivedTaskSet;
	private List<Task> initialTaskSet;
	private List<Task> taskSet;
	
	public TaskSetManager() {
		
		initialTaskSet = TaskSetBuilder.buildTaskSet(5);
		taskSet = new LinkedList<Task>();
		duplicateTaskSet(taskSet, initialTaskSet);
		arrivedTaskSet = new LinkedList<Task>();
	}
	
	public void reset() {
		taskSet.clear();
		arrivedTaskSet.clear();
		duplicateTaskSet(taskSet, initialTaskSet);
	}
	
	private void duplicateTaskSet(List<Task> out, List<Task> in) {
		in.forEach((t) -> {
			Task tn = t.clone();
			out.add(tn);
		});
	}
	
	public List<Task> getTaskSet(){
		return taskSet;
	}
	
	public List<Task> getInitialTaskSet(){
		return initialTaskSet;
	}
	
	public List<Task> getArrivedTaskSet(){
		return arrivedTaskSet;
	}
	
	public Task checkArrival(AbstractScheduler sched, Clock clock) {
		
		Iterator<Task> iterator = taskSet.iterator();
		while (iterator.hasNext()) {
			Task task = iterator.next();
			if (task.getArrivalTime() == clock.getTime()) {
				iterator.remove();
				arrivedTaskSet.add(task);
				sched.addTask(task);
				return task;
			}
		}
		
		return null;
	}
	
	public boolean hasMoreTaskstoArrive() {
		return !taskSet.isEmpty();
	}
}

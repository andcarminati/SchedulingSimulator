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
public abstract class AbstractScheduler {

	private Clock clock;
	private final Task idleTask;
	
	public AbstractScheduler() {
		clock = new Clock();
		idleTask = new Task(100, 100, Integer.MAX_VALUE, Integer.MAX_VALUE, 0);
	}
	
	public Task getIdleTask() {
		return idleTask;
	}
	
	public void timeTick() {
		clock.tick();
	}
	
	public int getTick() {
		return clock.getTime();
	}
	
	
	public abstract void addTask(Task task);
	public abstract boolean needReschedule();
	public abstract Task nextTask();
}

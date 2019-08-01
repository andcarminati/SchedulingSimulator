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

public class Task {

	private int PID;
	private int priority;
	private int executionTime;
	private int remainingExecutionTime;
	private int arrivalTime;
	
	public Task(int pID, int priority, int executionTime,
			int remainingExecutionTime, int arrivalTime) {
		super();
		PID = pID;
		this.priority = priority;
		this.executionTime = executionTime;
		this.remainingExecutionTime = remainingExecutionTime;
		this.arrivalTime = arrivalTime;
	}
	
	public Task(){
		this(0,0,0,0,0);
	}
	
	public int getArrivalTime() {
		return arrivalTime;
	}

	public void setArrivalTime(int arrivalTime) {
		this.arrivalTime = arrivalTime;
	}

	public int getPID() {
		return PID;
	}
	public void setPID(int pID) {
		PID = pID;
	}
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
	public int getExecutionTime() {
		return executionTime;
	}
	public void setExecutionTime(int executionTime) {
		this.executionTime = executionTime;
	}
	public int getRemainingExecutionTime() {
		return remainingExecutionTime;
	}
	public void setRemainingExecutionTime(int remainingExecutionTime) {
		this.remainingExecutionTime = remainingExecutionTime;
	}
	
	public Task clone() {
		return new Task(PID, priority, executionTime, executionTime, arrivalTime);
	}
	
}

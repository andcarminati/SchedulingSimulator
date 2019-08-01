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
import java.util.LinkedList;
import java.util.List;

public class TaskSetBuilder {

	
	public static List<Task> buildTaskSet(int n){
		List<Task> set = new LinkedList<Task>();
		
		for (int i = 0; i < n; i++) {
			Task t = new Task(i+1, 0, 10, 10, 0);
			set.add(t);
		}
		return set;
	}
}
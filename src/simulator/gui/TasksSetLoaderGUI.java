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
package simulator.gui;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.AbstractTableModel;

import simulator.engine.Task;

public class TasksSetLoaderGUI extends JDialog implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3499538205052265505L;
	private JButton button1;
	private JButton button2;
	private JButton button3;
	private JButton button4;
	private JLabel label;
	private TaksTableModel model;
	private JTable jTableTasks;
	private List<Task> tasks;
	private final static String EXTENSION = ".tsk";

	public TasksSetLoaderGUI(JFrame frame, List<Task> taskSet) {

		super(frame, "Edit task set", true);

		setLayout(new FlowLayout());
		setModal(true);
		setResizable(false);
		setLocationRelativeTo(frame);
		setSize(new Dimension(400, 430));
		
		tasks = taskSet;

		label = new JLabel("Update here the taks to be scheduled:");
		add(label);

		model = new TaksTableModel(taskSet);
		jTableTasks = new JTable(model);
		JScrollPane scroll = new JScrollPane(jTableTasks);
		add(scroll);
		scroll.setPreferredSize(new Dimension(300, 300));

		button1 = new JButton("New Task");
		button1.addActionListener(this);
		add(button1);

		button1 = new JButton("Remove Task");
		button1.addActionListener(this);
		add(button1);

		button2 = new JButton("Close");
		button2.addActionListener(this);
		add(button2);
		
		button3 = new JButton("Export to file");
		button3.addActionListener(this);
		add(button3);
		
		button4 = new JButton("Import from file");
		button4.addActionListener(this);
		add(button4);
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getActionCommand().equals("New Task")) {
			int id = 0;
			if (model.getRowCount() > 0) {
				id = model.getTask(model.getRowCount() - 1).getPID();
			}
			id++;

			Task task = new Task(id, 0, 0, 0, 0);
			model.addTask(task);

		} else if (e.getActionCommand().equals("Close")) {
			setVisible(false);
		} else if (e.getActionCommand().equals("Remove Task")) {
			int index = jTableTasks.getSelectedRow();
			if (index == -1) {
				JOptionPane.showMessageDialog(this, "Select a task to remove!");
			} else {
				model.removeTask(index);
			}
		} else if(e.getActionCommand().equals("Import from file")) {
			importTaskSet();
		} else if(e.getActionCommand().equals("Export to file")) {
			exportTaskSet();
		}
	}
	
	private void importTaskSet() {
		JFileChooser chooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("TASK SET FILES", "tsk", "text");
		chooser.setFileFilter(filter);
	    int retrival = chooser.showOpenDialog(null);
	    if (retrival == JFileChooser.APPROVE_OPTION) {
	        try {
	            FileReader fr = new FileReader(chooser.getSelectedFile());
	            BufferedReader reader = new BufferedReader(fr);
	            List<Task> newTasks = new LinkedList<Task>();
	            while(reader.ready()) {
	            	String str = reader.readLine();
	       
	            	StringTokenizer tokenizer = new StringTokenizer(str);
	            	
	            	int pid = Integer.parseInt(tokenizer.nextToken());
	            	int arrivalTime = Integer.parseInt(tokenizer.nextToken());
	            	int executionTime= Integer.parseInt(tokenizer.nextToken());
	            	int priority = Integer.parseInt(tokenizer.nextToken());
	            	int remainingTime= Integer.parseInt(tokenizer.nextToken());
	            	
	            	Task t = new Task(pid, priority, executionTime, remainingTime, arrivalTime);
	            	newTasks.add(t);
	            	
	            }
	            if(!newTasks.isEmpty()) {
	            	tasks.clear();
	            	tasks.addAll(newTasks);
	            	model.fireTableDataChanged();
	            }
	            reader.close();
	        } catch (Exception ex) {
	        	JOptionPane.showMessageDialog(null, "Unable to import file");
	        }
	    }
	}
	
	private void exportTaskSet() {
		JFileChooser chooser = new JFileChooser();
	    int retrival = chooser.showSaveDialog(null);
	    if (retrival == JFileChooser.APPROVE_OPTION) {
	        try {
	        	String filename = chooser.getSelectedFile().toString();
	        	if(filename.contains(EXTENSION)) {
	        		filename = filename.substring(0, 
	        				filename.length());
	        	}
	            FileWriter fw = new FileWriter(chooser.getSelectedFile()+EXTENSION);
	            for (Iterator<Task> iterator = tasks.iterator(); iterator.hasNext();) {
					Task task = iterator.next();
					fw.write(""+ task.getPID()+
							" "+ task.getArrivalTime()+
							" "+ task.getExecutionTime()+
							" "+ task.getPriority()+ 
							" "+task.getRemainingExecutionTime()+"\n");
				}
	            fw.close();
	        } catch (Exception ex) {
	        	JOptionPane.showMessageDialog(null, "Unable to save file");
	        }
	    }
	}
}

class TaksTableModel extends AbstractTableModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4353248822934889887L;
	private List<Task> tasks;
	private String[] colunas = new String[] { "PID", "Priority", "Exec. Time", "Arrival Time" };

	public TaksTableModel(List<Task> tasks) {
		this.tasks = tasks;
	}

	public TaksTableModel() {
		this.tasks = new ArrayList<Task>();
	}

	public int getRowCount() {
		return tasks.size();
	}

	public int getColumnCount() {
		return colunas.length;
	}

	@Override
	public String getColumnName(int columnIndex) {
		return colunas[columnIndex];
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return String.class;
	}

	public void setValueAt(Task aValue, int rowIndex) {
		Task usuario = tasks.get(rowIndex);

		usuario.setPID(aValue.getPID());
		usuario.setPriority(aValue.getPriority());
		usuario.setExecutionTime(aValue.getExecutionTime());

		fireTableCellUpdated(rowIndex, 0);
		fireTableCellUpdated(rowIndex, 1);
		fireTableCellUpdated(rowIndex, 2);
		fireTableCellUpdated(rowIndex, 3);

	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		Task task = tasks.get(rowIndex);
		
		int value=-1;
		try {
			value = Integer.parseInt(aValue.toString());
			if(value < 0) {
				JOptionPane.showMessageDialog(null, "Negative numbers are not allowed!");
				return;
			}
		} catch(NumberFormatException e) {
			JOptionPane.showMessageDialog(null, "Only numbers are allowed!");
		}

		switch (columnIndex) {
		case 0:
			if(value != -1) {
				task.setPID(value);
			}
			break;
		case 1:
			if(value != -1) {
				task.setPriority(value);
			}
			break;
		case 2:
			if(value != -1) {
				task.setExecutionTime(value);
			}
			break;
		case 3:
			if(value != -1) {
				task.setArrivalTime(value);
			}	
			break;
		default:
			System.err.println("Invalid col. index: " + columnIndex);
		}
		fireTableCellUpdated(rowIndex, columnIndex);
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		Task selectedTask = tasks.get(rowIndex);
		String valueObject = null;
		switch (columnIndex) {
		case 0:
			valueObject = "" + selectedTask.getPID();
			break;
		case 1:
			valueObject = "" + selectedTask.getPriority();
			break;
		case 2:
			valueObject = "" + selectedTask.getExecutionTime();
			break;
		case 3:
			valueObject = "" + selectedTask.getArrivalTime();
			break;
		default:
			System.err.println("Invalid index");
		}

		return valueObject;
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		if (columnIndex == 0) {
			return false;
		}
		return true;
	}

	public Task getTask(int lineIndex) {
		return tasks.get(lineIndex);
	}

	public void addTask(Task u) {
		tasks.add(u);

		int lastIndex = getRowCount() - 1;

		fireTableRowsInserted(lastIndex, lastIndex);
	}

	public void removeTask(int lineIndex) {
		tasks.remove(lineIndex);

		fireTableRowsDeleted(lineIndex, lineIndex);
	}

	public void addTasks(List<Task> newTasks) {

		int olderSize = getRowCount();
		tasks.addAll(newTasks);
		fireTableRowsInserted(olderSize, getRowCount() - 1);
	}

	public void clear() {
		tasks.clear();
		fireTableDataChanged();
	}

	public boolean isEmpty() {
		return tasks.isEmpty();
	}

}

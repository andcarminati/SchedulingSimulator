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
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import simulator.engine.AbstractScheduler;
import simulator.engine.Clock;
import simulator.engine.Task;
import simulator.engine.TaskSetManager;
import simulator.engine.scheduler.FIFOScheduler;
import simulator.engine.scheduler.NPPScheduler;
import simulator.engine.scheduler.PPScheduler;
import simulator.engine.scheduler.RRScheduler;
import simulator.engine.scheduler.SJFScheduler;

public class GUI implements ActionListener {

	private JFrame frame;
	private JTextPane textPane;
	private JTextPane taskInPane;
	private JTextPane taskOutPane;
	private JTextArea textArea;
	private JLabel label1;
	private JLabel label2;
	private JLabel label3;
	private JLabel label4;
	private JLabel label5;
	private JLabel label6;
	private JLabel label7;
	private JTextField field1;
	private JComboBox<String> comboBox;
	private JComboBox<Double> comboBox2;
	private JButton button1;
	private JButton button2;
	private JButton button3;
	private JButton button5;
	private JButton button6;
	private JButton button8;
	private JButton button9;
	private Thread t;
	private volatile boolean finalizeThread = false;
	private volatile boolean stopThread = false;

	private Lock mutex;
	private Condition condition;

	private TaskSetManager taskManager;

	private StringBuilder builderExecution;
	private int builderCount;

	public GUI() {
		initializeTaskSet();
		initializeLocks();
		initializeFrame();
		initializeSelectionBox();
		initializeButtons();
		initializeTaskSetPanels();
		initializeTimeDisplay();
		initializeTimeChart();
		initializeLogArea();

		updateTaskPanelsAndLog(null, 0);
	}

	private void initializeTimeDisplay() {
		JSeparator sep;
		sep = new JSeparator(SwingConstants.HORIZONTAL);
		sep.setPreferredSize(new Dimension(650, 3));
		frame.add(sep);

		label7 = new JLabel("Simulation time (time units): ");
		frame.add(label7);

		field1 = new JTextField(6);
		field1.setEditable(false);
		frame.add(field1);
	}

	private void initializeLogArea() {
		label5 = new JLabel("Execution log:");
		frame.add(label5);

		textArea = new JTextArea();
		textArea.setFont(new Font("Verdana", Font.BOLD, 11));
		textArea.setLineWrap(true);

		JScrollPane scrollPane = new JScrollPane(textArea);
		scrollPane.setPreferredSize(new Dimension(620, 120));
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

		frame.add(scrollPane);
		textArea.setText("");
	}

	private void initializeTimeChart() {
		JSeparator sep;
		sep = new JSeparator(SwingConstants.HORIZONTAL);
		sep.setPreferredSize(new Dimension(650, 3));
		frame.add(sep);

		label6 = new JLabel("Execution time chart:");
		frame.add(label6);

		textPane = new JTextPane();
		textPane.setPreferredSize(new Dimension(630, 160));
		textPane.setContentType("text/html");
		textPane.setEditable(true);

		JScrollPane jsp = new JScrollPane(textPane);
		frame.add(jsp);

		sep = new JSeparator(SwingConstants.HORIZONTAL);
		sep.setPreferredSize(new Dimension(650, 3));
		frame.add(sep);
	}

	private void initializeTaskSetPanels() {
		JSeparator sep;
		sep = new JSeparator(SwingConstants.HORIZONTAL);
		sep.setPreferredSize(new Dimension(650, 3));
		frame.add(sep);

		JPanel taskPanel = new JPanel();
		taskPanel.setLayout(new GridBagLayout());
		frame.add(taskPanel);
		taskPanel.setPreferredSize(new Dimension(640, 170));
		GridBagConstraints c = new GridBagConstraints();

		label1 = new JLabel("Not arrived tasks:");
		c.weighty = 0.5;
		c.gridx = 0;
		c.gridy = 0;
		taskPanel.add(label1, c);

		label2 = new JLabel("Arrived tasks:");
		c.weighty = 0.5;
		c.gridx = 1;
		c.gridy = 0;
		taskPanel.add(label2);

		field1 = new JTextField(5);

		taskInPane = new JTextPane();
		taskInPane.setContentType("text/html");
		taskInPane.setEditable(false);
		JScrollPane sp1 = new JScrollPane(taskInPane);
		sp1.setPreferredSize(new Dimension(320, 150));
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weighty = 4;
		c.gridx = 0;
		c.gridy = 1;
		taskPanel.add(sp1, c);

		taskOutPane = new JTextPane();
		taskOutPane.setContentType("text/html");
		taskOutPane.setEditable(false);
		JScrollPane sp2 = new JScrollPane(taskOutPane);
		sp2.setPreferredSize(new Dimension(320, 150));
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weighty = 4;
		c.gridx = 1;
		c.gridy = 1;
		taskPanel.add(sp2, c);

	}

	private void initializeButtons() {
		button1 = new JButton("Simulate");
		button1.addActionListener(this);
		frame.add(button1);
		button2 = new JButton("Finalize");
		button2.addActionListener(this);
		button2.setEnabled(false);
		frame.add(button2);

		button9 = new JButton("Reset");
		button9.addActionListener(this);
		button9.setEnabled(false);
		;
		frame.add(button9);

		button3 = new JButton("Stop");
		button3.addActionListener(this);
		button3.setEnabled(false);
		frame.add(button3);

		button6 = new JButton("Next");
		button6.addActionListener(this);
		button6.setEnabled(false);
		frame.add(button6);

		button5 = new JButton("Continue");
		button5.addActionListener(this);
		button5.setEnabled(false);
		frame.add(button5);

		button8 = new JButton("Edit Task Set");
		button8.setActionCommand("EditTaskSet");
		button8.addActionListener(this);
		frame.add(button8);
	}

	private void initializeSelectionBox() {
		label3 = new JLabel("Select a scheduler: ");
		frame.add(label3);
		comboBox = new JComboBox<String>();
		comboBox.addItem("First In First Out");
		comboBox.addItem("Shortest Job First");
		comboBox.addItem("Round Robin");
		comboBox.addItem("Preemptive Priority");
		comboBox.addItem("Non preemptive Priority");
		comboBox.setSelectedIndex(0);
		frame.add(comboBox);
		label4 = new JLabel("Simulation interval (sec.): ");
		frame.add(label4);
		comboBox2 = new JComboBox<Double>();
		comboBox2.addItem(0.1);
		comboBox2.addItem(0.5);
		comboBox2.addItem(1.0);
		comboBox2.addItem(2.0);
		comboBox2.addItem(4.0);
		comboBox2.setSelectedIndex(0);
		frame.add(comboBox2);
	}

	private void initializeFrame() {
		frame = new JFrame("Task scheduler simulator by Prof. Andreu Carminati");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(680, 660);
		frame.setVisible(true);
		frame.setLayout(new FlowLayout());
		frame.setResizable(false);
	}

	private void initializeLocks() {
		mutex = new ReentrantLock();
		condition = mutex.newCondition();
	}

	private void prepareToReset() {

		Runnable r = () -> {
			button9.setEnabled(true);
			button3.setEnabled(false);
			button2.setEnabled(false);

		};

		SwingUtilities.invokeLater(r);
	}

	private void initializeTaskSet() {
		taskManager = new TaskSetManager();
		builderExecution = new StringBuilder();
		builderCount = 0;
	}

	private void checkStop() {

		try {
			mutex.lock();
			if (stopThread) {
				condition.await();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			mutex.unlock();
		}
	}

	private void stopThread() {
		stopThread = true;
	}

	private void resumeThread() {
		stopThread = false;
		try {
			mutex.lock();
			condition.signal();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			mutex.unlock();
		}
	}

	private void stepThread() {
		try {
			mutex.lock();
			if (stopThread) {
				condition.signal();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			mutex.unlock();
		}
	}

	private AbstractScheduler getScheduler() {
		AbstractScheduler scheduler = null;
		String name = comboBox.getItemAt(comboBox.getSelectedIndex());

		if (name.equals("First In First Out")) {
			scheduler = new FIFOScheduler();
		} else if (name.equals("Shortest Job First")) {
			scheduler = new SJFScheduler();
		} else if (name.equals("Round Robin")) {
			scheduler = new RRScheduler();
		} else if (name.equals("Preemptive Priority")) {
			scheduler = new PPScheduler();
		} else if (name.equals("Non preemptive Priority")) {
			scheduler = new NPPScheduler();
		}

		return scheduler;
	}

	private int getSimulationTime() {

		return (int) (1000 * comboBox2.getItemAt(comboBox2.getSelectedIndex()));
	}

	public void runSimulation() {

		t = new Thread(() -> {

			//int time = 0;
			Clock clock = new Clock();
			AbstractScheduler sched = getScheduler();
			Task current = null;

			updateLogPanel("Starting simulation using: " + sched.getClass().getName());

			while (!finalizeThread) {

				try {
					Thread.sleep(getSimulationTime());
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				checkStop();

				Task task;

				while ((task = taskManager.checkArrival(sched, clock)) != null) {
					updateLogPanel("Task " + task.getPID() + " arrived.");
				}

				sched.timeTick();
				if (sched.needReschedule()) {
					Task oldCurrent = current;
					current = sched.nextTask();

					if (current == sched.getIdleTask() && !taskManager.hasMoreTaskstoArrive()) {
						updateLogPanel("Simulation terminated.");
						finalizeThread = true;
						continue;
					}

					if (oldCurrent != current) {
						if (current == sched.getIdleTask()) {
							updateLogPanel("Task IDLE " + " is executing.");
						} else {
							updateLogPanel("Task " + current.getPID() + " is executing.");
						}
					}
				}
				updateTaskPanelsAndLog(current, clock.getTime());
				clock.tick();;
			}
			updateTaskPanelsAndLog(null, clock.getTime());
			stopThread = false;
			finalizeThread = false;
			// resetSimulation();
			prepareToReset();
		});
		t.start();
	}

	private void resetSimulation() {

		button1.setEnabled(true);
		button2.setEnabled(false);
		button3.setEnabled(false);
		button5.setEnabled(false);
		button6.setEnabled(false);
		button8.setEnabled(true);
		builderExecution = new StringBuilder();
		builderCount = 0;
		reinitializeTaskSet();
		updateTaskPanelsAndLog(null, 0);
	}

	public static void main(String[] args) throws InvocationTargetException, InterruptedException {

		SwingUtilities.invokeLater(() -> {
			new GUI();
		});
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getActionCommand().equals("Simulate")) {
			button1.setEnabled(false);
			button2.setEnabled(true);
			button3.setEnabled(true);
			button8.setEnabled(false);
			runSimulation();
		} else if (e.getActionCommand().equals("Finalize")) {
			finalizeThread = true;
			resumeThread();
			button1.setEnabled(false);
			button2.setEnabled(false);
			button3.setEnabled(false);
			button5.setEnabled(false);
			button6.setEnabled(false);
			// resetSimulation();
		} else if (e.getActionCommand().equals("Stop")) {
			button3.setEnabled(false);
			button5.setEnabled(true);
			button6.setEnabled(true);
			stopThread();
		} else if (e.getActionCommand().equals("Continue")) {
			button3.setEnabled(true);
			button5.setEnabled(false);
			button6.setEnabled(false);
			resumeThread();
		} else if (e.getActionCommand().equals("Next")) {
			stepThread();
		} else if (e.getActionCommand().equals("EditTaskSet")) {
			List<Task> initialTaskSet = taskManager.getInitialTaskSet();
			TasksSetLoaderGUI d = new TasksSetLoaderGUI(frame, initialTaskSet);
			d.setVisible(true);
			resetSimulation();
		} else if (e.getActionCommand().equals("Reset")) {
			button9.setEnabled(false);
			resetSimulation();

		}
	}

	private void reinitializeTaskSet() {
		taskManager.reset();
	}

	private void updateLogPanel(String msg) {

		Runnable r = () -> {
			String text = textArea.getText();
			text += "\n" + msg;
			textArea.setText(text);

		};

		if (SwingUtilities.isEventDispatchThread()) {
			r.run();

		} else {
			SwingUtilities.invokeLater(r);
		}
	}

	private void updateTaskPanelsAndLog(Task task, int time) {

		Runnable r = () -> {
			taskInPane.setText(genHTMLForUnarrived(taskManager.getTaskSet()));
			taskOutPane.setText(genHTMLForUnarrived(taskManager.getArrivedTaskSet()));
			textPane.setText(updateHTMLForExecution(task));
			if (time != -1) {
				field1.setText("" + time);
			}

		};

		if (SwingUtilities.isEventDispatchThread()) {
			r.run();

		} else {
			SwingUtilities.invokeLater(r);
		}
	}

	private String genHTMLForUnarrived(List<Task> tasks) {
		StringBuilder builder = new StringBuilder();

		builder.append("<!DOCTYPE html>\n<html>\n<head>\n</head>\n<body style=\"background-color:#DCDCDC;\">\n");

		tasks.forEach((task) -> {
			String img = getTaskImage(task.getPID());
			builder.append("<img src=" + img + "\" width=\"18\" height=\"12\">");
			builder.append("T" + task.getPID() + " - AT: " + task.getArrivalTime() + " - ET: " + task.getExecutionTime()
					+ " - RT: " + task.getRemainingExecutionTime() + " - PRI: " + task.getPriority());
			builder.append("<br>");
		});

		builder.append("\n</body>\n</html>");
		return builder.toString();
	}

	private String updateHTMLForExecution(Task task) {

		String init = "<!DOCTYPE html>\n<html>\n<head>\n</head>\n<body style=\"background-color:#DCDCDC;\">\n";
		String end = "\n</body>\n</html>";

		if (task != null) {
			String img = getTaskImage(task.getPID());

			builderExecution.append("<img src=" + img + "\" width=\"18\" height=\"12\">");
			builderCount++;

			if (builderCount % 34 == 0) {
				builderExecution.append("<br>");
			}
		}

		return init + builderExecution.toString() + end;
	}

	private String getTaskImage(int i) {
		return this.getClass().getClassLoader().getResource("task" + i + ".png").toString();
	}
}

package Views;

import javax.swing.JPanel;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JToolBar;
import javax.swing.ScrollPaneLayout;
import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;
import com.github.lgooddatepicker.components.TimePicker;
import com.github.lgooddatepicker.components.TimePickerSettings;
import com.github.lgooddatepicker.components.TimePickerSettings.TimeIncrement;
import com.github.lgooddatepicker.optionalusertools.PickerUtilities;
import com.github.lgooddatepicker.zinternaltools.Pair;

import DataBase.DataBase;
import Main.MainWindowView;
import Model.Assignment;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JScrollPane;
import java.awt.event.ContainerAdapter;
import java.awt.event.ContainerEvent;
import java.awt.Component;
import java.awt.Label;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.Instant;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import java.awt.Panel;
import javax.swing.SwingConstants;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.border.BevelBorder;
import Main.*;
import javax.swing.JTextArea;

public class Assignments extends JScrollPane {
	private enum State {
		NONE, ADD, EDIT, DELETE
	};
	private DataBase classDB = DataBase.getDB();
	private MainWindowView mainWindow;
	private JPanel cardStack = new JPanel(new CardLayout());
	private GridBagLayout gbl_panel = new GridBagLayout();
	private JPanel panel = new JPanel(gbl_panel);
	private JPanel detailPanel = new JPanel(new GridBagLayout());
	private JPanel formPanel = new JPanel();
	private int compCount = 0;
	private boolean showNavBar = true;
	private GridBagConstraints gc_1;
	private Label label = new Label("New label");
	private JPanel toggleShow;
	private Label toggleLabel = new Label("Assignments");
	private String[] choices = { "Menu", "Add", "Edit", "Delete" };
	private final JComboBox comboBox = new JComboBox(choices);
	private State currentState = State.NONE;
	private JFormattedTextField nameForm = new JFormattedTextField();
	private final JLabel nameLabel = new JLabel("Assignment Name");
	private final JLabel pointsLabel = new JLabel("Points");
	private JFormattedTextField pointForm = new JFormattedTextField();
	private final JLabel dayLabel = new JLabel("Due Date");
	private final JLabel timeLabel = new JLabel("Due Time");
	private DatePickerSettings dateSettings;
	private DatePicker pickDate;
	private TimePickerSettings timeSettings;
	private TimePicker pickTime;
	private final JTextArea descriptionForm = new JTextArea();
	private final JButton uploadButton = new JButton("Upload file");
	private final JScrollPane scrollPane = new JScrollPane();
	private final JToolBar fileList = new JToolBar();
	private final JLabel descriptionLabel = new JLabel("Description");
	private final JButton submitButton = new JButton("Submit");
	private final JButton cancelButton = new JButton("Cancel");
	private final JButton backButton = new JButton("Back");
	public  ActionListener buttonListener;
	public  JToolBar navBar;
	private JButton assignmentButton;
	private Assignment tempASM;
	private JScrollPane navPane;
	
	/**
	 * Create the JScrollPane.
	 */
	public Assignments() {
		dateSettings = new DatePickerSettings();
		dateSettings.setFormatForDatesCommonEra("MM/dd/yyyy");
		pickDate = new DatePicker(dateSettings);
		pickDate.setDateToToday();
		pickDate.getComponentDateTextField().setHorizontalAlignment(SwingConstants.CENTER);
		timeSettings = new TimePickerSettings();
		timeSettings.setDisplaySpinnerButtons(true);
		timeSettings.setInitialTimeToNow();
		pickTime = new TimePicker(timeSettings);
		pickTime.getComponentTimeTextField().setHorizontalAlignment(SwingConstants.CENTER);

		gbl_panel.rowHeights = new int[] {60, 430};

		gbl_panel.columnWeights = new double[] { 0.0, 0.0, 1.0 };
		gbl_panel.columnWidths = new int[] { 120, 240, 120 };
		gbl_panel.rowWeights = new double[] { 0.0, 1.0 };

		// Config for JScrollPanel
		this.setLayout(new ScrollPaneLayout());
		this.setPreferredSize(new Dimension(510, 510));
		this.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		this.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		panel.setSize(new Dimension(500, 500));
		formPanel.setSize(new Dimension(500, 500));

		navBar = new JToolBar(1);
		navBar.addContainerListener(new ContainerAdapter() {
			@Override
			public void componentAdded(ContainerEvent e) {
				compCount = navBar.getComponents().length;
				navBar.setPreferredSize(new Dimension(260,compCount*60));
			}
			@Override
			public void componentRemoved(ContainerEvent e) {
				compCount = navBar.getComponents().length;
				navBar.setPreferredSize(new Dimension(260,compCount*60));
			}
		});
		
		navBar.setPreferredSize(new Dimension(260, 13));
		navBar.setSize(new Dimension(260, 1));
		navBar.setBackground(Color.BLACK);
		navBar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		navBar.setAlignmentX(0.5f);
		navBar.setFloatable(false);

		// Set Dimensions and Constraints before adding to panel
		GridBagConstraints gc = new GridBagConstraints();
		gc.ipadx = 300;
		gc.gridwidth = 0;
		gc.gridheight = 0;
		gc.weightx = 0.5;
		gc.weighty = 1;
		gc.gridx = 0;
		gc.gridy = 0;

		MouseAdapter onClick = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (showNavBar) {
					showNavBar = !showNavBar;
					navBar.setVisible(showNavBar);
				} else if (!showNavBar) {
					showNavBar = !showNavBar;
					navBar.setVisible(showNavBar);
				}
			}
		};

		GridBagLayout gbl_toggleShow = new GridBagLayout();
		gbl_toggleShow.columnWidths = new int[] { 80, 80, 80 };
		gbl_toggleShow.columnWeights = new double[] { 0.0, 0.0, 0.0 };
		toggleShow = new JPanel(gbl_toggleShow);
		toggleShow.setBorder(
				new BevelBorder(BevelBorder.RAISED, new Color(64, 64, 64), null, new Color(64, 64, 64), null));
		toggleShow.addMouseListener(onClick);
		toggleShow.setPreferredSize(new Dimension(260, 60));
		toggleShow.setMaximumSize(new Dimension(260, 60));
		toggleShow.setMinimumSize(new Dimension(260, 60));

		gc_1 = new GridBagConstraints();
		gc_1.insets = new Insets(0, 0, 5, 5);
		gc_1.gridx = 1;
		gc_1.gridy = 0;
		gc_1.weightx = 0;
		gc_1.weighty = 0;
		panel.add(toggleShow, gc_1);

		GridBagConstraints assignmentMenu = new GridBagConstraints();
		assignmentMenu.fill = GridBagConstraints.HORIZONTAL;
		assignmentMenu.gridx = 2;
		assignmentMenu.gridy = 0;

		toggleLabel.setAlignment(Label.CENTER);

		GridBagConstraints gbc_toggleLabel = new GridBagConstraints();
		gbc_toggleLabel.insets = new Insets(0, 0, 0, 5);
		gbc_toggleLabel.fill = GridBagConstraints.BOTH;
		gbc_toggleLabel.gridx = 0;
		gbc_toggleLabel.gridy = 0;
		toggleShow.add(toggleLabel, gbc_toggleLabel);

		toggleShow.add(comboBox, assignmentMenu);

		GridBagConstraints gbc_navBar = new GridBagConstraints();
		gbc_navBar.fill = GridBagConstraints.VERTICAL;
		gbc_navBar.weightx = 1.0;
		gbc_navBar.insets = new Insets(0, 0, 0, 5);
		gbc_navBar.gridx = 1;
		gbc_navBar.gridy = 1;
		navPane = new JScrollPane(navBar);
		navPane.setMinimumSize(navBar.getSize());
		navPane.setPreferredSize(navBar.getSize());
		navPane.setMaximumSize(new Dimension(260, 5555));
		navPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		navPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		panel.add(navPane, gbc_navBar);
		panel.setBackground(Color.LIGHT_GRAY);

		cardStack.setBackground(Color.blue);
		cardStack.add(panel);
		cardStack.add(detailPanel);
		cardStack.add(formPanel);

		GridBagLayout gbl_formPanel = new GridBagLayout();
		gbl_formPanel.columnWidths = new int[] { 240, 120, 120 };
		gbl_formPanel.rowHeights = new int[] {25, 25, 25, 25, 25, 325, 25};
		gbl_formPanel.columnWeights = new double[] { 0.0, 1.0, 1.0 };
		gbl_formPanel.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0 };
		formPanel.setLayout(gbl_formPanel);

		GridBagConstraints gbc_nameLabel = new GridBagConstraints();
		gbc_nameLabel.fill = GridBagConstraints.VERTICAL;
		gbc_nameLabel.insets = new Insets(5, 5, 5, 5);
		gbc_nameLabel.gridx = 0;
		gbc_nameLabel.gridy = 0;
		nameLabel.setVerticalAlignment(SwingConstants.TOP);
		formPanel.add(nameLabel, gbc_nameLabel);

		GridBagConstraints gbc_pointsLabel = new GridBagConstraints();
		gbc_pointsLabel.gridwidth = 2;
		gbc_pointsLabel.fill = GridBagConstraints.BOTH;
		gbc_pointsLabel.insets = new Insets(5, 0, 5, 0);
		gbc_pointsLabel.gridx = 1;
		gbc_pointsLabel.gridy = 0;
		pointsLabel.setHorizontalAlignment(SwingConstants.CENTER);
		formPanel.add(pointsLabel, gbc_pointsLabel);

		GridBagConstraints gbc_nameForm = new GridBagConstraints();
		gbc_nameForm.insets = new Insets(0, 5, 5, 5);
		gbc_nameForm.fill = GridBagConstraints.BOTH;
		gbc_nameForm.gridx = 0;
		gbc_nameForm.gridy = 1;
		nameForm.setHorizontalAlignment(SwingConstants.CENTER);
		formPanel.add(nameForm, gbc_nameForm);

		GridBagConstraints gbc_pointForm = new GridBagConstraints();
		gbc_pointForm.gridwidth = 2;
		gbc_pointForm.insets = new Insets(0, 0, 5, 0);
		gbc_pointForm.fill = GridBagConstraints.BOTH;
		gbc_pointForm.gridx = 1;
		gbc_pointForm.gridy = 1;
		pointForm.setHorizontalAlignment(SwingConstants.CENTER);
		formPanel.add(pointForm, gbc_pointForm);

		GridBagConstraints gbc_dayLabel = new GridBagConstraints();
		gbc_dayLabel.fill = GridBagConstraints.VERTICAL;
		gbc_dayLabel.insets = new Insets(0, 5, 5, 5);
		gbc_dayLabel.gridx = 0;
		gbc_dayLabel.gridy = 2;
		formPanel.add(dayLabel, gbc_dayLabel);

		GridBagConstraints gbc_timeLabel = new GridBagConstraints();
		gbc_timeLabel.gridwidth = 2;
		gbc_timeLabel.fill = GridBagConstraints.BOTH;
		gbc_timeLabel.insets = new Insets(0, 0, 5, 0);
		gbc_timeLabel.gridx = 1;
		gbc_timeLabel.gridy = 2;
		timeLabel.setHorizontalAlignment(SwingConstants.CENTER);
		formPanel.add(timeLabel, gbc_timeLabel);

		GridBagConstraints datePickerGC = new GridBagConstraints();
		datePickerGC.insets = new Insets(0, 5, 5, 5);
		datePickerGC.fill = GridBagConstraints.BOTH;
		datePickerGC.gridx = 0;
		datePickerGC.gridy = 3;
		pickDate.setToolTipText("Click to edit");
		formPanel.add(pickDate, datePickerGC);

		GridBagConstraints timePickerGC = new GridBagConstraints();
		timePickerGC.gridwidth = 2;
		timePickerGC.insets = new Insets(0, 0, 5, 0);
		timePickerGC.fill = GridBagConstraints.BOTH;
		timePickerGC.gridx = 1;
		timePickerGC.gridy = 3;
		pickTime.setToolTipText("Click to edit");
		formPanel.add(pickTime, timePickerGC);

		GridBagConstraints descriptionLabelGC = new GridBagConstraints();
		descriptionLabelGC.fill = GridBagConstraints.BOTH;
		descriptionLabelGC.insets = new Insets(0, 5, 5, 5);
		descriptionLabelGC.gridx = 0;
		descriptionLabelGC.gridy = 4;
		descriptionLabel.setHorizontalAlignment(SwingConstants.CENTER);
		formPanel.add(descriptionLabel, descriptionLabelGC);

		GridBagConstraints gbc_descriptionForm = new GridBagConstraints();
		gbc_descriptionForm.fill = GridBagConstraints.BOTH;
		gbc_descriptionForm.gridheight = 2;
		gbc_descriptionForm.insets = new Insets(0, 5, 5, 5);
		gbc_descriptionForm.gridx = 0;
		gbc_descriptionForm.gridy = 5;
		descriptionForm.setLineWrap(true);
		descriptionForm.setMinimumSize(new Dimension(240, 365));
		descriptionForm.setMaximumSize(new Dimension(240, 365));
		descriptionForm.setPreferredSize(new Dimension(240, 365));
		descriptionForm.setSize(new Dimension(240, 365));
		descriptionForm.setTabSize(4);
		descriptionForm.setWrapStyleWord(true);
		descriptionForm.setBorder(new BevelBorder(BevelBorder.LOWERED, Color.BLACK, null, Color.BLACK, null));
		formPanel.add(descriptionForm, gbc_descriptionForm);

		GridBagConstraints gbc_uploadButton = new GridBagConstraints();
		gbc_uploadButton.gridwidth = 2;
		gbc_uploadButton.fill = GridBagConstraints.BOTH;
		gbc_uploadButton.insets = new Insets(0, 0, 5, 0);
		gbc_uploadButton.gridx = 1;
		gbc_uploadButton.gridy = 4;
		formPanel.add(uploadButton, gbc_uploadButton);

		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.gridwidth = 2;
		gbc_scrollPane.insets = new Insets(0, 0, 5, 0);
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 1;
		gbc_scrollPane.gridy = 5;
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		formPanel.add(scrollPane, gbc_scrollPane);
		fileList.setFloatable(false);

		scrollPane.setColumnHeaderView(fileList);

		GridBagConstraints gc_backButton = new GridBagConstraints();
		gc_backButton.gridwidth = 2;
		gc_backButton.fill = GridBagConstraints.BOTH;
		gc_backButton.insets = new Insets(0, 0, 5, 5);
		gc_backButton.gridx = 1;
		gc_backButton.gridy = 6;
		formPanel.add(backButton, gc_backButton);

		GridBagConstraints gc_submitButton = new GridBagConstraints();
		gc_submitButton.fill = GridBagConstraints.BOTH;
		gc_submitButton.insets = new Insets(0, 0, 5, 5);
		gc_submitButton.gridx = 1;
		gc_submitButton.gridy = 6;
		formPanel.add(submitButton, gc_submitButton);

		GridBagConstraints gbc_CancelButton = new GridBagConstraints();
		gbc_CancelButton.insets = new Insets(0, 0, 5, 5);
		gbc_CancelButton.fill = GridBagConstraints.BOTH;
		gbc_CancelButton.gridx = 2;
		gbc_CancelButton.gridy = 6;
		formPanel.setMinimumSize(new Dimension(500, 480));
		formPanel.setMaximumSize(new Dimension(500, 480));
		formPanel.setPreferredSize(new Dimension(480, 480));
		formPanel.setMaximumSize(new Dimension(480, 480));


		GridBagConstraints gbc_label = new GridBagConstraints();
		gbc_label.weightx = 1.0;
		gbc_label.fill = GridBagConstraints.VERTICAL;
		gbc_label.gridx = 0;
		gbc_label.gridy = 0;
		detailPanel.add(label, gbc_label);

		setViewportView(cardStack);

		buttonListener = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				switch (currentState) {
				case NONE:
					panel.setVisible(false);
					formPanel.setVisible(true);
					detailPanel.setVisible(false);
					assignmentButton = (JButton) e.getSource();
					tempASM = DataBase.getAssignmentByID(Integer.parseInt(assignmentButton.getName()));
					nameForm.setText(tempASM.getName());
					pointForm.setText(String.valueOf(tempASM.getPoints()));
					descriptionForm.setText(tempASM.getDescription());
					pickDate.setDate(tempASM.getDueDate());
					pickTime.setTime(tempASM.getDueTime());
					break;
				case ADD:
					break;
				case EDIT:
					exitReadonly();
					panel.setVisible(false);
					formPanel.setVisible(true);
					detailPanel.setVisible(false);
					assignmentButton = (JButton) e.getSource();
					tempASM = DataBase.getAssignmentByID(Integer.parseInt(assignmentButton.getName()));
					nameForm.setText(tempASM.getName());
					pointForm.setText(String.valueOf(tempASM.getPoints()));
					descriptionForm.setText(tempASM.getDescription());
					pickDate.setDate(tempASM.getDueDate());
					pickTime.setTime(tempASM.getDueTime());
					break;
				case DELETE:
					assignmentButton = (JButton) e.getSource();
					navBar.remove(assignmentButton);
					DataBase.deleteAssignment(Integer.valueOf(assignmentButton.getName()));
					revalidate();
					repaint();
					break;
				}
			}
		};

		JButton tempButton;
		Map<Integer, Assignment> tempAssignmentDB = DataBase.getAssignments();
		for (Map.Entry<Integer, Assignment> tempDB: tempAssignmentDB.entrySet()) {
			tempButton = new JButton(tempDB.getValue().getName());
			tempButton.addActionListener(buttonListener);
			tempButton.setName(String.valueOf(tempDB.getValue().getID()));
			tempButton.setBorder(
					new BevelBorder(BevelBorder.RAISED, new Color(64, 64, 64), null, new Color(64, 64, 64), null));
			tempButton.setPreferredSize(new Dimension(260, 60));
			tempButton.setMaximumSize(new Dimension(260, 60));
			tempButton.setMinimumSize(new Dimension(260, 60));
			navBar.add(tempButton);
		}
		
		comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				@SuppressWarnings("unchecked")
				JComboBox cb = (JComboBox) e.getSource();

				switch ((String) cb.getSelectedItem()) {
				case "Menu":
					currentState = State.NONE;
					break;
				case "Add":
					currentState = State.ADD;
					exitReadonly();
					panel.setVisible(false);
					formPanel.setVisible(true);
					detailPanel.setVisible(false);
					break;
				case "Edit":
					currentState = State.EDIT;
					break;
				case "Delete":
					currentState = State.DELETE;
					break;
				}
			}
		});
		
		submitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if(currentState == State.ADD) {
					Pair<String,Integer> pair = classDB.addAssignement(nameForm.getText(), Integer.parseInt(pointForm.getText()),
							descriptionForm.getText(), pickDate.getDate(), pickTime.getTime(), new ArrayList<Integer>());
					
					addButton(pair);
					reset();
				}
				else if(currentState == State.EDIT) {
					if(tempASM !=null) {
						tempASM.setName(nameForm.getText());
						tempASM.setPoints(Integer.parseInt(pointForm.getText()));
						tempASM.setDescription(descriptionForm.getText());
						tempASM.setDueDate(pickDate.getDate());
						tempASM.setDueTime(pickTime.getTime());
						assignmentButton.setText(nameForm.getText());
					}
					reset();
				}
			}
		});

		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				reset();
			}
		});
		
		backButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				reset();
			}
		});

		formPanel.add(cancelButton, gbc_CancelButton);
		revalidate();
		repaint();
	}

	private void addButton(Pair<String,Integer> pair) {
		
		// Nice to have is to create a class that i can just pass into the class instantiation and 
		// this function , still have testing and comments to do on top of maybe having to generate documentation
	buttonListener = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				switch (currentState) {
				case NONE:
					panel.setVisible(false);
					formPanel.setVisible(true);
					detailPanel.setVisible(false);
					assignmentButton = (JButton) e.getSource();
					tempASM = DataBase.getAssignmentByID(Integer.parseInt(assignmentButton.getName()));
					nameForm.setText(tempASM.getName());
					pointForm.setText(String.valueOf(tempASM.getPoints()));
					descriptionForm.setText(tempASM.getDescription());
					pickDate.setDate(tempASM.getDueDate());
					pickTime.setTime(tempASM.getDueTime());
					break;
				case ADD:
					break;
				case EDIT:
					exitReadonly();
					panel.setVisible(false);
					formPanel.setVisible(true);
					detailPanel.setVisible(false);
					assignmentButton = (JButton) e.getSource();
					tempASM = DataBase.getAssignmentByID(Integer.parseInt(assignmentButton.getName()));
					nameForm.setText(tempASM.getName());
					pointForm.setText(String.valueOf(tempASM.getPoints()));
					descriptionForm.setText(tempASM.getDescription());
					pickDate.setDate(tempASM.getDueDate());
					pickTime.setTime(tempASM.getDueTime());
					break;
				case DELETE:
					assignmentButton = (JButton) e.getSource();
					navBar.remove(assignmentButton);
					DataBase.deleteAssignment(Integer.valueOf(assignmentButton.getName()));
					revalidate();
					repaint();
					break;
				}
			}
		};
		
		JButton tempButton = new JButton(pair.first);
		tempButton.setName(String.valueOf(pair.second));
		tempButton.setBorder(
				new BevelBorder(BevelBorder.RAISED, new Color(64, 64, 64), null, new Color(64, 64, 64), null));
		tempButton.addActionListener(buttonListener);
		tempButton.setPreferredSize(new Dimension(260, 60));
		tempButton.setMaximumSize(new Dimension(260, 60));
		tempButton.setMinimumSize(new Dimension(260, 60));
		tempButton.setSize(new Dimension(260, 60));
		navBar.add(tempButton);
	}
	
	public void viewReadonly() {
		nameForm.setEditable(false);
		pointForm.setEditable(false);
		descriptionForm.setEditable(false);
		pickDate.getComponentDateTextField().setEditable(false);
		pickDate.getComponentToggleCalendarButton().setVisible(false);
		pickTime.getComponentTimeTextField().setEditable(false);
		pickTime.getComponentToggleTimeMenuButton().setVisible(false);
		pickTime.getComponentDecreaseSpinnerButton().setVisible(false);
		pickTime.getComponentIncreaseSpinnerButton().setVisible(false);
		uploadButton.setText("Download file");
		submitButton.setVisible(false);
		cancelButton.setVisible(false);
		backButton.setVisible(true);
	}
	
	public void exitReadonly() {
		nameForm.setEditable(true);
		pointForm.setEditable(true);
		descriptionForm.setEditable(true);
		pickDate.getComponentDateTextField().setEditable(true);
		pickDate.getComponentToggleCalendarButton().setVisible(true);
		pickTime.getComponentTimeTextField().setEditable(true);
		pickTime.getComponentToggleTimeMenuButton().setVisible(true);
		pickTime.getComponentDecreaseSpinnerButton().setVisible(true);
		pickTime.getComponentIncreaseSpinnerButton().setVisible(true);
		uploadButton.setText("Upload file");
		submitButton.setVisible(true);
		cancelButton.setVisible(true);
		backButton.setVisible(false);
	}
	
	public void reset() {
		viewReadonly();
		panel.setVisible(true);
		detailPanel.setVisible(false);
		formPanel.setVisible(false);
		comboBox.setSelectedIndex(0);
		currentState = State.NONE;
		nameForm.setText("");
		pointForm.setText("");
		descriptionForm.setText("");
		pickDate.setDateToToday();
		pickTime.setTimeToNow();
		assignmentButton = null;
		tempASM = null;
		revalidate();
		repaint();
	}

}

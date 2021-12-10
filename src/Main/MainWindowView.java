package Main;
// This is the View
// Its only job is to display what the user sees
// It performs no calculations, but instead passes
// information entered by the user to whomever needs
// it. 

import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.WindowConstants;
import javax.swing.*;
import java.awt.*;
import DataBase.DataBase;
import Views.*;

public class MainWindowView extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel gridBagPanel = new JPanel(new GridBagLayout());
	private JToolBar NavBar = new JToolBar(1); 
	private JPanel cardStack = new JPanel(new CardLayout());
	private JButton calendarButton = new JButton("Calendar");
	public DashboardView dashboardView = new DashboardView();
	private JButton dashboardButton = new JButton("Dashboard");
	public Modules modulesView = new Modules();
	private JButton modulesButton = new JButton("Modules");
	public FileUpload fileUpload = new FileUpload(this);
	private JButton zoomButton = new JButton("Zoom");
	public Assignments assignmentsView = new Assignments();
	private JButton assignmentsButton = new JButton("Assignments");
	public FileView filesView = new FileView();
	private JButton filesButton = new JButton("Files");
	public GradeView gradeView = new GradeView();
	private JButton gradesButton = new JButton("Grades");

	
	MainWindowView(){
		this.setTitle("Classy");
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		this.setMinimumSize(new Dimension(650,550));
		this.setMaximumSize(new Dimension(650,550));
		this.setPreferredSize(new Dimension(650,550));
		this.setResizable(false);
		
		this.addWindowListener(new WindowAdapter() {
            // Invoked when a window is in the process of being closed.
	        // The close operation can be overridden at this point.
	        public void windowClosing(WindowEvent e) {
	            System.out.println("Window Closing Event");
	            DataBase.saveData();
	        }
		});
		
		// Set Dimensions for background gridbagPanel(GBP)
		gridBagPanel.setMaximumSize(new Dimension(500,500));
		gridBagPanel.setLayout(new GridBagLayout());
		
		// Set Dimensions and Constraints before adding to GBP
		NavBar.setMaximumSize(new Dimension(100,500));
		NavBar.setBackground(Color.black);
		GridBagConstraints gc = new GridBagConstraints();
		gc.fill= GridBagConstraints.BOTH;
		gc.weighty = 1;
		gc.gridx=0;
		gc.gridy=0;
		gc.anchor = GridBagConstraints.NORTHWEST;

		
		// Set Button Size and add to navigation bar
		dashboardButton.setMaximumSize(new Dimension(100,40));
		NavBar.add(dashboardButton);
		assignmentsButton.setMaximumSize(new Dimension(100, 40));
		NavBar.add(assignmentsButton);
		modulesButton.setMaximumSize(new Dimension(100, 40));
		NavBar.add(modulesButton);
		gradesButton.setMaximumSize(new Dimension(100, 40));
		NavBar.add(gradesButton);
		filesButton.setMaximumSize(new Dimension(100, 40));
		NavBar.add(filesButton);
		zoomButton.setMaximumSize(new Dimension(100, 40));
		NavBar.add(zoomButton);
		calendarButton.setMaximumSize(new Dimension(100, 40));
		NavBar.add(calendarButton);

		
		/*
		 * Remove ability to move NavBar around, set orientation to vertical and add the
		 * NavBar to the background Panel
		 */
		NavBar.setFloatable(false);
		NavBar.setOrientation(1);
		gridBagPanel.add(NavBar, gc);
		
		/*
		 * cardStack is where all the window panels will be added set card stack
		 * dimensions and insert to the right of the NavBar
		 */
		cardStack.setMaximumSize(new Dimension(500,500));
		gc.fill = GridBagConstraints.BOTH;
		gc.weightx = 1;
		gc.weighty = 1;
		gc.gridwidth=1;
		gc.gridx = 1;
		gc.gridy = 0;
		gridBagPanel.add(cardStack, gc);

        // Add the background panel to the application window
		this.add(gridBagPanel);
			
		cardStack.add(dashboardView);
		cardStack.add(modulesView);
		cardStack.add(fileUpload);
		cardStack.add(assignmentsView);
		cardStack.add(gradeView);
		cardStack.add(filesView);
	}
	
	void dashboardListener(ActionListener listenForNavBarButton){
		dashboardButton.addActionListener(listenForNavBarButton);
	}
	
	void gradesListener(ActionListener listenForNavBarButton){
		gradesButton.addActionListener(listenForNavBarButton);
	}
	
	void assignmentOnClick(ActionListener listenForNavBarButton) {
		assignmentsButton.addActionListener(listenForNavBarButton);
		pack();
	}
	  
	void filesOnClick (ActionListener listenForNavBarButton) {
		filesButton.addActionListener(listenForNavBarButton);
	}
}
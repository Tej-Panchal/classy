package Main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// The Controller coordinates interactions
// between the View and Model

public class MainWindowController {

	private MainWindowView theView;

	public MainWindowController(MainWindowView theView) {
		this.theView = theView;

		// Pass in listener objects to buttons
		this.theView.dashboardListener(new dashboardListener());
		this.theView.gradesListener(new gradesListener());
		this.theView.assignmentOnClick(new assignmentsListener());
		this.theView.filesOnClick(new filesListener());
	}

	class dashboardListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			theView.dashboardView.setVisible(true);
			theView.gradeView.setVisible(false);
			theView.assignmentsView.setVisible(false);
			theView.filesView.setVisible(false);
			reset();
		}
	}

	class gradesListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			theView.gradeView.onShowView();
			theView.gradeView.setVisible(true);
			theView.dashboardView.setVisible(false);
			theView.assignmentsView.setVisible(false);
			theView.filesView.setVisible(false);
		}
	}

	class assignmentsListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			theView.assignmentsView.setVisible(true);
			theView.gradeView.setVisible(false);
			theView.dashboardView.setVisible(false);
			theView.filesView.setVisible(false);
			reset();
		}
	}

	class filesListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			theView.filesView.setVisible(true);
			theView.assignmentsView.setVisible(false);
			theView.gradeView.setVisible(false);
			theView.dashboardView.setVisible(false);
			reset();
		}
	}

	private void reset() {
		theView.assignmentsView.reset();
		theView.gradeView.reset();
	}
}
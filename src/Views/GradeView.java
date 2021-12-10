package Views;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import DataBase.DataBase;
import Model.Assignment;
import Model.Student;
import java.awt.*;
import java.awt.Toolkit;
import java.awt.event.InputMethodEvent;
import java.awt.event.InputMethodListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.Image;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagLayout;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.awt.GridBagConstraints;

public class GradeView extends JPanel {
	private Image background = Toolkit.getDefaultToolkit().createImage("assets/GradientBG.png");
	private JTable gradeTable;
	private JTable assignmentNameTable;
	JScrollPane scrollPane;
	private Map<Integer, Map<Integer, Integer>> grades = new HashMap<Integer, Map<Integer, Integer>>();
	private int studentIndex;
	private int assignmentIndex;
	private int score;

	/**
	 * Create the panel.
	 */
	public GradeView() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 550 };
		gridBagLayout.rowHeights = new int[] { 15 };
		gridBagLayout.columnWeights = new double[] { 1.0, 0.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 1.0 };
		setLayout(gridBagLayout);
	}

	@Override
	protected void paintComponent(Graphics g) {
		// Create rectangle for source image.
		super.paintComponent(g);
		g.drawImage(this.background, 0, 0, this);
	}

	public void onShowView() {
		gradeTable = new JTable();
		assignmentNameTable = new JTable();

		grades = DataBase.getGrades();
		Map<Integer, Assignment> assignments = DataBase.getAssignments();
		Map<Integer, Student> students = DataBase.getStudents();
		ArrayList<String> studentNames = new ArrayList<String>();

		for (Map.Entry<Integer, Student> student : students.entrySet()) {
			studentNames.add(student.getValue().getName());
		}

		Object[][] gradeData = new Object[grades.size()][assignments.size()+1];

		int k = 0;

		int[] assignmentRowID = new int[assignments.size()];
		int[] studentColumnID = new int[students.size()];

		for (Map.Entry<Integer, Map<Integer, Integer>> data : grades.entrySet()) {
			Object[] arr1 = new Object[1];
			assignmentRowID[k] = data.getKey();
			if (assignments.get(data.getKey()) != null) {
				arr1[0] = assignments.get(data.getKey()).getName();
			}

			Object[] arr2 = concatWithCollection(grades.get(data.getKey()).values().toArray(), new Object[]{assignments.get(data.getKey()).getPoints()});
			for (int i = 0; i < arr2.length-1; i++) {
				// on the first traversal record student ids
				if (k == 0) {
					Object[] arr3 = grades.get(data.getKey()).keySet().toArray();
					studentColumnID[i] = (int) arr3[i];
				}
				// Format score into appropriate string value
				arr2[i] = gradeParse((int) arr2[i]);
			}

			gradeData[k] = concatWithCollection(arr1, arr2);
			++k;
		}

		Object temp[] = concatWithCollection(new Object[] { "Assignment Name" }, studentNames.toArray());
		Object studentNameArray[] = concatWithCollection(temp, new Object[] { "Points" });
		gradeTable.setModel(new DefaultTableModel(gradeData, studentNameArray));

		GridBagConstraints gbc_gradeTable = new GridBagConstraints();
		gbc_gradeTable.fill = GridBagConstraints.BOTH;
		gbc_gradeTable.gridx = 0;
		gbc_gradeTable.gridy = 0;
		gbc_gradeTable.weighty = 1;
		gbc_gradeTable.weightx = 1;
		scrollPane = new JScrollPane(gradeTable);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		gradeTable.setFillsViewportHeight(true);
		add(scrollPane, gbc_gradeTable);

		gradeTable.getTableHeader().setReorderingAllowed(false);
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();

		centerRenderer.setHorizontalAlignment(JLabel.CENTER);
		centerRenderer.setMinimumSize(new Dimension(15, 20));
		gradeTable.setDefaultRenderer(Object.class, centerRenderer);
		gradeTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		gradeTable.getModel().addTableModelListener(new TableModelListener() {
			public void tableChanged(TableModelEvent evt) {
				assignmentIndex = gradeTable.getSelectedRow();
				studentIndex = gradeTable.getSelectedColumn();
				if (!gradeTable.getModel().getValueAt(assignmentIndex, studentIndex).equals("-")) {
					score = Integer.parseInt((String) gradeTable.getModel().getValueAt(assignmentIndex, studentIndex));

					if (score > 0
							&& score <= DataBase.getAssignmentByID(assignmentRowID[assignmentIndex]).getPoints()) {
						System.out.println("Max points: "
								+ DataBase.getAssignmentByID(assignmentRowID[assignmentIndex]).getPoints()
								+ " assigning: " + score + " assignmentID:" + assignmentRowID[assignmentIndex] + " StudentID:"
								+ studentColumnID[studentIndex - 1]);
						grades.get(assignmentRowID[assignmentIndex]).put(studentColumnID[studentIndex - 1], score);
					}
				}
			}
		});

		revalidate();
		repaint();
	}

	public void reset() {
		if (scrollPane != null) {
			this.remove(assignmentNameTable);
			scrollPane.remove(gradeTable);
			this.remove(scrollPane);
		}
	}

	public String gradeParse(int num) {
		if (Integer.compare(num, -1) == 0) {
			return "-";
		} else {
			return String.valueOf(num);
		}
	}

	static <T> T[] concatWithCollection(T[] array1, T[] array2) {
		List<T> resultList = new ArrayList<>(array1.length + array2.length);
		Collections.addAll(resultList, array1);
		Collections.addAll(resultList, array2);

		@SuppressWarnings("unchecked")
		// the type cast is safe as the array1 has the type T[]
		T[] resultArray = (T[]) Array.newInstance(array1.getClass().getComponentType(), 0);
		return resultList.toArray(resultArray);
	}
}

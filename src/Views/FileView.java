package Views;

import java.io.File;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import Model.FileTreeModel;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

public class FileView extends JScrollPane {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private File fileDB = new File("FileDB");
	private TreeModel fileModel = new FileTreeModel(fileDB);
	private JTree jtree = new JTree(fileModel);
	JPanel panel = new JPanel(new GridBagLayout());
	
	/**
	 * Create the panel.
	 */
	public FileView() {
		jtree.setRootVisible(true);
		
		GridBagConstraints gc = new GridBagConstraints();
		gc.insets = new Insets(0, 0, 0, 0);
		gc.anchor = GridBagConstraints.NORTHWEST;
		gc.fill = GridBagConstraints.BOTH;
		gc.gridx = 0;
		gc.gridy = 0;
		gc.weightx = 1.0;
		gc.weighty = 1.0;
		
		panel.add(jtree, gc);
		
		setViewportView(panel);
	}
}

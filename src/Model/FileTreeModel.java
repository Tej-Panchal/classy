package Model;

import java.io.File;
import java.util.Arrays;
 
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
 
public class FileTreeModel implements TreeModel {
 
	/**
	 * Creating an object of this class and setting its root to the provided
	 * File object.
	 * 
	 * The root is the highest directory available in an object of this class.
	 * 
	 * @param file
	 *            - an object of type File, giving the root directory for an
	 *            object of type FileTreeModel.
	 */
	public FileTreeModel(File file) {
		this.root = file;
	}
 
	@Override
	public Object getRoot() {
		return this.root;
	}
 
	@Override
	public Object getChild(Object parent, int index) {
		File f = (File) parent;
		return f.listFiles()[index];
	}
 
	@Override
	public int getChildCount(Object parent) {
		File f = (File) parent;
 
		try {
			if (!f.isDirectory() && f.list() != null) {
				return 0;
			} else {
				return f.list().length;
			}
		} catch (NullPointerException ex) {
			return 0;
		}
	}
 
	@Override
	public boolean isLeaf(Object node) {
		File f = (File) node;
		return !f.isDirectory();
	}
 
	@Override
	public void valueForPathChanged(TreePath path, Object newValue) {
		// TODO Auto-generated method stub
 
	}
 
	@Override
	public int getIndexOfChild(Object parent, Object child) {
		File par = (File) parent;
		File ch = (File) child;
		return Arrays.asList(par.listFiles()).indexOf(ch);
	}
 
	@Override
	public void addTreeModelListener(TreeModelListener l) {
		// TODO Auto-generated method stub
	}
 
	@Override
	public void removeTreeModelListener(TreeModelListener l) {
		// TODO Auto-generated method stub
	}
 
	private File root;
}
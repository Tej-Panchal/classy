package Views;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;
import Main.MainWindowView;
import javax.swing.JButton;
import java.awt.GridLayout;
import javax.swing.JFileChooser;

public class FileUpload extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JButton uploadButton = new JButton("uploadFile");
	private JButton downloadButton = new JButton("downloadFile");
	private JFileChooser fcLoad;
	private JFileChooser fcSave;
	private MainWindowView mainWindow;

	/**
	 * Create the panel.
	 */
	public FileUpload(MainWindowView mw) {
		mainWindow = mw;
		setLayout(new GridLayout(2, 0, 0, 0));
		add(uploadButton);
		add(downloadButton);

		uploadButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = uploadFile();
				int response = chooser.showOpenDialog(mainWindow);
				if (response == JFileChooser.APPROVE_OPTION) {
					try {
						File file = chooser.getSelectedFile();
						String name = file.getName();

						FileInputStream in = null;
						FileOutputStream out = null;
						try {
							in = new FileInputStream(file);
							out = new FileOutputStream("fileDB\\" + name);

							byte[] buffer = new byte[1024];
							int bytesIn = -1;
							while ((bytesIn = in.read(buffer)) != -1) {
								out.write(buffer, 0, bytesIn);
							}
							out.flush();
						} finally {
							try {
								in.close();
								out.close();
							} catch (Exception exp) {
							}
						}
					} catch (Exception exc) {
						exc.printStackTrace();
					}
				}
			}
		});

		downloadButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = downloadFile();
				int response = chooser.showOpenDialog(mainWindow);
				if (response == JFileChooser.APPROVE_OPTION) {
					System.out.println("getCurrentDirectory(): " + fcSave.getCurrentDirectory());
					System.out.println("getSelectedFile() : " + fcSave.getSelectedFile());
					try {
						FileInputStream in = null;
						FileOutputStream out = null;
						try {
							File file = new File("fileDB\\Data.txt");
							in = new FileInputStream(file);
							out = new FileOutputStream(
									fcSave.getSelectedFile().getAbsolutePath() + "\\" + file.getName());

							byte[] buffer = new byte[1024];
							int bytesIn = -1;
							while ((bytesIn = in.read(buffer)) != -1) {
								out.write(buffer, 0, bytesIn);
							}
							out.flush();
						} finally {
							try {
								in.close();
								out.close();
							} catch (Exception exp) {
							}
						}
					} catch (Exception exc) {
						exc.printStackTrace();
					}
				}
			}
		});
	}

	protected JFileChooser uploadFile() {
		if (fcLoad == null) {
			fcLoad = new JFileChooser();
			fcLoad.setDialogTitle("Open");
			fcLoad.setApproveButtonText("Open");
			FileNameExtensionFilter txt = new FileNameExtensionFilter("Txt Files(.txt)", "txt");
			FileNameExtensionFilter doc = new FileNameExtensionFilter("MS Word file(.doc)", "doc");
			FileNameExtensionFilter docx = new FileNameExtensionFilter("MS Word file(.docx)", "docx");
			FileNameExtensionFilter pdf = new FileNameExtensionFilter("Pdf file(.pdf)", "pdf");
			fcLoad.addChoosableFileFilter(txt);
			fcLoad.addChoosableFileFilter(docx);
			fcLoad.addChoosableFileFilter(doc);
			fcLoad.addChoosableFileFilter(pdf);
			fcLoad.setFileFilter(txt);
			fcLoad.setAcceptAllFileFilterUsed(false);
		}
		return fcLoad;
	}

	protected JFileChooser downloadFile() {
		if (fcSave == null) {
			fcSave = new JFileChooser();
			fcSave.setCurrentDirectory(new java.io.File("."));
			fcSave.setDialogTitle("Save As");
			fcSave.setApproveButtonText("Save");
			fcSave.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			fcSave.setAcceptAllFileFilterUsed(false);
		}
		return fcSave;
	}
}

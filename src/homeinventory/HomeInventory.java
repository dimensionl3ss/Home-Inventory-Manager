package homeinventory;

import java.awt.*;
import java.awt.event.*;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;

import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JCheckBox;
import datechooser.beans.DateChooserCombo;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

public class HomeInventory {

	private JFrame frame;
	private JTextField itemTextField;
	private JTextField priceTextField;
	private JTextField storeTextField;
	private JTextField noteTextField;
	private JTextField serialTextField;
	private JPanel panel;
	private JButton newButton;
	private JButton deleteButton;
	private JButton saveButton;
	private JButton previousButton; 
	private JButton nextButton;
	private JButton printButton;
	private JButton exitButton;
	private JButton photoButton;

	private JPanel searchPanel;
	private PhotoPanel photoPanel;
	@SuppressWarnings("rawtypes")
	private JComboBox locationComboBox;
	private JCheckBox markedCheckBox;
	static JTextArea photoTextArea;
	private DateChooserCombo dateDateChooser;

	static final int maximumEntries = 300;
	static int numberEntries;
	static InventoryItem[] myInventory;
	int currentEntry;
	static final int entriesPerPage = 2;
	static int lastPage;

	/**
	 * Launch the application.
	 */
	private void sizeButton(JButton b, Dimension d) {
		b.setPreferredSize(d);
		b.setMinimumSize(d);
		b.setMaximumSize(d);
	}

	private void newButtonActionPerformed(ActionEvent e) {
		checkSave();
		blankValues();
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					HomeInventory window = new HomeInventory();
					window.frame.setVisible(true);
					window.frame.setLocationRelativeTo(null);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public HomeInventory() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void initialize() {
		frame = new JFrame("Home Inventory Manager");
		frame.setBounds(100, 100, 851, 558);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		panel = new JPanel();
		panel.setBackground(new Color(0, 0, 255));
		panel.setBounds(0, 0, 127, 511);
		frame.getContentPane().add(panel);
		panel.setLayout(null);

		newButton = new JButton("New");
		newButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				newButtonActionPerformed(e);
			}
		});
		newButton.setFocusable(false);
		newButton.setFont(new Font("Tahoma", Font.PLAIN, 15));
		newButton.setIcon(new ImageIcon(HomeInventory.class.getResource("/images/new.png")));
		newButton.setBounds(10, 29, 115, 50);
		panel.add(newButton);

		deleteButton = new JButton("delete");
		deleteButton.setFocusable(false);
		deleteButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				deleteButtonActionPerformed(e);
			}
		});
		deleteButton.setIcon(new ImageIcon(HomeInventory.class.getResource("/images/delete.png")));
		deleteButton.setFont(new Font("Tahoma", Font.PLAIN, 15));
		deleteButton.setBounds(10, 80, 115, 50);
		panel.add(deleteButton);

		saveButton = new JButton("Save");
		saveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveButtonActionPerformed(e);
			}
		});
		saveButton.setFocusable(false);
		saveButton.setFont(new Font("Tahoma", Font.PLAIN, 15));
		saveButton.setIcon(new ImageIcon(HomeInventory.class.getResource("/images/save.png")));
		saveButton.setBounds(10, 130, 115, 50);
		panel.add(saveButton);

		previousButton = new JButton("Prev");
		previousButton.setFocusable(false);
		previousButton.setFont(new Font("Tahoma", Font.PLAIN, 15));
		previousButton.setIcon(new ImageIcon(HomeInventory.class.getResource("/images/prev.png")));
		previousButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				previousButtonActionPerformed(e);
			}
		});
		previousButton.setBounds(10, 229, 115, 50);
		panel.add(previousButton);

		nextButton = new JButton("Next");
		nextButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				nextButtonActionPerformed(e);
			}
		});
		nextButton.setFocusable(false);
		nextButton.setFont(new Font("Tahoma", Font.PLAIN, 15));
		nextButton.setIcon(new ImageIcon(HomeInventory.class.getResource("/images/next.png")));
		nextButton.setBounds(10, 278, 115, 50);
		panel.add(nextButton);

		exitButton = new JButton("Exit");
		exitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				exitButtonActionPerformed(e);
			}
		});
		exitButton.setFocusable(false);
		exitButton.setFont(new Font("Tahoma", Font.PLAIN, 15));
		exitButton.setBounds(10, 415, 115, 50);
		panel.add(exitButton);

		printButton = new JButton("Print");
		printButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				printButtonActionPerformed(e);
			}
		});
		printButton.setFocusable(false);
		printButton.setFont(new Font("Tahoma", Font.PLAIN, 15));
		printButton.setIcon(new ImageIcon(HomeInventory.class.getResource("/images/print.png")));
		printButton.setBounds(10, 364, 115, 50);
		panel.add(printButton);

		JLabel itemLable = new JLabel("Inventory Item");
		itemLable.setHorizontalAlignment(SwingConstants.RIGHT);
		itemLable.setFont(new Font("Tahoma", Font.PLAIN, 15));
		itemLable.setBounds(137, 23, 119, 28);
		frame.getContentPane().add(itemLable);

		itemTextField = new JTextField();
		itemTextField.setFont(new Font("Tahoma", Font.PLAIN, 15));
		itemTextField.setBounds(275, 22, 522, 30);
		frame.getContentPane().add(itemTextField);
		itemTextField.setColumns(10);

		JLabel locationLabel = new JLabel("Location");
		locationLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));
		locationLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		locationLabel.setBounds(137, 61, 119, 28);
		frame.getContentPane().add(locationLabel);

		locationComboBox = new JComboBox();
		locationComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				locationComboBoxActionPerformed(e);
			}
		});
		locationComboBox.setEditable(true);
		locationComboBox.setBackground(Color.WHITE);
		locationComboBox.setFont(new Font("Tahoma", Font.PLAIN, 15));
		locationComboBox.setBounds(275, 61, 368, 30);
		frame.getContentPane().add(locationComboBox);

		markedCheckBox = new JCheckBox("Marked");
		markedCheckBox.setFont(new Font("Tahoma", Font.PLAIN, 15));
		markedCheckBox.setBounds(653, 58, 144, 28);
		frame.getContentPane().add(markedCheckBox);

		JLabel serialLable = new JLabel("Serial Number");
		serialLable.setHorizontalAlignment(SwingConstants.RIGHT);
		serialLable.setFont(new Font("Tahoma", Font.PLAIN, 15));
		serialLable.setBounds(137, 99, 119, 28);
		frame.getContentPane().add(serialLable);

		JLabel priceLable = new JLabel("Purchase Price");
		priceLable.setHorizontalAlignment(SwingConstants.RIGHT);
		priceLable.setFont(new Font("Tahoma", Font.PLAIN, 15));
		priceLable.setBounds(137, 152, 119, 28);
		frame.getContentPane().add(priceLable);

		priceTextField = new JTextField();
		priceTextField.setFont(new Font("Tahoma", Font.PLAIN, 15));
		priceTextField.setColumns(10);
		priceTextField.setBounds(275, 152, 157, 30);
		frame.getContentPane().add(priceTextField);
		priceTextField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				priceTextField.requestFocus();
			}
		});

		JLabel dateLable = new JLabel("Date Purchased");
		dateLable.setHorizontalAlignment(SwingConstants.RIGHT);
		dateLable.setFont(new Font("Tahoma", Font.PLAIN, 15));
		dateLable.setBounds(442, 152, 119, 28);
		frame.getContentPane().add(dateLable);

		dateDateChooser = new DateChooserCombo();
		dateDateChooser.setFieldFont(new Font("Tahoma", Font.PLAIN, 15));
		dateDateChooser.setBounds(571, 152, 228, 28);
		frame.getContentPane().add(dateDateChooser);

		JLabel storeLable = new JLabel("Store/Website");
		storeLable.setFont(new Font("Tahoma", Font.PLAIN, 15));
		storeLable.setHorizontalAlignment(SwingConstants.RIGHT);
		storeLable.setBounds(137, 190, 119, 35);
		frame.getContentPane().add(storeLable);

		storeTextField = new JTextField();
		storeTextField.setFont(new Font("Tahoma", Font.PLAIN, 15));
		storeTextField.setColumns(10);
		storeTextField.setBounds(275, 192, 522, 30);
		frame.getContentPane().add(storeTextField);
		storeTextField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				storeTextField.requestFocus();
			}
		});

		JLabel noteLable = new JLabel("Note");
		noteLable.setHorizontalAlignment(SwingConstants.RIGHT);
		noteLable.setFont(new Font("Tahoma", Font.PLAIN, 15));
		noteLable.setBounds(137, 235, 119, 28);
		frame.getContentPane().add(noteLable);

		noteTextField = new JTextField();
		noteTextField.setFont(new Font("Tahoma", Font.PLAIN, 15));
		noteTextField.setColumns(10);
		noteTextField.setBounds(275, 232, 522, 30);
		frame.getContentPane().add(noteTextField);
		noteTextField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				noteTextField.requestFocus();
			}
		});

		serialTextField = new JTextField();
		serialTextField.setFont(new Font("Tahoma", Font.PLAIN, 15));
		serialTextField.setColumns(10);
		serialTextField.setBounds(275, 99, 522, 30);
		frame.getContentPane().add(serialTextField);
		serialTextField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				serialTextField.requestFocus();
			}
		});

		JLabel photoLable = new JLabel("Photo");
		photoLable.setHorizontalAlignment(SwingConstants.RIGHT);
		photoLable.setFont(new Font("Tahoma", Font.PLAIN, 15));
		photoLable.setBounds(163, 273, 93, 28);
		frame.getContentPane().add(photoLable);

		photoTextArea = new JTextArea();
		photoTextArea.setEditable(false);
		photoTextArea.setWrapStyleWord(true);
		photoTextArea.setLineWrap(true);
		photoTextArea.setBackground(new Color(255, 255, 153));
		photoTextArea.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		photoTextArea.setBounds(275, 272, 482, 40);
		frame.getContentPane().add(photoTextArea);
		

		photoButton = new JButton("...");
		photoButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				photoButtonActionPerformed(e);
			}
		});
		photoButton.setFocusable(false);
		photoButton.setFont(new Font("Tahoma", Font.PLAIN, 15));
		photoButton.setBounds(762, 272, 65, 40);
		frame.getContentPane().add(photoButton);

		searchPanel = new JPanel();
		searchPanel.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		searchPanel.setBounds(137, 350, 314, 144);
		frame.getContentPane().add(searchPanel);

		photoPanel = new PhotoPanel();
		photoPanel.setSize(322, 174);
		photoPanel.setLocation(473, 322);
		frame.getContentPane().add(photoPanel);
		
		
		
		

		myInventory = new InventoryItem[maximumEntries];
		// pack();
		// photoPanel.setLayout(new GridLayout(1, 0, 0, 0));

		JButton[] searchButton = new JButton[26];
		int x = 0, y = 0;
		for (int i = 0; i < 26; i++) {
			// create new button
			searchButton[i] = new JButton();
			// set text property
			searchButton[i].setText(String.valueOf((char) (65 + i)));
			searchButton[i].setFont(new Font("Arial", Font.BOLD, 12));
			searchButton[i].setMargin(new Insets(-10, -10, -10, -10));
			sizeButton(searchButton[i], new Dimension(37, 27));
			searchButton[i].setBackground(Color.YELLOW);
			searchButton[i].setFocusable(false);
			searchPanel.add(searchButton[i]);
			searchButton[i].addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					searchButtonActionPerformed(e);
				}
			});
			x++;
			// six buttons per row
			if (x % 6 == 0) {
				x = 0;
				y++;
			}
		}
		
		int n;
		// open file for entries
		try {
			BufferedReader inputFile = new BufferedReader(new FileReader("inventory.txt"));
			numberEntries = Integer.valueOf(inputFile.readLine()).intValue();
			if (numberEntries != 0) {
				for (int i = 0; i < numberEntries; i++) {
					myInventory[i] = new InventoryItem();
					myInventory[i].description = inputFile.readLine();
					myInventory[i].location = inputFile.readLine();
					myInventory[i].serialNumber = inputFile.readLine();
					myInventory[i].marked = Boolean.valueOf(inputFile.readLine()).booleanValue();
					myInventory[i].purchasePrice = inputFile.readLine();
					myInventory[i].purchaseDate = inputFile.readLine();
					myInventory[i].purchaseLocation = inputFile.readLine();
					myInventory[i].note = inputFile.readLine();
					myInventory[i].photoFile = inputFile.readLine();
				}
			}
			// read in combo box elements
			n = Integer.valueOf(inputFile.readLine()).intValue();
			if (n != 0) {
				for (int i = 0; i < n; i++) {
					locationComboBox.addItem(inputFile.readLine());
				}
			}
			inputFile.close();
			currentEntry = 1;
			showEntry(currentEntry);
		} catch (Exception ex) {
			numberEntries = 0;
			currentEntry = 0;
		}
		if (numberEntries == 0) {
			newButton.setEnabled(false);
			deleteButton.setEnabled(false);
			nextButton.setEnabled(false);
			previousButton.setEnabled(false);
			printButton.setEnabled(false);
		}

	}

	private void checkSave() {
		boolean edited = false;
		if (!myInventory[currentEntry - 1].description.equals(itemTextField.getText()))
			edited = true;
		else if (!myInventory[currentEntry - 1].location.equals(locationComboBox.getSelectedItem().toString()))
			edited = true;
		else if (myInventory[currentEntry - 1].marked != markedCheckBox.isSelected())
			edited = true;
		else if (!myInventory[currentEntry - 1].serialNumber.equals(serialTextField.getText()))
			edited = true;
		else if (!myInventory[currentEntry - 1].purchasePrice.equals(priceTextField.getText()))
			edited = true;
		else if (!myInventory[currentEntry - 1].purchaseDate.equals(dateDateChooser.getSelectedDate().toString()))
			edited = true;
		else if (!myInventory[currentEntry - 1].purchaseLocation.equals(storeTextField.getText()))
			edited = true;
		else if (!myInventory[currentEntry - 1].note.equals(noteTextField.getText()))
			edited = true;
		else if (!myInventory[currentEntry - 1].photoFile.equals(photoTextArea.getText()))
			edited = true;
		if (edited) {
			if (JOptionPane.showConfirmDialog(null, "You have edited this item. Do you want to" + "save the changes?",
					"Save Item", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION)
				saveButton.doClick();
		}
	}

	private void blankValues() {
		// blank input screen
		newButton.setEnabled(false);
		deleteButton.setEnabled(false);
		saveButton.setEnabled(true);
		previousButton.setEnabled(false);
		nextButton.setEnabled(false);
		printButton.setEnabled(false);
		itemTextField.setText("");
		locationComboBox.setSelectedItem("");
		markedCheckBox.setSelected(false);
		serialTextField.setText("");
		priceTextField.setText("");
		dateDateChooser.getDefaultPeriods();
		storeTextField.setText("");
		noteTextField.setText("");
		photoTextArea.setText("");
		photoPanel.repaint();
		itemTextField.requestFocus();
	}

	private void deleteButtonActionPerformed(ActionEvent e) {
		if (JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this item?", "Delete Inventory Item",
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.NO_OPTION)
			return;
		deleteEntry(currentEntry);
		if (numberEntries == 0) {
			currentEntry = 0;
			blankValues();
		} else {
			currentEntry--;
			if (currentEntry == 0)
				currentEntry = 1;
			showEntry(currentEntry);
		}
	}

	private void deleteEntry(int j) {
		// delete entry j
		if (j != numberEntries) {
			// move all entries under j up one level
			for (int i = j; i < numberEntries; i++) {
				myInventory[i - 1] = new InventoryItem();
				myInventory[i - 1] = myInventory[i];
			}
		}
		numberEntries--;
	}

	private void showEntry(int j) {
		// display entry j (1 to numberEntries)
		itemTextField.setText(myInventory[j - 1].description);
		locationComboBox.setSelectedItem(myInventory[j - 1].location);
		markedCheckBox.setSelected(myInventory[j - 1].marked);
		serialTextField.setText(myInventory[j - 1].serialNumber);
		priceTextField.setText(myInventory[j - 1].purchasePrice);
		storeTextField.setText(myInventory[j - 1].purchaseLocation);
		noteTextField.setText(myInventory[j - 1].note);
		showPhoto(myInventory[j - 1].photoFile);
		nextButton.setEnabled(true);
		previousButton.setEnabled(true);
		if (j == 1)
			previousButton.setEnabled(false);
		if (j == numberEntries)
			nextButton.setEnabled(false);
		itemTextField.requestFocus();
	}

	private void showPhoto(String photoFile) {
		if (!photoFile.equals("")) {
			try {
				photoTextArea.setText(photoFile);
			} catch (Exception ex) {
				photoTextArea.setText("");
			}
		} else {
			photoTextArea.setText("");
		}
		photoPanel.repaint();
	}

	private void saveButtonActionPerformed(ActionEvent e) {
		// check for description
		itemTextField.setText(itemTextField.getText().trim());
		if (itemTextField.getText().equals("")) {
			JOptionPane.showConfirmDialog(null, "Must have item description.", "Error", JOptionPane.DEFAULT_OPTION,
					JOptionPane.ERROR_MESSAGE);
			itemTextField.requestFocus();
			return;
		}
		if (newButton.isEnabled()) {
			// delete edit entry then re save
			deleteEntry(currentEntry);
		}
		// capitalize first letter
		String s = itemTextField.getText();
		itemTextField.setText(s.substring(0, 1).toUpperCase() + s.substring(1));
		numberEntries++;
		// determine new current entry location based on description
		currentEntry = 1;
		if (numberEntries != 1) {
			do {
				if (itemTextField.getText().compareTo(myInventory[currentEntry - 1].description) < 0)
					break;
				currentEntry++;
			} while (currentEntry < numberEntries);
		}
		// move all entries below new value down one position unless at end
		if (currentEntry != numberEntries) {
			for (int i = numberEntries; i >= currentEntry + 1; i--) {
				myInventory[i - 1] = myInventory[i - 2];
				myInventory[i - 2] = new InventoryItem();
			}
		}
		myInventory[currentEntry - 1] = new InventoryItem();
		myInventory[currentEntry - 1].description = itemTextField.getText();
		myInventory[currentEntry - 1].location = locationComboBox.getSelectedItem().toString();
		myInventory[currentEntry - 1].marked = markedCheckBox.isSelected();
		myInventory[currentEntry - 1].serialNumber = serialTextField.getText();
		myInventory[currentEntry - 1].purchasePrice = priceTextField.getText();
		myInventory[currentEntry - 1].purchaseDate = dateDateChooser.getSelectedPeriodSet().toString();
		myInventory[currentEntry - 1].purchaseLocation = storeTextField.getText();
		myInventory[currentEntry - 1].photoFile = photoTextArea.getText();
		myInventory[currentEntry - 1].note = noteTextField.getText();
		showEntry(currentEntry);
		if (numberEntries < maximumEntries)
			newButton.setEnabled(true);
		else
			newButton.setEnabled(false);
		deleteButton.setEnabled(true);
		printButton.setEnabled(true);
	}

	private void previousButtonActionPerformed(ActionEvent e) {
		checkSave();
		currentEntry--;
		showEntry(currentEntry);
	}

	private void nextButtonActionPerformed(ActionEvent e) {
		checkSave();
		currentEntry++;
		showEntry(currentEntry);
	}

	private void printButtonActionPerformed(ActionEvent e) {
		lastPage = (int) (1 + (numberEntries - 1) / entriesPerPage);
		PrinterJob inventoryPrinterJob = PrinterJob.getPrinterJob();
		inventoryPrinterJob.setPrintable(new InventoryDocument());
		if (inventoryPrinterJob.printDialog()) {
			try {
				inventoryPrinterJob.print();
			} catch (PrinterException ex) {
				JOptionPane.showConfirmDialog(null, ex.getMessage(), "Print Error", JOptionPane.DEFAULT_OPTION,
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	private void photoButtonActionPerformed(ActionEvent e) 
	{
		JFileChooser openChooser = new JFileChooser();
		openChooser.setDialogType(JFileChooser.OPEN_DIALOG);
		openChooser.setDialogTitle("Open Photo File");
		openChooser.addChoosableFileFilter(new FileNameExtensionFilter("Photo Files", "jpg"));
		if (openChooser.showOpenDialog(frame.getContentPane()) == JFileChooser.APPROVE_OPTION)
			showPhoto(openChooser.getSelectedFile().toString());
	}

	private void exitButtonActionPerformed(ActionEvent e) {
		exitForm(null);
	}

	@SuppressWarnings("unchecked")
	private void locationComboBoxActionPerformed(ActionEvent e) {
		// If in list - exit method
		if (locationComboBox.getItemCount() != 0) {
			for (int i = 0; i < locationComboBox.getItemCount(); i++) {
				if (locationComboBox.getSelectedItem().toString().equals(locationComboBox.getItemAt(i).toString())) {
					serialTextField.requestFocus();
					return;
				}
			}
		}
		// If not found, add to list box
		locationComboBox.addItem(locationComboBox.getSelectedItem());
		serialTextField.requestFocus();
	}

	private void searchButtonActionPerformed(ActionEvent e) {
		int i;
		if (numberEntries == 0)
			return;
		// search for item letter
		String letterClicked = e.getActionCommand();
		i = 0;
		do {
			if (myInventory[i].description.substring(0, 1).equals(letterClicked)) {
				currentEntry = i + 1;
				showEntry(currentEntry);
				return;
			}
			i++;
		} while (i < numberEntries);
		JOptionPane.showConfirmDialog(null, "No " + letterClicked + " inventory items.", "None Found",
				JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE);
	}

	private void exitForm(WindowEvent evt) {
		if (JOptionPane.showConfirmDialog(null, "Any unsaved changes will be lost.\nAre you" + "sure you want to exit?",
				"Exit Program", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.NO_OPTION)
			return;
		// write entries back to file
		try {
			PrintWriter outputFile = new PrintWriter(new BufferedWriter(new FileWriter("inventory.txt")));
			outputFile.println(numberEntries);
			if (numberEntries != 0) {
				for (int i = 0; i < numberEntries; i++) {
					outputFile.println(myInventory[i].description);
					outputFile.println(myInventory[i].location);
					outputFile.println(myInventory[i].serialNumber);
					outputFile.println(myInventory[i].marked);
					outputFile.println(myInventory[i].purchasePrice);
					outputFile.println(myInventory[i].purchaseDate);
					outputFile.println(myInventory[i].purchaseLocation);
					outputFile.println(myInventory[i].note);
					outputFile.println(myInventory[i].photoFile);
				}
			}
			// write combo box entries
			outputFile.println(locationComboBox.getItemCount());
			if (locationComboBox.getItemCount() != 0) {
				for (int i = 0; i < locationComboBox.getItemCount(); i++)
					outputFile.println(locationComboBox.getItemAt(i));
			}
			outputFile.close();
		} catch (Exception ex) {

		}
		System.exit(0);
	}
}

@SuppressWarnings("serial")
class PhotoPanel extends JPanel 
{
	public void paintComponent(Graphics g) {
		Graphics2D g2D = (Graphics2D) g;
		super.paintComponent(g2D);
		// draw border
		g2D.setPaint(Color.BLACK);
		g2D.draw(new Rectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1));
		// show photo
		Image photoImage = new ImageIcon(HomeInventory.photoTextArea.getText()).getImage();
		int w = getWidth();
		int h = getHeight();
		double rWidth = (double) getWidth() / (double) photoImage.getWidth(null);
		double rHeight = (double) getHeight() / (double) photoImage.getHeight(null);
		if (rWidth > rHeight) {
			// leave height at display height, change width by amount height is changed
			w = (int) (photoImage.getWidth(null) * rHeight);
		} else {
			// leave width at display width, change height by amount width is changed
			h = (int) (photoImage.getHeight(null) * rWidth);
		}
		// center in panel
		g2D.drawImage(photoImage, (int) (0.5 * (getWidth() - w)), (int) (0.5 * (getHeight() - h)), w, h, null);
		g2D.dispose();
	}
}
class InventoryDocument implements Printable 
{
	public int print(Graphics g, PageFormat pf, int pageIndex) {
		Graphics2D g2D = (Graphics2D) g;
		if ((pageIndex + 1) > HomeInventory.lastPage) {
			return NO_SUCH_PAGE;
		}
		int i, iEnd;
		// here you decide what goes on each page and draw it
		// header
		g2D.setFont(new Font("Arial", Font.BOLD, 14));
		g2D.drawString("Home Inventory Items - Page " + String.valueOf(pageIndex + 1), (int) pf.getImageableX(),
				(int) (pf.getImageableY() + 25));
		// get starting y
		int dy = (int) g2D.getFont().getStringBounds("S", g2D.getFontRenderContext()).getHeight();
		int y = (int) (pf.getImageableY() + 4 * dy);
		iEnd = HomeInventory.entriesPerPage * (pageIndex + 1);
		if (iEnd > HomeInventory.numberEntries)
			iEnd = HomeInventory.numberEntries;
		for (i = 0 + HomeInventory.entriesPerPage * pageIndex; i < iEnd; i++) {
			// dividing line
			Line2D.Double dividingLine = new Line2D.Double(pf.getImageableX(), y,
					pf.getImageableX() + pf.getImageableWidth(), y);
			g2D.draw(dividingLine);
			y += dy;
			g2D.setFont(new Font("Arial", Font.BOLD, 12));
			g2D.drawString(HomeInventory.myInventory[i].description, (int) pf.getImageableX(), y);
			y += dy;
			g2D.setFont(new Font("Arial", Font.PLAIN, 12));
			g2D.drawString("Location: " + HomeInventory.myInventory[i].location, (int) (pf.getImageableX() + 25), y);
			y += dy;
			if (HomeInventory.myInventory[i].marked)
				g2D.drawString("Item is marked with identifying information.", (int) (pf.getImageableX() + 25), y);
			else
				g2D.drawString("Item is NOT marked with identifying information.", (int) (pf.getImageableX() + 25), y);
			y += dy;
			g2D.drawString("Serial Number: " + HomeInventory.myInventory[i].serialNumber,
					(int) (pf.getImageableX() + 25), y);
			y += dy;
			g2D.drawString("Price: $" + HomeInventory.myInventory[i].purchasePrice + "," + "Purchased on: "
					+ HomeInventory.myInventory[i].purchaseDate, (int) (pf.getImageableX() + 25), y);
			y += dy;
			g2D.drawString("Purchased at: " + HomeInventory.myInventory[i].purchaseLocation,
					(int) (pf.getImageableX() + 25), y);
			y += dy;
			g2D.drawString("Note: " + HomeInventory.myInventory[i].note, (int) (pf.getImageableX() + 25), y);
			y += dy;
			try {
				// maintain original width/height ratio
				Image inventoryImage = new ImageIcon(HomeInventory.myInventory[i].photoFile).getImage();
				double ratio = (double) (inventoryImage.getWidth(null)) / (double) inventoryImage.getHeight(null);
				g2D.drawImage(inventoryImage, (int) (pf.getImageableX() + 25), y, (int) (100 * ratio), 100, null);
			} catch (Exception ex) {
				// have place to go in case image file doesn't open
			}
			y += 2 * dy + 100;
		}
		return PAGE_EXISTS;
	}
}
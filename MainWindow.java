import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class MainWindow extends JFrame {

	private JPanel contentPane;
	private JTextField textField_size;
	private JTextField textField_number;
	private JTable table;
	
	int memorySize = 100;

	Memory memory = new Memory(memorySize);
	
	private List<Process> processList = new ArrayList<Process>();
	private List<MemoryBlock> blockList = new ArrayList<MemoryBlock>();
	
	public void terminateProcess() {
		try{
			Integer number = Integer.valueOf(textField_number.getText().trim());
			if(!memory.deallocate(number)){
				JOptionPane.showMessageDialog(this, "Failed!!!");
			}
			updateData();
		}catch (Exception e) {
			JOptionPane.showMessageDialog(this, "invalid input!!!");
		}
	}

	public void createProcess() {
		Integer size = Integer.valueOf(textField_size.getText().trim());
		Process process = new Process();
		process.size = size;
		
		if(!memory.allocate(process)){
			JOptionPane.showMessageDialog(this, "Failed!!!");
		}
		updateData();
	}
	
	public void updateData(){
		processList = memory.getProcessList();
		blockList = memory.getMemoryBlockList();
		
		System.out.println(processList.toString());
		System.out.println(blockList.toString());
		
		setTableData();
		repaint();
	}
	
	public void setTableData(){
		String[] titles = new String[]{"Number","Size", "Location"};
		String[][] data = new String[processList.size()][3];
		int i=0;
		for (Process process : processList) {
			String[] dataLine = new String[]{process.number.toString(),process.size.toString(),process.location.toString()};
			data[i++] = dataLine;
		}
		
		
		DefaultTableModel model = new DefaultTableModel(data, titles){
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setModel(model);
	}
	
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		
		int x = 30;
		int y = 180;
		int width = (getWidth()-60);
		int height = 100;
		g.setColor(Color.GREEN);
		g.fillRect(x, y, width+1, height+1);
		
		for (MemoryBlock memoryBlock : blockList) {
			if(!memoryBlock.available){
				int width1 = (int) (((double)(memoryBlock.size * width)) / memorySize);
				int x1 = (int) (((double)(memoryBlock.location * width)) / memorySize);
				
				g.setColor(Color.RED);
				g.fillRect(x + x1, y, width1+1, height+1);
				g.setColor(Color.BLUE);
				g.drawLine(x + x1, y, x + x1, y+height);
				g.drawLine(x + x1 + width1, y, x + x1 + width1, y+height);
			}
		}
		
		g.setColor(Color.RED);
		for(int i=0;i<memorySize;i+=memorySize/20){
			int x1 = (int) (((double)(i * width)) / memorySize);
			g.drawString(i+"", x + x1, y+height+15);
		}
	}
	
	
	/**
	 * Create the frame.
	 */
	public MainWindow() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 723, 538);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Size:");
		lblNewLabel.setBounds(130, 42, 49, 18);
		contentPane.add(lblNewLabel);
		
		textField_size = new JTextField();
		textField_size.setBounds(193, 39, 106, 24);
		contentPane.add(textField_size);
		textField_size.setColumns(10);
		
		JButton btnCreateProcess = new JButton("Create Process");
		btnCreateProcess.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				createProcess();
			}
		});
		btnCreateProcess.setBounds(331, 38, 185, 27);
		contentPane.add(btnCreateProcess);
		
		JLabel lblNumber = new JLabel("Number:");
		lblNumber.setBounds(115, 92, 64, 18);
		contentPane.add(lblNumber);
		
		textField_number = new JTextField();
		textField_number.setColumns(10);
		textField_number.setBounds(193, 89, 106, 24);
		contentPane.add(textField_number);
		
		JButton btnTerminateProcess = new JButton("Terminate Process");
		btnTerminateProcess.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				terminateProcess();
			}
		});
		btnTerminateProcess.setBounds(331, 88, 185, 27);
		contentPane.add(btnTerminateProcess);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(14, 261, 677, 217);
		contentPane.add(scrollPane);
		
		table = new JTable();
		scrollPane.setViewportView(table);
		
		updateData();
	}

}

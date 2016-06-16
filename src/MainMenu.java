import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MainMenu extends JFrame{

	static MainMenu menu;
	
	public MainMenu() {
		super("Snake");
		setSize(400,300);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setLayout(new GridLayout(3,1));
		
		JPanel top = new JPanel();
		top.setBorder(new EmptyBorder(30, 120, 30,120) );
		top.setLayout(new FlowLayout(FlowLayout.CENTER,10,10));
		top.add(new JLabel("GEORGE'S SNAKE"));
		
		JPanel middle = new JPanel();
		middle.setBorder(new EmptyBorder(0, 10, 0,10) );
		middle.setLayout(new GridLayout(2,4,20,10));
		middle.add(new JLabel("# Rows:"));
		JSpinner rows = new JSpinner(new SpinnerNumberModel(20, 10, 100, 5));
		middle.add(rows);
		middle.add(new JLabel("# Columns:"));
		JSpinner columns = new JSpinner(new SpinnerNumberModel(20, 10, 100, 5));
		middle.add(columns);
		middle.add(new JLabel("Cell Size:"));
		JSpinner size = new JSpinner(new SpinnerNumberModel(20, 10, 100, 5));
		middle.add(size);
		middle.add(new JLabel("Game Type:"));
		JComboBox<String> cboTypes = new JComboBox<String>();
		cboTypes.addItem("Classic");
		cboTypes.addItem("2 Players");
		cboTypes.addItem("AI Test");
		cboTypes.addItem("Versus AI");
		cboTypes.addItem("Eyebrows");
		cboTypes.setEditable(false);
		middle.add(cboTypes);
		
		JPanel bottom = new JPanel();
		bottom.setBorder(new EmptyBorder(30, 120, 30,120) );
		bottom.setLayout(new BorderLayout());
		JButton start = new JButton("Begin");
		start.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent e) {
				Game.start(cboTypes.getSelectedIndex(),(int)columns.getValue(),(int)rows.getValue(),(int)size.getValue());
			}
		});
		
		bottom.add(start, BorderLayout.CENTER);
		
		add(top);
		add(middle);
		add(bottom);
		setVisible(true);
	}
	
	public static void main(String[] args) {
		menu = new MainMenu();
	}

}

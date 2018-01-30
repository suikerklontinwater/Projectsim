package parkeerGarageMVCTest.Controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import parkeerGarageMVCTest.Model.parkeerGarageSimulator;

public class parkeerGarageSimulatorController extends JPanel implements ActionListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private parkeerGarageSimulator simulator;
	private JButton test;
	
	public parkeerGarageSimulatorController(parkeerGarageSimulator simulator) {
		this.simulator = simulator;
		
		setSize(500,800);
		test= new JButton("Test");
		test.addActionListener(this);
		
		this.setLayout(null);
		add(test);
		test.setBounds(200, 850, 70, 30);
		
		setVisible(true);
	}
	
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==test) {
			System.out.println("werkt!");
		}
	}
	
}
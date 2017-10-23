package Server;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Interface extends JFrame implements ActionListener {

	private JLabel ClientIP;
	private JLabel ClientPort;
	private JLabel BufferSize;
	private JLabel ObjectsNumber;

	private JTextField ClientIPField;
	private JTextField ClientPortField;
	private JTextField BufferSizeField;
	private JTextField ObjectsNumberField;

	private JButton accept;

	public static final int weidht = 250;
	public static final int height = 300;
	public static final int rowsNumber = 10;

	private ServerSender server;

	public Interface() {

		setTitle("Enter all Information");
		setSize(weidht, height);
		setVisible(true);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(new GridLayout(rowsNumber, 2));

		JPanel panel3 = new JPanel();
		panel3.setPreferredSize(new Dimension(weidht, height / rowsNumber));
		panel3.setLayout(new GridLayout(1, 1));
		BufferSize = new JLabel("Buffer Size:", SwingConstants.CENTER);
		panel3.add(BufferSize);
		BufferSizeField = new JTextField("1000", SwingConstants.HORIZONTAL);
		panel3.add(BufferSizeField);
		add(panel3);

		JPanel panel4 = new JPanel();
		panel4.setPreferredSize(new Dimension(weidht, height / rowsNumber));
		panel4.setLayout(new GridLayout(1, 2));
		ObjectsNumber = new JLabel("Objects Number:", SwingConstants.CENTER);
		panel4.add(ObjectsNumber);
		ObjectsNumberField = new JTextField("10000", SwingConstants.HORIZONTAL);
		panel4.add(ObjectsNumberField);
		add(panel4);

		JPanel panel6 = new JPanel();
		panel6.setPreferredSize(new Dimension(weidht, height / rowsNumber));
		panel6.setLayout(new GridLayout(1, 3));
		add(panel6);

		accept = new JButton("Enter");
		accept.setActionCommand("c1");
		accept.addActionListener(this);
		add(accept);

		validate();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		if (command.equalsIgnoreCase(accept.getActionCommand())) {
			start();
		}
	}

	public void start() {

		if (isNumeric(ClientPortField.getText()) && isNumeric(BufferSizeField.getText())
				&& isNumeric(ObjectsNumberField.getText())) {

			server = new ServerSender(7779, "localhost", 7778, Integer.parseInt(BufferSizeField.getText()), 
									  Integer.parseInt(ObjectsNumberField.getText()));
			server.start();
			
			System.out.println("Server Sender Started");
			
			this.dispose();
		} else {
			JOptionPane.showMessageDialog(null,
					"the data has been entered in an incorrect format or this is not complete", "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private static boolean isNumeric(String string) {
		try {
			Integer.parseInt(string);
			return true;
		} catch (NumberFormatException nfe) {
			return false;
		}
	}

	public static void main(String[] args) {
		new Interface();
	}
}

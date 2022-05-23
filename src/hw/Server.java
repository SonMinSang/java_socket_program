package hw;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.security.PublicKey;
import java.util.Base64;
import java.util.HashSet;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.border.LineBorder;

import hw.Algorithm.AESEncryption;
import hw.Algorithm.RSAEncryption;
import hw.Algorithm.RSASignature;
import hw.Component.Button;
import hw.Component.Label;
import hw.Component.Layout;
import hw.Component.Panel;
import hw.Component.ScrollPane;
import hw.Component.TextArea;
import hw.Dto.EncryptedAESkey;
import hw.Dto.EncryptedMessage;
import hw.Dto.Signature;

public class Server {

	private Panel main_panel, mode_panel, key_panel, chat_panel, send_panel, receive_panel;
	private Label server_label, send_label, send_file_dir_label, send_file_label, receive_label, file_label,
			verification_label;
	private TextArea mode_info_txt, is_connect_txt, keypair_info_txt, pubkey_txt, chat_txt, msg_txt, send_file_dir_txt,
			send_file_txt, file_txt, verification_txt;
	private ScrollPane mode_scroll, keypair_scroll, pubkey_scroll, chat_scroll, msg_scroll;
	private Button address, connect, key_gen, load_file, save_file, send_public, send_msg, send_file, find_file,
			verification_file, save_rec_file;
	private JFrame fr;
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	private ServerSocket serverSocket;
	private Socket socket;
	private RSAEncryption rsa;
	private AESEncryption aes;
	private String server_ip = "0.0.0.0";
	private String server_port = "7777";
	private HashSet<String> keyPairs = new HashSet<>();;
	private Object input;
	private PublicKey pubKey;
	private RSASignature signature;
	private Signature sign;

	public Server() {
		
		try {
			server_ip = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		server_ip = JOptionPane.showInputDialog(fr, "Server Ip Address", server_ip);
		server_port = JOptionPane.showInputDialog(fr, "Server Port Number", server_port);

		fr = new JFrame("hw");
		//Main panel
		main_panel = new Panel(new Layout(0, 10, 500, 600), new LineBorder(Color.black, 1, true));
		
		//Mode panel
		mode_panel = new Panel(new Layout(20, 10, 500, 110));
		mode_info_txt = new TextArea("[server] ip : " + server_ip + " port : " + server_port + "\n");
		mode_scroll = new ScrollPane(mode_info_txt.getTextArea(), new Layout(180, 0, 300, 60));
		server_label = new Label("Server", new Layout(0, 0, 160, 20));
		is_connect_txt = new TextArea("It is not connected\n", new Layout(0, 70, 480, 40));
		address = new Button("Address", new Layout(0, 20, 160, 20), true);
		address.getButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setAddress();
			}
		});
		connect = new Button("Connect", new Layout(0, 40, 160, 20), true);
		connect.getButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				connects();
			}
		});
		mode_panel.addAll(server_label.getLabel(), address.getButton(), connect.getButton(),
				mode_scroll.getScrollPane(), is_connect_txt.getTextArea());
		
		// Key panel
		key_panel = new Panel(new Layout(20, 120, 480, 160), new LineBorder(Color.black, 1, true));
		keypair_info_txt = new TextArea("Public/Private key pair information (displaying key information)");
		keypair_scroll = new ScrollPane(keypair_info_txt.getTextArea(), new Layout(10, 10, 460, 40));
		key_gen = new Button("Key_generation", new Layout(20, 60, 140, 40), true);
		key_gen.getButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				keyGeneration();
			}
		});

		load_file = new Button("Load from a file", new Layout(170, 60, 140, 40), true);
		load_file.getButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				loadKeyFromFile();
			}
		});
		save_file = new Button("Save into a file", new Layout(320, 60, 140, 40), false);
		save_file.getButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveKeyInfoFile();
			}
		});
		send_public = new Button("Send public key", new Layout(20, 110, 140, 40), false);
		send_public.getButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				sendPublicKey();
			}
		});
		pubkey_txt = new TextArea("Another user's public key information\r\n" + "(if it is in the system or received)");
		pubkey_scroll = new ScrollPane(pubkey_txt.getTextArea(), new Layout(170, 110, 300, 40));
		key_panel.addAll(keypair_scroll.getScrollPane(), pubkey_scroll.getScrollPane(), key_gen.getButton(),
				load_file.getButton(), save_file.getButton(), send_public.getButton());

		// Chat panel
		chat_panel = new Panel(new Layout(20, 280, 480, 195), new LineBorder(Color.black, 1, true));
		chat_txt = new TextArea(false);
		chat_scroll = new ScrollPane(chat_txt.getTextArea(), new Layout(5, 5, 470, 150));
		msg_txt = new TextArea(true);
		msg_scroll = new ScrollPane(msg_txt.getTextArea(), new Layout(5, 160, 345, 30));
		send_msg = new Button("Send message", new Layout(355, 160, 120, 30), false);
		send_msg.getButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				sendMessage();
			}
		});
		chat_panel.addAll(chat_scroll.getScrollPane(), msg_scroll.getScrollPane(), send_msg.getButton());

		// Send_panel
		send_panel = new Panel(new Layout(20, 480, 480, 150), new LineBorder(Color.black, 1, true));
		send_label = new Label("File send part", new Layout(5, 5, 100, 20));
		send_file_dir_label = new Label("directory :", new Layout(25, 30, 65, 20));
		send_file_dir_txt = new TextArea("", new Layout(90, 30, 380, 20));
		send_file_label = new Label("file : ", new Layout(58, 60, 80, 20));
		send_file_txt = new TextArea("", new Layout(90, 60, 380, 20));
		send_file = new Button("Send file", new Layout(20, 90, 140, 40), false);
		send_file.getButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				sendFile();
			}
		});
		find_file = new Button("Find file", new Layout(170, 90, 140, 40), false);
		find_file.getButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				findFile();
			}
		});
		send_panel.addAll(send_label.getLabel(), send_file_dir_label.getLabel(), send_file_dir_txt.getTextArea(),
				send_file_label.getLabel(), send_file_txt.getTextArea(), send_file.getButton(), find_file.getButton());

		// Recieve_panel
		receive_panel = new Panel(new Layout(20, 640, 480, 150), new LineBorder(Color.black, 1, true));
		receive_label = new Label("File receive part", new Layout(5, 5, 100, 20));
		file_label = new Label("file :", new Layout(58, 30, 80, 20));
		file_txt = new TextArea("", new Layout(90, 30, 380, 20));
		verification_label = new Label("verification :", new Layout(10, 60, 80, 20));
		verification_txt = new TextArea("", new Layout(90, 60, 380, 20));
		receive_panel.addAll(send_panel.getPanel());
		verification_file = new Button("Verification", new Layout(20, 90, 140, 40), false);
		verification_file.getButton().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				verificationFile();
			}
		});
		save_rec_file = new Button("Save file", new Layout(170, 90, 140, 40), false);
		save_rec_file.getButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveReceiveFile();
			}
		});
		receive_panel.addAll(receive_label.getLabel(), file_label.getLabel(), file_txt.getTextArea(),
				verification_label.getLabel(), verification_txt.getTextArea(), verification_file.getButton(),
				save_rec_file.getButton());
		main_panel.addAll(mode_panel.getPanel(), key_panel.getPanel(), chat_panel.getPanel(), send_panel.getPanel(),
				receive_panel.getPanel());
		fr.setContentPane(main_panel.getPanel());
		fr.setSize(500, 900);
		fr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		fr.setVisible(true);
		fr.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				windowClose(e);
			}
		});
	}
	// Set server IP address
	private void setAddress() {
		server_ip = JOptionPane.showInputDialog(fr, "Server Ip Address", server_ip);
		server_port = JOptionPane.showInputDialog(fr, "Server Port Number", server_port);
		mode_info_txt.getTextArea().setText("[server] ip : " + server_ip + " port : " + server_port + "\n");
	}
	
	private void keyGeneration() {
		rsa = new RSAEncryption();
		keypair_info_txt.getTextArea()
				.setText("public key : " + Base64.getEncoder().encodeToString(rsa.getBytePublicKey()) + "\n"
						+ "private key : " + Base64.getEncoder().encodeToString(rsa.getBytePrivateKey()));
		send_public.getButton().setEnabled(true);
		save_file.getButton().setEnabled(true);
	
	}
	
	private void loadKeyFromFile() {
		rsa = new RSAEncryption();
		String fileAddress = JOptionPane.showInputDialog(fr, "folder", "C:\\Users\\82102\\");
		String fileName = JOptionPane.showInputDialog(fr, "file name", "keyPair.txt");
		File file = new File(fileAddress + fileName);
		FileReader filereader;
		try {
			filereader = new FileReader(file);
			BufferedReader bufReader = new BufferedReader(filereader);
			String line = bufReader.readLine();
			String[] lines = line.split(" ");
			String publicKey = lines[3];
			line = bufReader.readLine();
			lines = line.split(" ");
			String privateKey = lines[3];
			rsa.setKeyPair(publicKey, privateKey);
			keypair_info_txt.getTextArea()
					.setText("public key : " + publicKey + "\n" + "private key : " + privateKey);
			send_public.getButton().setEnabled(true);
		} catch (FileNotFoundException e1) {

			e1.printStackTrace();
		} catch (IOException e2) {
			System.out.println(e2);
		}
	}
	//save public key, private key pair in file
	private void saveKeyInfoFile() {
		
		String fileAddress = JOptionPane.showInputDialog(fr, "folder", "C:\\Users\\82102\\");
		String fileName = JOptionPane.showInputDialog(fr, "file name", "keyPair.txt");

		try {
			OutputStream output = new FileOutputStream(fileAddress + fileName);
			String mykey = "public key : " + Base64.getEncoder().encodeToString(rsa.getBytePublicKey()) + "\n"
					+ "private key : " + Base64.getEncoder().encodeToString(rsa.getBytePrivateKey());
			output.write(mykey.getBytes());
			output.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	//send public key to opponent
	private void sendPublicKey() {
		try {
			oos.writeObject(rsa.getPublicKey());
			oos.flush();
			key_gen.getButton().setEnabled(false);
			load_file.getButton().setEnabled(false);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void sendFile() {
		byte[] filebyte = null;
		try {
			FileInputStream fi = new FileInputStream(
					send_file_dir_txt.getTextArea().getText() + send_file_txt.getTextArea().getText());
			// file size
			int bufferSize = fi.available();
			byte[] buf = new byte[bufferSize];
			fi.read(buf);
			// aes encryption
			byte[] ciphertext = aes.encrypt(buf);
			RSASignature signature = new RSASignature(rsa.getPublicKey(), rsa.getPrivateKey());
			byte[] sign = signature.RSAsign(ciphertext);
			oos.writeObject(new Signature(ciphertext, sign, send_file_txt.getTextArea().getText()));

		} catch (FileNotFoundException e1) {
			// If the file doesn't exist
			JOptionPane.showMessageDialog(fr, "Wrong file path");
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	// find file in local computer
	private void findFile() {
		String directory = JOptionPane.showInputDialog(fr, "File directory", "C:\\Users\\82102\\");

		String file = JOptionPane.showInputDialog(fr, "File name", "file.txt");
		send_file_dir_txt.getTextArea().setText(directory);
		send_file_txt.getTextArea().setText(file);
		if (aes != null && pubKey != null)
			send_file.getButton().setEnabled(true);
	}
	
	// verify the received file
	private void verificationFile() {
		signature = new RSASignature(pubKey);
		if (signature.RSAverify(sign.getOriginMessage(), sign.getSignature())) {
			verification_txt.getTextArea().setText("True");
			save_rec_file.getButton().setEnabled(true);
		} else {
			verification_txt.getTextArea().setText("False");
			save_rec_file.getButton().setEnabled(false);
		}
	}
	
	// socket connect
	public void connects() {
		try {
			//create server socket
			serverSocket = new ServerSocket();
			//bind
			serverSocket.bind(new InetSocketAddress(server_ip, Integer.parseInt(server_port)));
			//block until client enter
			socket = serverSocket.accept();
			//create Object stream for socket
			ois = new ObjectInputStream(socket.getInputStream());
			oos = new ObjectOutputStream(socket.getOutputStream());
			InetSocketAddress clientSocketAddress = (InetSocketAddress) socket.getRemoteSocketAddress();
			// client's IP address
			String clientHostName = clientSocketAddress.getAddress().getHostAddress();
			int clientHostPort = clientSocketAddress.getPort();
			is_connect_txt.getTextArea().setText("[server] ip : " + server_ip + " port : " + server_port
					+ "\n[server] client :" + clientHostName + ", port:" + clientHostPort + " is connected \n");
			mode_info_txt.getTextArea()
					.setText("[server] client :" + clientHostName + ", port:" + clientHostPort + " is connected \n");
			chat_txt.getTextArea().append("[server] " + clientHostName + " : " + clientHostPort + " entered\n");
			//make one more thread to receive data
			new Thread(new Runnable() {

				@Override
				public void run() {
					receive();
				}
			}).start();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// in while constantly get the data and separate the data into type
	// Make dto for serialization class and check the type for instanceof
	public void receive() {
		try {
			//in while constantly get the data and separate the data into type
			while (true) {
				input = ois.readObject();
				//chat message encrypted
				if (input instanceof EncryptedMessage) {
					byte[] message = ((EncryptedMessage) input).getEncryptedMessage();
					chat_txt.getTextArea().append("client : " + message.toString() + "\n");
					try {
						chat_txt.getTextArea().append("decrypt : " + new String(aes.decrypt(message)) + "\n");
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				//public Key
				} else if (input instanceof PublicKey) {
					pubKey = (PublicKey) input;
					pubkey_txt.getTextArea().setText(Base64.getEncoder().encodeToString(pubKey.getEncoded()));
					if (aes != null)
						find_file.getButton().setEnabled(true);
				// AES key encrypted by RSA
				} else if (input instanceof EncryptedAESkey) {
					receiveSecretKey();
				// RSASignature
				} else if (input instanceof Signature) {
					sign = (Signature) input;
					file_txt.getTextArea().setText(sign.getFileName());
					verification_file.getButton().setEnabled(true);
				}
			}
		} catch (EOFException e) {
			chat_txt.getTextArea().append("Client's connection is removed.\n");
		} catch (SocketException e) {
			chat_txt.getTextArea().append("Server's connection is removed.\n");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			connects();
		}
	}
	//SaveReceiveFile
	private void saveReceiveFile() {
		String directory = JOptionPane.showInputDialog(fr, "File directory", "C:\\Users\\82102\\");
		String file = JOptionPane.showInputDialog(fr, "File name", "file.txt");
		
		try {
			OutputStream output = new FileOutputStream(directory + file);
			output.write(aes.decrypt(sign.getOriginMessage()));
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendMessage() {
		String msg = msg_txt.getTextArea().getText();
		chat_txt.getTextArea().append(msg + "\n");
		try {
			oos.writeObject(new EncryptedMessage(aes.encrypt(msg)));
		} catch (Exception e) {
			e.printStackTrace();
		}
		msg_txt.getTextArea().setText("");
		msg_txt.getTextArea().requestFocus();
	}
	// when you receive secret key
	public void receiveSecretKey() {
		aes = new AESEncryption((EncryptedAESkey) input, rsa);
		send_msg.getButton().setEnabled(true);
		mode_info_txt.getTextArea().append("Get the AES secret key from client\n");
		if (pubKey != null)
			find_file.getButton().setEnabled(true);
	}
	// window close check the socket close
	private void windowClose(WindowEvent e) {
		JFrame frame = (JFrame) e.getWindow();
		frame.dispose();
		try {
			if (socket != null)
				socket.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		new Server();
	}
}
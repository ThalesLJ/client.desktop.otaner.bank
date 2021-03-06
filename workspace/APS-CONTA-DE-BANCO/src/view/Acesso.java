package view;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.text.MaskFormatter;
import control.Control;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.awt.Toolkit;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JProgressBar;
import javax.swing.Timer;

@SuppressWarnings("serial")
public class Acesso extends JFrame {

	// Instancias
	static Control control = new Control();

	// Diz se o sistema esta autenticando o usuario ou nao
	private boolean logging = false;
	
	// Para mostrar o cursor do mouse diferente para clicar nas coisas
	Cursor cursor = new Cursor(Cursor.HAND_CURSOR);

	// Para armazenar a resolucao da maquina
	public static Toolkit toolkit = Toolkit.getDefaultToolkit();
	public static Dimension dimension = toolkit.getScreenSize();

	// Para gerar os numeros da senha
	private static String strSenha1 = "", strSenha2 = "", strSenha3 = "", strSenha4 = "", strSenha5 = "";
	private static List<Integer> senhas_1, senhas_2, senhas_3, senhas_4, senhas_5;

	// para guardar a senha digitada pelo usuario e suas combinacoes
	protected static List<String> ListaDeSenhasPossiveis = new ArrayList<String>();
	protected static String senhasPossiveis[] = new String[16];

	// Variaveis utilizadas pelos timers (temporizadores)
	// Timer utilizado para fazer a barra de progresso
	Timer timer;
	// Se inicia quando o timer1 for finalizado (serve para efetuar o acesso)
	Timer timer2;
	// Timer puramente para o design, para dar mais fluidez ao programa
	Timer timer3;
	// variavel que diz o progresso da ProgressBar
	static int progresso = 0;

	// Componentes
	private static JPanel LoginContentPane;
	private static JTextField txtConta;
	private static JLabel lblConta;
	private static JLabel lblSenha;
	private static JPasswordField txtSenha;
	private static JButton btnSenha1;
	private static JButton btnSenha3;
	private static JButton btnSenha2;
	private static JButton btnSenha4;
	private static JButton btnSenha5;
	private static JButton btnApagar;
	private static JProgressBar ProgressBar;
	private static JLabel lblCriarConta;
	private static JLabel lblRecuperarSenha;
	private static JButton btnAcessar;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Acesso frame = new Acesso();
					frame.setVisible(true); // Inicia a janela
					frame.setLocationRelativeTo(null); // Deixa a janela centralizada
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "Ocorreu um erro: " + e.getMessage(), "ERRO",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
	}

	@SuppressWarnings("deprecation")
	public Acesso() {
		setSize((dimension.width / 2), (dimension.height - 50)); // Muda o tamanho da janela
		setResizable(false); // Bloqueia que o usuario modifique o tamanho da janela
		setAlwaysOnTop(false); // Obriga a janela ficar em primeiro plano sempre
		setTitle("Otaner Bank - Acesse sua Conta"); // Altera o titulo da janela
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Muda o que acontece ao clicar no botao "X" da janela
		setBounds(100, 100, (dimension.width / 2), (dimension.height - 50)); // Altera o tamanho e a posicao da janela

		// Painel que cont�m todos os componentes
		LoginContentPane = new JPanel();
		LoginContentPane.setForeground(Color.YELLOW);
		LoginContentPane.setBackground(new Color(255, 255, 255));
		LoginContentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		LoginContentPane.setLayout(null);
		setContentPane(LoginContentPane);

		// Texto "Bem Vindo ao Otaner Bank !"
		JLabel lblBemVindo = new JLabel("Bem Vindo ao Otaner Bank !");
		lblBemVindo.setFont(new Font("Felix Titling", Font.PLAIN, 30));
		lblBemVindo.setHorizontalAlignment(SwingConstants.CENTER);
		lblBemVindo.setBounds(10, 11, 657, 55);
		LoginContentPane.add(lblBemVindo);

		// Titulo do campo numero da conta
		lblConta = new JLabel("N�mero da Conta:");
		lblConta.setFont(new Font("Felix Titling", Font.PLAIN, 15));
		lblConta.setHorizontalAlignment(SwingConstants.CENTER);
		lblConta.setBounds(155, 94, 359, 25);
		LoginContentPane.add(lblConta);

		// Campo para digitar o numero da Conta
		txtConta = new JFormattedTextField(Mascara("####-#"));
		txtConta.setHorizontalAlignment(SwingConstants.CENTER);
		txtConta.setBounds(276, 120, 120, 25);
		txtConta.setColumns(10);
		LoginContentPane.add(txtConta);

		// Titulo do campo senha
		lblSenha = new JLabel("Senha:");
		lblSenha.setFont(new Font("Felix Titling", Font.PLAIN, 15));
		lblSenha.setHorizontalAlignment(SwingConstants.CENTER);
		lblSenha.setBounds(155, 183, 359, 25);
		LoginContentPane.add(lblSenha);

		// Campo para inserir a senha
		txtSenha = new JPasswordField(5);
		txtSenha.setHorizontalAlignment(SwingConstants.CENTER);
		txtSenha.setFont(new Font("Tahoma", Font.PLAIN, 99));
		txtSenha.setBackground(new Color(255, 255, 255));
		txtSenha.setBounds(155, 209, 359, 74);
		txtSenha.setEditable(false);
		LoginContentPane.add(txtSenha);

		// Adiciona os textos dos botoes de senha aleatoriamente para seguranca
		DefinirTextoDosBotoesDeSenha();

		// Botoes para o usuario poder "digitar" a senha
		// numero 1
		btnSenha1 = new JButton(strSenha1);
		btnSenha1.setBackground(Color.WHITE);
		btnSenha1.setCursor(cursor);
		btnSenha1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Registra os dois possiveis numero do botao clicado para a senha
				if (!logging) {
					if (txtSenha.getText().length() < 4) {
						txtSenha.setText(txtSenha.getText() + "X");
						ListaDeSenhasPossiveis.add(btnSenha1.getText().replace(" ou ", "|"));
					}

				}

			}
		});
		btnSenha1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnSenha1.setBounds(155, 328, 94, 74);
		
		LoginContentPane.add(btnSenha1);

		// numero 2
		btnSenha2 = new JButton(strSenha2);
		btnSenha2.setBackground(Color.WHITE);
		btnSenha2.setCursor(cursor);
		btnSenha2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Registra os dois possiveis numero do botao clicado para a senha
				if (!logging) {
					if (txtSenha.getText().length() < 4) {
						txtSenha.setText(txtSenha.getText() + "X");
						ListaDeSenhasPossiveis.add(btnSenha2.getText().replace(" ou ", "|"));
					}
				}

			}
		});
		btnSenha2.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnSenha2.setBounds(285, 328, 94, 74);
		
		LoginContentPane.add(btnSenha2);

		// numero 3
		btnSenha3 = new JButton(strSenha3);
		btnSenha3.setBackground(Color.WHITE);
		btnSenha3.setCursor(cursor);
		btnSenha3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Registra os dois possiveis numero do botao clicado para a senha
				if (!logging) {
					if (txtSenha.getText().length() < 4) {
						txtSenha.setText(txtSenha.getText() + "X");
						ListaDeSenhasPossiveis.add(btnSenha3.getText().replace(" ou ", "|"));
					}
				}

			}
		});
		btnSenha3.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnSenha3.setBounds(420, 328, 94, 74);
		LoginContentPane.add(btnSenha3);

		// numero 4
		btnSenha4 = new JButton(strSenha4);
		btnSenha4.setBackground(Color.WHITE);
		btnSenha4.setCursor(cursor);
		btnSenha4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Registra os dois possiveis numero do botao clicado para a senha
				if (!logging) {
					if (txtSenha.getText().length() < 4) {
						txtSenha.setText(txtSenha.getText() + "X");
						ListaDeSenhasPossiveis.add(btnSenha4.getText().replace(" ou ", "|"));
					}
				}

			}
		});
		btnSenha4.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnSenha4.setBounds(155, 425, 94, 74);
		LoginContentPane.add(btnSenha4);

		// numero 5
		btnSenha5 = new JButton(strSenha5);
		btnSenha5.setBackground(Color.WHITE);
		btnSenha5.setCursor(cursor);
		btnSenha5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Registra os dois possiveis numero do botao clicado para a senha
				if (!logging) {
					if (txtSenha.getText().length() < 4) {
						txtSenha.setText(txtSenha.getText() + "X");
						ListaDeSenhasPossiveis.add(btnSenha5.getText().replace(" ou ", "|"));
					}
				}

			}
		});
		btnSenha5.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnSenha5.setBounds(285, 425, 94, 74);
		LoginContentPane.add(btnSenha5);

		// Botao para apagar um numero da senha
		btnApagar = new JButton("APAGAR");
		btnApagar.setBackground(Color.WHITE);
		btnApagar.setCursor(cursor);
		btnApagar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Utilizado para apagar o ultimo digito da senha digitada
				if (!logging) {
					String strSenhaDigitada = txtSenha.getText();
					txtSenha.setText(DeletarUltimoDigitoDaSenha(strSenhaDigitada));
				}

			}
		});
		btnApagar.setFont(new Font("Tahoma", Font.PLAIN, 13));
		btnApagar.setBounds(420, 425, 94, 74);
		LoginContentPane.add(btnApagar);

		// Botao que redireciona para a tela de criacao de conta
		lblCriarConta = new JLabel("> CRIAR CONTA");
		lblCriarConta.setFont(new Font("Tahoma", Font.PLAIN, 12));
		// lblCriarConta.setFont(new Font("Felix Titling", Font.PLAIN, 12));
		lblCriarConta.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent arg0) {
				if (!logging) {
					LimparDados();
					CriarConta.main(null);
					dispose();
				}
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				Cursor cursor = new Cursor(Cursor.HAND_CURSOR);
				lblCriarConta.setCursor(cursor);
			}
		});
		lblCriarConta.setHorizontalAlignment(SwingConstants.CENTER);
		lblCriarConta.setForeground(Color.BLUE);
		lblCriarConta.setBounds(247, 583, 173, 14);
		LoginContentPane.add(lblCriarConta);

		// Botao que redireciona para a tela de recuperacao de senha
		lblRecuperarSenha = new JLabel("> ESQUECI MEUS DADOS");
		// lblRecuperarSenha.setFont(new Font("Felix Titling", Font.PLAIN, 12));
		lblRecuperarSenha.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent arg0) {
				if (!logging) {
					LimparDados();
					RecuperarSenha.main(null);
					dispose();
				}
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				Cursor cursor = new Cursor(Cursor.HAND_CURSOR);
				lblRecuperarSenha.setCursor(cursor);
			}
		});
		lblRecuperarSenha.setHorizontalAlignment(SwingConstants.CENTER);
		lblRecuperarSenha.setForeground(Color.BLUE);
		lblRecuperarSenha.setBounds(247, 608, 173, 14);
		lblRecuperarSenha.setEnabled(false);
		lblRecuperarSenha.setVisible(false);
		LoginContentPane.add(lblRecuperarSenha);

		// Progress Bar para deixar o layout mais fluido
		ProgressBar = new JProgressBar();
		ProgressBar.setBounds(-5, 675, dimension.width+5, 15);
		ProgressBar.setForeground(Color.GREEN);
		ProgressBar.setValue(0);
		ProgressBar.setStringPainted(false);
		ProgressBar.setMaximum(100);
		ProgressBar.setMinimum(0);
		LoginContentPane.add(ProgressBar);

		// Botao para verificar as credencias e acessar a conta
		btnAcessar = new JButton("ACESSAR CONTA");
		btnAcessar.setBackground(Color.WHITE);
		btnAcessar.setCursor(cursor);
		btnAcessar.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				// Esse botao verifica a senha e a conta para efetuar login
				if (!logging) {
					if (txtSenha.getText().length() < 4 || txtConta.getText().length() < 6
							|| txtConta.getText().contains(" ")) {
						JOptionPane.showMessageDialog(null, "Preencha todos os campos para continuar, valeu !", "AVISO",
								JOptionPane.WARNING_MESSAGE);
					} else {
						logging = true;
						IniciarProgressBar();
					}
				}
			}

		});
		// btnAcessar.setFont(new Font("Felix Titling", Font.PLAIN, 11));
		btnAcessar.setBounds(247, 539, 173, 33);
		LoginContentPane.add(btnAcessar);

	}

	// Cria aleatoriamente os pares para que o usuario digite a senha
	private static void DefinirTextoDosBotoesDeSenha() {
		try {
			Random random = new Random();
			int num1 = 0, num2 = 0;

			senhas_1 = new ArrayList<Integer>();
			senhas_2 = new ArrayList<Integer>();
			senhas_3 = new ArrayList<Integer>();
			senhas_4 = new ArrayList<Integer>();
			senhas_5 = new ArrayList<Integer>();

			for (int i = 0; i <= 5;) {

				num1 = random.nextInt(10);
				num2 = random.nextInt(10);

				if (i == 0) {
					if (num1 != num2) {
						senhas_1.add(num1);
						senhas_1.add(num2);
						i++;
					}
				}

				else if (i == 1) {
					if (num1 != num2 && !senhas_1.contains(num1) && !senhas_1.contains(num2)) {
						senhas_2.add(num1);
						senhas_2.add(num2);
						i++;
					}
				}

				else if (i == 2) {
					if (num1 != num2 && !senhas_2.contains(num1) && !senhas_2.contains(num2) && !senhas_1.contains(num1)
							&& !senhas_1.contains(num2)) {
						senhas_3.add(num1);
						senhas_3.add(num2);
						i++;
					}
				}

				else if (i == 3) {
					if (num1 != num2 && !senhas_3.contains(num1) && !senhas_3.contains(num2) && !senhas_2.contains(num1)
							&& !senhas_2.contains(num2) && !senhas_1.contains(num1) && !senhas_1.contains(num2)) {
						senhas_4.add(num1);
						senhas_4.add(num2);
						i++;
					}
				}

				else if (i == 4) {
					if (num1 != num2 && !senhas_4.contains(num1) && !senhas_4.contains(num2) && !senhas_3.contains(num1)
							&& !senhas_3.contains(num2) && !senhas_2.contains(num1) && !senhas_2.contains(num2)
							&& !senhas_1.contains(num1) && !senhas_1.contains(num2)) {
						senhas_5.add(num1);
						senhas_5.add(num2);
						i++;
					}
				}

				else if (i == 5) {
					strSenha1 = senhas_1.get(0) + " ou " + senhas_1.get(1);
					strSenha2 = senhas_2.get(0) + " ou " + senhas_2.get(1);
					strSenha3 = senhas_3.get(0) + " ou " + senhas_3.get(1);
					strSenha4 = senhas_4.get(0) + " ou " + senhas_4.get(1);
					strSenha5 = senhas_5.get(0) + " ou " + senhas_5.get(1);

					i++;
				}

			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Ocorreu um erro: " + e.getMessage(), "ERRO",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	// Define os padroes possiveis de senha com base no que o usuario digitou
	private static void DefinirPossiveisPadroesDeSenha() {
		try {
			for (int i = 0, j = 0; i < 16;) {

				if (i < 8) {

					senhasPossiveis[i] = (ListaDeSenhasPossiveis.get(0).substring(0,
							ListaDeSenhasPossiveis.get(0).indexOf("|")));

					if (i < 4) {
						senhasPossiveis[i] += (ListaDeSenhasPossiveis.get(1).substring(0,
								ListaDeSenhasPossiveis.get(1).indexOf("|")));
					} else {
						senhasPossiveis[i] += (ListaDeSenhasPossiveis.get(1)
								.substring(ListaDeSenhasPossiveis.get(1).indexOf("|")).replace("|", ""));
					}

					if (j < 2) {
						senhasPossiveis[i] += (ListaDeSenhasPossiveis.get(2).substring(0,
								ListaDeSenhasPossiveis.get(1).indexOf("|")));
						j++;
					} else {
						senhasPossiveis[i] += (ListaDeSenhasPossiveis.get(2)
								.substring(ListaDeSenhasPossiveis.get(1).indexOf("|")).replace("|", ""));
						if (i % 2 != 0)
							j = 0;
					}

					if (i % 2 == 0) {
						senhasPossiveis[i] += (ListaDeSenhasPossiveis.get(3).substring(0,
								ListaDeSenhasPossiveis.get(1).indexOf("|")));
					} else {
						senhasPossiveis[i] += (ListaDeSenhasPossiveis.get(3)
								.substring(ListaDeSenhasPossiveis.get(1).indexOf("|")).replace("|", ""));
					}

					i++;

				} else {

					senhasPossiveis[i] = ((ListaDeSenhasPossiveis.get(0)
							.substring(ListaDeSenhasPossiveis.get(0).indexOf("|"))).replace("|", ""));

					if (i < 12) {
						senhasPossiveis[i] += (ListaDeSenhasPossiveis.get(1).substring(0,
								ListaDeSenhasPossiveis.get(1).indexOf("|")));
					} else {
						senhasPossiveis[i] += (ListaDeSenhasPossiveis.get(1)
								.substring(ListaDeSenhasPossiveis.get(1).indexOf("|")).replace("|", ""));
					}

					if (j < 2) {
						senhasPossiveis[i] += (ListaDeSenhasPossiveis.get(2).substring(0,
								ListaDeSenhasPossiveis.get(1).indexOf("|")));
						j++;
					} else {
						senhasPossiveis[i] += (ListaDeSenhasPossiveis.get(2)
								.substring(ListaDeSenhasPossiveis.get(1).indexOf("|")).replace("|", ""));
						if (i % 2 != 0)
							j = 0;
					}

					if (i % 2 == 0) {
						senhasPossiveis[i] += (ListaDeSenhasPossiveis.get(3).substring(0,
								ListaDeSenhasPossiveis.get(1).indexOf("|")));
					} else {
						senhasPossiveis[i] += (ListaDeSenhasPossiveis.get(3)
								.substring(ListaDeSenhasPossiveis.get(1).indexOf("|")).replace("|", ""));
					}

					i++;

				}

			}

			/*
			 * System.out.println("Possibilidades:\n" + ListaDeSenhasPossiveis + "\n\n");
			 * for (int j = 0; j < senhasPossiveis.length; j++) { if (j == 8)
			 * System.out.println(); System.out.println(senhasPossiveis[j]); }
			 */

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Ocorreu um erro: " + e.getMessage(), "ERRO",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	// Isso � um botao de deletar, que vai deletando numero por numero, um de cada
	// vez no campo senha
	private static String DeletarUltimoDigitoDaSenha(String strSenhaDigitada) {
		try {

			String strRetornoParaOCampoSenha = "";

			if (strSenhaDigitada.length() == 0) {

			} else if (strSenhaDigitada.length() == 1) {
				strRetornoParaOCampoSenha = "";
			} else if (strSenhaDigitada.length() == 2) {
				strRetornoParaOCampoSenha = ("" + strSenhaDigitada.charAt(0));
			} else if (strSenhaDigitada.length() == 3) {
				strRetornoParaOCampoSenha = ("" + strSenhaDigitada.charAt(0) + strSenhaDigitada.charAt(1));
			} else if (strSenhaDigitada.length() == 4) {
				strRetornoParaOCampoSenha = ("" + strSenhaDigitada.charAt(0) + strSenhaDigitada.charAt(1)
						+ strSenhaDigitada.charAt(2));
			}

			if (ListaDeSenhasPossiveis.size() == 0) {

			} else if (ListaDeSenhasPossiveis.size() == 1) {
				ListaDeSenhasPossiveis.remove(0);
			} else if (ListaDeSenhasPossiveis.size() == 2) {
				ListaDeSenhasPossiveis.remove(1);
			} else if (ListaDeSenhasPossiveis.size() == 3) {
				ListaDeSenhasPossiveis.remove(2);
			} else if (ListaDeSenhasPossiveis.size() == 4) {
				ListaDeSenhasPossiveis.remove(3);
			}

			return strRetornoParaOCampoSenha;

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Ocorreu um erro: " + e.getMessage(), "ERRO",
					JOptionPane.ERROR_MESSAGE);
			return "";
		}
	}

	// Metodo para criar mascaras nos campos
	public static MaskFormatter Mascara(String Mascara) {

		MaskFormatter F_Mascara = new MaskFormatter();
		try {
			F_Mascara.setMask(Mascara); // Atribui a mascara
			F_Mascara.setPlaceholderCharacter(' '); // Caracter para preencimento
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Ocorreu um erro: " + e.getMessage(), "ERRO",
					JOptionPane.ERROR_MESSAGE);
		}
		return F_Mascara;
	}

	// Limpa os dados do cliente para seguran�a
	private static void LimparDados() {
		try {
			txtSenha.setText("");
			ListaDeSenhasPossiveis.clear();
			DefinirTextoDosBotoesDeSenha(); // Adiciona os textos dos botoes de senha aleatoriamente para seguranca
			btnSenha1.setText(strSenha1);
			btnSenha2.setText(strSenha2);
			btnSenha3.setText(strSenha3);
			btnSenha4.setText(strSenha4);
			btnSenha5.setText(strSenha5);
			txtConta.setText("");
			progresso = 0;
			ProgressBar.setValue(0);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Ocorreu um erro: " + e.getMessage(), "ERRO",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	// Metodo responsavel por iniciar a progressBar e leva ao "acesso permito" ou
	// "acesso negado"
	private void IniciarProgressBar() {
		try {

			timer = new Timer((10), AtualizarProgressBar);
			timer.start();

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Ocorreu um erro: " + e.getMessage(), "ERRO",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	// Faz o movimento da progressBar
	ActionListener AtualizarProgressBar = new ActionListener() {
		public void actionPerformed(ActionEvent evt) {
			try {
				progresso++;
				ProgressBar.setValue(progresso);
				if (progresso == 100) {
					timer.stop();
					timer2 = new Timer((1), ValidarAcesso);
					timer2.start();
				}
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "Ocorreu um erro: " + e.getMessage(), "ERRO",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	};

	// Vem ap�s o movimento da progressBar para validar os dados
	ActionListener ValidarAcesso = new ActionListener() {
		public void actionPerformed(ActionEvent evt) {
			try {
				DefinirPossiveisPadroesDeSenha();
				if (control.acessarConta(txtConta.getText(), senhasPossiveis)) {

					timer3 = new Timer((1000), IniciarMenuPrincipal);
					timer3.setRepeats(false);
					timer3.start();

				} else {
					LimparDados();
					logging = false;
					
					JOptionPane.showMessageDialog(null, "Acesso negado", "AVISO", JOptionPane.WARNING_MESSAGE);
				}
				timer2.stop();
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "Ocorreu um erro: " + e.getMessage(), "ERRO",
						JOptionPane.ERROR_MESSAGE);
			}

		}
	};

	ActionListener IniciarMenuPrincipal = new ActionListener() {
		public void actionPerformed(ActionEvent evt) {
			try {
				dispose();
				Principal.main(null, txtConta.getText());

				senhasPossiveis = null;
				LimparDados();

				timer3.stop();
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "Ocorreu um erro: " + e.getMessage(), "ERRO",
						JOptionPane.ERROR_MESSAGE);
			}

		}
	};

}

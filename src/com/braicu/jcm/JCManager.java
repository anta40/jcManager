/*
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
package com.braicu.jcm;
import com.braicu.jcm.card.CardWorker;
import com.braicu.jcm.card.cap.utils.CapFilter;
import com.braicu.jcm.layout.JTextAreaOutputStream;
import com.braicu.jcm.layout.MySwingWorker;
import com.braicu.jcm.utils.ByteUtils;
import com.braicu.jcm.utils.Settings;
import com.cloudgarden.layout.AnchorConstraint;
import com.cloudgarden.layout.AnchorLayout;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.PrintStream;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


public class JCManager extends javax.swing.JFrame {

	{
		//Set Look & Feel
		try {
			javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
		} catch(Exception e) {
			e.printStackTrace();
		}
	}


	private JMenuItem aboutMenuItem;
	private JMenu jMenu5;
	private JFormattedTextField tfOldKey3;
	private JLabel lbOldKey2;
	private JFormattedTextField tfOldKey2;
	private JLabel lbOldKey1;
	private JFormattedTextField tfOldKey1;
	private JPanel pTwo;
	private JPanel pOne;
	private JTabbedPane tpMain;
	private JMenu jMenu4;
	private JMenuItem exitMenuItem;
	private JLabel lbCMAID;
	private ButtonGroup bgKeys;
	private JCheckBoxMenuItem debugMenuItem;
	private JLabel lbNotYet;
	private JButton btResetCard;
	private JButton btGetInfo;
	private JButton btAuthenticate;
	private JButton btUpload;
	private JTextField tfInstallParameters;
	private JLabel labelParamInstall;
	private JTextField tfFile;
	private JButton btFile;
	private JPanel panelCAP;
	private JButton btAddKeys;
	private JRadioButton bgModifyKeys;
	private JRadioButton rbAddKeys;
	private JLabel lbKeyset;
	private JTextField tfKeyset;
	private JPanel panelOPKeys;
	private JFormattedTextField tfCMAID;
	private JButton btRefresh;
	private JComboBox cbReader;
	private JLabel lbReader;
	private JLabel lbNewKey1;
	private JFormattedTextField tfNewKey1;
	private JLabel lbNewKey2;
	private JFormattedTextField tfNewKey2;
	private JLabel lbNewKey3;
	private JFormattedTextField tfNewKey3;
	private JPanel panelNewKeys;
	private JPanel panelAuthKeys;
	private JLabel lbOldKey3;
	private JMenu jMenu3;
	private JMenuBar jMenuBar1;

	
	//bb
    private CardWorker cw;
    private JTextAreaOutputStream jtaos;
    private JTextArea textArea;
    private JScrollPane jsp;
    private JFileChooser fc;
    private File f;
    private JPanel panel;
    private JPanel panel_1;
    private JPanel panel_2;

	
	/**
	* Auto-generated main method to display this JFrame
	*/
	public static void main(String[] args) {
		Settings.initContext();
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				JCManager inst = new JCManager();
				inst.setLocationRelativeTo(null);
				inst.setVisible(true);
				inst.setPreferredSize(new java.awt.Dimension(0, 0));
				inst.setSize(681, 432);
			}
		});
	}
	
	public JCManager() {
		super();
		initGUI();
	}
	
	private void initGUI() {
		try {
			{
				this.setTitle("jcManager");
				{
					tpMain = new JTabbedPane();
					getContentPane().add(tpMain, BorderLayout.NORTH);
					//bb
					{
						pOne = new JPanel();
						tpMain.addTab("Manage Card", null, pOne, null);
						GridBagLayout gbl_pOne = new GridBagLayout();
						gbl_pOne.columnWidths = new int[]{0, 0, 0};
						gbl_pOne.rowHeights = new int[]{0, 0, 0, 0, 0};
						gbl_pOne.columnWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
						gbl_pOne.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
						pOne.setLayout(gbl_pOne);
						{
							panelAuthKeys = new JPanel();
							GridBagConstraints gbc_panelAuthKeys = new GridBagConstraints();
							gbc_panelAuthKeys.fill = GridBagConstraints.BOTH;
							gbc_panelAuthKeys.insets = new Insets(0, 0, 5, 5);
							gbc_panelAuthKeys.gridx = 0;
							gbc_panelAuthKeys.gridy = 0;
							pOne.add(panelAuthKeys, gbc_panelAuthKeys);
							panelAuthKeys.setBorder(BorderFactory.createTitledBorder("Authentication Keys"));
							panelAuthKeys.setOpaque(false);
							GridBagLayout gbl_panelAuthKeys = new GridBagLayout();
							gbl_panelAuthKeys.columnWidths = new int[]{0, 0, 0};
							gbl_panelAuthKeys.rowHeights = new int[]{0, 0, 0, 0};
							gbl_panelAuthKeys.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
							gbl_panelAuthKeys.rowWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
							panelAuthKeys.setLayout(gbl_panelAuthKeys);
							{
								lbOldKey1 = new JLabel();
								GridBagConstraints gbc_lbOldKey1 = new GridBagConstraints();
								gbc_lbOldKey1.anchor = GridBagConstraints.EAST;
								gbc_lbOldKey1.insets = new Insets(0, 0, 5, 5);
								gbc_lbOldKey1.gridx = 0;
								gbc_lbOldKey1.gridy = 0;
								panelAuthKeys.add(lbOldKey1, gbc_lbOldKey1);
								lbOldKey1.setText("Key 1 (S_ENC)");
							}
							{
								tfOldKey1 = new JFormattedTextField();
								GridBagConstraints gbc_tfOldKey1 = new GridBagConstraints();
								gbc_tfOldKey1.fill = GridBagConstraints.BOTH;
								gbc_tfOldKey1.insets = new Insets(0, 0, 5, 0);
								gbc_tfOldKey1.gridx = 1;
								gbc_tfOldKey1.gridy = 0;
								panelAuthKeys.add(tfOldKey1, gbc_tfOldKey1);
								tfOldKey1.addFocusListener(new FocusAdapter() {
									public void focusLost(FocusEvent evt) {
										tfOldKey1FocusLost(evt);
									}
								});
							}
							
							//bb
							
							tfOldKey1.setText(Settings.props.getProperty("AuthKey1"));
							{
								lbOldKey2 = new JLabel();
								GridBagConstraints gbc_lbOldKey2 = new GridBagConstraints();
								gbc_lbOldKey2.anchor = GridBagConstraints.EAST;
								gbc_lbOldKey2.insets = new Insets(0, 0, 5, 5);
								gbc_lbOldKey2.gridx = 0;
								gbc_lbOldKey2.gridy = 1;
								panelAuthKeys.add(lbOldKey2, gbc_lbOldKey2);
								lbOldKey2.setText("Key 2 (S_MAC)");
							}
							{
								tfOldKey2 = new JFormattedTextField();
								GridBagConstraints gbc_tfOldKey2 = new GridBagConstraints();
								gbc_tfOldKey2.fill = GridBagConstraints.BOTH;
								gbc_tfOldKey2.insets = new Insets(0, 0, 5, 0);
								gbc_tfOldKey2.gridx = 1;
								gbc_tfOldKey2.gridy = 1;
								panelAuthKeys.add(tfOldKey2, gbc_tfOldKey2);
								tfOldKey2.addFocusListener(new FocusAdapter() {
									public void focusLost(FocusEvent evt) {
										tfOldKey2FocusLost(evt);
									}
								});
							}
							tfOldKey2.setText(Settings.props.getProperty("AuthKey2"));
							{
								lbOldKey3 = new JLabel();
								GridBagConstraints gbc_lbOldKey3 = new GridBagConstraints();
								gbc_lbOldKey3.anchor = GridBagConstraints.EAST;
								gbc_lbOldKey3.insets = new Insets(0, 0, 0, 5);
								gbc_lbOldKey3.gridx = 0;
								gbc_lbOldKey3.gridy = 2;
								panelAuthKeys.add(lbOldKey3, gbc_lbOldKey3);
								lbOldKey3.setText("Key 3 (DEK)");
							}
							{
								tfOldKey3 = new JFormattedTextField();
								GridBagConstraints gbc_tfOldKey3 = new GridBagConstraints();
								gbc_tfOldKey3.fill = GridBagConstraints.BOTH;
								gbc_tfOldKey3.gridx = 1;
								gbc_tfOldKey3.gridy = 2;
								panelAuthKeys.add(tfOldKey3, gbc_tfOldKey3);
								tfOldKey3.addFocusListener(new FocusAdapter() {
									public void focusLost(FocusEvent evt) {
										tfOldKey3FocusLost(evt);
									}
								});
							}
							tfOldKey3.setText(Settings.props.getProperty("AuthKey3"));
						}
						{
							panelNewKeys = new JPanel();
							GridBagConstraints gbc_panelNewKeys = new GridBagConstraints();
							gbc_panelNewKeys.fill = GridBagConstraints.BOTH;
							gbc_panelNewKeys.insets = new Insets(0, 0, 5, 0);
							gbc_panelNewKeys.gridx = 1;
							gbc_panelNewKeys.gridy = 0;
							pOne.add(panelNewKeys, gbc_panelNewKeys);
							panelNewKeys.setOpaque(false);
							panelNewKeys.setBorder(BorderFactory.createTitledBorder("New Keys"));
							GridBagLayout gbl_panelNewKeys = new GridBagLayout();
							gbl_panelNewKeys.columnWidths = new int[]{0, 0, 0};
							gbl_panelNewKeys.rowHeights = new int[]{0, 0, 0, 0};
							gbl_panelNewKeys.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
							gbl_panelNewKeys.rowWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
							panelNewKeys.setLayout(gbl_panelNewKeys);
							{
								lbNewKey1 = new JLabel();
								GridBagConstraints gbc_lbNewKey1 = new GridBagConstraints();
								gbc_lbNewKey1.anchor = GridBagConstraints.EAST;
								gbc_lbNewKey1.insets = new Insets(0, 0, 5, 5);
								gbc_lbNewKey1.gridx = 0;
								gbc_lbNewKey1.gridy = 0;
								panelNewKeys.add(lbNewKey1, gbc_lbNewKey1);
								lbNewKey1.setText("Key 1 (S_ENC)");
							}
							{
								tfNewKey1 = new JFormattedTextField();
								GridBagConstraints gbc_tfNewKey1 = new GridBagConstraints();
								gbc_tfNewKey1.fill = GridBagConstraints.BOTH;
								gbc_tfNewKey1.insets = new Insets(0, 0, 5, 0);
								gbc_tfNewKey1.gridx = 1;
								gbc_tfNewKey1.gridy = 0;
								panelNewKeys.add(tfNewKey1, gbc_tfNewKey1);
								tfNewKey1.addFocusListener(new FocusAdapter() {
									public void focusLost(FocusEvent evt) {
										tfNewKey1FocusLost(evt);
									}
								});
							}
							tfNewKey1.setText(Settings.props.getProperty("NewKey1"));
							{
								lbNewKey2 = new JLabel();
								GridBagConstraints gbc_lbNewKey2 = new GridBagConstraints();
								gbc_lbNewKey2.anchor = GridBagConstraints.EAST;
								gbc_lbNewKey2.insets = new Insets(0, 0, 5, 5);
								gbc_lbNewKey2.gridx = 0;
								gbc_lbNewKey2.gridy = 1;
								panelNewKeys.add(lbNewKey2, gbc_lbNewKey2);
								lbNewKey2.setText("Key 2 (S_MAC)");
							}
							{
								tfNewKey2 = new JFormattedTextField();
								GridBagConstraints gbc_tfNewKey2 = new GridBagConstraints();
								gbc_tfNewKey2.fill = GridBagConstraints.BOTH;
								gbc_tfNewKey2.insets = new Insets(0, 0, 5, 0);
								gbc_tfNewKey2.gridx = 1;
								gbc_tfNewKey2.gridy = 1;
								panelNewKeys.add(tfNewKey2, gbc_tfNewKey2);
								tfNewKey2.addFocusListener(new FocusAdapter() {
									public void focusLost(FocusEvent evt) {
										tfNewKey2FocusLost(evt);
									}
								});
							}
							tfNewKey2.setText(Settings.props.getProperty("NewKey2"));
							{
								lbNewKey3 = new JLabel();
								GridBagConstraints gbc_lbNewKey3 = new GridBagConstraints();
								gbc_lbNewKey3.anchor = GridBagConstraints.EAST;
								gbc_lbNewKey3.insets = new Insets(0, 0, 0, 5);
								gbc_lbNewKey3.gridx = 0;
								gbc_lbNewKey3.gridy = 2;
								panelNewKeys.add(lbNewKey3, gbc_lbNewKey3);
								lbNewKey3.setText("Key 3 (DEK)");
							}
							{
								tfNewKey3 = new JFormattedTextField();
								GridBagConstraints gbc_tfNewKey3 = new GridBagConstraints();
								gbc_tfNewKey3.fill = GridBagConstraints.BOTH;
								gbc_tfNewKey3.gridx = 1;
								gbc_tfNewKey3.gridy = 2;
								panelNewKeys.add(tfNewKey3, gbc_tfNewKey3);
								tfNewKey3.addFocusListener(new FocusAdapter() {
									public void focusLost(FocusEvent evt) {
										tfNewKey3FocusLost(evt);
									}
								});
							}
							tfNewKey3.setText(Settings.props.getProperty("NewKey3"));
						}
						ComboBoxModel cbReaderModel = 
								new DefaultComboBoxModel(
										new String[] { "Item One", "Item Two" });
						{
							panel_1 = new JPanel();
							GridBagConstraints gbc_panel_1 = new GridBagConstraints();
							gbc_panel_1.insets = new Insets(0, 0, 5, 5);
							gbc_panel_1.fill = GridBagConstraints.BOTH;
							gbc_panel_1.gridx = 0;
							gbc_panel_1.gridy = 1;
							pOne.add(panel_1, gbc_panel_1);
							GridBagLayout gbl_panel_1 = new GridBagLayout();
							gbl_panel_1.columnWidths = new int[]{0, 0, 0, 0};
							gbl_panel_1.rowHeights = new int[]{0, 0};
							gbl_panel_1.columnWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
							gbl_panel_1.rowWeights = new double[]{0.0, Double.MIN_VALUE};
							panel_1.setLayout(gbl_panel_1);
							{
								lbReader = new JLabel();
								GridBagConstraints gbc_lbReader = new GridBagConstraints();
								gbc_lbReader.anchor = GridBagConstraints.EAST;
								gbc_lbReader.insets = new Insets(0, 0, 0, 5);
								gbc_lbReader.gridx = 0;
								gbc_lbReader.gridy = 0;
								panel_1.add(lbReader, gbc_lbReader);
								lbReader.setText("Select Card Reader");
							}
							cbReader = new JComboBox();
							GridBagConstraints gbc_cbReader = new GridBagConstraints();
							gbc_cbReader.fill = GridBagConstraints.HORIZONTAL;
							gbc_cbReader.insets = new Insets(0, 0, 0, 5);
							gbc_cbReader.gridx = 1;
							gbc_cbReader.gridy = 0;
							panel_1.add(cbReader, gbc_cbReader);
							cbReader.setModel(cbReaderModel);
							{
								btRefresh = new JButton();
								GridBagConstraints gbc_btRefresh = new GridBagConstraints();
								gbc_btRefresh.gridx = 2;
								gbc_btRefresh.gridy = 0;
								panel_1.add(btRefresh, gbc_btRefresh);
								btRefresh.setBorderPainted(false);
								btRefresh.setIcon(new ImageIcon(getClass().getClassLoader().getResource("img/Refresh16.gif")));
								btRefresh.addActionListener(new ActionListener() {
									public void actionPerformed(ActionEvent evt) {
										btRefreshActionPerformed(evt);
									}
								});
							}
							cbReader.addItemListener(new ItemListener() {
								public void itemStateChanged(ItemEvent evt) {
									cbReaderItemStateChanged(evt);
								}
							});
						}
						{
							panel_2 = new JPanel();
							GridBagConstraints gbc_panel_2 = new GridBagConstraints();
							gbc_panel_2.insets = new Insets(0, 0, 5, 0);
							gbc_panel_2.fill = GridBagConstraints.BOTH;
							gbc_panel_2.gridx = 1;
							gbc_panel_2.gridy = 1;
							pOne.add(panel_2, gbc_panel_2);
							GridBagLayout gbl_panel_2 = new GridBagLayout();
							gbl_panel_2.columnWidths = new int[]{0, 0, 0};
							gbl_panel_2.rowHeights = new int[]{0, 0};
							gbl_panel_2.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
							gbl_panel_2.rowWeights = new double[]{0.0, Double.MIN_VALUE};
							panel_2.setLayout(gbl_panel_2);
							{
								lbCMAID = new JLabel();
								GridBagConstraints gbc_lbCMAID = new GridBagConstraints();
								gbc_lbCMAID.anchor = GridBagConstraints.EAST;
								gbc_lbCMAID.insets = new Insets(0, 0, 0, 5);
								gbc_lbCMAID.gridx = 0;
								gbc_lbCMAID.gridy = 0;
								panel_2.add(lbCMAID, gbc_lbCMAID);
								lbCMAID.setText("CardManager AID");
							}
							{
								tfCMAID = new JFormattedTextField();
								GridBagConstraints gbc_tfCMAID = new GridBagConstraints();
								gbc_tfCMAID.fill = GridBagConstraints.HORIZONTAL;
								gbc_tfCMAID.gridx = 1;
								gbc_tfCMAID.gridy = 0;
								panel_2.add(tfCMAID, gbc_tfCMAID);
								tfCMAID.addFocusListener(new FocusAdapter() {
									public void focusLost(FocusEvent evt) {
										tfCMAIDFocusLost(evt);
									}
								});
							}
							tfCMAID.setText(Settings.props.getProperty("CMAID"));
						}
						panelOPKeys = new JPanel();
						GridBagConstraints gbc_panelOPKeys = new GridBagConstraints();
						gbc_panelOPKeys.fill = GridBagConstraints.BOTH;
						gbc_panelOPKeys.insets = new Insets(0, 0, 5, 5);
						gbc_panelOPKeys.gridx = 0;
						gbc_panelOPKeys.gridy = 2;
						pOne.add(panelOPKeys, gbc_panelOPKeys);
						panelOPKeys.setBorder(BorderFactory.createTitledBorder("Manage keys"));
						GridBagLayout gbl_panelOPKeys = new GridBagLayout();
						gbl_panelOPKeys.columnWidths = new int[]{0, 0, 0, 0, 0};
						gbl_panelOPKeys.rowHeights = new int[]{0, 0, 0};
						gbl_panelOPKeys.columnWeights = new double[]{0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE};
						gbl_panelOPKeys.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
						panelOPKeys.setLayout(gbl_panelOPKeys);
						{
							lbKeyset = new JLabel();
							GridBagConstraints gbc_lbKeyset = new GridBagConstraints();
							gbc_lbKeyset.fill = GridBagConstraints.HORIZONTAL;
							gbc_lbKeyset.insets = new Insets(0, 0, 0, 5);
							gbc_lbKeyset.gridx = 0;
							gbc_lbKeyset.gridy = 0;
							panelOPKeys.add(lbKeyset, gbc_lbKeyset);
							lbKeyset.setText("Keyset");
						}
						{
							tfKeyset = new JTextField();
							GridBagConstraints gbc_tfKeyset = new GridBagConstraints();
							gbc_tfKeyset.fill = GridBagConstraints.HORIZONTAL;
							gbc_tfKeyset.insets = new Insets(0, 0, 0, 5);
							gbc_tfKeyset.gridx = 1;
							gbc_tfKeyset.gridy = 0;
							panelOPKeys.add(tfKeyset, gbc_tfKeyset);
							tfKeyset.addFocusListener(new FocusAdapter() {
								public void focusLost(FocusEvent evt) {
									tfKeysetFocusLost(evt);
								}
							});
						}
						
						tfKeyset.setText(Settings.props.getProperty("KeySet"));
						{
							rbAddKeys = new JRadioButton();
							GridBagConstraints gbc_rbAddKeys = new GridBagConstraints();
							gbc_rbAddKeys.anchor = GridBagConstraints.WEST;
							gbc_rbAddKeys.insets = new Insets(0, 0, 0, 5);
							gbc_rbAddKeys.gridx = 2;
							gbc_rbAddKeys.gridy = 0;
							panelOPKeys.add(rbAddKeys, gbc_rbAddKeys);
							rbAddKeys.setText("Add");
							getBgKeys().add(rbAddKeys);
						}
						{
							GridBagConstraints gbc_btAddKeys = new GridBagConstraints();
							gbc_btAddKeys.insets = new Insets(0, 0, 0, 0);
							gbc_btAddKeys.fill = GridBagConstraints.HORIZONTAL;
							gbc_btAddKeys.gridx = 3;
							gbc_btAddKeys.gridy = 0;
							panelOPKeys.add(getBtAddKeys(), gbc_btAddKeys);
						}
						bgModifyKeys = new JRadioButton();
						GridBagConstraints gbc_bgModifyKeys = new GridBagConstraints();
						gbc_bgModifyKeys.anchor = GridBagConstraints.WEST;
						gbc_bgModifyKeys.insets = new Insets(0, 0, 0, 5);
						gbc_bgModifyKeys.gridx = 2;
						gbc_bgModifyKeys.gridy = 1;
						panelOPKeys.add(bgModifyKeys, gbc_bgModifyKeys);
						bgModifyKeys.setText("Modify");
						getBgKeys().add(bgModifyKeys);
						bgModifyKeys.setSelected(true);
						GridBagConstraints gbc_panelCAP = new GridBagConstraints();
						gbc_panelCAP.insets = new Insets(0, 0, 5, 0);
						gbc_panelCAP.fill = GridBagConstraints.BOTH;
						gbc_panelCAP.gridx = 1;
						gbc_panelCAP.gridy = 2;
						pOne.add(getPanelCAP(), gbc_panelCAP);
					}
					{
						pTwo = new JPanel();
						BorderLayout pTwoLayout = new BorderLayout();
						pTwo.setLayout(pTwoLayout);
						tpMain.addTab("Test APDU", null, pTwo, null);
						pTwo.setEnabled(false);
						pTwo.add(getLbNotYet(), BorderLayout.CENTER);
					}
				}
				this.addWindowListener(new WindowAdapter() {
					public void windowClosing(WindowEvent evt) {
						thisWindowClosing(evt);
					}
					public void windowClosed(WindowEvent evt) {
						thisWindowClosed(evt);
					}
				});
			}
			{
				jMenuBar1 = new JMenuBar();
				setJMenuBar(jMenuBar1);
				{
					jMenu3 = new JMenu();
					jMenuBar1.add(jMenu3);
					jMenu3.setText("File");
					{
						exitMenuItem = new JMenuItem();
						jMenu3.add(exitMenuItem);
						exitMenuItem.setText("Exit");
						exitMenuItem.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								exitMenuItemActionPerformed(evt);
							}
						});
					}
				}
				{
					jMenu4 = new JMenu();
					jMenuBar1.add(jMenu4);
					jMenu4.setText("Display");
					jMenu4.add(getDebugMenuItem());
				}
				{
					jMenu5 = new JMenu();
					jMenuBar1.add(jMenu5);
					jMenu5.setText("Help");
					{
						aboutMenuItem = new JMenuItem();
						jMenu5.add(aboutMenuItem);
						aboutMenuItem.setText("About");
						aboutMenuItem.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								aboutMenuItemActionPerformed(evt);
							}
						});
					}
				}
			}

			textArea = new JTextArea();
			jtaos = new JTextAreaOutputStream(textArea);
	     	PrintStream ps = new PrintStream(jtaos);
	    	System.setOut(ps);
	    	System.setErr(ps);
			
			jsp = new JScrollPane(textArea);
			getContentPane().add(jsp, BorderLayout.CENTER);

			
			
			pack();
			pack();
		} catch (Exception e) {
			e.printStackTrace();
		}
		tfInstallParameters.setText(Settings.props.getProperty("InstallParameters"));
		{
			panel = new JPanel();
			GridBagConstraints gbc_panel = new GridBagConstraints();
			gbc_panel.gridwidth = 2;
			gbc_panel.fill = GridBagConstraints.BOTH;
			gbc_panel.gridx = 0;
			gbc_panel.gridy = 3;
			pOne.add(panel, gbc_panel);
			GridBagLayout gbl_panel = new GridBagLayout();
			gbl_panel.columnWidths = new int[]{0, 0, 0, 0};
			gbl_panel.rowHeights = new int[]{0, 0};
			gbl_panel.columnWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
			gbl_panel.rowWeights = new double[]{0.0, Double.MIN_VALUE};
			panel.setLayout(gbl_panel);
			GridBagConstraints gbc_btAuthenticate = new GridBagConstraints();
			gbc_btAuthenticate.insets = new Insets(0, 0, 0, 5);
			gbc_btAuthenticate.gridx = 0;
			gbc_btAuthenticate.gridy = 0;
			panel.add(getBtAuthenticate(), gbc_btAuthenticate);
			GridBagConstraints gbc_btGetInfo = new GridBagConstraints();
			gbc_btGetInfo.insets = new Insets(0, 0, 0, 5);
			gbc_btGetInfo.gridx = 1;
			gbc_btGetInfo.gridy = 0;
			panel.add(getBtGetInfo(), gbc_btGetInfo);
			GridBagConstraints gbc_btResetCard = new GridBagConstraints();
			gbc_btResetCard.gridx = 2;
			gbc_btResetCard.gridy = 0;
			panel.add(getBtResetCard(), gbc_btResetCard);
		}

		debugMenuItem.setSelected(Settings.isDebugMode());
    	
    	fc=new JFileChooser(); 
    	
        cw = new CardWorker();
        cw.setTextArea(jtaos);

        rereadReaders();
		
		
		
	}
	
	public JFormattedTextField getTfOldKey1() {
		return tfOldKey1;
	}
	
	public JComboBox getCbReader() {
		return cbReader;
	}
	
	public JFormattedTextField getTfCMAID() {
		return tfCMAID;
	}
	
	private ButtonGroup getBgKeys() {
		if(bgKeys == null) {
			bgKeys = new ButtonGroup();
		}
		return bgKeys;
	}
	
	private JButton getBtAddKeys() {
		if(btAddKeys == null) {
			btAddKeys = new JButton();
			btAddKeys.setText("Add/Modify Keys");
			btAddKeys.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					btAddKeysActionPerformed(evt);
				}
			});
		}
		return btAddKeys;
	}
	
	private JPanel getPanelCAP() {
		if(panelCAP == null) {
			panelCAP = new JPanel();
			panelCAP.setBorder(BorderFactory.createTitledBorder(null, "Upload & Install CAP", TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION));
			GridBagLayout gbl_panelCAP = new GridBagLayout();
			gbl_panelCAP.columnWidths = new int[]{0, 0, 0, 0, 0};
			gbl_panelCAP.rowHeights = new int[]{0, 0, 0};
			gbl_panelCAP.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
			gbl_panelCAP.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
			panelCAP.setLayout(gbl_panelCAP);
			GridBagConstraints gbc_btFile = new GridBagConstraints();
			gbc_btFile.fill = GridBagConstraints.BOTH;
			gbc_btFile.insets = new Insets(0, 0, 5, 5);
			gbc_btFile.gridx = 0;
			gbc_btFile.gridy = 0;
			panelCAP.add(getBtFile(), gbc_btFile);
			GridBagConstraints gbc_tfFile = new GridBagConstraints();
			gbc_tfFile.fill = GridBagConstraints.BOTH;
			gbc_tfFile.insets = new Insets(0, 0, 5, 0);
			gbc_tfFile.gridwidth = 3;
			gbc_tfFile.gridx = 1;
			gbc_tfFile.gridy = 0;
			panelCAP.add(getTfFile(), gbc_tfFile);
			GridBagConstraints gbc_labelParamInstall = new GridBagConstraints();
			gbc_labelParamInstall.fill = GridBagConstraints.HORIZONTAL;
			gbc_labelParamInstall.insets = new Insets(0, 0, 0, 5);
			gbc_labelParamInstall.gridwidth = 2;
			gbc_labelParamInstall.gridx = 0;
			gbc_labelParamInstall.gridy = 1;
			panelCAP.add(getLabelParamInstall(), gbc_labelParamInstall);
			GridBagConstraints gbc_tfInstallParameters = new GridBagConstraints();
			gbc_tfInstallParameters.fill = GridBagConstraints.BOTH;
			gbc_tfInstallParameters.insets = new Insets(0, 0, 0, 5);
			gbc_tfInstallParameters.gridx = 2;
			gbc_tfInstallParameters.gridy = 1;
			panelCAP.add(getTfInstallParameters(), gbc_tfInstallParameters);
			GridBagConstraints gbc_btUpload = new GridBagConstraints();
			gbc_btUpload.fill = GridBagConstraints.BOTH;
			gbc_btUpload.gridx = 3;
			gbc_btUpload.gridy = 1;
			panelCAP.add(getBtUpload(), gbc_btUpload);
		}
		return panelCAP;
	}
	
	private JButton getBtFile() {
		if(btFile == null) {
			btFile = new JButton();
			btFile.setText("File...");
			btFile.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					btFileActionPerformed(evt);
				}
			});
		}
		return btFile;
	}
	
	public JTextField getTfFile() {
		if(tfFile == null) {
			tfFile = new JTextField();
		}
		return tfFile;
	}
	
	private JLabel getLabelParamInstall() {
		if(labelParamInstall == null) {
			labelParamInstall = new JLabel();
			labelParamInstall.setText("Install parameters");
		}
		return labelParamInstall;
	}
	
	private JTextField getTfInstallParameters() {
		if(tfInstallParameters == null) {
			tfInstallParameters = new JTextField();
			tfInstallParameters.addFocusListener(new FocusAdapter() {
				public void focusLost(FocusEvent evt) {
					tfInstallParametersFocusLost(evt);
				}
			});
		}
		return tfInstallParameters;
	}
	
	private JButton getBtUpload() {
		if(btUpload == null) {
			btUpload = new JButton();
			btUpload.setText("Upload");
			btUpload.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					btUploadActionPerformed(evt);
				}
			});
		}
		return btUpload;
	}
	
	private JButton getBtAuthenticate() {
		if(btAuthenticate == null) {
			btAuthenticate = new JButton();
			btAuthenticate.setText("Authenticate");
			btAuthenticate.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					btAuthenticateActionPerformed(evt);
				}
			});
		}
		return btAuthenticate;
	}
	
	private JButton getBtGetInfo() {
		if(btGetInfo == null) {
			btGetInfo = new JButton();
			btGetInfo.setText("Get Card Info");
			btGetInfo.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					btGetInfoActionPerformed(evt);
				}
			});
		}
		return btGetInfo;
	}
	
	private JButton getBtResetCard() {
		if(btResetCard == null) {
			btResetCard = new JButton();
			btResetCard.setText("Reset Card");
			btResetCard.setToolTipText("Delete all applets & packages from card");
			btResetCard.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					btResetCardActionPerformed(evt);
				}
			});
		}
		return btResetCard;
	}
	
	private void btRefreshActionPerformed(ActionEvent evt) {
		rereadReaders();
	}
	
	private void btAuthenticateActionPerformed(ActionEvent evt) {
    	  textArea.setText("");
            authenticate();	}

	
    private void rereadReaders(){
        final MySwingWorker worker = new MySwingWorker() {
          public Object construct() {
          	try{
          	cbReader.removeAllItems();
            String[] sa = cw.getReaders();
            for (int i=0 ; i < sa.length ; i++) {
            	cbReader.insertItemAt(sa[i], i);
            }
            if (cbReader.getItemCount()>0)
            	cbReader.setSelectedIndex(0);
          	}catch(Exception e){
        	    System.err.println("EX: msg " + e.getMessage() + ", class " + e.getClass());
        	    e.printStackTrace(System.err);
        	}
          	finally{
          		cw.disconnectCard();
          	}
            return "1";
          }
          

          public void finished() {
          }
        };
        worker.start();    	  
//  	System.exit(0);
    }

    private void authenticate(){
        final MySwingWorker worker = new MySwingWorker() {
          public Object construct() {
          	try{
            cw.authenticate(getAuthKeysArray(), false);
          	}catch(Exception e){
        	    System.err.println("EX: msg " + e.getMessage() + ", class " + e.getClass());
        	    e.printStackTrace(System.err);
        	}
          	finally{
          		cw.disconnectCard();
          	}
            return "1";
          }


          public void finished() {
          }
        };
        worker.start();    	  
//  	System.exit(0);
    }

    
    private String[] getAuthKeysArray(){
        String[] tmpArray = new String[3];
        tmpArray[0]=tfOldKey1.getText();
        tmpArray[1]=tfOldKey2.getText();
        tmpArray[2]=tfOldKey3.getText();
        return tmpArray;
      }
      private String[] getNewKeysArray(){
          String[] tmpArray = new String[3];
          tmpArray[0]=tfNewKey1.getText();
          tmpArray[1]=tfNewKey2.getText();
          tmpArray[2]=tfNewKey3.getText();
          return tmpArray;
        }
      
      private void thisWindowClosed(WindowEvent evt) {
    	  System.exit(1);
      }
      
      private void tfInstallParametersFocusLost(FocusEvent evt) {
    	  Settings.props.setProperty("InstallParameters", tfInstallParameters.getText());
      }
      
      private void tfCMAIDFocusLost(FocusEvent evt) {
    	  Settings.props.setProperty("CMAID", tfCMAID.getText());    	  
      }
      
      private void tfKeysetFocusLost(FocusEvent evt) {
    	  Settings.props.setProperty("KeySet", tfKeyset.getText());
      }
      
      private void tfOldKey1FocusLost(FocusEvent evt) {
    	  Settings.props.setProperty("AuthKey1", tfOldKey1.getText());
      }
      
      private void tfOldKey2FocusLost(FocusEvent evt) {
    	  Settings.props.setProperty("AuthKey2", tfOldKey2.getText());
      }
      
      private void tfOldKey3FocusLost(FocusEvent evt) {
    	  Settings.props.setProperty("AuthKey3", tfOldKey3.getText());
      }
      
      private void tfNewKey1FocusLost(FocusEvent evt) {
    	  Settings.props.setProperty("NewKey1", tfNewKey1.getText());
      }
      
      private void tfNewKey2FocusLost(FocusEvent evt) {
    	  Settings.props.setProperty("NewKey2", tfNewKey3.getText());
      }
      
      private void tfNewKey3FocusLost(FocusEvent evt) {
    	  Settings.props.setProperty("NewKey3", tfNewKey3.getText());
      }
      
      private void exitMenuItemActionPerformed(ActionEvent evt) {
    	  Settings.saveContext();
    	  System.exit(0);
      }
      
      private void thisWindowClosing(WindowEvent evt) {
    	 Settings.saveContext();
    	  System.exit(0);
      }
      
      private void cbReaderItemStateChanged(ItemEvent evt) {
    	  if (cbReader.getItemCount()>0)
    		  Settings.setSelectedReader(cbReader.getSelectedIndex());
    	  else {
        	  Settings.setSelectedReader(-1);    		  
    	  }
      }
      
      private void btAddKeysActionPerformed(ActionEvent evt) {
    	  textArea.setText("");
      	  replaceKeys();
      }

      private void replaceKeys(){
          final MySwingWorker worker = new MySwingWorker() {
            public Object construct() {
            	try{
              cw.changekeys(getAuthKeysArray(), getNewKeysArray(), Integer.parseInt(tfKeyset.getText()),false, rbAddKeys.isSelected());
            	}catch(Exception e){
          	    System.err.println("EX: msg " + e.getMessage() + ", class " + e.getClass());
          	    e.printStackTrace(System.err);
          	}
              	finally{
              		cw.disconnectCard();
              	}
            	
              return "1";
            }


            public void finished() {
            }
          };
          worker.start();    	  
      }
      
      private void btGetInfoActionPerformed(ActionEvent evt) {
    	  textArea.setText("");
    	  getInfo();
      }

      private void getInfo(){
          final MySwingWorker worker = new MySwingWorker() {
            public Object construct() {
              try{
              cw.getInfo(getAuthKeysArray(), false);
              }catch(Exception e){
                  System.err.println("EX: msg " + e.getMessage() + ", class " + e.getClass());
                  e.printStackTrace(System.err);
              }
            	finally{
            		cw.disconnectCard();
            	}
              return "1";
            }
            public void finished() {
            }
          };
          worker.start();       
//        System.exit(0);
      }
      
      private void btResetCardActionPerformed(ActionEvent evt) {
    	  textArea.setText("");
    	  resetCard();
      }

      private void resetCard(){
          final MySwingWorker worker = new MySwingWorker() {
            public Object construct() {
              try{
              cw.resetCard(getAuthKeysArray(), false);
              }catch(Exception e){
                  System.err.println("EX: msg " + e.getMessage() + ", class " + e.getClass());
                  e.printStackTrace(System.err);
              }
            	finally{
            		cw.disconnectCard();
            	}
              return "1";
            }
            public void finished() {
            }
          };
          worker.start();       
//        System.exit(0);
      }     
      
      private void btUploadActionPerformed(ActionEvent evt) {
    	  textArea.setText("");
    	  f = new File(tfFile.getText());
    	  if (f.exists())
    		  uploadCAP();
      }
      
      private void uploadCAP(){
          final MySwingWorker worker = new MySwingWorker() {
            public Object construct() {
            	try{
            	  cw.uploadCAP(f, getAuthKeysArray(), false, ByteUtils.stoh(tfInstallParameters.getText()));
            	}catch(Exception e){
          	    System.err.println("EX: msg " + e.getMessage() + ", class " + e.getClass());
          	    e.printStackTrace(System.err);
          	}
              	finally{
              		cw.disconnectCard();
              	}

            	return "1";
            }
            public void finished() {
            }
          };
          worker.start();    	  
      }
      
      private void btFileActionPerformed(ActionEvent evt) {
          try{
              fc.addChoosableFileFilter(new CapFilter());
              fc.setCurrentDirectory(new File(System.getProperties().getProperty("user.dir")));
              int returnVal = fc.showOpenDialog(JCManager.this);
              if (returnVal == JFileChooser.APPROVE_OPTION) {
                tfFile.setText(fc.getSelectedFile().getAbsolutePath());
              } else {
              }
            }
            catch (Exception ex) {
              ex.printStackTrace();
            }
      }
      
      private JLabel getLbNotYet() {
    	  if(lbNotYet == null) {
    		  lbNotYet = new JLabel();
    		  BorderLayout lbNotYetLayout = new BorderLayout();
    		  lbNotYet.setLayout(lbNotYetLayout);
    		  lbNotYet.setText("Feature not released yet");
    		  lbNotYet.setBounds(306, 114, 120, 14);
    		  lbNotYet.setHorizontalAlignment(SwingConstants.CENTER);
    	  }
    	  return lbNotYet;
      }
      
      private JCheckBoxMenuItem getDebugMenuItem() {
    	  if(debugMenuItem == null) {
    		  debugMenuItem = new JCheckBoxMenuItem();
    		  debugMenuItem.setText("Debug mode");
    		  debugMenuItem.addChangeListener(new ChangeListener() {
    			  public void stateChanged(ChangeEvent evt) {
    				  debugMenuItemStateChanged(evt);
    			  }
    		  });
    	  }
    	  return debugMenuItem;
      }
      
      private void debugMenuItemStateChanged(ChangeEvent evt) {
    	  if (debugMenuItem.isSelected())
    		  Settings.props.setProperty("DebugMode", "1");
    	  else
    		  Settings.props.setProperty("DebugMode", "0");
    		  
      }
      
      private void aboutMenuItemActionPerformed(ActionEvent evt) {
    	  JOptionPane.showMessageDialog(this, "Java Card Manager v. 1.2\n (c) 2010 Stefan Braicu",
                  "About jcManager", JOptionPane.INFORMATION_MESSAGE);
      }

 }

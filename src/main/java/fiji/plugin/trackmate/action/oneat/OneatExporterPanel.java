
package fiji.plugin.trackmate.action.oneat;

import static fiji.plugin.trackmate.tracking.TrackerKeys.KEY_LINKING_MAX_DISTANCE;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileFilter;

import org.scijava.prefs.PrefService;

import fiji.plugin.trackmate.LoadTrackMatePlugIn;
import fiji.plugin.trackmate.Model;
import fiji.plugin.trackmate.Settings;
import fiji.plugin.trackmate.gui.components.ConfigurationPanel;
import fiji.plugin.trackmate.util.TMUtils;
import ij.ImageJ;
import ij.plugin.PlugIn;

import static fiji.plugin.trackmate.action.oneat.OneatCorrectorFactory.APOPTOSIS_FILE;
import static fiji.plugin.trackmate.action.oneat.OneatCorrectorFactory.DIVISION_FILE;
import static fiji.plugin.trackmate.action.oneat.OneatCorrectorFactory.KEY_BREAK_LINKS;
import static fiji.plugin.trackmate.action.oneat.OneatCorrectorFactory.KEY_CREATE_LINKS;
import static fiji.plugin.trackmate.action.oneat.OneatCorrectorFactory.KEY_SIZE_RATIO;
import static fiji.plugin.trackmate.action.oneat.OneatCorrectorFactory.KEY_TIME_GAP;
import static fiji.plugin.trackmate.action.oneat.OneatCorrectorFactory.KEY_TRACKLET_LENGTH;
import static fiji.plugin.trackmate.detection.DetectorKeys.KEY_TARGET_CHANNEL;
import static fiji.plugin.trackmate.gui.Fonts.SMALL_FONT;
import static fiji.plugin.trackmate.gui.Fonts.BIG_FONT;;

public class OneatExporterPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private static File oneatdivisionfile;
	private static File oneatapoptosisfile;

	private JButton Loaddivisioncsvbutton;
	private JButton Loadapoptosiscsvbutton;
	private JFormattedTextField MinTracklet;
	private JFormattedTextField DetectionChannel;
	private JFormattedTextField TimeGap;
	private JFormattedTextField MotherDaughterSizeRatio;

	private JCheckBox CreateNewLinks;
	private JCheckBox BreakCurrentLinks;

	public OneatExporterPanel(final Settings settings, final Model model) {

		final GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 1.0, 1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE };
		setLayout( gridBagLayout );

		final JLabel lblOneatModel = new JLabel("Oneat, a Keras based library for action events and static cell type classification, written by Varun Kapoor");
		lblOneatModel.setFont(BIG_FONT);
		final GridBagConstraints gbcLblOneatModel = new GridBagConstraints();
		gbcLblOneatModel.anchor = GridBagConstraints.EAST;
		gbcLblOneatModel.insets = new Insets(0, 0, 0, 0);
		gbcLblOneatModel.gridx = 0;
		gbcLblOneatModel.gridy = 0;
		add(lblOneatModel, gbcLblOneatModel);

		Loaddivisioncsvbutton = new JButton("Load Oneat division detections From CSV");

		Loaddivisioncsvbutton.setHorizontalTextPosition(SwingConstants.LEFT);
		Loaddivisioncsvbutton.setFont(SMALL_FONT);
		final GridBagConstraints gbcLoadDivision = new GridBagConstraints();
		gbcLoadDivision.anchor = GridBagConstraints.EAST;
		gbcLoadDivision.insets = new Insets(0, 0, 0, 0);
		gbcLoadDivision.gridx = 0;
		gbcLoadDivision.gridy = 1;
		add(Loaddivisioncsvbutton, gbcLoadDivision);

		Loadapoptosiscsvbutton = new JButton("Load Oneat apoptosis detections From CSV");
		Loaddivisioncsvbutton.setHorizontalTextPosition(SwingConstants.LEFT);
		Loaddivisioncsvbutton.setFont(SMALL_FONT);
		final GridBagConstraints gbcLoadApoptosis = new GridBagConstraints();
		gbcLoadApoptosis.anchor = GridBagConstraints.EAST;
		gbcLoadDivision.insets = new Insets(0, 0, 0, 0);
		gbcLoadApoptosis.gridx = 1;
		gbcLoadApoptosis.gridy = 1;
		add(Loadapoptosiscsvbutton, gbcLoadApoptosis);

		final JLabel lblDetectionChannel = new JLabel( "Integer label detection channel:" );
		final GridBagConstraints gbc_lblDetectionChannel = new GridBagConstraints();
		gbc_lblDetectionChannel.anchor = GridBagConstraints.EAST;
		gbc_lblDetectionChannel.insets = new Insets( 0, 0, 5, 5 );
		gbc_lblDetectionChannel.gridx = 0;
		gbc_lblDetectionChannel.gridy = 3;
		add( lblDetectionChannel, gbc_lblDetectionChannel );
		
		DetectionChannel = new JFormattedTextField(Integer.valueOf(2));
		DetectionChannel.setFont(new Font("Arial", Font.PLAIN, 10));
		final GridBagConstraints gbc_DetectionChannel = new GridBagConstraints();
		gbc_DetectionChannel.insets = new Insets(0, 5, 5, 5);
		gbc_DetectionChannel.fill = GridBagConstraints.BOTH;
		gbc_DetectionChannel.gridx = 0;
		gbc_DetectionChannel.gridy = 4;
		add(DetectionChannel, gbc_DetectionChannel);
		
		
		final JLabel lblMinTracklet = new JLabel( "Minimum length of tracklet:" );
		final GridBagConstraints gbc_lblMinTracklet = new GridBagConstraints();
		gbc_lblMinTracklet.anchor = GridBagConstraints.EAST;
		gbc_lblMinTracklet.insets = new Insets( 0, 0, 5, 5 );
		gbc_lblMinTracklet.gridx = 0;
		gbc_lblMinTracklet.gridy = 5;
		add( lblMinTracklet, gbc_lblMinTracklet );
		
		MinTracklet = new JFormattedTextField(Integer.valueOf(2));
		MinTracklet.setFont(new Font("Arial", Font.PLAIN, 10));
		final GridBagConstraints gbc_MinTracklet = new GridBagConstraints();
		gbc_MinTracklet.insets = new Insets(0, 0, 5, 5);
		gbc_MinTracklet.fill = GridBagConstraints.BOTH;
		gbc_MinTracklet.gridx = 0;
		gbc_MinTracklet.gridy = 6;
		add(MinTracklet, gbc_MinTracklet);

		
		final JLabel lblTimeGap = new JLabel( "Allowed timegap between oneat & TM events:" );
		final GridBagConstraints gbc_lblTimeGap = new GridBagConstraints();
		gbc_lblTimeGap.anchor = GridBagConstraints.EAST;
		gbc_lblTimeGap.insets = new Insets( 0, 0, 5, 5 );
		gbc_lblTimeGap.gridx = 0;
		gbc_lblTimeGap.gridy = 7;
		add( lblTimeGap, gbc_lblTimeGap );
		
		TimeGap = new JFormattedTextField(Integer.valueOf(2));
		TimeGap.setFont(new Font("Arial", Font.PLAIN, 10));
		final GridBagConstraints gbcTimeGap = new GridBagConstraints();
		gbcTimeGap.insets = new Insets(0, 0, 5, 5);
		gbcTimeGap.fill = GridBagConstraints.BOTH;
		gbcTimeGap.gridx = 0;
		gbcTimeGap.gridy = 8;
		add(TimeGap, gbcTimeGap);

		
		final JLabel lblMotherDaughterSizeRatio = new JLabel( "Max Size ratio daughter/mother cell:" );
		final GridBagConstraints gbc_lblMotherDaughterSizeRatio = new GridBagConstraints();
		gbc_lblMotherDaughterSizeRatio.anchor = GridBagConstraints.EAST;
		gbc_lblMotherDaughterSizeRatio.insets = new Insets( 0, 0, 5, 5 );
		gbc_lblMotherDaughterSizeRatio.gridx = 0;
		gbc_lblMotherDaughterSizeRatio.gridy = 9;
		add( lblMotherDaughterSizeRatio, gbc_lblMotherDaughterSizeRatio );
		
		MotherDaughterSizeRatio = new JFormattedTextField(Double.valueOf(0.75));
		MotherDaughterSizeRatio.setFont(new Font("Arial", Font.PLAIN, 10));
		MotherDaughterSizeRatio.setColumns(5);
		final GridBagConstraints gbc_MotherDaughterSizeRatio = new GridBagConstraints();
		gbc_MotherDaughterSizeRatio.insets = new Insets(0, 0, 5, 5);
		gbc_MotherDaughterSizeRatio.fill = GridBagConstraints.BOTH;
		gbc_MotherDaughterSizeRatio.gridx = 0;
		gbc_MotherDaughterSizeRatio.gridy = 10;
		add(MotherDaughterSizeRatio, gbc_MotherDaughterSizeRatio);

		
		
		CreateNewLinks = new JCheckBox(" Create new mitosis events ");
		CreateNewLinks.setHorizontalTextPosition(SwingConstants.LEFT);
		CreateNewLinks.setFont(SMALL_FONT);
		final GridBagConstraints gbcChckbxCreateNewLinks = new GridBagConstraints();
		gbcChckbxCreateNewLinks.anchor = GridBagConstraints.EAST;
		gbcChckbxCreateNewLinks.insets = new Insets(0, 0, 5, 5);
		gbcChckbxCreateNewLinks.gridx = 0;
		gbcChckbxCreateNewLinks.gridy = 11;
		add(CreateNewLinks, gbcChckbxCreateNewLinks);

		BreakCurrentLinks = new JCheckBox(" Break current mitosis events ");
		BreakCurrentLinks.setHorizontalTextPosition(SwingConstants.LEFT);
		BreakCurrentLinks.setFont(SMALL_FONT);
		final GridBagConstraints gbcChckbxBreakCurrentLinks = new GridBagConstraints();
		gbcChckbxBreakCurrentLinks.anchor = GridBagConstraints.EAST;
		gbcChckbxBreakCurrentLinks.insets = new Insets(0, 0, 5, 5);
		gbcChckbxBreakCurrentLinks.gridx = 1;
		gbcChckbxBreakCurrentLinks.gridy = 11;
		add(CreateNewLinks, gbcChckbxBreakCurrentLinks);

		Loaddivisioncsvbutton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent a) {

				JFileChooser csvfile = new JFileChooser();
				FileFilter csvfilter = new FileFilter() {
					// Override accept method
					public boolean accept(File file) {

						// if the file extension is .log return true, else false
						if (file.getName().endsWith(".csv")) {
							return true;
						}
						return false;
					}

					@Override
					public String getDescription() {

						return null;
					}
				};

				csvfile.setCurrentDirectory(new File(settings.imp.getOriginalFileInfo().directory));
				csvfile.setDialogTitle("Division Detection file");
				csvfile.setFileSelectionMode(JFileChooser.FILES_ONLY);
				csvfile.setFileFilter(csvfilter);

				if (csvfile.showOpenDialog(getParent()) == JFileChooser.APPROVE_OPTION)

					oneatdivisionfile = new File(csvfile.getSelectedFile().getPath());
			}

		});

		Loadapoptosiscsvbutton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent a) {

				JFileChooser csvfile = new JFileChooser();
				FileFilter csvfilter = new FileFilter() {
					// Override accept method
					public boolean accept(File file) {

						// if the file extension is .log return true, else false
						if (file.getName().endsWith(".csv")) {
							return true;
						}
						return false;
					}

					@Override
					public String getDescription() {

						return null;
					}
				};

				csvfile.setCurrentDirectory(new File(settings.imp.getOriginalFileInfo().directory));
				csvfile.setDialogTitle("Apoptosis Detection file");
				csvfile.setFileSelectionMode(JFileChooser.FILES_ONLY);
				csvfile.setFileFilter(csvfilter);

				if (csvfile.showOpenDialog(getParent()) == JFileChooser.APPROVE_OPTION)

					oneatapoptosisfile = new File(csvfile.getSelectedFile().getPath());
			}

		});

	}

	public void setSettings(final Map<String, Object> settings) {
		MinTracklet.setValue(settings.get(KEY_TRACKLET_LENGTH));
		DetectionChannel.setValue(settings.get(KEY_TARGET_CHANNEL));
		TimeGap.setValue(settings.get(KEY_TIME_GAP));
		MotherDaughterSizeRatio.setValue(settings.get(KEY_SIZE_RATIO));
		CreateNewLinks.setSelected((boolean) settings.get(KEY_CREATE_LINKS));
		BreakCurrentLinks.setSelected((boolean) settings.get(KEY_BREAK_LINKS));
	}

	public Map<String, Object> getSettings() {
		final Map<String, Object> settings = new HashMap<>();

		settings.put(DIVISION_FILE, oneatdivisionfile);
		settings.put(APOPTOSIS_FILE, oneatapoptosisfile);
		settings.put(KEY_TRACKLET_LENGTH, ((Number) MinTracklet.getValue()).doubleValue());
		settings.put(KEY_TIME_GAP, ((Number) TimeGap.getValue()).doubleValue());
		settings.put(KEY_SIZE_RATIO, ((Number) MotherDaughterSizeRatio.getValue()).doubleValue());
		settings.put(KEY_BREAK_LINKS, BreakCurrentLinks.isSelected());
		settings.put(KEY_CREATE_LINKS, CreateNewLinks.isSelected());
		settings.put(KEY_TARGET_CHANNEL, ((Number) DetectionChannel.getValue()).doubleValue());

		return settings;
	}

}

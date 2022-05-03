
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
		setLayout( gridBagLayout );

		final GridBagConstraints gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets( 5, 5, 5, 5 );
		gbc.gridx = 0;
		gbc.gridy = 0;



		Loaddivisioncsvbutton = new JButton("Load Oneat division detections From CSV");
		add(Loaddivisioncsvbutton, gbc);
		gbc.gridy++;

		Loadapoptosiscsvbutton = new JButton("Load Oneat apoptosis detections From CSV");
		add(Loadapoptosiscsvbutton, gbc);
		gbc.gridy++;
		
		final JLabel lblDetectionChannel = new JLabel( "Integer label detection channel:" );
		add( lblDetectionChannel, gbc );
		gbc.gridx++;
		
		
		DetectionChannel = new JFormattedTextField(Integer.valueOf(2));
		DetectionChannel.setColumns( 4 );
		DetectionChannel.setFont(new Font("Arial", Font.PLAIN, 10));
		add(DetectionChannel, gbc);
		gbc.gridy++;
		gbc.gridx--;
		
		final JLabel lblMinTracklet = new JLabel( "Minimum length of tracklet:" );
		add( lblMinTracklet, gbc );
		gbc.gridx++;
		
		MinTracklet = new JFormattedTextField(Integer.valueOf(2));
		MinTracklet.setColumns( 4 );
		MinTracklet.setFont(new Font("Arial", Font.PLAIN, 10));
		add(MinTracklet, gbc);
		
		gbc.gridy++;
		gbc.gridx--;
		
		final JLabel lblTimeGap = new JLabel( "Allowed timegap between oneat & TM events:" );
		add( lblTimeGap, gbc );
		gbc.gridx++;
		
		
		TimeGap = new JFormattedTextField(Integer.valueOf(2));
		TimeGap.setColumns( 4 );
		TimeGap.setFont(new Font("Arial", Font.PLAIN, 10));
		add(TimeGap, gbc);
		gbc.gridy++;
		gbc.gridx--;
		
		final JLabel lblMotherDaughterSizeRatio = new JLabel( "Max Size ratio daughter/mother cell:" );
		add( lblMotherDaughterSizeRatio, gbc );
		gbc.gridx++;
		
		MotherDaughterSizeRatio = new JFormattedTextField(Double.valueOf(0.75));
		MotherDaughterSizeRatio.setFont(new Font("Arial", Font.PLAIN, 10));
		MotherDaughterSizeRatio.setColumns(4);
		add(MotherDaughterSizeRatio, gbc);
		gbc.gridy++;
		gbc.gridx--;
		
		
		CreateNewLinks = new JCheckBox(" Create new mitosis events ");
		CreateNewLinks.setHorizontalTextPosition(SwingConstants.LEFT);
		CreateNewLinks.setFont(SMALL_FONT);
		add(CreateNewLinks, gbc);
		gbc.gridx++;

		BreakCurrentLinks = new JCheckBox(" Break current mitosis events ");
		BreakCurrentLinks.setHorizontalTextPosition(SwingConstants.LEFT);
		BreakCurrentLinks.setFont(SMALL_FONT);
		add(BreakCurrentLinks, gbc);

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

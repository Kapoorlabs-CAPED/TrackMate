
package fiji.plugin.trackmate.action.oneat;

import static fiji.plugin.trackmate.tracking.TrackerKeys.KEY_LINKING_MAX_DISTANCE;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
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
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileFilter;


import fiji.plugin.trackmate.Model;
import fiji.plugin.trackmate.Settings;



import static fiji.plugin.trackmate.gui.Fonts.SMALL_FONT;

public class OneatExporterPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private static File oneatdivisionfile;
	private  static File oneatapoptosisfile;

	private  int detchannel;
	private  double sizeratio;
	private double linkdist;
	private  int deltat;
	private  int tracklet;
	private boolean createlinks;
	private boolean breaklinks;
	
	private JButton Loaddivisioncsvbutton;
	private JButton Loadapoptosiscsvbutton;
	private JFormattedTextField MinTracklet;
	private JFormattedTextField DetectionChannel;
	private JFormattedTextField TimeGap;
	private JFormattedTextField MotherDaughterSizeRatio;
	private JFormattedTextField MotherDaughterLinkDist;

	private JCheckBox CreateNewLinks;
	private JCheckBox BreakCurrentLinks;
	
	public OneatExporterPanel(final Settings settings, final Model model, final int detchannel, final double sizeratio, final double linkdist, final int deltat,
			final int tracklet) {

		this.detchannel = detchannel;
		this.sizeratio = sizeratio;
		this.linkdist = linkdist;
		this.deltat = deltat;
		this.tracklet = tracklet;
		
		
		
		
		final GridBagLayout gridBagLayout = new GridBagLayout();
		setLayout( gridBagLayout );

		final GridBagConstraints gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets( 5, 5, 5, 5 );
		gbc.gridx = 0;
		gbc.gridy = 0;



		Loaddivisioncsvbutton = new JButton("Load Oneat mitosis detections From CSV");
		add(Loaddivisioncsvbutton, gbc);
		gbc.gridy++;

		Loadapoptosiscsvbutton = new JButton("Load Oneat apoptosis detections From CSV");
		add(Loadapoptosiscsvbutton, gbc);
		gbc.gridy++;
		
		final JLabel lblDetectionChannel = new JLabel( "Integer label detection channel:" );
		add( lblDetectionChannel, gbc );
		gbc.gridx++;
		
		
		DetectionChannel = new JFormattedTextField();
		DetectionChannel.setValue(detchannel);
		DetectionChannel.setColumns( 4 );
		DetectionChannel.setFont(new Font("Arial", Font.PLAIN, 10));
		add(DetectionChannel, gbc);
		gbc.gridy++;
		gbc.gridx--;
		
		final JLabel lblMinTracklet = new JLabel( "Minimum length of tracklet:" );
		add( lblMinTracklet, gbc );
		gbc.gridx++;
		
		MinTracklet = new JFormattedTextField();
		MinTracklet.setValue(tracklet);
		MinTracklet.setColumns( 4 );
		MinTracklet.setFont(new Font("Arial", Font.PLAIN, 10));
		add(MinTracklet, gbc);
		
		gbc.gridy++;
		gbc.gridx--;
		
		final JLabel lblTimeGap = new JLabel( "Allowed timegap between oneat & TM events:" );
		add( lblTimeGap, gbc );
		gbc.gridx++;
		
		
		TimeGap = new JFormattedTextField();
		TimeGap.setValue(deltat);
		TimeGap.setColumns( 4 );
		TimeGap.setFont(new Font("Arial", Font.PLAIN, 10));
		add(TimeGap, gbc);
		gbc.gridy++;
		gbc.gridx--;
		
		final JLabel lblMotherDaughterSizeRatio = new JLabel( "Max Size ratio daughter/mother cell:" );
		add( lblMotherDaughterSizeRatio, gbc );
		gbc.gridx++;
		
		MotherDaughterSizeRatio = new JFormattedTextField();
		MotherDaughterSizeRatio.setValue(sizeratio);
		MotherDaughterSizeRatio.setFont(new Font("Arial", Font.PLAIN, 10));
		MotherDaughterSizeRatio.setColumns(4);
		add(MotherDaughterSizeRatio, gbc);
		gbc.gridy++;
		gbc.gridx--;
		
		final JLabel lblMotherDaughterLinkDist = new JLabel( "Linking distance between mother/daughter:" );
		add( lblMotherDaughterLinkDist, gbc );
		gbc.gridx++;
		
		MotherDaughterLinkDist = new JFormattedTextField();
		MotherDaughterLinkDist.setValue(linkdist);
		MotherDaughterLinkDist.setFont(new Font("Arial", Font.PLAIN, 10));
		MotherDaughterLinkDist.setColumns(4);
		add(MotherDaughterLinkDist, gbc);
		gbc.gridy++;
		gbc.gridx--;
		
		CreateNewLinks = new JCheckBox("Create new mitosis events (Verified by oneat, missed by TM) ");
		CreateNewLinks.setSelected(createlinks);
		CreateNewLinks.setHorizontalTextPosition(SwingConstants.LEFT);
		CreateNewLinks.setFont(SMALL_FONT);
		add(CreateNewLinks, gbc);
		gbc.gridx++;

		BreakCurrentLinks = new JCheckBox("Break current mitosis events (Labelled by TM, Unverfied by oneat ) ");
		BreakCurrentLinks.setSelected(breaklinks);
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

				int showparent = csvfile.showOpenDialog(getParent());
				if (showparent == JFileChooser.APPROVE_OPTION)

					oneatdivisionfile = new File(csvfile.getSelectedFile().getPath());
				
				if (showparent == JFileChooser.CANCEL_OPTION)
					oneatdivisionfile = null;
				
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
				int showparent = csvfile.showOpenDialog(getParent());
				if (showparent == JFileChooser.APPROVE_OPTION)

					oneatapoptosisfile = new File(csvfile.getSelectedFile().getPath());
				
				if (showparent == JFileChooser.CANCEL_OPTION)
					oneatapoptosisfile = null;
				
			}

		});
		
		DetectionChannel.addPropertyChangeListener( "value", ( e ) -> this.detchannel = ( ( Number ) DetectionChannel.getValue() ).intValue() );
		MotherDaughterSizeRatio.addPropertyChangeListener( "value", ( e ) -> this.sizeratio = ( ( Number ) MotherDaughterSizeRatio.getValue() ).doubleValue() );
		
		CreateNewLinks.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				
				if (e.getStateChange() == ItemEvent.SELECTED)
					  createlinks = true;
				if (e.getStateChange() == ItemEvent.DESELECTED)
					  createlinks = false;
				
			}
		});
		
		BreakCurrentLinks.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				
				if (e.getStateChange() == ItemEvent.SELECTED)
					  breaklinks = true;
				if (e.getStateChange() == ItemEvent.DESELECTED)
					  breaklinks = false;
			}
		});
		
		
		MinTracklet.addPropertyChangeListener( "value", ( e ) -> this.tracklet = ((Number) MinTracklet.getValue()).intValue() );
		TimeGap.addPropertyChangeListener( "value", ( e ) -> this.deltat = ((Number) TimeGap.getValue()).intValue() );
		MotherDaughterLinkDist.addPropertyChangeListener( "value", ( e ) -> this.linkdist = ( ( Number ) MotherDaughterLinkDist.getValue() ).doubleValue() );
		
	}

	public double getLinkDist() {
		
		
		return linkdist;
	}
	
	
	public int getDetectionChannel() {
		
		
		return detchannel;
	}
	
	public double getSizeRatio() {
		
		
		return sizeratio;
	}
	
	public int getMinTracklet() {
		
		return tracklet;
	}
	
	public boolean getBreakLinks() {
		
		return breaklinks;
	}
	
	public boolean getCreateLinks() {
		
		return createlinks;
	}
	
	public int getTimeGap() {
		
		return deltat;
	}
	
	public File getMistosisFile() {
		
		
		return oneatdivisionfile;
	}
	 
	
	public File getApoptosisFile() {
		
		return oneatapoptosisfile;
	}
	




}

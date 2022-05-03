package fiji.plugin.trackmate.action;

import java.awt.Frame;

import javax.swing.JOptionPane;
import static fiji.plugin.trackmate.detection.DetectorKeys.KEY_TARGET_CHANNEL;
import static fiji.plugin.trackmate.gui.Icons.TRACKMATE_ICON;
import fiji.plugin.trackmate.Model;
import fiji.plugin.trackmate.SelectionModel;
import fiji.plugin.trackmate.Settings;
import fiji.plugin.trackmate.TrackMate;
import fiji.plugin.trackmate.gui.displaysettings.DisplaySettings;
import fiji.plugin.trackmate.util.TMUtils;
import net.imagej.ImgPlus;

public class OneatExporterAction extends AbstractTMAction {

	
	public static final String INFO_TEXT = "<html>"
			+ "This action initiates Oneat track correction for the tracking results. "
			+ "<p> "
			+ "Oneat is an action classification software in python "
			+ "which provides csv files of event locations such as mitosis/apoptosis "
			+ "using the csv file of event locations the tracks are corrected "
			+ "and a new trackscheme is generated with corrected tracks. "
			+ "</html>";

	public static final String KEY = "LAUNCH_ONEAT";

	public static final String NAME = "Launch Oneat track corrector";
	
	
	@Override
	public void execute(TrackMate trackmate, SelectionModel selectionModel, DisplaySettings displaySettings,
			Frame gui) {
		
		Settings settings = trackmate.getSettings();
		Model model = trackmate.getModel();
		final ImgPlus img = TMUtils.rawWraps( settings.imp );
		
		if (gui!=null)
		{
			
			
			final OneatExporterPanel panel = new OneatExporterPanel(settings, model);
			final int userInput = JOptionPane.showConfirmDialog(gui, panel, "Launch Oneat track corrector", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, TRACKMATE_ICON);
			if ( userInput != JOptionPane.OK_OPTION )
				return;
			
			final int channel = (int) panel.getSettings().get(KEY_TARGET_CHANNEL);
			
		}
		
		
		
	}

}

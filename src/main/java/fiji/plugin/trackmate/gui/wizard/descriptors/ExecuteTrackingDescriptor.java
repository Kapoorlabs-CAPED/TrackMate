package fiji.plugin.trackmate.gui.wizard.descriptors;

import org.scijava.Cancelable;

import fiji.plugin.trackmate.TrackMate;
import fiji.plugin.trackmate.gui.LogPanel;
import fiji.plugin.trackmate.gui.wizard.WizardPanelDescriptor2;

public class ExecuteTrackingDescriptor extends WizardPanelDescriptor2
{

	public static final String KEY = "ExecuteTracking";

	private final TrackMate trackmate;

	public ExecuteTrackingDescriptor( final TrackMate trackmate, final LogPanel logPanel )
	{
		super( KEY );
		this.trackmate = trackmate;
		this.targetPanel = logPanel;
	}

	@Override
	public Runnable getForwardRunnable()
	{
		return () -> {
			final long start = System.currentTimeMillis();
			trackmate.execTracking();
			final long end = System.currentTimeMillis();
			trackmate.getModel().getLogger().log( String.format( "Tracking done in %.1f s.\n", ( end - start ) / 1e3f ) );
		};
	}

	@Override
	public Cancelable getCancelable()
	{
		return trackmate;
	}
}

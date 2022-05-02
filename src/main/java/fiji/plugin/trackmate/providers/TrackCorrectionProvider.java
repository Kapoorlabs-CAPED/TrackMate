
package fiji.plugin.trackmate.providers;
import fiji.plugin.trackmate.tracking.TrackCorrectorFactory;

public class TrackCorrectionProvider extends AbstractProvider< TrackCorrectorFactory >
{


	public TrackCorrectionProvider()
	{
		super( TrackCorrectorFactory.class );
	}

	public static void main( final String[] args )
	{
		final TrackCorrectionProvider provider = new TrackCorrectionProvider();
		System.out.println( provider.echo() );
	}
}
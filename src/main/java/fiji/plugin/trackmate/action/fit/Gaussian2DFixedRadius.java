/*-
 * #%L
 * Fiji distribution of ImageJ for the life sciences.
 * %%
 * Copyright (C) 2010 - 2021 Fiji developers.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */
package fiji.plugin.trackmate.action.fit;

import net.imglib2.algorithm.localization.FitFunction;

/**
 * A n-dimensional Gaussian peak function, representing an elliptical Gaussian,
 * with axis constrained to be aligned with the main axis.
 * <p>
 * This fitting target function is defined over dimension <code>n</code>, by the
 * following <code>4</code> parameters:
 * 
 * <ol start="0">
 * <li>x0
 * <li>y0
 * <li>A0
 * </ol>
 * 
 * with
 * 
 * <pre>
 * f(x) = A × exp( - S )
 * </pre>
 * 
 * and
 * 
 * <pre>
 * S = A0 * exp( -( x - x0 ) ^ 2 / ( 2 * sigmaxy ^ 2 ) - ( y - y0 ) ^ 2 / ( 2 * sigmaxy ^ 2 ) )
 * </pre>
 * 
 * @author Jean-Yves Tinevez
 *
 */
public class Gaussian2DFixedRadius implements FitFunction
{

	@Override
	public double val( final double[] x, final double[] a )
	{
		return a[ 3 ] * E( x, a );
	}

	@Override
	public double grad( final double[] pos, final double[] a, final int ak )
	{
		final double x0 = a[ 0 ];
		final double y0 = a[ 1 ];
		final double A0 = a[ 2 ];
		
		final double x = pos[0];
		final double y = pos[1];
		final double sigmaxy = pos[ 2 ];
		
		final double dx = x - x0;
		final double dy = y - y0;

		final double sigmaxy2 = sigmaxy *sigmaxy; 

		switch ( ak )
		{
		// x0
		case 0:
			return A0 * E( pos, a ) * dx / sigmaxy2;

		// y0
		case 1:
			return A0 * E( pos, a ) * dy / sigmaxy2;

		// A0
		case 2:
			return E( pos, a );
			
		default:
			throw new IllegalArgumentException( "There is no parameter #" + ak + " in " + getClass().getCanonicalName() );
		}
	}

	@Override
	public double hessian( final double[] pos, final double[] a, final int r, final int c )
	{
		final double x0 = a[ 0 ];
		final double y0 = a[ 1 ];
		final double A0 = a[ 3 ];
		
		final double x = pos[0];
		final double y = pos[1];
		final double sigmaxy = pos[ 2 ];
		
		final double dx = x - x0;
		final double dy = y - y0;

		final double dx2 = dx*dx;
		final double dy2 = dy*dy;
		final double sigmaxy2 = sigmaxy *sigmaxy; 

		final double sigmaxy4 = sigmaxy *sigmaxy * sigmaxy* sigmaxy; 

		if ( r == c )
		{
			/*
			 * Diagonal terms.
			 */
			switch ( r )
			{
			case 0:
				return A0 * E( pos, a ) * 2. * dx2 / ( 4. * sigmaxy4 ) - A0 * E( pos, a ) / sigmaxy2;
				 
			case 1:
				return A0 * E( pos, a ) * 2. * dy2 / ( 4. * sigmaxy4 ) - A0 * E( pos, a ) / sigmaxy2;
						 
			case 2:
				return 0.;
			}
		}

		/*
		 * First line.
		 */
		
		if ( ( r == 0 && c == 1 ) || ( r == 1 && c == 0 ) )
		{
			// df / dx0 / dy0
			return A0 * E( pos, a ) * dx * dy / sigmaxy4;
		}
		else if ( ( r == 0 && c == 2 ) || ( r == 2 && c == 0 ) )
		{
			// df / dx0 / dA0
			return E( pos, a ) * dx / sigmaxy2;
		}

		/*
		 * Second line
		 */

		else if ( ( r == 1 && c == 2 ) || ( r == 2 && c == 1 ) )
		{
			// df / dy0 / dA0
			return E( pos, a ) * dy / sigmaxy2;
		}
		else
		{
			throw new IllegalArgumentException( "Cannot compute Hessian for parameter indices #" + r + " and " + c );
		}
	}

	/*
	 * PRIVATE METHODS
	 */

	private static final double E( final double[] x, final double[] a )
	{
		final double dx = x[ 0 ] - a[ 0 ];
		final double dy = x[ 1 ] - a[ 1 ];
		final double sigmaxy = x[ 2 ];
		final double sum = ( dx * dx + dy * dy ) / ( 2 * sigmaxy * sigmaxy );
		return Math.exp( -sum );
	}
}

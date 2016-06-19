package eu.epicpvp.kcore.Particle.Wings;

import eu.epicpvp.kcore.Particle.NoMoveShape;

public class WingState extends NoMoveShape.SimpleLastMoveHolder { //unfortunaly this class can't be an innerclass anymore due to issues with generics and NoMoveShape
	public double rotBase;
	public double rotTransformed;
}

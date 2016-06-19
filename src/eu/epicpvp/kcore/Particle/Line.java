package eu.epicpvp.kcore.Particle;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.util.Vector;

import eu.epicpvp.kcore.Util.Tuple;

/**
 * seems to contain bugs
 */
public class Line<P extends Enum<P>> {

	private double particlesPerBlock = 6;

	private final List<Tuple<Tuple<Vector, Vector>, P>> lineData = new ArrayList<>();
	private Vector prev;
	private double defaultZ;
	private P defaultPart;

	public Line(double x, double y) {
		this(x, y, null);
	}

	public Line(double x, double y, P defaultPart) {
		this(x, y, 0, defaultPart);
	}

	public Line(double startX, double startY, double startZ) {
		this(startX, startY, startZ, null);
	}

	public Line(double startX, double startY, double startZ, P defaultPart) {
		this.defaultPart = defaultPart;
		prev = new Vector(startX, startY, startZ);
	}

	public Line<P> lineTo(Vector v, P part) {
		lineData.add(new Tuple<>(new Tuple<>(prev.clone(), v), part));
		return this;
	}

	public Line<P> lineTo(double x, double y, double z, P part) {
		return lineTo(new Vector(x, y, z), part);
	}

	public Line<P> lineTo(double x, double y, P part) {
		return lineTo(x, y, defaultZ, part);
	}

	public Line<P> lineTo(double x, double y) {
		return lineTo(x, y, defaultZ);
	}

	public Line<P> lineTo(double x, double y, double z) {
		return lineTo(x, y, z, defaultPart);
	}

	public Line<P> lineTo(Vector v) {
		return lineTo(v, defaultPart);
	}

	public Line<P> symmetricLineTo(double x, double y, P leftPart, P rightPart) {
		Vector prevClone = prev.clone();
		//inversed line
		Vector inversedPrev = prevClone.clone();
		inversedPrev.setX(-inversedPrev.getX());
		setFrom(inversedPrev).lineTo(-x, y, leftPart);
		//normal line
		prev = prevClone;
		return lineTo(x, y, rightPart);
	}

	/**
	 * uses the default part as origin part
	 * (call this only once and no makeAllSymmetric with the default part as origin part after you called this)
	 */
	public Line<P> makeAllSymmetric(P newPart) {
		return makeAllSymmetric(defaultPart, newPart);
	}

	/**
	 * never call it with the same originPart or a part which was already a newPart (would create double particles)
	 * (same origin and new part is okay, but also only once per part)
	 */
	public Line<P> makeAllSymmetric(P originPart, P newPart) {
		Vector prevClone = prev.clone();
		ArrayList<Tuple<Tuple<Vector, Vector>, P>> lineDataCopy = new ArrayList<>(lineData);
		for (Tuple<Tuple<Vector, Vector>, P> lineData : lineDataCopy) {
			if (lineData.b != originPart) { //no equals needed because they're enums
				continue;
			}
			Vector from = lineData.a.a;
			from.setX(-from.getX());
			Vector to = lineData.a.b.clone();
			to.setX(-to.getX());
			setFrom(from).lineTo(to, newPart);
		}
		prev = prevClone;
		return this;
	}

	public void addTo(ParticleShape<P, ?> shape) {
		for (Tuple<Tuple<Vector, Vector>, P> lineData : this.lineData) {
			Vector from = lineData.a.a;
			Vector to = lineData.a.b;
			P part = lineData.b;
			Vector diff = from.clone().subtract(to);
			double len = diff.length();
			int amount = (int) (Math.round(particlesPerBlock * len));
			diff.multiply(1.0 / amount);
			amount++; //have the end point included
			for (int i = 0; i < amount; i++) {
				Vector toPut = from.clone().add(diff.clone().multiply(-i));
				shape.getPositions().put(toPut, part);
			}
		}
		this.lineData.clear();
	}

	public Line<P> setFrom(double x, double y) {
		return setFrom(x, y, defaultZ);
	}

	public Line<P> setFrom(double x, double y, double z) {
		prev = new Vector(x, y, z); // no call to setFrom(Vector) to avoid unneeded clone
		return this;
	}

	public Line<P> setFrom(Vector v) {
		prev = v.clone();
		return this;
	}

	public Line<P> modifyFrom(double x, double y) {
		return modifyFrom(x, y, defaultZ);
	}

	public Line<P> modifyFrom(double x, double y, double z) {
		return modifyFrom(new Vector(x, y, z));
	}

	public Line<P> modifyFrom(Vector v) {
		prev.add(v);
		return this;
	}

	/**
	 * The particlesPerBlock setting will be evaluated when calling {@link #addTo(ParticleShape)}
	 */
	public Line<P> setParticlesPerBlock(double particlesPerBlock) {
		this.particlesPerBlock = particlesPerBlock;
		return this;
	}

	/**
	 * The particlesPerBlock setting will be evaluated when calling {@link #addTo(ParticleShape)}
	 */
	public double getParticlesPerBlock() {
		return particlesPerBlock;
	}

	public Line<P> setDefaultPart(P defaultPart) {
		this.defaultPart = defaultPart;
		return this;
	}

	public P getDefaultPart() {
		return defaultPart;
	}

	public Line<P> setDefaultZ(double defaultZ) {
		this.defaultZ = defaultZ;
		return this;
	}

	public double getDefaultZ() {
		return defaultZ;
	}
}

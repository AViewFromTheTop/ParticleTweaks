package net.lunade.particletweaks.impl;

public class FluidFallingCalculator {
	public static double getFluidFallingAdjustedMovement(double velocity) {
		boolean descending = velocity < 0D;
		double d;
		if (descending && Math.abs(velocity - 0.005D) >= 0.003D && Math.abs(velocity - 1F / 16D) < 0.003D) {
			d = -0.003D;
		} else {
			d = velocity - 1F / 16D;
		}
		return d;
	}
}

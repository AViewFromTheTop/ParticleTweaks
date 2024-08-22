package net.lunade.particletweaks.impl;

public class FluidFallingCalculator {
	public static double getFluidFallingAdjustedMovement(double descentSpeed, double velocity) {
		boolean descending = velocity < 0D;
		if (descentSpeed != 0D) {
			double d;
			if (descending && Math.abs(velocity - 0.005D) >= 0.003D && Math.abs(velocity - descentSpeed / 16D) < 0.003D) {
				d = -0.003D;
			} else {
				d = velocity - descentSpeed / 16D;
			}

			return d;
		} else {
			return velocity;
		}
	}
}

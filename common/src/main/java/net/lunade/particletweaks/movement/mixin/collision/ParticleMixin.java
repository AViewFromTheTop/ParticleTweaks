package net.lunade.particletweaks.movement.mixin.collision;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.mehvahdjukaar.candlelight.api.ClientOnly;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.world.phys.Vec3;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@ClientOnly
@Mixin(value = Particle.class, priority = 1001)
public abstract class ParticleMixin {

	@Shadow
	@Final
	public ClientLevel level;
	@Shadow
	public boolean stoppedByCollision;
	@Shadow
	public double yd;
	@Shadow
	public double xd;
	@Shadow
	public double zd;

	@ModifyExpressionValue(
		method = "move",
		at = @At(
			value = "FIELD",
			target = "Lnet/minecraft/client/particle/Particle;stoppedByCollision:Z",
			opcode = Opcodes.GETFIELD
		)
	) public boolean particleTweaks$skipStoppedByCollision(boolean original) {
		return false;
	}

	@Inject(
		method = "move",
		at = @At(
			value = "FIELD",
			target = "Lnet/minecraft/world/phys/Vec3;x:D",
			shift = At.Shift.BEFORE
		),
		cancellable = true
	)
	public void particleTweaks$continueOrCancelMovementBasedOnCurrentCollision(
		double xa, double ya, double za, CallbackInfo info,
		@Local(name = "movement") Vec3 movement
	) {
		if (this.stoppedByCollision) {
			this.xd = 0D;
			this.yd = 0D;
			this.zd = 0D;
		}

		if ((xa == movement.x || ya == movement.y || za == movement.z) && this.stoppedByCollision) this.stoppedByCollision = false;
		if (this.stoppedByCollision) info.cancel();
	}
}

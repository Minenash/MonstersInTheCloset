package net.fabricmc.example.mixin;

import net.fabricmc.example.MonstersInTheCloset;
import net.minecraft.block.BedBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.enums.BedPart;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(BedBlock.class)
public class BedBlockMixin {

	@Inject(method = "onUse", at = @At("HEAD"))
	public void onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand _hand, BlockHitResult _result, CallbackInfoReturnable<ActionResult> _info) {
		if (!world.isClient()) {
			MonstersInTheCloset.world = world;
			return;
		}

		if (!notSafe(state,world,pos,player)) {
			System.out.println("A");
			return;
		}
		System.out.println("B");

		Vec3d vec3d = Vec3d.ofBottomCenter(pos);
		List<HostileEntity> list = world.getEntitiesByClass(
				HostileEntity.class,
				new Box(vec3d.getX() - 8.0D, vec3d.getY() - 5.0D, vec3d.getZ() - 8.0D, vec3d.getX() + 8.0D, vec3d.getY() + 5.0D, vec3d.getZ() + 8.0D),
				(hostileEntity) -> hostileEntity.isAngryAt(player)
		);

		if (!list.isEmpty()) {
			for (HostileEntity entity : list) {
				entity.setGlowing(true);
			}
			MonstersInTheCloset.duration = 60;
			MonstersInTheCloset.list = list;
		}

	}

	private boolean notSafe(BlockState state, World world, BlockPos pos, PlayerEntity player) {

		long time = world.getTimeOfDay() % 24000;
		if (!world.getDimension().isNatural() || (time >= 0 && time <= 12000 && !world.isThundering()) ) {
			System.out.println("Natural/Day");
			return false;
		}

		Direction direction = state.get(HorizontalFacingBlock.FACING);
		if (state.get(BedBlock.PART) == BedPart.HEAD) {
			direction = direction.getOpposite();
		}

		BlockPos pos2 = pos.offset(direction);

		return !(isTooFar(pos, player) || isTooFar(pos2, player) || wouldSuffocate(world, pos) || wouldSuffocate(world, pos2));

	}

	private boolean isTooFar(BlockPos pos, PlayerEntity player) {
		Vec3d vec3d = Vec3d.ofBottomCenter(pos);
		return !(Math.abs(player.getX() - vec3d.getX()) <= 3.0D && Math.abs(player.getY() - vec3d.getY()) <= 2.0D && Math.abs(player.getZ() - vec3d.getZ()) <= 3.0D);
	}

	private boolean wouldSuffocate(World world, BlockPos pos) {
		System.out.println("XYZ: " + pos.getX() + " " + pos.getY() + " " + pos.getZ());
		return world.getBlockState(pos.up()).shouldSuffocate(world, pos.up());
	}

}

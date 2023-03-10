package net.lunade.onetwenty.mixin;

import net.lunade.onetwenty.interfaces.SuspiciousSandBlockEntityInterface;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.SuspiciousSandBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(SuspiciousSandBlock.class)
public abstract class SuspiciousSandBlockMixin extends BaseEntityBlock {

	protected SuspiciousSandBlockMixin(Properties properties) {
		super(properties);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
		return BaseEntityBlock.createTickerHelper(blockEntityType, BlockEntityType.SUSPICIOUS_SAND, (worldx, pos, statex, blockEntity) -> ((SuspiciousSandBlockEntityInterface) blockEntity).luna120$tick());
	}
}

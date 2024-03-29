package net.lunade.onetwenty.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.lunade.onetwenty.interfaces.BrushableBlockEntityInterface;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BrushableBlockRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BrushableBlockEntity;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BrushableBlockRenderer.class)
public class BrushableBlockItemRenderMixin {

	@Shadow
	@Final
	private ItemRenderer itemRenderer;

	@Inject(method = "render*", at = @At("HEAD"), cancellable = true)
	public void luna120$render(BrushableBlockEntity brushableBlockEntity, float partialTick, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int j, CallbackInfo info) {
		info.cancel();
		this.luna120$smoothItemRender(brushableBlockEntity, partialTick, poseStack, multiBufferSource, i, j);
	}

	@Unique
	public void luna120$smoothItemRender(@NotNull BrushableBlockEntity brushableBlockEntity, float partialTick, @NotNull PoseStack poseStack, @NotNull MultiBufferSource multiBufferSource, int i, int j) {
		if (brushableBlockEntity.getLevel() == null) {
			return;
		}
		BrushableBlockEntityInterface brushableBlockEntityInterface = (BrushableBlockEntityInterface) brushableBlockEntity;
		Direction direction = brushableBlockEntityInterface.luna120$getHitDirection();
		if (direction == null) {
			return;
		}
		ItemStack itemStack = brushableBlockEntity.getItem();
		if (itemStack.isEmpty()) {
			return;
		}

		float itemX = brushableBlockEntityInterface.luna120$getXOffset(partialTick);
		float itemY = brushableBlockEntityInterface.luna120$getYOffset(partialTick);
		float itemZ = brushableBlockEntityInterface.luna120$getZOffset(partialTick);

		poseStack.pushPose();
		poseStack.translate(0.0f, 0.5f, 0.0f);
		poseStack.translate(itemX, itemY, itemZ);
		poseStack.mulPose(Axis.YP.rotationDegrees(75.0f));
		poseStack.mulPose(Axis.YP.rotationDegrees(brushableBlockEntityInterface.luna120$getRotation(partialTick) + 15F));
		poseStack.scale(0.5f, 0.5f, 0.5f);
		int l = LevelRenderer.getLightColor(brushableBlockEntity.getLevel(), brushableBlockEntity.getBlockState(), brushableBlockEntity.getBlockPos().relative(direction));
		this.itemRenderer.renderStatic(itemStack, ItemDisplayContext.FIXED, l, OverlayTexture.NO_OVERLAY, poseStack, multiBufferSource, brushableBlockEntity.getLevel(), 0);
		poseStack.popPose();
	}

}

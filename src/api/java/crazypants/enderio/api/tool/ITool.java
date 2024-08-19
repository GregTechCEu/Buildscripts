package crazypants.enderio.api.tool;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

import org.jetbrains.annotations.NotNull;

public interface ITool extends IHideFacades {

    boolean canUse(@NotNull EnumHand stack, @NotNull EntityPlayer player, @NotNull BlockPos pos);

    void used(@NotNull EnumHand stack, @NotNull EntityPlayer player, @NotNull BlockPos pos);
}
